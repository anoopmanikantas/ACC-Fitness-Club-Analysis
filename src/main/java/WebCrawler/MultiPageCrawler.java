package WebCrawler;

import WebCrawler.Fit4Less.CrawlFit4Less;
import WebCrawler.GoodLifeFitness.CrawlGoodLifeFitness;
import WebCrawler.Model.FitnessDataModel;
import WebCrawler.PlanetFitness.CrawlPlanetFitness;
import helpers.Strings;

import java.util.ArrayList;
import java.util.List;

public class MultiPageCrawler {
    private final List<Strings> webPages;

    public MultiPageCrawler() {
        this.webPages = List.of(
                Strings.PlanetFitnessWebURL,
                Strings.GoodLifeFitnessWebURL,
                Strings.Fit4LessWebURL
        );
    }

    public List<FitnessDataModel> getAllFitnessModels() {
        List<FitnessDataModel> fitnessDataModel = new ArrayList<>();
        for (Strings webPage : webPages) {
            switch (webPage) {
                case Strings.GoodLifeFitnessWebURL -> fitnessDataModel.add((new CrawlGoodLifeFitness(webPage)).getFitnessDataModel()) ;
                case Strings.PlanetFitnessWebURL -> fitnessDataModel.add((new CrawlPlanetFitness(webPage)).getFitnessDataModel());
                case Strings.Fit4LessWebURL -> fitnessDataModel.add((new CrawlFit4Less(webPage)).getFitnessDataModel());
            }
        }
        return fitnessDataModel;
    }

    public static void main(String[] args) {
        MultiPageCrawler multipageCrawler = new MultiPageCrawler();
        for (FitnessDataModel allFitnessModel : multipageCrawler.getAllFitnessModels()) {
            System.out.println(allFitnessModel);
        }
    }
}
