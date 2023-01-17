import structure5.*;
import java.util.Comparator;

class TFIDFComparator implements Comparator<Association<String, Double>> {
   /*pre: Associations A and B are valid
   *post: returns difference between cumulative TFIDF
   * Returns:  < 0  if a less than b,  0  if a equal to b,  > 0  if a more than b */

  public int compare(Association<String, Double> a, Association<String, Double> b) {
    Assert.pre(a != null && b != null, "Associations A and B aren't null");
    if (a.getValue() == b.getValue()) {
      return 0;
    }
    if (a.getValue() > b.getValue()) {
      return -1;
    } else {
      return 1;
    }
  }

}
