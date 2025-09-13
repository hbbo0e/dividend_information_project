package com.example.dividence.scheduler;

import com.example.dividence.model.Company;
import com.example.dividence.model.ScrapedResult;
import com.example.dividence.model.constants.CacheKey;
import com.example.dividence.persist.CompanyRepository;
import com.example.dividence.persist.DividendRepository;
import com.example.dividence.persist.entity.CompanyEntity;
import com.example.dividence.persist.entity.DividendEntity;
import com.example.dividence.scraper.Scraper;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableCaching
@AllArgsConstructor
public class ScraperScheduler {

  private final CompanyRepository companyRepository;
  private final Scraper yahooFinanceScraper;
  private final DividendRepository dividendRepository;

  @Scheduled(cron = "${scheduler.scrap.yahoo}")
  @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true)
  public void yahoofinanceScheduling(){
    // 저장된 회사 목록 조회
    List< CompanyEntity> companies = this.companyRepository.findAll();

    // 회사마다 배당금 정보 새로 스크래핑
    for (var company : companies){
      log.info("scraping scheduler is started -> " + company.getName());
      ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(new Company(company.getTicker(), company.getName()));

      // 스크래핑한 배당금 정보 중 데이터베이스에 없는 값은 저장
      scrapedResult.getDividends().stream()
          // dividend 모델을 엔티티로 매핑
          .map(e -> new DividendEntity(company.getId(), e))
          // 엘리먼트를 하나씩 dividend repository 에 삽입
          .forEach(e -> {
            boolean exists = this.dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
            if (!exists) this.dividendRepository.save(e);
          });

      // 연속적인 스크래핑 방지 위함
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }
}
