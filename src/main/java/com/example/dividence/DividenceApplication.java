package com.example.dividence;
import com.example.dividence.model.Company;
import com.example.dividence.scraper.YahooFinanceScraper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class DividenceApplication {
  public static void main(String[] args) {
    SpringApplication.run(DividenceApplication.class, args);

  }
}