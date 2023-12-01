package helpers;

import WebCrawler.Model.FitnessDataModel;
import WebCrawler.Model.MembershipDetailsModel;

import java.util.*;

public class InvertedIndex {
    private final Map<String, List<FitnessDataModel>> invertedIndex;
    private final List<FitnessDataModel> fitnessDataModels;
    private final List<String> tokenizedWords;

    public InvertedIndex(List<FitnessDataModel> fitnessDataModels) {
        invertedIndex = new HashMap<>();
        this.fitnessDataModels = fitnessDataModels;
        this.tokenizedWords = new ArrayList<>();
    }

    private void convertModelsToInvertedIndex() {
        for (FitnessDataModel model : fitnessDataModels) {
            tokenizeStringAndInsertIntoInvertedIndex(model.gymName, model);
            for (String location : model.locations) {
                tokenizeStringAndInsertIntoInvertedIndex(location, model);
            }
            for (String detail : model.additionalDetails) {
                tokenizeStringAndInsertIntoInvertedIndex(detail, model);
            }
            for (MembershipDetailsModel membership : model.membershipDetails) {
                tokenizeStringAndInsertIntoInvertedIndex(membership.membershipTier, model);
                tokenizeStringAndInsertIntoInvertedIndex(membership.monthlyFee, model);
                tokenizeStringAndInsertIntoInvertedIndex(membership.annualFee, model);
                tokenizeStringAndInsertIntoInvertedIndex(membership.additionalFeeInfo, model);
                tokenizeStringAndInsertIntoInvertedIndex(membership.biWeeklyFee, model);
                for (String amenity : membership.amenities) {
                    tokenizeStringAndInsertIntoInvertedIndex(amenity, model);
                }
            }
        }
    }

    private void tokenizeStringAndInsertIntoInvertedIndex(String text, FitnessDataModel model) {
        List<String> tokens = Arrays.stream(text.split(Strings.RegexSpaceAndNonWordCharacters.value)).toList();
        for (String token : tokens) {
            if (!token.isBlank()) {
                if (!invertedIndex.containsKey(token.strip())) {
                    invertedIndex.put(token.strip().toLowerCase(), new ArrayList<>());
                    tokenizedWords.add(token.strip().toLowerCase());
                }
                invertedIndex.get(token.strip().toLowerCase()).add(model);
            }
        }
    }

    public Map<String, List<FitnessDataModel>> getInvertedIndex() {
        convertModelsToInvertedIndex();
        return invertedIndex;
    }

    public List<String> getTokenizedWords() {
        return tokenizedWords;
    }

    public static void main(String[] args) {
        FitnessDataModel fitnessDataModel = new FitnessDataModel();
        fitnessDataModel.locations = List.of("705 dougall avenue, Windsor, ON, Canada", "708 dougall avenue, Windsor, ON, Canada");
        fitnessDataModel.gymName = "Anoop's Gym";
        MembershipDetailsModel membershipDetailsModel = new MembershipDetailsModel();
        membershipDetailsModel.amenities = List.of("Some amenity", "Swimming pool", "steam bath");
        membershipDetailsModel.monthlyFee = "$49";
        membershipDetailsModel.membershipTier = "Basic";
        fitnessDataModel.membershipDetails = List.of(membershipDetailsModel);

        InvertedIndex invertedIndex = new InvertedIndex(List.of(fitnessDataModel));
        invertedIndex.getInvertedIndex().forEach((key, value)-> System.out.printf("%s:\t%s\n", key, value));

        System.out.println("Tokenized Words\n");
        for (String tokenizedWord : invertedIndex.getTokenizedWords()) {
            System.out.println(tokenizedWord);
        }
    }
}
