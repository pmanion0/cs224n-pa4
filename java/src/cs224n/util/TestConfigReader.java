package cs224n.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestConfigReader {

  /**
   * Read a test configuration file into a set of command line strings to run
   * @param filepath - Full filepath to a test config file
   * @return - list of all the Configuration to run
   */
  public static List<Configuration> parseFile(String filepath) throws IOException {
    Map<String,String> options = new HashMap<String,String>();
    List<Configuration> output = new ArrayList<Configuration>();
    BufferedReader reader = new BufferedReader(new FileReader(filepath));
    String line = reader.readLine();
    
    // Simple check to catch files that aren't valid test configurations
    if (!isSeparator(line)) {
      reader.close();
      throw new IOException("ERROR: First line of file does not start with '----'");
    }
    
    // Read through the file and create all requested configurations
    while ((line = reader.readLine()) != null) {
      if (isSeparator(line)) {
        Configuration config = new Configuration(options);
        output.add(config);
      } else {
        String option = line.trim().split(":")[0];
        String value = line.trim().split(":")[1];
        options.put(option, value);
      }
    }
    reader.close();
    return output;
  }
  
  /**
   * Return TRUE if the string is a test config separator (line starting with ----)
   */
  public static boolean isSeparator(String line) {
    return line.substring(0,4).equals("----");
  }
  
  /**
   * Convert the map of options into an argument string for Configuration
   * @param options - Map in the form <option, value>
   * @return String in the form "-<option1> <value1> ... -<optionN> <valueN>"
   */
  public static String optionMapToString(Map<String,String> options) {
    String output = "";
    for (String key : options.keySet()) {
      output += "-" + key + " " + options.get(key);
    }
    return output;
  }
}
