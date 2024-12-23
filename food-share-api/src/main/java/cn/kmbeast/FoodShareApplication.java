package cn.kmbeast;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目启动类
 */
@MapperScan("cn.kmbeast.mapper")
@SpringBootApplication
public class FoodShareApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoodShareApplication.class, args);
    }
}
