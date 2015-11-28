package cs224n.document;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cs224n.deep.Datum;

public class DocumentSetTest {

  @Test
  public void test() {
    String tswift = "-DOCSTART- Say you'll remember me " +
                    "-DOCSTART- Standing in a nice dress " +
                    "-DOCSTART- Staring at the sunset " +
                    "-DOCSTART- Babe";
    DocumentSet docset = new DocumentSet(datumList(tswift));
    
    Document[] rightDocs = new Document[4];
    rightDocs[0] = new Document(datumList("Say you'll remember me"));
    rightDocs[1] = new Document(datumList("Standing in a nice dress"));
    rightDocs[2] = new Document(datumList("Staring at the sunset"));
    rightDocs[3] = new Document(datumList("Babe"));
    
    // Make sure the two DocumentSet have the same number of Document
    assertTrue(rightDocs.length == docset.size());
    
    // Make sure the document words are the same (labels are empty)
    for (int i=0; i < rightDocs.length; i++)
      assertTrue(rightDocs[i].wordEquals(docset.get(i)));
  }
  
  /** Create a List<Datum> from a space delimited string of words */
  public static List<Datum> datumList(String items) {
    List<Datum> doc = new ArrayList<Datum>();
    for (String item : items.split(" "))
      doc.add(new Datum(item, ""));
    return doc;
  }

}
