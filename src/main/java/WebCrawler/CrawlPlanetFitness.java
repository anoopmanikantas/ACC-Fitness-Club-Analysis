package WebCrawler;

import WebCrawler.Model.FitnessDataModel;
import helpers.Strings;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;

public class CrawlPlanetFitness extends Crawler implements CrawlerDelegate {

    CrawlPlanetFitness(WebDriver driver, Strings url, WebDriverWait webDriverWait) {
        super(driver, url, webDriverWait);
    }

    private void crawlPage() {
        driver.get(this.url);
        html = driver.getPageSource();
        parseAndStoreHTMLUsing();
        traverseThroughAllTheLocations();
    }

    private void parseAndStoreHTMLUsing() {
        createDirectory(Strings.PlanetFitnessParsedHTMLDirectory);
        WebElement showAllLocationsButton = driver.findElement(By.xpath(Strings.PlanetFitnessShowAllLocationsButtonXpath.value));
        actions.moveToElement(showAllLocationsButton).perform();
        showAllLocationsButton.click();
        html = driver.getPageSource();
        document = Jsoup.parse(html);
        parseAndStoreHTMLFile();
    }

    private void traverseThroughAllTheLocations() {
        WebElement rootContainer = driver.findElement(By.xpath(Strings.PlanetFitnessLocationsContainerXpath.value));
        Integer containerSize = rootContainer.findElements(By.xpath(Strings.ChildrenXpath.value)).size();

        for (int containerIndex = 1; containerIndex < containerSize - 2; containerIndex++) {
            try {
                webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Strings.PlanetFitnessLocationsHeaderXpath.value)));
                WebElement element = driver.findElement(By.xpath(Strings.PlanetFitnessLocationsContainerFirstXpath.getValueUsing(containerIndex + Strings.PlanetFitnessLocationsContainerSecondXPath.value)));
                actions.moveToElement(element).perform();
                element.click();
                driver.switchTo().window(new ArrayList<>(driver.getWindowHandles()).get(1));
                fetchDataFromLocation();
                driver.close();
                driver.switchTo().window(new ArrayList<>(driver.getWindowHandles()).get(0));
            } catch (Exception e) {
                System.out.println();
                break;
            }
        }
    }

    private void fetchDataFromLocation() {
        document = Jsoup.parse(driver.getPageSource());
        parseAndStoreHTMLFile();
        String location = getLocation();
        String clubHours = getTextFrom(Strings.PlanetFitnessClubHoursXpath.value);
        // TODO: get membership costs and amenities
    }

    private String getLocation() {
        StringBuilder location = new StringBuilder();
        for (int pTagIndex = 1; pTagIndex < 4; pTagIndex++) {
            if (pTagIndex == 3) {
                location.append(getTextFrom(Strings.PlanetFitnessLocationXpath.getValueUsing(String.valueOf(pTagIndex), Strings.ClosingBox)));
                continue;
            }
            location.append(getTextFrom(Strings.PlanetFitnessLocationXpath.getValueUsing(String.valueOf(pTagIndex), Strings.ClosingBox))).append(Strings.Separator.value);
        }
        return location.toString();
    }

    @Override
    public FitnessDataModel getFitnessDataModel() {
        crawlPage();
        return null;
    }
}
