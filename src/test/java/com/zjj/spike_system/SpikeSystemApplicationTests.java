package com.zjj.spike_system;

import com.zjj.spike_system.utils.MD5Util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpikeSystemApplicationTests {

//    @Autowired
//    private MD5Util md5Util;

    @Test
    void md5Test() {
        System.out.println(MD5Util.inputPassToFromPass("123456"));
        System.out.println(MD5Util.fromPassToDBPass("dd693230e0bec6bd57f3dd1b05bfb08f","123abc"));
        System.out.println(MD5Util.inputPassToDBPass("123456", "123abc"));
        // 结果：第二和第三的结果一致
    }

}
