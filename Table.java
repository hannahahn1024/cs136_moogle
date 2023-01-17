import structure5.*;
import java.nio.file.*;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;

class Table {
    private Hashtable<String, TermFrequency> _table;
    private int collection = 0;   //count of documents

    /**
     * Build term frequency table for all documents in path. Searches for
     * documents recursively.
     *
     * @param dir Document path.
     */
    public Table(Path dir) {
        _table = new Hashtable<String, TermFrequency>();
        recurFile(dir);
    }

    public void recurFile(Path p) {
      if (!Files.isDirectory(p)) {  //finds a file
        _table.put(p.getFileName().toString(), new TermFrequency(p));   //adds to Hashtable file name and TermFrequency
        collection++;   //increments count of documents
      } else {    //not a file, so path is a subdirectory
        try {
          Iterable<Path> files = (Iterable<Path>)Files.walk(p)::iterator;
          for (Path f : files) {
            if (f.equals(p)) continue;
            recurFile(f); //recursively parses through document/subdirectory paths
          }

        }
        catch(IOException e) {
          System.err.println(e.getMessage());
          System.exit(1);
        }
      }

    }

    /**
     * Compute inverse document frequency (IDF) for term across a corpus.
     *
     * @param term A string term.
     */
    public double idf(String term) {
        int nTerm = 0;    //Number of documents that term appears in;
        for (TermFrequency termf : _table) {
          Set<String> search = termf.terms();  //set of terms in each file
          if (search.contains(term)) {
            nTerm++;
          }
        }
        //logarithmic manipulation to find IDF
        double IDF = (Math.log(collection) - Math.log(nTerm + 1)) / Math.log(2);
        return IDF;
    }

    /**
     * Compute the TF-IDF score for a collection of documents and
     * a given search term.
     *
     * @param term A search term.
     */
    public Hashtable<String, Double> tfidf(String term) {
        Hashtable<String, Double> TFIDF = new Hashtable<String, Double>();
        Set<String> docs = _table.keySet();   //set of docs in _table

        for (String doc : docs) { //goes through all docs in _table
          double tf = _table.get(doc).tf(term);   //find tf of given term
          double tfidf = tf * idf(term);  //multiply with idf
          TFIDF.put(doc, tfidf);    //put into Hashtable
        }

        return TFIDF;   //Hashtable holds mapping of documents to their tfidf of given term
    }

    /**
     * Computes the cumulative TF-IDF score for each document with
     * respect to a given query.
     *
     * @param query A vector of search terms.
     */
    public Hashtable<String, Double> score(Vector<String> query) {
        Hashtable<String, Double> cumulative = new Hashtable<String, Double>();
        Set<String> docs = _table.keySet();   //set of docs in _table

        for (String doc : docs) { //goes through all docs in _table
          double cumtfidf = 0.0;
          for (int i = 0; i < query.size(); i++) {  //calculates tfidf of each term in query
            String term = query.get(i);
            double tf = _table.get(doc).tf(term);   //find tf of given term
            double tfidf = tf * idf(term);
            cumtfidf += tfidf;   //sums up each term's tfidf
          }
          cumulative.put(doc, cumtfidf); //put into Hashtable document name and cumtfidf
        }

        return cumulative;  //Hashtable holds mapping of documents to their cumtfidf of given query
    }

    /**
     * Returns the top K documents.
     *
     * @param doc_tfidf A map from documents to their cumulative TF-IDF score.
     * @param k The number of documents to return.
     */
    public Vector<Association<String, Double>> topK(Vector<String> query, int k) {
        Hashtable<String, Double> docsTable = score(query);
        Set<String> transfer = docsTable.keySet();

        Vector<Association<String, Double>> topDocs = new Vector<Association<String, Double>>();

        for (String str : transfer) {
          Association<String, Double> top = new Association<String, Double>(str, docsTable.get(str));
          topDocs.add(top);
        }

        TFIDFComparator comp = new TFIDFComparator();
        sort(comp, topDocs);

        Vector<Association<String, Double>> kReturn = new Vector<Association<String, Double>>(k);
        for (int i = 0; i < k; i++) {
          kReturn.add(topDocs.get(i));
        }

        return kReturn;
    }

    /**
     * Outputs frequency table in CSV format.  Useful for
     * debuggging.
     *
     * @param table A frequency table for the entire corpus.
     */
    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      for (String doc : _table.keySet()) {
        TermFrequency docFreqs = _table.get(doc);
        for (String term : docFreqs.terms()) {
          sb.append("\"" + doc + "\",");
          sb.append("\"" + term + "\",");
          sb.append(docFreqs.getCount(term));
          sb.append("\n");
        }
      }
      return sb.toString();
    }

    //sorts vector of documents from best to worst cumtfidf
    public void sort(Comparator<Association<String, Double>> c, Vector<Association<String, Double>> vec) {
        int numSorted = 0;
        int index;
        while (numSorted < vec.size()) {
          Association<String, Double> temp = vec.get(numSorted);
          for (index = numSorted; index > 0; index--) {
            if (c.compare(temp, vec.get(index - 1)) < 0) {
              vec.set(index, vec.get(index - 1));
            } else {
              break;
            }
          }
          vec.set(index, temp);
          numSorted++;
        }
    }

}
