package com.zjj.spike_system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author zjj
 * @since 2021-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("spike_system_skorder")
@ApiModel(value="Skorder对象", description="")
public class Skorder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "秒杀订单id")
      @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "逻辑删除 0未删除 1已删除")
    @TableField(value = "is_deleted")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty(value = "记录创建时间")
      @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "记录最后修改时间")
      @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
