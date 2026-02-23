package com.sjcapstone.tandanji;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
        basePackages = "com.sjcapstone.tandanji",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = "com\\.sjcapstone\\.tandanji\\.domain\\..*"
        )
)
public class TandanjiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TandanjiApplication.class, args);
    }
}