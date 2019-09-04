package com.mcc;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfWordSearcher extends Filter {

  private String pathPdf;

  public PdfWordSearcher(Pipe in, Pipe out, String pathPdf) {
    super(in, out);
    this.pathPdf = pathPdf;
  }

  private List<TextPositionSequence> findSubwords(PDDocument document, int page, String searchTerm)
      throws IOException {

    final List<TextPositionSequence> hits = new ArrayList<>();

    PDFTextStripper stripper =
        new PDFTextStripper() {
          @Override
          protected void writeString(String text, List<TextPosition> textPositions)
              throws IOException {
            TextPositionSequence word = new TextPositionSequence(textPositions);
            String string = word.toString();

            int fromIndex = 0;
            int index;
            while ((index = string.indexOf(searchTerm, fromIndex)) > -1) {
              hits.add(word.subSequence(index, index + searchTerm.length()));
              fromIndex = index + 1;
            }
            super.writeString(text, textPositions);
          }
        };

    stripper.setSortByPosition(true);
    stripper.setStartPage(0);
    stripper.setEndPage(page);
    stripper.getText(document);
    return hits;
  }

  private List<String> findWordNumberPage(String path, List<String> words) {
    List<String> wordsResult = new ArrayList<>();
    if (words != null && !words.isEmpty()) {
      try {
        PDDocument document = PDDocument.load(new File(path));

        int pos;
        StringBuilder format;
        for (int i = 0; i < document.getNumberOfPages(); i++) {
          pos = 0;
          for (String item : words) {

            item = item.replaceAll("\n", "");
            boolean isUpdate = wordsResult.size() - 1 > 0 && pos <= (wordsResult.size() - 1);
            format = new StringBuilder((isUpdate) ? wordsResult.get(pos) : "");

            if (i == 0) {
              format.append("Palabra:");
              format.append(item);
              format.append(", Paginas:");
            }

            List<TextPositionSequence> textPositionSequences = findSubwords(document, i, item);
            if (!textPositionSequences.isEmpty()) {
              format.append(i);
              format.append(" ");
            }

            if (isUpdate) {
              wordsResult.set(pos, format.toString());
            } else {
              wordsResult.add(pos, format.toString());
            }
            pos++;
          }
        }

        wordsResult.addAll(words);

      } catch (IOException e) {
        System.out.println(e.toString());
      }
    }
    return wordsResult;
  }

  @Override
  protected void transform() {
    try {
      List<String> lines = new ArrayList<>();
      CharArrayWriter writer = new CharArrayWriter();

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
        lines.remove(0);
        List<String> words = findWordNumberPage(path, lines);
      }

      output.closeWriter();
    } catch (IOException exc) {
      exc.printStackTrace();
      System.err.println("KWIC Error: No se pudieron ordenar los datos.");
      System.exit(1);
    }
  }
}
