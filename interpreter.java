import java.io.*;
import java.util.*;

public class interpreter {

  private List<String> keywords = new ArrayList<String>();

  private List<String> orderOfOps = new ArrayList<String>();
  private List<ArrayList<String>> loopFuncts = new ArrayList<ArrayList<String>>();

  private List<String> file = new ArrayList<String>();

  private boolean debug_mode = false;

  private int bowl_val = 0;
  private int curr_person;
  private int[] people;

  public interpreter() {
    keywords.add("inhale"); //start loop
    keywords.add("exhale"); //end loop
    keywords.add("cherry"); //bowl -1
    keywords.add("pack"); //bowl +1
    keywords.add("ash"); //bowl = 0
    keywords.add("pass"); //move index 1
    keywords.add("blaze"); //index +bowl
    keywords.add("story"); //print index
  }

  public void readFile(String filePath) {
    if (debug_mode) { System.out.println("Reading file"); }

    try {
      BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
      String line = null;
      while ((line = fileReader.readLine()) != null) {
        if (debug_mode) { System.out.println("New line: " + line); }
        String[] newLine = line.split(" ");
        for (String word : newLine) {
          file.add(word);
        }
      }
    } catch (Exception e) {}
    parseFile();
  }

  public void parseFile() {
    for (int i = 0; i < file.size(); i++) {
      if (file.get(i).equals("inhale")) {
        orderOfOps.add("loop-"+i);
        i = getLoop(i);
      } else if (file.get(i).equals("exhale")) {
        //Not in loop, shouldn't be here
        System.out.println("ERROR - Loop exit while out of loop, check your .ws file");
        System.exit(1);
      } else if (file.get(i).equals("ash") ||
                 file.get(i).equals("cherry") ||
                 file.get(i).equals("pack") ||
                 file.get(i).equals("pass") ||
                 file.get(i).equals("blaze") ||
                 file.get(i).equals("story")) {
        orderOfOps.add(file.get(i));
      } else {
        System.out.println("Invalid symbol: \""+file.get(i)+"\"");
      }
    }
    runOperations();
  }

  private void runOperations() {
    for (int i = 0; i < orderOfOps.size(); i++) {
      if (debug_mode) { System.out.println(orderOfOps.get(i)); }
      String op = orderOfOps.get(i);
      if (op.equals("ash")) {
        bowl_val = 0;
      } else if (op.equals("cherry")) {
        if (bowl_val > 0) {
          bowl_val--;
        }
      } else if (op.equals("pack")) {
        bowl_val++;
      } else if (op.equals("blaze")) {
        people[curr_person] += bowl_val;
      } else if (op.equals("story")) {
        System.out.print((char)people[curr_person]);
      } else if (op.equals("pass")) {
        curr_person++;
        if (curr_person == people.length) {
          curr_person++;
        }
      }
    }
    System.out.println();
  }

  private int getLoop(int ind) {
    List<String> loopConts = new ArrayList<String>();
    while (ind < file.size() && file.get(ind) != "exhale") {
      loopConts.add(file.get(ind));
      if (file.get(ind) == "inhale") {
        orderOfOps.add("loop-"+ind);
        ind = getLoop(ind);
      }
      ind++;
    }
    return ind;
  }

/*
  private void readLineArray(String[] line) {
    List<String> line_words = new ArrayList<String>();
    for (String word : line) {
      line_words.add(word);
    }
    for (int i = 0; i < line_words.size(); i++) {
      if (keywords.contains(line_words.get(i))) {
        if (line_words.get(i).equals("sesh") && line_words.size() > 4) {
          if (startSesh(line_words.get(i),
                        line_words.get(i+1),
                        line_words.get(i+2),
                        line_words.get(i+3))) {
            i += 3;
          }
        }
      }
    }
  }

  private boolean startSesh(String a, String b, String c, String d) {
    if (a.equals("sesh") && c.equals("doob")) {
      try {
        Integer.parseInt(b);
        Integer.parseInt(d);
      } catch (NumberFormatException e) {
        return false;
      }

      people = new int[Integer.parseInt(b)];
      for (int i = 0; i < people.length; i++) {
        people[i] = 0;
      }
      d_max = Integer.parseInt(d);
      d_val = d_max;
      curr_person = 0;

    }
    return true;
  }

  private void pass() {
    curr_person++;
    if (curr_person == people.length) {
      curr_person = 0;
    }
  }*/

  //keywords.add("inhale"); //start loop
  //keywords.add("exhale"); //end loop
  //keywords.add("ash"); //bowl -1
  //keywords.add("pack"); //bowl +1
  //keywords.add("pass"); //move index 1
  //keywords.add("blaze"); //index +bowl

  private void inhale() {

  }

  private void exhale() {

  }

  private void ash() {

  }

  private void pack() {

  }

  private void pass() {

  }

  private void blaze() {

  }

  private void story() {

  }

  public static void main (String[] args) {

    if (args.length == 0) {
      System.out.println("WARNING - No ws file given, exiting.");
      return;
    } else {
      String file = args[0];
      if (file.length() < 3) {
        System.out.println("ERROR - Invalid file given.");
        return;
      }
      String fileSuffix = file.substring(file.length()-3,file.length());
      if (!fileSuffix.equals(".ws")) {
        System.out.println("ERROR - Given file not .ws, exiting.");
        return;
      }
    }

    interpreter i = new interpreter();
    i.people = new int[3];
    i.people[0] = 0;
    i.people[1] = 0;
    i.people[2] = 0;
    i.curr_person = 0;

    if (args.length > 1 && Integer.parseInt(args[1]) == 1) {
      i.debug_mode = true;
      System.out.println("Debug mode ON");
    } else {
      System.out.println("Debug mode OFF");
    }

    i.readFile(args[0]);

  }

}
