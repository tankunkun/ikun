package com.atguigu.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
        String encode1 = bcpe.encode("111111");
        String encode2 = bcpe.encode("111111");
        System.out.println("encode1 = " + encode1);
        System.out.println("encode2 = " + encode2);
//        encode1 = 
//        encode2 = $2a$10$sXWRszjsQHIQGm4TW750xe9YnYm6razz2kTALiUFozILb0uVNICKi

        boolean flag = bcpe.matches("111111", "$2a$10$7D.kg.CiG3NQm20P4xkQieAwDuAyw/UsVaVqxw7wZ4zcmL8mIcbeu");
        System.out.println("flag = " + flag);
    }
}
