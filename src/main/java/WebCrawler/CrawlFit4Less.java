package WebCrawler;

import WebCrawler.Model.FitnessDataModel;
import WebCrawler.Model.MembershipDetailsModel;
import helpers.Log;
import helpers.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import us.codecraft.xsoup.Xsoup;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

public class CrawlFit4Less extends Crawler implements CrawlerDelegate {
    public static void info(String message) {
        java.util.logging.Logger.getLogger(CrawlFit4Less.class.getName()).info(message);
    }

    public static void error(String error) {
        java.util.logging.Logger.getLogger(CrawlFit4Less.class.getName()).severe(error);
    }
    Fit4LessCurrentPage fit4LessCurrentPage;

    CrawlFit4Less(Strings url) {
        super(url);
    }

    private void crawlPage() {
        createDirectory(Strings.Fit4LessParsedHTMLDirectory);
        if (isDirectoryEmpty) {
            traverseTheWebPage();
        }
        traverseLocally();
    }

    private void traverseTheWebPage() {
        driver.get(url);
        fit4LessCurrentPage = Fit4LessCurrentPage.HomeMembershipPage;
        parseAndStoreHTML();
        driver.findElement(By.xpath(Strings.Fit4LessLocationsWebPageXpath.value)).click();
        fit4LessCurrentPage = Fit4LessCurrentPage.LocationsPage;
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Strings.Fit4LessProvinceDropdownXpath.value))).click();
        scrollAndSelectElementInDropDownUsing(Strings.Fit4LessProvinceOntarioXpath);
        driver.findElement(By.xpath(Strings.Fit4LessCityDropdownXpath.value)).click();
        scrollAndSelectElementInDropDownUsing(Strings.Fit4LessCityWindsorXpath);
        driver.findElement(By.xpath(Strings.Fit4LessFindClubsButtonXpath.value)).click();
        parseAndStoreHTML();
        driver.quit();
    }

    private void scrollAndSelectElementInDropDownUsing(Strings xpath) {
        while (true) {
            actions.sendKeys(Keys.ARROW_DOWN).perform();
            try {
                driver.findElement(By.xpath(xpath.value)).click();
                break;
            } catch (Exception ignored){ }
        }
    }

    private void parseAndStoreHTML() {
        document = Jsoup.parse(driver.getPageSource());
        parseAndStoreHTMLFile(Strings.Fit4LessParsedHTMLDirectory, fit4LessCurrentPage.name());
    }

    private void traverseLocally() {
        try {
            for (Fit4LessCurrentPage page : Fit4LessCurrentPage.values()) {
                document = Jsoup.parse(new File(page.getPath()));
                switch (page) {
                    case HomeMembershipPage -> fitnessDataModel.membershipDetails.addAll(getMembershipDetails());
                    case LocationsPage -> fitnessDataModel.locations.addAll(getLocation());
                }
            }
            fitnessDataModel.gymName = Strings.Fit4Less.name();
        } catch (Exception e) {
            Log.error(e.getMessage());
        }
    }

    private List<MembershipDetailsModel> getMembershipDetails() {
        List<MembershipDetailsModel> membershipDetailsModels = new ArrayList<>();
        for (int membershipDetailsContainer = 1; membershipDetailsContainer <= 2; membershipDetailsContainer++) {
            MembershipDetailsModel membershipDetailsModel = new MembershipDetailsModel();
            Element membershipTopDetails = Xsoup
                    .compile(
                        Strings.getFormattedXpath(
                                Strings.Fit4LessMembershipContainerXpath,
                                membershipDetailsContainer,
                                Strings.Fit4LessMembershipPriceContainerXpath
                        )
                    )
                    .evaluate(document)
                    .getElements()
                    .getFirst();
            String details = membershipTopDetails.text();
            List<String> headerAndAdditionalInfo = Arrays.stream(pricePattern.split(details)).toList();
            Matcher matcher = pricePattern.matcher(details);
            if (matcher.find()) membershipDetailsModel.biWeeklyFee += matcher.group();
            membershipDetailsModel.membershipTier = headerAndAdditionalInfo.getFirst();
            updateMemberShipDetailsWithAmenities(membershipDetailsContainer, membershipDetailsModel);
            membershipDetailsModels.add(membershipDetailsModel);
        }
        return membershipDetailsModels;
    }

    private void updateMemberShipDetailsWithAmenities(
            int index,
            MembershipDetailsModel membershipDetailsModel
    ) {
        Element amenitiesUnorderedListContainer = Xsoup.compile(
                Strings.getFormattedXpath(
                        Strings.Fit4LessMembershipContainerXpath,
                        index,
                        Strings.Fit4LessAmenitiesContainerXpath
                )
        ).evaluate(document).getElements().getFirst();
        for (Element amenityFromContainer: amenitiesUnorderedListContainer.children()) {
            if (amenityFromContainer.className().toLowerCase().contains(Strings.Fit4LessCheckedClassName.value)) {
                Matcher matcher = pricePattern.matcher(amenityFromContainer.text());
                if (matcher.find()) membershipDetailsModel.annualFee = matcher.group();
                else membershipDetailsModel.amenities.add(amenityFromContainer.text());
            }
        }
    }

    private List<String> getLocation() {
        List<String> locations = new ArrayList<>();
        Element locationsContainer = Xsoup.compile(Strings.Fit4LessLocationsContainerXpath.value).evaluate(document).getElements().getFirst();
        for (Element locationContainer : locationsContainer.children()) {
            locations.add(locationContainer.children().get(1).text());
        }
        return locations;
    }

    @Override
    public FitnessDataModel getFitnessDataModel() {
        crawlPage();
        return fitnessDataModel;
    }
}
