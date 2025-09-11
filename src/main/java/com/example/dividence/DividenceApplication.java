package com.example.dividence;
import java.io.IOException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
public class DividenceApplication {
  public static void main(String[] args) {
    //SpringApplication.run(DividenceApplication.class, args);
    try{
      Connection connection = Jsoup.connect("https://finance.yahoo.com/quote/COKE/history/?frequency=1mo&period1=1726033815&period2=1757399159")
          .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36");
      Document document = connection.get();
      Elements dividendRows = document.select("tr:has(td.event:contains(Dividend))");

      for(Element row : dividendRows) {
        String[] date = row.select("td").first().text().split(",");
        String month = date[0];
        String year = date[1];

        String dividendText = row.select("td.event").text();
        System.out.println(year + "/" + month + "/" + "->" + dividendText);
      }

    }catch(IOException e){
      e.printStackTrace();
    }
  }
}