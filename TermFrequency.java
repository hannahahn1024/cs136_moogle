import structure5.*;
import java.util.Scanner;
import java.nio.file.*;
import java.nio.file.*;

/**
 * A class that tracks term frequencies (counts) for a single document.
 */
class TermFrequency {
    protected Hashtable<String,Integer> _counts;
    private Term t = new Term();

    /**
      * Opens the given file, and for each word in the file, converts
      * it to a normalized term, and counts it.
      *
      * @param file Path to a document.
      */
    public TermFrequency(Path file) {
        _counts = new Hashtable<String,Integer>();
        String fileName = file.toString();  //converts path to String
        Scanner input = new Scanner(new FileStream(fileName));

        StringBuffer textBuffer = new StringBuffer();
        while (input.hasNextLine()) {
          String line = input.nextLine();
          textBuffer.append(line);
          textBuffer.append("\n");
        }
        String doc = textBuffer.toString();   //contains the full doc
        populateHash(doc);
    }

    public void populateHash(String text) {
      Vector<String> termVec = t.toTerms(text);

      for (String str : termVec) {
        if (!_counts.containsKey(str)) {    //new term, add to Hashtable
          _counts.put(str, 1);
        } else {      //term already exists, update its count
          int currVal = _counts.get(str);
          //System.out.println(currVal);
          _counts.put(str, currVal + 1);
        }
      }

    }

    public boolean isEmpty() {
        return _counts.size() == 0;
    }

    /**
     * Computes the term frequency (TF_i) for term i in this document.
     *
     * @param term A string term.
     */
    public double tf(String term) {
        int termCount = getCount(term);
        double maxFreq = (double)getCount(mostFrequentTerm().getKey());
        double termfreq = termCount / maxFreq;
        return termfreq;
    }

    /**
     * Returns an association containing the most frequent term
     * along with its count.
     */
    public Association<String,Integer> mostFrequentTerm() {
        String most = "";
        int max = 0;

        Set<String> keys = terms();
        for (String k : keys) {
          if (getCount(k) > max) {
            max = getCount(k);
            most = k;
          }
        }

        Association<String,Integer> mostFreq = new Association<String,Integer>(most,max);
        return mostFreq;
    }

    /**
     * Returns the count for a given term.
     *
     * @param term The given term.
     */
    public int getCount(String term) {
        if (!terms().contains(term)) {
          return 0;
        }
        return _counts.get(term);
    }

    /**
     * Returns all of the stored terms as a set.
     */
    public Set<String> terms() {
        return _counts.keySet();
    }

}
