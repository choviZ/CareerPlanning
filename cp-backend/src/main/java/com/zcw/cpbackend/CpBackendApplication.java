package com.zcw.cpbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zcw.cpbackend.mapper")
public class CpBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CpBackendApplication.class, args);
    }

}
