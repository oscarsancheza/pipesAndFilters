package com.mcc;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

public class Pipe {

  private PipedReader reader;
  private PipedWriter writer;


  public Pipe() throws IOException {
    writer = new PipedWriter();
    reader = new PipedReader();
    writer.connect(reader);
  }

  public void write(int character) throws IOException {
    writer.write(character);
  }

  public int read() throws IOException {
    return reader.read();
  }

  public void closeWriter() throws IOException {
    writer.flush();
    writer.close();
  }

  public void closeReader() throws IOException {
    reader.close();
  }


}
