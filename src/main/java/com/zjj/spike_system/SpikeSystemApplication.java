package com.zjj.spike_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan("com.zjj")
public class SpikeSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpikeSystemApplication.class, args);
    }

}
