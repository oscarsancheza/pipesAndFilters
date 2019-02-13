package com.mcc;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;


public class KWIC {

    private static final String path =
            "./src/main/java/com/mcc/KWIC_file.txt";

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
        kwic.execute(path,System.out::println);

    }
}
