package com.ivangeorgiev.wintergamesmanager.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.ivangeorgiev.wintergamesmanager.web",
        "com.ivangeorgiev.wintergamesmanager.core",
        "com.ivangeorgiev.wintergamesmanager.data"
})
@EntityScan(basePackages = "com.ivangeorgiev.wintergamesmanager.data.models")
@EnableJpaRepositories(basePackages = "com.ivangeorgiev.wintergamesmanager.data.repositories")
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}