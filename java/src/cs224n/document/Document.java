package cs224n.document;

import java.util.ArrayList;
import java.util.List;

import cs224n.deep.Datum;

public class Document extends ArrayList<Datum> {
  
  private static final long serialVersionUID = 1L;
  
  public Document(List<Datum> words) {
    super();
    this.addAll(words);
  }
  
  public List<Datum> getWords() {
    return this;
  }
  
  public boolean wordEquals(Document other) {
    if (this.size() != other.size())
      return false;
    for (int i=0; i < this.size(); i++)
      if (!this.get(i).word.equals(other.get(i).word))
        return false;
    return true;
  }
  
  @Override
  public String toString() {
    String output = "";
    for (Datum d : this)
      output += " " + d.word + ":" + d.label;
    return output;
  }
}
