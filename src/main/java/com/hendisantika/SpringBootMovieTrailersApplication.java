package com.hendisantika;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.dialect.springdata.SpringDataDialect;

@SpringBootApplication
public class SpringBootMovieTrailersApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMovieTrailersApplication.class, args);
    }

    @Bean
    public SpringDataDialect springDataDialect() {
        return new SpringDataDialect();
    }
}
