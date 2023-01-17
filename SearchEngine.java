import structure5.*;
import java.nio.file.*;

/**
 * This is an implementation of a search engine using TF-IDF and hash tables.
 */
class SearchEngine {
  protected static String query;
  protected static Path docPath;
  protected static int k;
  protected static Table table;
  protected static Term term;
  /**
   * This main method should allow a user to call this program as follows:
   * $ java SearchEngine "<query string>" <document folder path> <k>
   *   where
   *   <query string> is a search query, potentially composed of multiple terms,
   *   <document folder path> is a string representing the location of a
   *     document collection, and
   *   <k> is a positive integer representing how many documents to return.
   *
   * For example,
   * $ java SearchEngine "dog" ~/Desktop/ufo 5
   *   will return the 5 most relevant documents in the ~/Desktop/ufo folder
   *   for documents mentioning the word "dog".
   *
   * This method should first understand the user's arguments, extract the search
   * terms from the query, compute term counts for the documents in the given
   * path, then compute the TF-IDF score for each document given the query. Finally,
   * it should generate a list of documents, sorted by their TF-IDF score, and it
   * should print out the top k most relevant documents back to the user.
   *
   * @param args The command line argument array.
   */
  public static void main(String[] args) {
      query = args[0];
      docPath = Paths.get(args[1]);
      k = Integer.parseInt(args[2]);

      table = new Table(docPath);
      term = new Term();

      Vector<String> queryVec = term.toTerms(query);  //uses helper method from Term class
      //retrieves vector of the best k documents
      Vector<Association<String, Double>> returnDocs = table.topK(queryVec, k);

      for (Association<String, Double> doc : returnDocs) {
        String d = doc.getKey();
        double tfidf = doc.getValue();
        System.out.println(d + " --> " + tfidf);
      }

  }
}
