package com.zjj.spike_system.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一结果返回类
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "全局统一返回结果")
public class Result {
    @ApiModelProperty(value = "是否成功")
    private Boolean success;
    @ApiModelProperty(value = "返回码")
    private Integer code;
    @ApiModelProperty(value = "返回消息")
    private String message;
    @ApiModelProperty(value = "返回数据")
    private Map<String, Object> data = new HashMap<String, Object>();

    private Result() {
    }

    public static Result ok() {
        Result r = new Result();
        r.setSuccess(ResultCode.SUCCESS.getSuccess());
        r.setCode(ResultCode.SUCCESS.getCode());
        r.setMessage(ResultCode.SUCCESS.getMessage());
        return r;
    }

    public static Result error() {
        Result r = new Result();
        r.setSuccess(ResultCode.UNKNOWN_REASON.getSuccess());
        r.setCode(ResultCode.UNKNOWN_REASON.getCode());
        r.setMessage(ResultCode.UNKNOWN_REASON.getMessage());
        return r;
    }

    public static Result setResult(ResultCode resultCode){
        Result r = new Result();
        r.setSuccess(resultCode.getSuccess());
        r.setCode(resultCode.getCode());
        r.setMessage(resultCode.getMessage());
        return r;
    }

    /**
     *  向返回数据添加一组键值对,之所以添加这个方法，是因为很多的时候，返回对象只有一个
     * @param key：键
     * @param value：值
     * @return：this对象
     */
    public Result setData(String key, Object value){
        this.data.put(key, value);
        return this;
    }
}
