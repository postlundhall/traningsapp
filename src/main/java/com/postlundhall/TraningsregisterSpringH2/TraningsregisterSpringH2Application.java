package com.postlundhall.TraningsregisterSpringH2;

import com.postlundhall.TraningsregisterSpringH2.granssnitt.TraningsMeny;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class TraningsregisterSpringH2Application implements CommandLineRunner {

    private final TraningsMeny meny;

    public static void main(String[] args) {
        SpringApplication.run(TraningsregisterSpringH2Application.class, args);
    }

    @Override
    public void run(String... args) {
        meny.start();
    }
}