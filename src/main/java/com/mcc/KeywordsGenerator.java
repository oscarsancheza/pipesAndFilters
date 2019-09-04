package com.mcc;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KeywordsGenerator extends Filter {

  public KeywordsGenerator(Pipe in, Pipe out) {
    super(in, out);
  }

  private Set<String> generateKeywordsIndex(String path) {
    Set<String> keywords = new HashSet<>();
    try {
      PDDocument document = PDDocument.load(new File(path));
      PDFTextStripperByArea stripper = new PDFTextStripperByArea();
      stripper.setSortByPosition(true);
      PDFTextStripper tStripper = new PDFTextStripper();
      String pdfFileInText = tStripper.getText(document);

      Rake rake = new Rake();
      keywords = rake.getKeywordsFromText(pdfFileInText);

    } catch (IOException e) {
      e.printStackTrace();
      System.err.println("KWIC Error: No se encontro el archivo a leer.");
    }

    return keywords;
  }

  @Override
  protected void transform() {
    List<String> lines = new ArrayList<>();
    CharArrayWriter writer = new CharArrayWriter();
    try {
      int c = input.read();
      while (c != -1) {
        writer.write(c);
        if (((char) c) == '\n') {
          String line = writer.toString();
          lines.add(line);
          writer.reset();
        }
        c = input.read();
      }

      if (!lines.isEmpty()) {
        String path = lines.get(0).replaceAll("\n", "");

        List<String> keywords = new ArrayList<>(generateKeywordsIndex(path));

        if (!keywords.isEmpty()) {
          for (String word : keywords) {
            String shift = word + '\n';

            char[] chars = shift.toCharArray();
            for (char aChar : chars) {
              output.write(aChar);
            }
          }
          output.closeWriter();
        }
      }
    } catch (IOException e) {
      System.err.println("KWIC Error: Ha ocurrido un error al leer los datos.");
    }
  }
}
