package FinalProjectDeliverable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class QuestionLoader {
    public void readFile() {
        try {
            FileReader fileReader = new FileReader("MarineAnimalsQuestions.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            int readerCount = 0;

            String line = "";
            while (true) {
                line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                readerCount++;
                System.out.println(readerCount + ": " + line);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    HashMap<Integer, ArrayList<String>> map = new HashMap<>();

    public HashMap<Integer, ArrayList<String>> readQuestions(int category) {
        try {
            FileReader fileReader = null;
            if(category == 1) {
                fileReader = new FileReader("src/FinalProjectDeliverable/MarineAnimalsQuestions.txt");
            } else if(category == 2) {
                fileReader = new FileReader("src/FinalProjectDeliverable/SolarSystemQuestions.txt");
            } else {
                System.out.print("CATEGORY SELECTOR FAILED");
                return map;
            }

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            int readerCount = 0;

            String line = "";
            while (true) {
                line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                System.out.println(readerCount + ": " + line);
                int questionNumber = (readerCount / 5);
                if (readerCount % 5 == 0) {
                    map.put(questionNumber, new ArrayList<>());
                }
                map.get(questionNumber).add(line);

                readerCount++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return map;
    }
}
