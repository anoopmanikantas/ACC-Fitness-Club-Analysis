package Main;

import Main.Menu.*;
import WebCrawler.Model.FitnessDataModel;
import WebCrawler.Model.MembershipDetailsModel;
import helpers.*;
import lib.ACCException;
import lib.E;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends MainExtension{
    Main() {
        super();
    }

    @SuppressWarnings("All")
    public static void main(String[] args) {
        Main main = new Main();
        main.display(Strings.WelcomeString);
        while (true) {
            main.displayMainMenu();
            main.getInputFor(Menus.MainMenu);
            main.getInputFor(Menus.SubMenuForSearchByDetails);
            switch (main.subMenuForSearchByDetails) {
                case SearchByGymDetails -> main.getInputFor(Menus.SubMenuForSearchByGymDetails);
                case SearchByMembershipFee -> main.getInputFor(Menus.SubMenuForSearchByMembershipFee);
            }
        }
    }

    private void getInputFor(Menus menu) {
        int option;
        while (true) {
            System.out.print(Strings.MenuEnterAnOption.value);
            try {
                option = getOptionInput();
                if (isValidMenuOptionFor(option, menu)) break;
            } catch (ACCException exception) {
                handle(exception);
            }
        }
        handleMenuEntryFor(menu);
    }

    private int getOptionInput() throws ACCException {
        int option;
        try {
            Scanner scanner = new Scanner(System.in);
            option = scanner.nextInt();
        } catch (Exception ignored) {
            throw new ACCException(E.InputNotInteger);
        }
        return option;
    }

    private boolean isValidMenuOptionFor(int option, Menus menus) throws ACCException{
        if (option == 0) {
            display(Strings.GoodByeMessage);
            System.exit(0);
        }
        option--;
        try {
            switch (menus) {
                case MainMenu -> mainMenu = MainMenu.values()[option];
                case SubMenuForSearchByDetails -> subMenuForSearchByDetails = SubMenuForSearchByDetails.values()[option];
                case SubMenuForSearchByGymDetails -> subMenuForSearchByGymDetails = SubMenuForSearchByGymDetails.values()[option];
                case SubMenuForSearchByMembershipFee -> subMenuForSearchByMembershipFee = SubMenuForSearchByMembershipFee.values()[option];
            }
            return true;
        } catch (Exception e) {
            throw new ACCException(E.InputOptionDoesNotExist);
        }
    }

    private void handleMenuEntryFor(Menus menu) {
        System.out.println();
        switch (menu) {
            case MainMenu -> {
                if (Objects.requireNonNull(mainMenu) == MainMenu.SearchByDetails) displaySubMenuForSearchDetails();
            }
            case SubMenuForSearchByDetails -> {
                switch (subMenuForSearchByDetails) {
                    case SearchByGymDetails -> displaySubMenuForSearchByGymDetails();
                    case SearchByMembershipFee -> displaySubMenuForSearchByMembershipFee();
                }
            }
            case SubMenuForSearchByGymDetails -> {
                switch (subMenuForSearchByGymDetails) {
                    case GymName -> displayResultsForGymName();
                    case Location -> displayResultsForLocations();
                    case Amenities -> displayResultsForAmenities();
                }
            }
            case SubMenuForSearchByMembershipFee -> displayResultsForMembershipFeeLessThan();
        }
    }

    private void displayResultsForMembershipFeeLessThan() {
        String userInput;
        String formattedUserInput;
        Pattern pattern = Pattern.compile(Strings.RegexPrice.value);
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.print(Strings.EnterFee.value);
                userInput = scanner.nextLine();
                if (userInput.strip().split(Strings.RegexSpace.value).length > 1) throw new ACCException(E.InvalidFee);
                formattedUserInput = String.format(Strings.Price.value, userInput);
                if (!pattern.matcher(formattedUserInput).find()) throw new ACCException(E.InvalidFee);
                List<FitnessDataModel>resultFitnessDataModel = getResultFitnessModelsForFee(formattedUserInput, pattern);
                display(Strings.Output);
                if (resultFitnessDataModel.isEmpty()) {
                    System.out.println(Strings.NoGymFound.value);
                } else {
                    for (FitnessDataModel fitnessDataModel : resultFitnessDataModel) {
                        System.out.println(fitnessDataModel);
                    }
                }
                break;
            } catch (ACCException exception){
                handle(exception);
            }
        }

    }

    private List<FitnessDataModel> getResultFitnessModelsForFee(String formattedUserInput, Pattern pattern) throws ACCException {
        BigDecimal userInputFee = extractValue(formattedUserInput);
        List<FitnessDataModel> resultFitnessDataModel = new ArrayList<>();
        invertedIndexMap.forEach((key, value) -> {
            Matcher matcher = pattern.matcher(key);
            if (matcher.find()) {
                BigDecimal feeFromInvertedIndex;
                try {
                    feeFromInvertedIndex = extractValue(matcher.group());
                } catch (ACCException e) {
                    throw new RuntimeException(e);
                }
                for (FitnessDataModel fitnessDataModel : value) {
                    for (MembershipDetailsModel membershipDetail : fitnessDataModel.membershipDetails) {
                        if ((!membershipDetail.biWeeklyFee.isBlank() || !membershipDetail.annualFee.isBlank() || !membershipDetail.monthlyFee.isBlank()) && feeFromInvertedIndex.compareTo(userInputFee) < 0) {
                            resultFitnessDataModel.add(fitnessDataModel);
                        }
                    }
                }
            }
        });
        return resultFitnessDataModel;
    }

    public static BigDecimal extractValue(String text) throws ACCException {
        Pattern pattern = Pattern.compile(Strings.RegexPriceExtraction.value);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            try {
                return new BigDecimal(matcher.group(1).replace(Strings.DollarSymbol.value, Strings.Empty.value).trim());
            } catch (Exception ignored) {
                return BigDecimal.valueOf(0);
            }
        } else {
            throw new ACCException(E.InvalidFee);
        }
    }

    private void displayResultsForLocations() {
        List<String> locations = invertedIndex.getLocations();
        String userInput;
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.print(Strings.EnterLocation.value);
                userInput = scanner.nextLine();
                if (userInput.strip().isBlank()) throw new ACCException(E.StringInputCannotBeEmpty);
                if (userInput.strip().split(Strings.RegexSpaceAndNonWordCharacters.value).length > 1) throw new ACCException(E.MoreThanOneWordEntered);
                display(Strings.Output);
                String suggestedWord = suggestWords(userInput, locations, false);
                displayCountFor(suggestedWord);
                List<String> results = new ArrayList<>();
                System.out.println();
                List<FitnessDataModel> suggestedModels = invertedIndexMap.get(suggestedWord);
                if (suggestedModels.isEmpty()) display(Strings.NoGymFound);
                for (FitnessDataModel fitnessDataModel : suggestedModels) {
                    String formattedString = String.format(Strings.LocationDetails.value, fitnessDataModel.gymName, fitnessDataModel.gymURL);
                    if (!results.contains(formattedString)) {
                        results.add(formattedString);
                        System.out.printf(formattedString);
                    }
                }
                break;
            } catch (ACCException exception) {
                handle(exception);
            }
        }
    }

    private void displayResultsForAmenities() {
        List<String> amenities = invertedIndex.getAmenities();
        String userInput;
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.print(Strings.EnterAmenity.value);
                userInput = scanner.nextLine();
                if (userInput.strip().isBlank()) throw new ACCException(E.StringInputCannotBeEmpty);
                if (userInput.strip().split(Strings.RegexSpaceAndNonWordCharacters.value).length > 1) throw new ACCException(E.MoreThanOneWordEntered);
                display(Strings.Output);
                String suggestedWord = suggestWords(userInput, amenities, true);
                displayCountFor(suggestedWord);
                List<String> results = new ArrayList<>();
                System.out.println();
                List<FitnessDataModel> suggestedModels = invertedIndexMap.get(suggestedWord);
                if (suggestedModels.isEmpty()) display(Strings.NoGymFound);
                for (FitnessDataModel fitnessDataModel : suggestedModels) {
                         String formattedString = String.format(Strings.AmenitiesDetails.value, fitnessDataModel.gymName, fitnessDataModel.gymURL);
                         if (!results.contains(formattedString)) {
                             results.add(formattedString);
                             System.out.printf(formattedString);
                         }
                }
                break;
            } catch (ACCException exception) {
                handle(exception);
            }
        }
    }

    private void displayResultsForGymName() {

        List<String> gyms = invertedIndex.getGymNames();
        String userInput;
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.print(Strings.EnterGymName.value);
                userInput = scanner.nextLine();
                if (userInput.strip().isBlank()) throw new ACCException(E.StringInputCannotBeEmpty);
                String suggestedWord = suggestWords(userInput, gyms, false);
                display(Strings.Output);
                for (FitnessDataModel fitnessDataModel : invertedIndexMap.get(suggestedWord)) {
                    if (!gyms.contains(fitnessDataModel.gymName.toLowerCase())) throw new ACCException(E.GymNotFound); {
                        displayCountFor(suggestedWord);
                        System.out.println(fitnessDataModel);
                    }
                }
                break;
            } catch (ACCException exception) {
                handle(exception);
            }
        }
    }

    private void displayCountFor(String suggestedWord) {
        System.out.println();
        System.out.printf(Strings.WordSearchFrequencyFor.value, suggestedWord, wordSearchCount.getCountFor(suggestedWord));
        System.out.println();
        System.out.println();
        for (Map<String, Integer> stringIntegerMap : wordFrequency.getFrequencyCountFor(suggestedWord).get(suggestedWord)) {
            stringIntegerMap.forEach((fileName, count) -> System.out.printf(Strings.WordFrequency.value, fileName, count));
        }
    }
}
