package WebCrawler.GoodLifeFitness;

import helpers.Strings;

public enum GoodLifeFitnessCurrentPage {
    MembershipPage,
    LocationsPage;

    public String getPath() {
        return Strings.GoodLifeFitnessParsedHTMLDirectory.value + this.name() + Strings.HTMLExtension.value;
    }
}
