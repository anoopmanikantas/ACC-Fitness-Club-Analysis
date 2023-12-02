package helpers;

import java.util.List;

class Node {
    String word;
    int count;
    Node left, right;

    Node(String word) {
        this.word = word;
        this.count = 0;
    }
}

public class WordSearchCount {
    Node parentNode;

    public void insert(String word) {
        parentNode = insert(parentNode, word);
    }

    private Node insert(Node parentNode, String word) {
        if (parentNode == null) {
            parentNode = new Node(word);
            return parentNode;
        }
        int comparisonValue = word.compareTo(parentNode.word);
        if (comparisonValue < 0)
            parentNode.left = insert(parentNode.left, word);
        else if (comparisonValue > 0)
            parentNode.right = insert(parentNode.right, word);
        else
            parentNode.count++;
        return parentNode;
    }

    public int getCountFor(String word) {
        Node node = search(parentNode, word);
        return node != null ? ++node.count : 0;
    }

    private Node search(Node parentNode, String word) {
        if (parentNode == null || parentNode.word.equals(word))
            return parentNode;
        if (parentNode.word.compareTo(word) > 0)
            return search(parentNode.left, word);
        return search(parentNode.right, word);
    }

    public static void main(String[] args) {
        WordSearchCount wordSearchCount = new WordSearchCount();
        String text = "THis is the test string to test out word search count";
        List<String> tokenizedWords = (new StringTokenizer(text)).getTokenizedWords();
        for (String tokenizedWord : tokenizedWords) {
            wordSearchCount.insert(tokenizedWord);
        }

        System.out.println("Count of 'this': " + wordSearchCount.getCountFor("this"));
        System.out.println("Count of 'this': " + wordSearchCount.getCountFor("this"));
    }
}
