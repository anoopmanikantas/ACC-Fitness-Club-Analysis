package WebCrawler;

import WebCrawler.Model.FitnessDataModel;
import helpers.Strings;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class MultipageCrawler {
    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final List<Strings> webPages;
    private final EdgeOptions edgeOptions;

    MultipageCrawler() {
        this.edgeOptions = new EdgeOptions();
        this.driver = new EdgeDriver(edgeOptions);
        this.webPages = List.of(
                Strings.PlanetFitnessWebURL,
                Strings.GoodLifeFitnessWebURL,
                Strings.MovatiAthleticWebURL
        );
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    private void parseWebDataToText() {

    }

    private Document scrapeAndGetDataFor(String site) {
        return Document.createShell(Strings.Empty.value);
    }

    public List<FitnessDataModel> getAllFitnessModels() {
        driver.manage().window().maximize();
        List<FitnessDataModel> fitnessDataModel = new ArrayList<>();
        (new CrawlPlanetFitness(driver, Strings.PlanetFitnessWebURL, webDriverWait)).getFitnessDataModel();
        // TODO: uncomment the code block below
//        for (Strings webPage : webPages) {
//            switch (webPage) {
//                case Strings.GoodLifeFitnessWebURL:
//                    fitnessDataModel.add((new CrawlGoodLifeFitness()).getFitnessDataModel()) ;
//                    break;
//                case Strings.PlanetFitnessWebURL:
//                    fitnessDataModel.add((new CrawlPlanetFitness(driver, webPage, webDriverWait)).getFitnessDataModel());
//                    break;
//                case Strings.MovatiAthleticWebURL:
//                    fitnessDataModel.add((new CrawlMovatiAthletic()).getFitnessDataModel());
//                    break;
//            }
//        }
//        driver.quit();
        return fitnessDataModel;
    }



    public static void main(String[] args) {
        MultipageCrawler multipageCrawler = new MultipageCrawler();
        for (FitnessDataModel allFitnessModel : multipageCrawler.getAllFitnessModels()) {
            System.out.println(allFitnessModel);
        }
    }
}
