package helpers;

import WebCrawler.Model.FitnessDataModel;
import WebCrawler.Model.MembershipDetailsModel;

import java.util.*;

public class InvertedIndex {
    private final Map<String, List<FitnessDataModel>> invertedIndex;
    private final List<FitnessDataModel> fitnessDataModels;
    private List<String> tokenizedWords;

    private List<String> gymNames;
    private List<String> locations;
    private List<String> amenities;

    private void clearData() {
        invertedIndex.clear();
        tokenizedWords = new ArrayList<>();
        gymNames = new ArrayList<>();
        locations = new ArrayList<>();
        amenities = new ArrayList<>();
    }

    public InvertedIndex(List<FitnessDataModel> fitnessDataModels) {
        invertedIndex = new HashMap<>();
        this.fitnessDataModels = fitnessDataModels;
        this.tokenizedWords = new ArrayList<>();
    }

    private void convertModelsToInvertedIndex() {
        clearData();
        for (FitnessDataModel model : fitnessDataModels) {
            gymNames.addAll(tokenizeStringAndInsertIntoInvertedIndex(model.gymName, model));
            for (String location : model.locations) {
                locations.addAll(tokenizeStringAndInsertIntoInvertedIndex(location, model));
            }
            for (String detail : model.additionalDetails) {
                tokenizeStringAndInsertIntoInvertedIndex(detail, model);
            }
            for (MembershipDetailsModel membership : model.membershipDetails) {
                tokenizeStringAndInsertIntoInvertedIndex(membership.membershipTier, model);
                insertIntoInvertedIndexWithoutTokenizing(membership.monthlyFee, model);
                insertIntoInvertedIndexWithoutTokenizing(membership.annualFee, model);
                tokenizeStringAndInsertIntoInvertedIndex(membership.additionalFeeInfo, model);
                insertIntoInvertedIndexWithoutTokenizing(membership.biWeeklyFee, model);
                List<String> amenities = new ArrayList<>();
                for (String amenity : membership.amenities) {
                    amenities.addAll(tokenizeStringAndInsertIntoInvertedIndex(amenity, model));
                }
                this.amenities.addAll(amenities);
            }
        }
    }

    private void insertIntoInvertedIndexWithoutTokenizing(String text, FitnessDataModel model) {
        if (!text.isBlank()) {
            String token = text.strip().toLowerCase();
            if (!invertedIndex.containsKey(token)) {
                invertedIndex.put(token, new ArrayList<>());
                tokenizedWords.add(token);
            }
            invertedIndex.get(token).add(model);
        }
    }

    private List<String> tokenizeStringAndInsertIntoInvertedIndex(String text, FitnessDataModel model) {
        List<String> uniqueTokens = new ArrayList<>();
        List<String> tokens = (new StringTokenizer(text)).getTokenizedWords();
        for (String token : tokens) {
            if (!token.isBlank()) {
                if (!uniqueTokens.contains(token)) uniqueTokens.add(token);
                if (!invertedIndex.containsKey(token.strip())) {
                    invertedIndex.put(token.strip().toLowerCase(), new ArrayList<>());
                    tokenizedWords.add(token.strip().toLowerCase());
                }
                invertedIndex.get(token.strip().toLowerCase()).add(model);
            }
        }
        return uniqueTokens;
    }

    public List<String> getGymNames() {
        return gymNames;
    }

    public List<String> getLocations() {
        return locations;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public Map<String, List<FitnessDataModel>> getInvertedIndex() {
        convertModelsToInvertedIndex();
        return invertedIndex;
    }

    public List<String> getTokenizedWords() {
        convertModelsToInvertedIndex();
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
