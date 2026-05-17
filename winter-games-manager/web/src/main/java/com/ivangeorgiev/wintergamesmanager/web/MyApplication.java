package com.ivangeorgiev.wintergamesmanager.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.ivangeorgiev.wintergamesmanager.web",
        "com.ivangeorgiev.wintergamesmanager.core",
        "com.ivangeorgiev.wintergamesmanager.data"
})
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}