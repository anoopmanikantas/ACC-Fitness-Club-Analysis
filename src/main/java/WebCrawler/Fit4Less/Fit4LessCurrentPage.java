package WebCrawler.Fit4Less;

import helpers.Strings;

public enum Fit4LessCurrentPage {
    HomeMembershipPage,
    LocationsPage;

    public String getPath() {
        return Strings.Fit4LessParsedHTMLDirectory.value + this.name() + Strings.HTMLExtension.value;
    }

}