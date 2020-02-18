package com.yong.bsxt_admin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

@SpringBootTest
class BsxtAdminApplicationTests {
    /**
     * 判断A B C三家谁处于等待中
     */
    private boolean isWaitA = false;
    private boolean isWaitB = false;
    private boolean isWaitC = false;

    @Test
    void contextLoads() {
//        String a = "0111";
//        boolean b = "小得湖".matches("[\u4E00-\u9FA5]+");
//        System.out.println(b);
        System.out.println(RandomCode());
    }

    private String RandomCode() {
        String ran = "1234567890QWERTYUIOPASDFGHJKLZXCVBNM";
        StringBuilder strCode = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 18; i++) {
            int num = random.nextInt(ran.length());
            strCode.append(ran, num, num + 1);
        }

        return strCode.toString();
    }

}
