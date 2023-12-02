package helpers;

public enum Strings {
    //- General Strings
    Empty,
    ArgumentRemoteAllowOrigins("--remote-allow-origins=*"),
    SourceParsedHTMLDirectory("src/main/java/parsedHTML/"),
    UTF8("UTF-8"),
    HTMLExtension(".html"),
    ForwardSlash("/"),
    ChildrenXpath(".//*"),
    ClosingBox("]"),
    Separator(", "),
    Windsor,
    Monthly,
    Month,
    Annual,
    Yearly,

    //- Regex
    RegexSpecialCharacters("[^a-zA-Z0-9]"),
    RegexPrice("\\$\\s?\\d{1,3}(\\.\\d{1,3})?"),
    RegexPhoneNumber("\\(\\d{3}\\) \\d{3}-\\d{4}"),
    RegexSpaceAndNonWordCharacters("\\s+|\\W+"),

    //- Planet Fitness
    PlanetFitnessWebURL("https://www.planetfitness.ca/"),
    PlanetFitness(Empty.value),
    PlanetFitnessParsedHTMLDirectory(SourceParsedHTMLDirectory.value + PlanetFitness.name() + ForwardSlash.value),
    PlanetFitnessShowAllLocationsButtonXpath("/html/body/div[3]/div/div/div[2]/div[1]/div/div[3]/div/a"),
    PlanetFitnessLocationsContainerXpath("/html/body/div[2]/div[2]/div[2]/div"),
    PlanetFitnessLocationsContainerFirstXpath("/html/body/div[2]/div[2]/div[2]/div/div["),
    PlanetFitnessLocationsContainerSecondXPath("]/div/div[2]/div[2]/a[1]"),
    PlanetFitnessLocationsHeaderXpath("/html/body/div[2]/div[2]/div[2]/h1"),
    PlanetFitnessClubHoursXpath("/html/body/div[1]/div[2]/div[1]/div[1]/div[3]/p"),
    PlanetFitnessLocationXpath("/html/body/div[1]/div[2]/div[1]/div[1]/div[2]/p["),
    PlanetFitnessMembershipContainerXpath("/html/body/div[1]/div[2]/div[1]/div[4]"),
    PlanetFitnessMembershipContainerChildXpath("/html/body/div[1]/div[2]/div[1]/div[4]/div["),
    PlanetFitnessMembershipHeaderXpath("]/h2"),
    PlanetFitnessMemberShipDetailsXpath("]/div[3]/div/div[2]"),
    PlanetFitnessAmenitiesContainerXpath("/html/body/div[1]/div[2]/div[2]/div/div[2]"),

    //- Good Life Fitness
    GoodLifeFitness(Empty.value),
    GoodLifeFitnessParsedHTMLDirectory(SourceParsedHTMLDirectory.value + GoodLifeFitness.name() + ForwardSlash.value),
    GoodLifeFitnessWebURL("https://www.goodlifefitness.com/"),
    GoodLifeFitnessLocationsHeaderXpath("/html/body/div[1]/div/div[2]/div/div[1]/div/div/div/div/div/div/div/div/div[1]/div/div[2]/div/h1"),
    GoodLifeFitnessLocationNavigationXpath("/html/body/div[1]/div/div[1]/div/div/div/div/div[1]/header/div/div[1]/div[1]/nav/ul/li[2]/div/a"),
    GoodLifeFitnessMembershipNavigationXpath("/html/body/div[1]/div/div[1]/div/div/div/div/div[1]/header/div/div[1]/div[1]/nav/ul/li[4]/div/a"),
    GoodLifeFitnessLocationsListContainerXpath("/html/body/div[1]/div/div[2]/div/div[2]/div/div[3]/div/div[5]/div/div[1]/ul"),
    GoodLifeFitnessAmenitiesTableRowXpath("/html/body/div[1]/div/div[2]/div/div[3]/div/div/div/div/div/div/div/div[1]/table/tbody/tr[4]"),
    GoodLifeFitnessPricingTableRowXpath("/html/body/div[1]/div/div[2]/div/div[3]/div/div/div/div/div/div/div/div[1]/table/thead/tr"),
    GoodLifeFitnessAmenitiesAvailableString("available"),
    GoodLifeFitnessAmenitiesIsHiddenString("u-is-hidden"),
    GoodLifeFitnessEssentialPlanString("Essential"),
    GoodLifeFitnessUltimatePlanString("Ultimate"),
    GoodLifeFitnessPerformancePlanString("Performance"),

    //- Fit4Less
    Fit4Less(Empty.value),
    Fit4LessParsedHTMLDirectory(SourceParsedHTMLDirectory.value + Fit4Less.name() + ForwardSlash.value),
    Fit4LessWebURL("https://www.fit4less.ca/"),
    Fit4LessCheckedClassName("checked"),
    Fit4LessProvinceDropdownXpath("/html/body/form/div[7]/div[2]/div[1]/div/div[1]/div[1]"),
    Fit4LessProvinceOntarioXpath("/html/body/form/div[7]/div[2]/div[1]/div/div[1]/div[1]/ul/li[7]"),
    Fit4LessCityDropdownXpath("/html/body/form/div[7]/div[2]/div[1]/div/div[1]/div[2]"),
    Fit4LessCityWindsorXpath("/html/body/form/div[7]/div[2]/div[1]/div/div[1]/div[2]/ul/li[63]"),
    Fit4LessFindClubsButtonXpath("/html/body/form/div[7]/div[2]/div[1]/div/div[1]/div[3]/a"),
    Fit4LessMembershipContainerXpath("/html/body/form/div[7]/div[2]/div/div/div/div["),
    Fit4LessMembershipPriceContainerXpath("]/div[1]/div[1]/div"),
    Fit4LessAmenitiesContainerXpath("]/div[1]/div[2]/ul/ul"),
    Fit4LessLocationsContainerXpath("/html/body/form/div[7]/div[1]/div[3]/div[2]/div[1]/div"),
    Fit4LessLocationsWebPageXpath("/html/body/form/div[6]/header/div[2]/div/div[2]/ul/li[2]");

    public final String value;

    Strings() {
        this.value = "";
    }
    Strings(String value) {
        this.value = value;
    }

    public static String getFormattedXpath(Strings string1, int index, Strings strings2) {
        return String.format("%s%d%s", string1.value, index, strings2.value);
    }

    public String getValueUsing(String string, Strings strings) {
        return this.value + string + strings.value;
    }
}
