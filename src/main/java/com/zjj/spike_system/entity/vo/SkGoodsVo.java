package com.zjj.spike_system.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用于查询秒杀商品的信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkGoodsVo {
    @ApiModelProperty("秒杀id")
    private  Long id;
    @ApiModelProperty("商品id")
    private  Long goodId;
    @ApiModelProperty("秒杀商品名称")
    private String skGoodName;
    @ApiModelProperty("秒杀商品标题")
    private String skGoodTitle;
    @ApiModelProperty("秒杀商品图片")
    private String skGoodImg;
    @ApiModelProperty("秒杀商品细节")
    private String skGoodDetail;
    @ApiModelProperty("秒杀商品原价")
    private BigDecimal goodPrice;
    @ApiModelProperty("秒杀价")
    private BigDecimal skPrice;
    @ApiModelProperty("秒杀库存")
    private Integer skStock;
    @ApiModelProperty("秒杀开始时间")
    private Date startTime;
    @ApiModelProperty("秒杀结束时间")
    private Date endTime;



}
