package helpers;

import java.util.Arrays;
import java.util.List;

public class StringTokenizer {
    String words;

    StringTokenizer(String words) {
        this.words = words;
    }

    public List<String> getTokenizedWords() {
        return Arrays.stream(words.toLowerCase().split(Strings.RegexSpaceAndNonWordCharacters.value)).toList();
    }
}
