package com.example.dividence;
import com.example.dividence.model.Company;
import com.example.dividence.scraper.YahooFinanceScraper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
public class DividenceApplication {
  public static void main(String[] args) {
    //SpringApplication.run(DividenceApplication.class, args);
    YahooFinanceScraper scraper = new YahooFinanceScraper();
    var result = scraper.scrap(Company.builder().ticker("O").build());
    System.out.println(result);
  }
}