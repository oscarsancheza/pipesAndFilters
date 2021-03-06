package com.mcc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class pipesAndFiltersTest {

  private final String path = "./src/main/java/com/mcc/KWIC_file.txt";
  private final String path2 = "./src/main/java/com/mcc/KWIC_file_pruebas.txt";
  private final String pathOutput = "./src/main/java/com/mcc/KWIC_file_output.txt";


  private KWIC kwic;
  private KWIC kwic2;
  private CompletableFuture<String> future = new CompletableFuture<>();
  private CompletableFuture<String> future2 = new CompletableFuture<>();

  @Before
  public void setUp() {
    kwic = new KWIC();
    kwic2 = new KWIC();
    future = new CompletableFuture<>();
    future2 = new CompletableFuture<>();
  }

  @Test
  public void isResultEqualToAnotherResult() throws ExecutionException, InterruptedException {
    kwic.execute(path, future::complete);
    kwic.execute(path, future2::complete);
    Assert.assertEquals(future.get(), future2.get());
  }

  @Test
  public void isResultNotEqualToAnotherResult() throws ExecutionException, InterruptedException {
    kwic.execute(path, future::complete);
    kwic2.execute(path2, future2::complete);

    Assert.assertNotEquals(future.get(), future2.get());
  }
  @Test
  public void isFileInputDataNotEqualToOutputData() throws ExecutionException, InterruptedException{
      String content_Input = Util.fromFileDataToString(path);
      kwic.execute(pathOutput,future::complete);

      Assert.assertNotEquals(content_Input,future.get());
  }
}
