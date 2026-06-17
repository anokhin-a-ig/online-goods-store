package ru.anokhin.dev.onlinegoodsstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "ru.anokhin")
public class OnlineGoodsStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineGoodsStoreApplication.class, args);
    }

}
