package com.zjj.spike_system.config;

import org.mapstruct.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.zjj.spike_system.mapper")  //扫描包
public class WebConfig {
}
