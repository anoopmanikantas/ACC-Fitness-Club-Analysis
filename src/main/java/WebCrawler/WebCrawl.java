package WebCrawler;

import WebCrawler.Model.FitnessDataModel;
import helpers.Strings;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

import java.util.ArrayList;
import java.util.List;

public class WebCrawl {
    private final WebDriver driver;
    private final List<Strings> webPages;

    WebCrawl() {
        this.driver = new EdgeDriver();
        this.webPages = List.of(
                Strings.WebURLPlanetFitness,
                Strings.WebURLGoodLifeFitness,
                Strings.WebURLMovatiAthletic
        );
    }

    private void parseWebDataToText() {

    }

    private Document scrapeAndGetDataFor(String site) {
        return Document.createShell(Strings.empty.value);
    }

    public List<FitnessDataModel> getAllFitnessModels() {
        List<FitnessDataModel> fitnessDataModel = new ArrayList<>();
        for (Strings webPage : webPages) {
            switch (webPage) {
                case Strings.WebURLGoodLifeFitness:
                    fitnessDataModel.add((new CrawlGoodLifeFitness()).getFitnessDataModel()) ;
                    break;
                case Strings.WebURLPlanetFitness:
                    fitnessDataModel.add((new CrawlPlanetFitness(driver, webPage)).getFitnessDataModel());
                    break;
                case Strings.WebURLMovatiAthletic:
                    fitnessDataModel.add((new CrawlMovatiAthletic()).getFitnessDataModel());
                    break;
            }
        }
        return fitnessDataModel;
    }



    public static void main(String[] args) {
        WebCrawl webCrawl = new WebCrawl();
        for (FitnessDataModel allFitnessModel : webCrawl.getAllFitnessModels()) {
            System.out.println(allFitnessModel);
        }
    }
}
