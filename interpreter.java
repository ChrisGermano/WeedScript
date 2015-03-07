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
    keywords.add("toke"); //set curr person to char input
    keywords.add("sesh"); //set the number of people in the sesh
    keywords.add("chill"); //curr person value -1
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
                 file.get(i).equals("toke") ||
                 file.get(i).equals("sesh") ||
                 file.get(i).equals("chill") ||
                 file.get(i).equals("story")) {
        orderOfOps.add(file.get(i));
      } else {
        System.out.println("ERROR - Invalid symbol: \""+file.get(i)+"\", skipping");
      }
    }
    runOperations(orderOfOps, 0);
  }

  private void runOperations(List<String> fileList, int loopDepth) {
    for (int i = 0; i < fileList.size(); i++) {
      String op = fileList.get(i);
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
      } else if (op.equals("chill")) {
        if (people[curr_person] > 0) {
          people[curr_person] = people[curr_person]-1;
        }
      } else if (op.equals("story")) {
        System.out.print((char)people[curr_person]);
      } else if (op.equals("pass")) {
        curr_person++;
        if (curr_person == people.length) {
          curr_person=0;
        }
      } else if (op.equals("toke")) {
        Scanner nug = new Scanner(System.in);
        char c = (char)0;
        try {
          c = nug.next().charAt(0);
          people[curr_person] = (int)c;
        } catch (Exception e) {}
      } else if (op.equals("sesh")) {
        Scanner nug = new Scanner(System.in);
        int in = 0;
        try {
          in = nug.nextInt();
          people = new int[in];
          curr_person = 0;
          bowl_val = 0;
          for (int x : people) {
            x = 0;
          }
        } catch (Exception e) {}
      } else if (op.split("-")[0].equals("loop")) {
        ArrayList<String> loopString = loopFuncts.get(0);
        loopFuncts.remove(0);
        runOperations(loopString, (loopDepth+1));
      }

      if (debug_mode) { System.out.println("Loop depth: " + loopDepth); }
      if (debug_mode) { System.out.println("Current person: " + curr_person); }
      if (debug_mode) { System.out.println("Current person val: " + people[curr_person]); }
      if (debug_mode) { System.out.println("Bowl val: " + bowl_val); }
    }

    if (loopDepth > 0) {
      if (bowl_val > 0 && people[curr_person] > 0) {
        runOperations(fileList, loopDepth);
      } else {
        runOperations(fileList, loopDepth-1);
      }
    }

  }

  private int getLoop(int ind) {
    ArrayList<String> loopConts = new ArrayList<String>();
    while (ind < file.size()) {
      if (file.get(ind).equals("exhale")) {
        break;
      }
      loopConts.add(file.get(ind));
      if (file.get(ind) == "inhale") {
        orderOfOps.add("loop-"+ind);
        ind = getLoop(ind);
      }
      ind++;
    }
    loopConts.remove(0);
    loopFuncts.add(loopConts);
    return ind;
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
    i.people = new int[2];
    i.people[0] = 0;
    i.people[1] = 0;
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
