package WebCrawler;

import WebCrawler.Model.FitnessDataModel;
import WebCrawler.Model.MembershipDetailsModel;
import helpers.Log;
import helpers.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import us.codecraft.xsoup.Xsoup;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlGoodLifeFitness extends Crawler implements CrawlerDelegate{
    public static void info(String message) {
        java.util.logging.Logger.getLogger(CrawlGoodLifeFitness.class.getName()).info(message);
    }

    public static void error(String error) {
        java.util.logging.Logger.getLogger(CrawlGoodLifeFitness.class.getName()).severe(error);
    }

    GoodLifeFitnessCurrentPage goodLifeFitnessCurrentPage;

    CrawlGoodLifeFitness(Strings url) {
        super(url);
    }

    private void crawlPage() {
        createDirectory(Strings.GoodLifeFitnessParsedHTMLDirectory);
        if (isDirectoryEmpty) {
            traverseWebPage();
        }
        traverseWebPageLocally();
    }

    private void traverseWebPage() {
        driver.get(url);
        goodLifeFitnessCurrentPage = GoodLifeFitnessCurrentPage.LocationsPage;
        driver.findElement(By.xpath(Strings.GoodLifeFitnessLocationNavigationXpath.value)).click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Strings.GoodLifeFitnessLocationsHeaderXpath.value)));
        parseAndStoreHTML();
        goodLifeFitnessCurrentPage = GoodLifeFitnessCurrentPage.MembershipPage;
        driver.findElement(By.xpath(Strings.GoodLifeFitnessMembershipNavigationXpath.value)).click();
        parseAndStoreHTML();
        driver.quit();
    }

    private List<MembershipDetailsModel> getMembershipDetails() {
        Map<Strings, List<String>> amenitiesData = getAmenities();
        Map<Strings, String> pricingDetails = getPricingDetails();
        List<Strings> plans = List.of(Strings.GoodLifeFitnessEssentialPlanString, Strings.GoodLifeFitnessUltimatePlanString, Strings.GoodLifeFitnessPerformancePlanString);
        List<MembershipDetailsModel> membershipDetailsModels = new ArrayList<>();
        for (Strings plan : plans) {
            MembershipDetailsModel membershipDetailsModel = new MembershipDetailsModel();
            membershipDetailsModel.membershipTier = plan.value;
            membershipDetailsModel.biWeeklyFee = pricingDetails.get(plan);
            membershipDetailsModel.amenities = amenitiesData.get(plan);
            membershipDetailsModels.add(membershipDetailsModel);
        }
        return membershipDetailsModels;
    }

    private Map<Strings, String> getPricingDetails() {
        Element pricingRow = Xsoup.compile(Strings.GoodLifeFitnessPricingTableRowXpath.value).evaluate(document).getElements().getFirst();
        Map<Strings, String> prices = new HashMap<>();
        for (Element pricingDetails : pricingRow.children()) {
            if (!pricingDetails.className().toLowerCase().contains(Strings.GoodLifeFitnessAmenitiesIsHiddenString.value.toLowerCase())) {
                Matcher matcher = pricePattern.matcher(pricingDetails.text());
                if (matcher.find()) {
                    if (pricingDetails.text().toLowerCase().contains(Strings.GoodLifeFitnessUltimatePlanString.value.toLowerCase()))
                        prices.put(Strings.GoodLifeFitnessUltimatePlanString, matcher.group());
                    else if (pricingDetails.text().toLowerCase().contains(Strings.GoodLifeFitnessPerformancePlanString.value.toLowerCase()))
                        prices.put(Strings.GoodLifeFitnessPerformancePlanString, matcher.group());
                    else prices.put(Strings.GoodLifeFitnessEssentialPlanString, matcher.group());
                }
            }
        }
        System.out.println(prices);
        return prices;
    }

    private Map<Strings, List<String>> getAmenities() {
        Map<Strings, List<String>> amenitiesDetails = new HashMap<>();
            Element amenitiesTableRow = Xsoup.compile(Strings.GoodLifeFitnessAmenitiesTableRowXpath.value).evaluate(document).getElements().getFirst();
            List<Element> amenitiesTableRowChildren = amenitiesTableRow.children();
            int count = 0;
            for (Element amenitiesTableRowChild : amenitiesTableRowChildren) {
                String className = amenitiesTableRowChild.className().toLowerCase();
                if (
                        className.contains(Strings.GoodLifeFitnessAmenitiesAvailableString.value.toLowerCase())
                                && !className.contains(Strings.GoodLifeFitnessAmenitiesIsHiddenString.value.toLowerCase())
                ) {
                    count++;
                    List<String> amenities = new ArrayList<>();
                    for (Element amenitiesTableColumnChild : amenitiesTableRowChild.children())
                        for (Element child : amenitiesTableColumnChild.children())
                            amenities.add(child.text());
                    switch (count) {
                        case 1 -> amenitiesDetails.put(Strings.GoodLifeFitnessEssentialPlanString, amenities);
                        case 2 -> amenitiesDetails.put(Strings.GoodLifeFitnessUltimatePlanString, amenities);
                        case 3 -> amenitiesDetails.put(Strings.GoodLifeFitnessPerformancePlanString, amenities);
                    }
                }
                if (count == 3) break;
            }
        return amenitiesDetails;
    }

    private List<String> getLocations() {
        List<String> locations = new ArrayList<>();
        Pattern pattern = Pattern.compile(Strings.RegexPhoneNumber.value);
        Element locationsContainer = Xsoup.compile(Strings.GoodLifeFitnessLocationsListContainerXpath.value).evaluate(document).getElements().getFirst();
        for (Element location : locationsContainer.children()) {
            if (location.text().toLowerCase().contains(Strings.Windsor.name().toLowerCase())) {
                locations.add(location.child(0).child(0).child(0).text() + pattern.split(Strings.Separator.value + location.child(0).child(0).child(2).text())[0] );
            }
        }
        return locations;
    }

    private void traverseWebPageLocally() {
        try {
            for (GoodLifeFitnessCurrentPage page : GoodLifeFitnessCurrentPage.values()) {
                document = Jsoup.parse(new File(page.getPath()));
                switch (page) {
                    case LocationsPage -> fitnessDataModel.locations.addAll(getLocations());
                    case MembershipPage -> fitnessDataModel.membershipDetails.addAll(getMembershipDetails());
                }
            }
            fitnessDataModel.gymName = Strings.GoodLifeFitness.name();
        } catch (Exception e) {
            Log.error(e.getMessage());
        }
    }

    private void parseAndStoreHTML() {
        document = Jsoup.parse(driver.getPageSource());
        parseAndStoreHTMLFile(Strings.GoodLifeFitnessParsedHTMLDirectory, goodLifeFitnessCurrentPage.name());
    }

    @Override
    public FitnessDataModel getFitnessDataModel() {
        crawlPage();
        return fitnessDataModel;
    }
}
