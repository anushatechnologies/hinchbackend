package com.hinchmart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class HinchmartBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HinchmartBackendApplication.class, args);
    }
}

