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
    private final List<Strings> webPages;

    MultipageCrawler() {
        this.webPages = List.of(
                Strings.PlanetFitnessWebURL,
                Strings.GoodLifeFitnessWebURL,
                Strings.MovatiAthleticWebURL
        );
    }

    private void parseWebDataToText() {

    }

    private Document scrapeAndGetDataFor(String site) {
        return Document.createShell(Strings.Empty.value);
    }

    public List<FitnessDataModel> getAllFitnessModels() {
        List<FitnessDataModel> fitnessDataModel = new ArrayList<>();
        System.out.println((new CrawlPlanetFitness(Strings.PlanetFitnessWebURL)).getFitnessDataModel());
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
