package com.example.dividence.scraper;

import com.example.dividence.model.Company;
import com.example.dividence.model.Dividend;
import com.example.dividence.model.ScrapedResult;
import com.example.dividence.model.constants.Month;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component // 해당 클래스를 빈으로 사용하기 위해서
public class YahooFinanceScraper implements Scraper{

  private static final String STATISTICS_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";
  private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";
  static long now = System.currentTimeMillis() / 1000;
  private static final long START_TIME = now - 31536000;

  @Override
  public ScrapedResult scrap (Company company) {
    var scrapResult = new ScrapedResult();
    scrapResult.setCompany(company);

    try {
      String url = String.format(STATISTICS_URL, company.getTicker(), START_TIME, now);

      Connection connection = Jsoup.connect(url)
          .userAgent(
              "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36");
      Document document = connection.get();
      Elements dividendRows = document.select("tr:has(td.event:contains(Dividend))");

      List<Dividend> dividends = new ArrayList<>();
      for (Element row : dividendRows) {
        String[] date = row.select("td").first().text().split("[, ]+");
        int month = Month.strToNumber(date[0]);
        int day = Integer.parseInt(date[1]);
        int year = Integer.parseInt(date[2]);
        String dividendText = row.select("td.event").text();

        if (month < 0){
          throw new RuntimeException("unexpected Month enum value -> " + date[0]);
        }

        dividends.add(Dividend.builder()
                            .date(LocalDateTime.of(year, month, day, 0, 0))
                            .dividend(dividendText)
                            .build());
      }
      scrapResult.setDividends(dividends);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return scrapResult;
  }

  @Override
  public Company scrapCompanyByTicker(String ticker){
    String url = String.format(SUMMARY_URL, ticker, ticker);

    try{
      Connection connection = Jsoup.connect(url)
          .userAgent(
              "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36");
      Document document = connection.get();
      Element titleEle = document.getElementsByTag("h1").get(1);
      // String title = titleEle.text().split(" - ")[1].trim();
      String title = titleEle.text();

      return Company.builder()
                    .ticker(ticker)
                    .name(title)
                    .build();
    } catch(IOException e){
      e.printStackTrace();
    }

    return null;
  }
}
