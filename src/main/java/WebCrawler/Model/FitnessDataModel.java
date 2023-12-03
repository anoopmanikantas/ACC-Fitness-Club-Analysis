package WebCrawler.Model;

import helpers.Strings;

import java.util.ArrayList;
import java.util.List;

public class FitnessDataModel {
    public List<String> locations = new ArrayList<>();
    public List<MembershipDetailsModel> membershipDetails = new ArrayList<>();
    public String gymName = "";
    public List<String> additionalDetails = new ArrayList<>();
    public String gymURL = "";

    @Override
    public String toString() {
        StringBuilder locations = new StringBuilder();
        for (String location : this.locations) {
            locations.append("\t- ").append(location).append(Strings.NewLine.value);
        }
        StringBuilder membershipDetails = new StringBuilder();
        for (MembershipDetailsModel membershipDetail : this.membershipDetails) {
            membershipDetails.append(membershipDetail).append(Strings.NewLine.value);
        }
        return  "\ngymName: '" + gymName + '\'' + "\n" +
                "locations: \n" + locations +
                "\n" + membershipDetails +
                "\n additionalDetails=" + additionalDetails;
    }
}

