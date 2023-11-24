package helpers;

public enum Strings {
    //- General Strings
    IgnoreSSLErrors("--ignore-ssl-errors=yes"),
    IgnoreCertificateErrors("--ignore-certificate-errors"),
    AllowInsecureLocalHosts("--allow-insecure-localhost"),
    UseAutomationExtension("useAutomationExtension"),
    Empty(""),
    SourceParsedHTMLDirectory("src/main/java/parsedHTML/"),
    HTMLExtension(".html"),
    JSONExtension(".json"),
    ForwardSlash("/"),
    ChildrenXpath(".//*"),
    ClosingBox("]"),
    Separator(", "),

    //- Regex
    RegexSpecialCharacters("[^a-zA-Z0-9]"),

    //- Planet Fitness
    PlanetFitnessWebURL("https://www.planetfitness.ca/"),
    PlanetFitness(Empty.value),
    PlanetFitnessParsedHTMLDirectory(SourceParsedHTMLDirectory.value + PlanetFitness.name() + ForwardSlash.value),
    PlanetFitnessParsedHTMLFile(PlanetFitnessParsedHTMLDirectory.value + "PlanetFitnessHome" + HTMLExtension.value),
    PlanetFitnessShowAllLocationsButtonXpath("/html/body/div[3]/div/div/div[2]/div[1]/div/div[3]/div/a"),
    PlanetFitnessLocationsContainerXpath("/html/body/div[2]/div[2]/div[2]/div"),
    PlanetFitnessLocationsContainerFirstXpath("/html/body/div[2]/div[2]/div[2]/div/div["),
    PlanetFitnessLocationsContainerSecondXPath("]/div/div[2]/div[2]/a[1]"),
    PlanetFitnessLocationsHeaderXpath("/html/body/div[2]/div[2]/div[2]/h1"),
    PlanetFitnessClubHoursXpath("/html/body/div[1]/div[2]/div[1]/div[1]/div[3]/p"),
    PlanetFitnessLocationXpath("/html/body/div[1]/div[2]/div[1]/div[1]/div[2]/p["),

    //- Good Life Fitness
    GoodLifeFitness(Empty.value),
    GoodLifeFitnessParsedHTMLDirectory(SourceParsedHTMLDirectory.value + GoodLifeFitness.name() + ForwardSlash.value),
    GoodLifeFitnessWebURL("https://www.planetfitness.ca/"), // TODO: Update URL

    //- Movati Athletic
    MovatiAthletic(Empty.value),
    MovatiAthleticParsedHTMLDirectory(SourceParsedHTMLDirectory.value + MovatiAthletic.name() + ForwardSlash.value),
    MovatiAthleticWebURL("https://www.planetfitness.ca/"); // TODO: Update URL

    public final String value;

    Strings(String value) {
        this.value = value;
    }

    public String getValueUsing(String string) {
        return this.value + string;
    }

    public String getValueUsing(String string, Strings strings) {
        return this.value + string + strings.value;
    }
}
