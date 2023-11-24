package WebCrawler.Model;

import java.util.ArrayList;
import java.util.List;

public class FitnessDataModel {
    public List<String> locations = new ArrayList<>();
    public List<MembershipDetailsModel> membershipDetails = new ArrayList<>();
    public String gymName = "";
    public List<String> additionalDetails = new ArrayList<>();

    @Override
    public String toString() {
        return "FitnessDataModel{" +
                "locations=" + locations +
                "\n, membershipDetails=" + membershipDetails +
                "\n, gymName='" + gymName + '\'' +
                "\n, additionalDetails=" + additionalDetails +
                "\n}";
    }
}

