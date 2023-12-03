package Main;

import Main.Menu.*;
import WebCrawler.Model.FitnessDataModel;
import WebCrawler.MultiPageCrawler;
import helpers.*;
import lib.ACCException;
import lib.E;

import java.util.*;

public class MainExtension{
    List<FitnessDataModel> fitnessDataModels;
    WordSearchCount wordSearchCount;
    WordCompletion wordCompletion;
    WordFrequency wordFrequency;
    InvertedIndex invertedIndex;
    SpellCheck spellCheck;
    MainMenu mainMenu;
    SubMenuForSearchByDetails subMenuForSearchByDetails;

    public MainExtension(){
        fitnessDataModels = new MultiPageCrawler().getAllFitnessModels();
        invertedIndex = new InvertedIndex(fitnessDataModels);
        List<String> tokenizedWords = invertedIndex.getTokenizedWords();
        wordCompletion = WordCompletion.getInstanceUsing(tokenizedWords);
        spellCheck = new SpellCheck();
        wordFrequency = new WordFrequency();
        wordSearchCount = WordSearchCount.getInstanceUsing(tokenizedWords);
        invertedIndexMap = invertedIndex.getInvertedIndex();
    }

    SubMenuForSearchByGymDetails subMenuForSearchByGymDetails;
    SubMenuForSearchByMembershipFee subMenuForSearchByMembershipFee;
    Map<String, List<FitnessDataModel>> invertedIndexMap;

    void display(Strings string) {
        System.out.println(string.value);
    }
    void handle(ACCException exception) {
        System.out.println(exception.exception.getMessage());
    }

    String suggestWords(String userInput, List<String> list, boolean isAmenities) {
        int min = Integer.MAX_VALUE;
        String minWord = Strings.Empty.value;
        int secondMin = Integer.MAX_VALUE;
        String secondMinWord = Strings.Empty.value;
        for (String element : list) {
            int editDistance = spellCheck.calculateAndFetchEditDistanceFor(userInput, element);
            if (editDistance < min) {
                secondMin = min;
                secondMinWord = minWord;
                min = editDistance;
                minWord = element;
            } else if (editDistance < secondMin) {
                secondMin = editDistance;
                secondMinWord = element;
            }
        }
        int c = 1;
        if (!minWord.isEmpty()) {
            System.out.println(Strings.getString(Strings.DidYouMeanString, minWord, Strings.Type, c));
            c++;
        }
        if (!secondMinWord.isEmpty()) {
            System.out.println(Strings.getString(Strings.DidYouMeanString, secondMinWord, Strings.Type, c));
        }

        int secondUserInput;
        display(Strings.TypeToGetWordCompletion);
        if (isAmenities) display(Strings.ProceedWithManuallyEnteredString);
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.print(Strings.MenuEnterAnOption.value);
                try {
                    secondUserInput = scanner.nextInt();
                } catch (Exception ignored) {
                    throw new ACCException(E.InputNotInteger);
                }
                return switch (secondUserInput) {
                    case 0: yield userInput;
                    case 1: yield minWord;
                    case 2: yield secondMinWord;
                    case 3: yield suggestWordCompletion(userInput, isAmenities);
                    default:
                        throw new ACCException(E.InputOptionDoesNotExist);
                };
            } catch (ACCException exception) {
                handle(exception);
            }
        }
    }

    private String suggestWordCompletion(String word, boolean isAmenities) throws ACCException {
        display(Strings.WordCompletionString);
        List<String> completedWords = wordCompletion.getWordsForPrefix(word);
        int count = 1;
        int completedWordsSize = completedWords.size();
        if (completedWordsSize == 0) throw new ACCException(E.CompletedWordsNotFound);
        for (String completedWord : completedWords) {
            System.out.println(Strings.getString(Strings.DidYouMeanString, completedWord, Strings.Type, count));
            count++;
        }
        if(isAmenities) display(Strings.ProceedWithManuallyEnteredString);
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.print(Strings.MenuEnterAnOption.value);
                int userInput;
                try {
                    userInput = scanner.nextInt();
                } catch (Exception ignored) {
                    throw new ACCException(E.InputNotInteger);
                }
                if (userInput > count) throw new ACCException(E.InputOptionDoesNotExist);
                if (userInput == 0) return word;
                return completedWords.get(userInput-1);
            } catch (ACCException exception) {
                handle(exception);
            }
        }
    }

    void displaySubMenuForSearchByGymDetails() {
        displayMenu(Strings.SearchByDetailsMenuByDetailsTitle,
                List.of(
                        Strings.SearchByDetailsMenuByDetailsGym,
                        Strings.SearchByDetailsMenuByDetailsAmenities,
                        Strings.SearchByDetailsMenuByDetailsLocation,
                        Strings.MenuExit
                )
        );
    }

    void displaySubMenuForSearchByMembershipFee() {
        displayMenu(Strings.SearchDetailsMenuByMembershipTitle,
                List.of(
                        Strings.SearchDetailsMenuByMembershipBiWeekly,
                        Strings.SearchDetailsMenuByMembershipMonthly,
                        Strings.SearchDetailsMenuByMembershipAnnually,
                        Strings.MenuExit
                )
        );
    }

    void displaySubMenuForSearchDetails() {
        displayMenu(Strings.SearchDetailsMenu,
                List.of(
                        Strings.SearchDetailsMenuByDetails,
                        Strings.SearchDetailsMenuByMembership,
                        Strings.MenuExit
                )
        );
    }
    private void displayMenu(Strings title, List<Strings> strings) {
        display(Strings.MenuSeparator);
        display(title);
        display(Strings.MenuSeparator);
        for (Strings string : strings) {
            display(string);
        }
        display(Strings.MenuSeparator);
    }

    void displayMainMenu() {
        displayMenu(Strings.MenuString, List.of(Strings.MenuSearchDetails, Strings.MenuExit));
    }
}
