package com.dyrnq.sbs.undertow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.dyrnq.sbs.undertow", "com.dyrnq.sbs.common"})
@Slf4j
public class App {

    public static void main(String[] args) {
        log.info("App is starting");
        SpringApplication.run(App.class, args);
    }
}
