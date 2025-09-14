package com.example.dividence.scraper;

import com.example.dividence.model.Company;
import com.example.dividence.model.ScrapedResult;

public interface Scraper {
  Company scrapCompanyByTicker(String ticker);
  ScrapedResult scrap(Company company);

}
