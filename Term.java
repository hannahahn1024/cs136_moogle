import structure5.*;

/**
 * A class that contains some static helper methods for working
 * with terms.
 */
class Term {

    /**
     * Converts a query string into a normalized term array.
     *
     * @param query Query string.
     */
    public static Vector<String> toTerms(String query) {
      query = normalize(query);
      Vector<String> terms = new Vector<>();
      String[] splitArray = query.split(" ");
      for (int i = 0; i < splitArray.length; i++) {
        String newTerm = splitArray[i];
        if (!newTerm.equals("")) {
          terms.add(newTerm);
        }
      }
      return terms;
    }

    /**
     * Returns a normalized a word by making the given word
     * lowercase and by removing all punctuation.
     *
     * @param word An unprocessed word.
     */
    public static String normalize(String word) {
      word = word.trim().toLowerCase().replaceAll("\\p{Punct}","");
      return word;
    }

}
