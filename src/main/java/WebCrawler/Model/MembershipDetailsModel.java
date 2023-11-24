package WebCrawler.Model;

import java.util.ArrayList;
import java.util.List;

public class MembershipDetailsModel {
    public String membershipTier = "";
    public List<String> amenities = new ArrayList<>();
    public String monthlyFee = "";
    public String annualFee = "";
    public String additionalFeeInfo = "";

    @Override
    public String toString() {
        return "\tMembershipDetailsModel{" +
                "\n\tmembershipTier='" + membershipTier + '\'' +
                "\n\tamenities=" + amenities +
                "\n\tmonthlyFee='" + monthlyFee + '\'' +
                "\n\tannualFee='" + annualFee + '\'' +
                "\n\tadditionalFeeInfo='" + additionalFeeInfo + '\'' +
                "\n\t" +'}';
    }
}
