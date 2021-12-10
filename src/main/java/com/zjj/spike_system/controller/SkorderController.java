package com.zjj.spike_system.controller;


import com.zjj.spike_system.entity.Skorder;
import com.zjj.spike_system.entity.User;
import com.zjj.spike_system.entity.message.SkMessage;
import com.zjj.spike_system.entity.vo.SkGoodsVo;
import com.zjj.spike_system.rabbitmq.MQSender;
import com.zjj.spike_system.service.SkgoodsService;
import com.zjj.spike_system.service.SkorderService;
import com.zjj.spike_system.utils.JsonUtil;
import com.zjj.spike_system.utils.Result;
import com.zjj.spike_system.utils.VerCodeUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zjj
 * @since 2021-11-15
 */
@RestController
@RequestMapping("/spike_system/skorder")
@Slf4j
public class SkorderController implements InitializingBean {
    @Autowired
    private SkorderService skorderService;
    @Autowired
    private SkgoodsService  skgoodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MQSender mqSender;
    @Autowired
    private DefaultRedisScript script;

    // 使用内存标记：记录各个商品是否库存是否足够，以减少与redis的通信
    private Map<Long,Boolean> isGoodsEmpty = new HashMap<>();

    @PostMapping("check/vercode")
    public Result checkCode(User user, @RequestBody Map<String,String> map){
        if (user == null){
            return Result.error().setCode(22222).setMessage("请先登录");
        }

        String verCode = map.get("vercode");
        String goodId = map.get("goodId");
        String redisCode = (String) redisTemplate.opsForValue().get("verCode:" + goodId + ":" + user.getId());
        if (verCode == null || redisCode == null || !verCode.equals(redisCode)){
            return Result.error().setMessage("验证码错误");
        }
        return Result.ok();
    }


    @ApiOperation("获取验证码")
    @GetMapping("vercode")
    public Result getVerCode(User user, Long goodId){
        if (user == null){
            return Result.error().setCode(22222).setMessage("请先登录");
        }

        Map<String, String> captcha = VerCodeUtil.getArithmeticCaptcha(130,48,4);
        // 将验证码存入redis并设置过期事件
        redisTemplate.opsForValue().set("verCode:" + goodId + ":" + user.getId(), captcha.get("verCode"), 5, TimeUnit.MINUTES);
        return Result.ok().setData("image", captcha.get("image"));
    }

    /**
     * 获取隐藏路径
     * @param user
     * @param goodId
     * @return
     */
    @GetMapping("path/{goodId}")
    public Result getPath(User user, @PathVariable("goodId") Long goodId){
        if (user == null){
            return Result.error().setCode(22222).setMessage("请先登录");
        }
        // 生成随机的隐藏路径并返回给前端
        String path = skorderService.getPath(goodId, user.getId());
        return Result.ok().setData("path", path);
    }


    /**
     * 秒杀接口
     * @param user
     * @param skid
     * @return
     */
    @GetMapping("{path}/buy/{skid}")
    public Result SpikeGoods(User user, @PathVariable("path") String path, @PathVariable("skid") Long skid){
        // 用户未登录（一般不会出现，因为未登录不出显示秒杀页面）
        if (user == null){
            return Result.error().setCode(22222).setMessage("请先登录");
        }

        // 判断隐藏地址是正确
        boolean check = skorderService.checkPath(path,skid,user.getId());
        if (!check){
            return  Result.error().setMessage("非法请求");
        }

        ValueOperations opsForValue = redisTemplate.opsForValue();
        // 判断是否重复抢购
        Skorder isSpiked = (Skorder)opsForValue.get("skorder" + user.getId() + skid);
        if (!Objects.isNull(isSpiked)){
            return Result.error().setMessage("您已经抢过了噢~下次再来");
        }
        // 判断库存：使用Redis的原子操作自增或自减
        // 使用内存标记：先判断是否为空，以减少通信
        if(isGoodsEmpty.get(skid)){
            return Result.error().setMessage("对不起,库存不足");
        }
        // Long count = opsForValue.decrement("skGoods:" + skid);
        // 使用分布式锁处理缓存中的库存
        Long count = (Long) redisTemplate.execute(script, Collections.singletonList("skGoods:" + skid), Collections.emptyList());
        if (count <= 0){
            // 将内存标记置为true
            isGoodsEmpty.put(skid, true);
//            opsForValue.increment("skGoods:" + skid);
            return Result.error().setMessage("对不起,库存不足");
        }
        // 下订单
        // 生成一个订单消息
        SkMessage skMessage = new SkMessage(user, skid);
        mqSender.sendSkOrderMessage(JsonUtil.obj2String(skMessage));
        // 相应正在排队
        return Result.ok().setCode(22220).setMessage("正在排队");

        /*
        以下为原始方法，执行逻辑如下：
                    - 每次用户访问都要先查询数据库，判断库存是否足够
                    - 再从redis中根据用户id、和秒杀商品id查询用户是否抢购过
                    - 创建订单，并完成后续扣库存等操作

       // 判断库存
        Skgoods skgoods = skgoodsService.getById(skid);
        Integer spikeStock = skgoods.getSpikeStock();
        if (spikeStock <= 0){
            return Result.error().setMessage("对不起,库存不足");
        }
        //判断当前用户是否已经秒杀过:
        //         原来是从数据库中查询，优化之后选择从redis中查询
        //         从redis中查询用户是否已经抢购过
        Skorder isSpiked = (Skorder)redisTemplate.opsForValue().get("skorder" + user.getId() + skid);
        if (!Objects.isNull(isSpiked)){
            return Result.error().setMessage("您已经抢过了噢~下次再来");
        }
        // 创订单，并购买
        Result res = skorderService.addOrder(user, skgoods);
        return res;
        */
    }

    /**
     * 确认用户是否下单成功(秒杀结果)
     * @param user
     * @param goodId
     * @return
     */
    @GetMapping("confirm/{goodId}")
    public Result confirmSkResult(User user, @PathVariable("goodId") Long goodId){
        if(user == null){
            return Result.error().setCode(22222).setMessage("请先登录");
        }
        Long res = skorderService.confirmSkResult(user,goodId);
        log.info("------------------------" + res);
        if (res == -1){
            return Result.error().setMessage("抱歉，没抢到~");
        }else if(res == 0){
            return Result.error().setCode(23333).setMessage("排队中~");
        }else{
            return Result.ok().setMessage("秒杀成功");
        }
    }

    /**
     * Bean初识化时执行的方法：
     *         将商品库存存放在redis中，进行库存预减
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<SkGoodsVo> goods = skgoodsService.getGoods();
        if (CollectionUtils.isEmpty(goods)){
            return;
        }
        // 如果秒杀商品列表不为空，则将美国个商品的库存存放在redis中
        goods.forEach(goodvo->{
            // 添加内存标记,初始为false
            isGoodsEmpty.put(goodvo.getId(), false);
            redisTemplate.opsForValue().set("skGoods:" + goodvo.getId(), goodvo.getSkStock());
        });
    }
}

