package WebCrawler.Model;

import helpers.Strings;

import java.util.ArrayList;
import java.util.List;

public class MembershipDetailsModel {
    public String membershipTier = "";
    public List<String> amenities = new ArrayList<>();
    public String monthlyFee = "";
    public String annualFee = "";
    public String additionalFeeInfo = "";
    public String biWeeklyFee = "";

    @Override
    public String toString() {
        StringBuilder amenities = new StringBuilder();
        for (String amenity : this.amenities)
            amenities.append("\t\t- ").append(amenity).append(Strings.NewLine.value);
        return "\n\tMembershipDetails" +
                "\n\t\tmembershipTier= '" + membershipTier + '\'' +
                "\n\t\tamenities=\n" + amenities +
                "\n\t\tmonthlyFee= '" + monthlyFee + '\'' +
                "\n\t\tannualFee= '" + annualFee + '\'' +
                "\n\t\tadditionalFeeInfo= '" + additionalFeeInfo + '\'' +
                "\n\t\tbiWeeklyFee= '" + biWeeklyFee + '\'';
    }
}
