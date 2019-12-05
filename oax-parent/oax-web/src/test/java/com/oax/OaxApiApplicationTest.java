package com.oax;

import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/6/4
 * Time: 14:02
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OaxApiApplicationTest {

    public static void main(String[] args) {
        SpringApplication.run(OaxApiApplicationTest.class, args);
        System.out.print("111111111111");
    }

}