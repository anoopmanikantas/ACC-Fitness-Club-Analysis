package helpers;

public class SpellCheck {
    /**
     * below function is used to calculate the edit distance for 2 given words.
     *
     * @return Integer - The minimum edit distance.
     */
    public Integer calculateAndFetchEditDistanceFor(String word1, String word2) {
        int firstWordSize = word1.length();
        int secondWordSize = word2.length();
        // If the words are blank or empty, the size of those specific words are returned as it is.
        if (firstWordSize == 0) return secondWordSize;
        if (secondWordSize == 0) return firstWordSize;
        Integer[][] wordsEditDistanceMatrix = new Integer[firstWordSize + 1][secondWordSize + 1];
        /*
         * Primary loop and secondary loop for the 2 words add indices for each character in the first row and column,
         *  in edit distance matrix.
         * */
        for (int primaryLoopIndex = 0; primaryLoopIndex <= firstWordSize; primaryLoopIndex++)
            wordsEditDistanceMatrix[primaryLoopIndex][0] = primaryLoopIndex;
        for (int secondaryLoopIndex = 0; secondaryLoopIndex <= secondWordSize; secondaryLoopIndex++)
            wordsEditDistanceMatrix[0][secondaryLoopIndex] = secondaryLoopIndex;
        /*
        * Below loop is the core functionality of edit distance algorithm.
        * The primary loop is used to iterate over the characters from `word1`
           the secondary loop is used to iterate over the characters from `word2`.
        * */
        for (int primaryLoopIndex = 1; primaryLoopIndex <= firstWordSize; primaryLoopIndex++) {
            for (int secondaryLoopIndex = 1; secondaryLoopIndex <= secondWordSize; secondaryLoopIndex++) {
                // Characters from both the words are compared and if they are equal there will be no operation costs.
                if (word1.charAt(primaryLoopIndex - 1) == word2.charAt(secondaryLoopIndex - 1))
                    wordsEditDistanceMatrix[primaryLoopIndex][secondaryLoopIndex] =
                            wordsEditDistanceMatrix[primaryLoopIndex - 1][secondaryLoopIndex - 1];
                /*
                * There will be additional operational costs if there is any character that needs to be edited.
                * Costs of each operation are calculated and the minimum of those is selected.
                * If it is insertion, removal or substitution in `word1`,
                    the operational cost for all the mentioned operations would be 1 each.
                * */
                else
                    wordsEditDistanceMatrix[primaryLoopIndex][secondaryLoopIndex] =
                            min(wordsEditDistanceMatrix[primaryLoopIndex - 1][secondaryLoopIndex] + 1, // insert
                                    wordsEditDistanceMatrix[primaryLoopIndex][secondaryLoopIndex - 1] + 1, // remove
                                    wordsEditDistanceMatrix[primaryLoopIndex - 1][secondaryLoopIndex - 1] + 1); // substitute
            }
        }
        return wordsEditDistanceMatrix[firstWordSize][secondWordSize];
    }

    /**
     * The function below is used to find the minimum value for the edit distance's editing operation.
     *
     * @return Integer - Minimum value for the editing operation.
     */
    private Integer min(Integer value1, Integer value2, Integer value3) {
        return Math.min(Math.min(value1, value2), value3);
    }
}
