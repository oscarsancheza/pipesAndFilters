package com.mcc;

abstract public class Filter implements Runnable {

  protected Pipe input;
  protected Pipe output;

  private boolean isStarted = false;

  public Filter(Pipe input, Pipe output) {
    this.input = input;
    this.output = output;
  }

  public void start() {
    if (!isStarted) {
      isStarted = true;
      Thread thread = new Thread(this);
      thread.start();
    }
  }

  public void stop() {
    isStarted = false;
  }

  @Override
  public void run() {
    transform();
  }

  abstract protected void transform();


  public interface onFinishListener {
    void onFinish(String data);
  }

}
