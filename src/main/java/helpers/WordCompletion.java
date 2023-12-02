package helpers;

import WebCrawler.Model.FitnessDataModel;
import WebCrawler.Model.MembershipDetailsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isWord;
}

public class WordCompletion {
    private TrieNode parentNode;

    public WordCompletion() {
        parentNode = new TrieNode();
    }

    public void insert(String word) {
        word = word.toLowerCase();
        TrieNode node = parentNode;
        for (char character : word.toCharArray()) {
            node.children.putIfAbsent(character, new TrieNode());
            node = node.children.get(character);
        }
        node.isWord = true;
    }

    public List<String> getWordsForPrefix(String prefix) {
        prefix = prefix.toLowerCase();
        List<String> words = new ArrayList<>();
        TrieNode node = parentNode;
        for (char character : prefix.toCharArray()) {
            if (!node.children.containsKey(character)) {
                return words;
            }
            node = node.children.get(character);
        }
        findAllWords(node, words, new StringBuilder(prefix));
        return words;
    }

    private void findAllWords(TrieNode node, List<String> words, StringBuilder prefix) {
        if (node.isWord) {
            words.add(prefix.toString());
        }
        for (char ch : node.children.keySet()) {
            prefix.append(ch);
            findAllWords(node.children.get(ch), words, prefix);
            prefix.deleteCharAt(prefix.length() - 1);

        }
    }

    public static WordCompletion getInstanceUsing(List<String> tokenizedWords) {
        WordCompletion wordCompletion = new WordCompletion();
        for (String tokenizedWord : tokenizedWords) {
            wordCompletion.insert(tokenizedWord);
        }
        return wordCompletion;
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
        for (String word : WordCompletion.getInstanceUsing(invertedIndex.getTokenizedWords()).getWordsForPrefix("on")) {
            System.out.println(word);
        }
    }
}
