package WebCrawler.PlanetFitness;

import WebCrawler.Crawler.Crawler;
import WebCrawler.Crawler.CrawlerInterface;
import WebCrawler.Model.FitnessDataModel;
import WebCrawler.Model.MembershipDetailsModel;
import helpers.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import us.codecraft.xsoup.Xsoup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

public class CrawlPlanetFitness extends Crawler implements CrawlerInterface {
    public static void info(String message) {
        java.util.logging.Logger.getLogger(CrawlPlanetFitness.class.getName()).info(message);
    }

    private List<File> files;

    public CrawlPlanetFitness(Strings url) {
        super(url);
        files = new ArrayList<>();
    }

    private void crawlPage() {
        createDirectory(Strings.PlanetFitnessParsedHTMLDirectory);
        if (isDirectoryEmpty) {
            driver.get(this.url);
            html = driver.getPageSource();
            parseAndStoreHTMLUsing();
            traverseThroughAllTheLocations();
        } else {
            try {
                traverseThroughAllTheFilesLocally();
            } catch (Exception ignored) { }
        }
    }

    private void parseAndStoreHTMLUsing() {
        WebElement showAllLocationsButton = driver.findElement(By.xpath(Strings.PlanetFitnessShowAllLocationsButtonXpath.value));
        actions.moveToElement(showAllLocationsButton).perform();
        showAllLocationsButton.click();
        html = driver.getPageSource();
        document = Jsoup.parse(html);
        parseAndStoreHTMLFile(Strings.PlanetFitnessParsedHTMLDirectory);
    }

    private void traverseThroughAllTheLocations() {
        WebElement rootContainer = driver.findElement(By.xpath(Strings.PlanetFitnessLocationsContainerXpath.value));
        int containerSize = rootContainer.findElements(By.xpath(Strings.ChildrenXpath.value)).size();

        for (int containerIndex = 1; containerIndex < containerSize - 2; containerIndex++) {
            try {
                webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Strings.PlanetFitnessLocationsHeaderXpath.value)));
                WebElement element = driver.findElement(
                        By.xpath(
                                Strings.getFormattedXpath(
                                        Strings.PlanetFitnessLocationsContainerFirstXpath,
                                        containerIndex,
                                        Strings.PlanetFitnessLocationsContainerSecondXPath
                                )
                        )
                );
                actions.moveToElement(element).perform();
                element.click();
                driver.switchTo().window(new ArrayList<>(driver.getWindowHandles()).get(1));
                fetchDataFromLocation();
                driver.close();
                driver.switchTo().window(new ArrayList<>(driver.getWindowHandles()).get(0));
            } catch (Exception ignored) {
                driver.close();
                break;
            }
        }
        driver.quit();
    }

    private void fetchDataFromLocation() {
        if (isDirectoryEmpty) {
            document = Jsoup.parse(driver.getPageSource());
            parseAndStoreHTMLFile(Strings.PlanetFitnessParsedHTMLDirectory);
        }
        String location = getLocation();
        fitnessDataModel.locations.add(location);
        fitnessDataModel.additionalDetails.add(getTextFrom(Strings.PlanetFitnessClubHoursXpath.value));
        fitnessDataModel.gymName = Strings.PlanetFitness.name();
        List<MembershipDetailsModel> membershipDetailsModels = getMembershipDetails();
        getAmenitiesDetails(membershipDetailsModels);
        fitnessDataModel.membershipDetails.addAll(membershipDetailsModels);
        fitnessDataModel.gymURL = url;
    }

    private void getAmenitiesDetails(List<MembershipDetailsModel> membershipDetailsModels) {
        Element amenitiesContainer = Xsoup.compile(Strings.PlanetFitnessAmenitiesContainerXpath.value).evaluate(document).getElements().getFirst();
        for (Element amenity: amenitiesContainer.children()) {
            if (amenity.text().toLowerCase().contains(membershipDetailsModels.get(0).membershipTier.toLowerCase()))
                membershipDetailsModels.get(0).amenities.add(amenity.text());
            else membershipDetailsModels.get(1).amenities.add(amenity.text());
        }
    }

    private void traverseThroughAllTheFilesLocally() throws IOException {
        File directory = new File(Strings.PlanetFitnessParsedHTMLDirectory.value);
        List<File> files = List.of(Objects.requireNonNull(directory.listFiles()));
        this.files = files;
        for (File file : files) {
            document = Jsoup.parse(file);
            fetchDataFromLocation();
        }
    }

    private String getLocation() {
        StringBuilder location = new StringBuilder();
        for (int pTagIndex = 1; pTagIndex < 4; pTagIndex++) {
            if (pTagIndex == 3) {
                location.append(getTextFrom(Strings.PlanetFitnessLocationXpath.getValueUsing(String.valueOf(pTagIndex), Strings.ClosingBox)));
                continue;
            }
            location.append(getTextFrom(Strings.PlanetFitnessLocationXpath.getValueUsing(String.valueOf(pTagIndex), Strings.ClosingBox)))
                    .append(Strings.Separator.value);
        }
        return location.toString();
    }

    private List<MembershipDetailsModel> getMembershipDetails() {
        List<MembershipDetailsModel> membershipDetailsModels = new ArrayList<>();
        int memberShipContainerCount = Xsoup
                .compile(
                        Strings.PlanetFitnessMembershipContainerXpath.value
                ).evaluate(document).getElements().get(0).children().size();
        for (int membershipContainer = 1; membershipContainer <= memberShipContainerCount; membershipContainer++) {
            MembershipDetailsModel membershipDetailsModel = new MembershipDetailsModel();
            membershipDetailsModel.membershipTier = Xsoup
                    .compile(
                            Strings.getFormattedXpath(
                                    Strings.PlanetFitnessMembershipContainerChildXpath,
                                    membershipContainer,
                                    Strings.PlanetFitnessMembershipHeaderXpath
                            )
                    ).evaluate(document).getElements().get(0).text();
            Element element = Xsoup
                    .compile(
                            Strings.getFormattedXpath(
                                    Strings.PlanetFitnessMembershipContainerChildXpath,
                                    membershipContainer,
                                    Strings.PlanetFitnessMemberShipDetailsXpath
                            )
                    ).evaluate(document).getElements().get(0);
            for (Element child: element.children()) {
                String childText = child.text();
                Matcher matcher = pricePattern.matcher(childText);
                if (matcher.find()) {
                    if (childText.toLowerCase().contains(Strings.Month.name().toLowerCase()) || childText.toLowerCase().contains(Strings.Monthly.name().toLowerCase()))
                        membershipDetailsModel.monthlyFee = matcher.group();
                    else if (childText.toLowerCase().contains(Strings.Annual.name().toLowerCase()) || childText.toLowerCase().contains(Strings.Yearly.name().toLowerCase()))
                        membershipDetailsModel.annualFee = matcher.group();
                    else membershipDetailsModel.additionalFeeInfo = childText;
                }
            }
            membershipDetailsModels.add(membershipDetailsModel);
        }
        return membershipDetailsModels;
    }

    public List<File> getFiles() {
        return this.files;
    }

    @Override
    public FitnessDataModel getFitnessDataModel() {
        crawlPage();
        return fitnessDataModel;
    }
}
