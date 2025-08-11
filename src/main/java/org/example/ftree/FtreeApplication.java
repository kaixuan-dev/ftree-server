package org.example.ftree;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(value = {"org.example.ftree.mapper"})
@SpringBootApplication
public class FtreeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FtreeApplication.class, args);
    }

}
