package cs224n.document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs224n.deep.Datum;

public class DocumentSet extends ArrayList<Document> {

  private static final long serialVersionUID = 1L;
  private static final String DOCUMENT_BOUNDARY = "-DOCSTART-";
  
  public DocumentSet(List<Datum> data) {
    super();
    
    List<Datum> wordList = new ArrayList<Datum>();
    
    for(Datum d: data) {
      if (d.word.equals(DOCUMENT_BOUNDARY)) {
        if (wordList.size() > 0)
          this.add(new Document(wordList));
        wordList = new ArrayList<Datum>();
      } else {
        wordList.add(d);
      }
    }
    // Add every document with positive size
    if (wordList.size() > 0)
      this.add(new Document(wordList));
  }
  
  public void shuffle() {
    Collections.shuffle(this);
  }
}
