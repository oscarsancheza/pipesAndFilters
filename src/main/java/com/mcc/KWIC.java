package com.mcc;

import java.io.FileInputStream;
import java.io.IOException;


public class KWIC {

  private static final String path =
      "./src/main/java/com/mcc/input.txt";

  public void execute(String file, Filter.onFinishListener finishListener) {
    try {
      Pipe inputToCircularShifter = new Pipe();
      Pipe circularShifterToAlphabetizer = new Pipe();
      Pipe alphabetizerToOutput = new Pipe();

      FileInputStream in = new FileInputStream(file);

      Input input = new Input(in, inputToCircularShifter);
      CircularShifter shifter =
          new CircularShifter(inputToCircularShifter, circularShifterToAlphabetizer);
      Alphabetizer alpha = new Alphabetizer(circularShifterToAlphabetizer, alphabetizerToOutput);
      Output output = new Output(alphabetizerToOutput, finishListener);

      input.start();
      shifter.start();
      alpha.start();
      output.start();
    } catch (IOException exc) {
      exc.printStackTrace();
    }
  }

  public static void main(String[] args) {

    KWIC kwic = new KWIC();
    kwic.execute(path, System.out::println);

  }
}
