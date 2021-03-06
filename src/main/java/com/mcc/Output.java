package com.mcc;

import java.io.*;

public class Output extends Filter {

  private Filter.onFinishListener onFinishListener;

  public Output(Pipe input, Filter.onFinishListener onFinishListener) {
    super(input, null);
    this.onFinishListener = onFinishListener;
  }

  @Override
  protected void transform() {
    try {
      int c = input.read();
      CharArrayWriter writer = new CharArrayWriter();
      while (c != -1) {
        writer.write(c);
        c = input.read();
      }

      Util.fromStringToFile(writer.toString(),"KWIC_file_output");

      if(onFinishListener != null) {
        onFinishListener.onFinish(writer.toString());
      }

    } catch (IOException exc) {
      exc.printStackTrace();
      System.err.println("KWIC Error: en los datos de salida");
      System.exit(1);
    }
  }
}
