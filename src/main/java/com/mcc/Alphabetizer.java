package com.mcc;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Alphabetizer extends Filter {


  public Alphabetizer(Pipe input, Pipe output) {
    super(input, output);
  }

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

      Iterator iterator = lines.stream().sorted(Comparator.naturalOrder()).iterator();
      while (iterator.hasNext()) {
        char[] chars = ((String) iterator.next()).toCharArray();
        for (char aChar : chars) {
          output.write(aChar);
        }
      }

      output.closeWriter();
    } catch (IOException exc) {
      exc.printStackTrace();
      System.err.println("KWIC Error: No se pudieron ordenar los datos.");
      System.exit(1);
    }
  }
}
