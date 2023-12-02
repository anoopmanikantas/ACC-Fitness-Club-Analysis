package helpers;

import WebCrawler.Fit4Less.Fit4LessCurrentPage;
import WebCrawler.GoodLifeFitness.GoodLifeFitnessCurrentPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.util.*;

public class WordFrequency {
    private List<File> files;
    private final Map<String, Map<String, Integer>> allWordsFrequencies;

    public WordFrequency() {
        this.allWordsFrequencies = new TreeMap<>();
        initFiles();
    }

    private void initFiles() {
        this.files = new ArrayList<>();
        File directory = new File(Strings.PlanetFitnessParsedHTMLDirectory.value);
        files.addAll(Arrays.stream(Objects.requireNonNull(directory.listFiles())).toList());
        List<Fit4LessCurrentPage> fit4LessCurrentPages = Arrays.stream(Fit4LessCurrentPage.values()).toList();
        for (Fit4LessCurrentPage fit4LessCurrentPage : fit4LessCurrentPages) {
            files.add(new File(fit4LessCurrentPage.getPath()));
        }
        for (GoodLifeFitnessCurrentPage value : GoodLifeFitnessCurrentPage.values()) {
            files.add(new File(value.getPath()));
        }
    }

    private void calculateWordFrequency() {
        allWordsFrequencies.clear();
        try {
            for (File file : files) {
                Map<String, Integer> wordFrequencyMapForAPage = new TreeMap<>();
                Document document = Jsoup.parse(file, Strings.UTF8.value);
                String text = document.body().text().toLowerCase();
                List<String> words = new StringTokenizer(text).getTokenizedWords();
                for (String word : words)
                    if (!word.strip().isBlank())
                        wordFrequencyMapForAPage.put(word, wordFrequencyMapForAPage.getOrDefault(word, 0) + 1);
                allWordsFrequencies.put(file.getPath(), wordFrequencyMapForAPage);
            }
        } catch (Exception e) {
            Log.info(e.getMessage());
        }
    }

    public Map<String, Map<String, Integer>> getWordFrequencyMap() {
        calculateWordFrequency();
        return allWordsFrequencies;
    }

    public Map<String, List<Map<String, Integer>>> getFrequencyCountFor(String word) {
        calculateWordFrequency();
        Map<String, List<Map<String, Integer>>> filesWithFrequencies= new TreeMap<>();
        List<Map<String, Integer>> countWithLocation = new ArrayList<>();
        allWordsFrequencies.forEach((file, treeMap) -> {
            if (treeMap.containsKey(word)) {
                countWithLocation.add(Map.of(file, treeMap.get(word)));
            }
        });
        filesWithFrequencies.put(word, countWithLocation);
        return filesWithFrequencies;
    }

    public static void main(String[] args) {
        WordFrequency wordFrequency = new WordFrequency();
        wordFrequency.getWordFrequencyMap().forEach((key, value) -> System.out.printf("key: %s, \t value: %s\n", key, value));
        System.out.println("\n\n\n Word frequency count for");
        System.out.println(wordFrequency.getFrequencyCountFor("membership"));
    }
}
