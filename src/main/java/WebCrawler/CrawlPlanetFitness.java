package WebCrawler;

import WebCrawler.Model.FitnessDataModel;
import helpers.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class CrawlPlanetFitness implements CrawlerDelegate{
    private final WebDriver driver;
    private final String url;

    CrawlPlanetFitness(WebDriver driver, Strings url) {
        this.driver = driver;
        this.url = url.value;
    }

    private void crawlPage() {
        driver.get(url);

    }

    private void parseAndStoreHTMLUsing(String pageSource) {

    }
//driver.get(webPage.value);
//    String html = driver.getPageSource();
//    Document document = Jsoup.parse(html);
//        for (Element link : links) {
//        if (!link.toString().strip().isBlank() && link.toString().contains("membership"))
//            System.out.println(link.toString());
//    }
//    List<String> membershipDetails = document.select("membership").eachText();
//        for (String membershipDetail : membershipDetails) {
//        System.out.println("membership detail: " + membershipDetail);
//    }
//    Elements links = document.select("a[href]");
    @Override
    public FitnessDataModel getFitnessDataModel() {
        return null;
    }
}
