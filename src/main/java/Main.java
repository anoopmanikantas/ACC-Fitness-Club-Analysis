import WebCrawler.Model.FitnessDataModel;
import WebCrawler.MultiPageCrawler;
import helpers.*;

import java.util.List;

public class Main {
    List<FitnessDataModel> fitnessDataModels;
    WordSearchCount wordSearchCount;
    WordCompletion wordCompletion;
    WordFrequency wordFrequency;
    InvertedIndex invertedIndex;
    SpellCheck spellCheck;


    Main() {
        fitnessDataModels = new MultiPageCrawler().getAllFitnessModels();
        invertedIndex = new InvertedIndex(fitnessDataModels);
        List<String> tokenizedWords = invertedIndex.getTokenizedWords();
        wordCompletion = WordCompletion.getInstanceUsing(tokenizedWords);
        spellCheck = new SpellCheck();
        wordFrequency = new WordFrequency();
        wordSearchCount = WordSearchCount.getInstanceUsing(tokenizedWords);
    }

    public static void main(String[] args) {

    }
}
