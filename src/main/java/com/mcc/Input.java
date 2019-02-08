package com.mcc;

import java.io.IOException;
import java.io.InputStream;

public class Input extends Filter {

  private InputStream in;

  public Input(InputStream in, Pipe output) {
    super(null, output);
    this.in = in;
  }

  protected void transform() {
    try {
      int character = in.read();
      while (character != -1) {
        output.write(character);
        character = in.read();
      }
      output.write('\n');
      output.closeWriter();
    } catch (IOException exc) {
      exc.printStackTrace();
      System.err.println("KWIC Error: No se pudo leer el archivo de entrada.");
      System.exit(1);
    }
  }

}
