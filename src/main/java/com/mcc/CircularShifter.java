package com.mcc;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class CircularShifter extends Filter {


  public CircularShifter(Pipe input, Pipe output) {
    super(input, output);
  }

  protected void transform() {
    try {
      CharArrayWriter writer = new CharArrayWriter();

      int c = input.read();
      while (c != -1) {
        if (((char) c) == '\n') {
          String line = writer.toString();
          StringTokenizer tokenizer = new StringTokenizer(line);
          String[] words = new String[tokenizer.countTokens()];
          int i = 0;
          while (tokenizer.hasMoreTokens()) {
            words[i++] = tokenizer.nextToken();
          }

          doCircularShifter(Arrays.asList(words));
          writer.reset();
        } else {
          writer.write(c);
        }

        c = input.read();
      }

      output.closeWriter();
    } catch (IOException exc) {
      exc.printStackTrace();
      System.err.println("KWIC Error: No se pudo realizar el circular shifter");
      System.exit(1);
    }
  }

  private void doCircularShifter(List<String> words) throws IOException {
    try {
      for (String word : words) {
        Collections.rotate(words, 1);

        String shift = words.stream().map(Object::toString)
            .collect(Collectors.joining(" ")) + '\n';

        char[] chars = shift.toCharArray();
        for (char aChar : chars) {
          output.write(aChar);
        }
      }
    } catch (IOException e) {
      throw new IOException(e);
    }
  }
}
