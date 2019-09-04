package com.mcc;

import java.io.FileInputStream;
import java.io.IOException;

public class KWIC {

  private static final String pathPDF = "./src/main/java/com/mcc/ejemplo.pdf";
  private static final String path = "./src/main/java/com/mcc/input.txt";

  public void execute(String file, Filter.onFinishListener finishListener) {

    try {
      // Pipe circularShifterToAlphabetizer = new Pipe();

      Pipe inToKw = new Pipe();
      Pipe kwToPdfS = new Pipe();
      Pipe pdfSToAlp = new Pipe();
      Pipe alphabetizerToOutput = new Pipe();

      FileInputStream in = new FileInputStream(file);

      Input input = new Input(in, inToKw);
      KeywordsGenerator keywordsGenerator = new KeywordsGenerator(inToKw, kwToPdfS);
      //PdfWordSearcher pdfWordSearcher = new PdfWordSearcher(kwToPdfS, pdfSToAlp, pathPDF);
      Alphabetizer alpha = new Alphabetizer(kwToPdfS, alphabetizerToOutput);
      Output output = new Output(alphabetizerToOutput, finishListener);

      // shifter.start();
      input.start();
      keywordsGenerator.start();
      //pdfWordSearcher.start();
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
