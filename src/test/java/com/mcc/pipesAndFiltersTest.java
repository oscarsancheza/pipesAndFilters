package com.mcc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class pipesAndFiltersTest {

  private final String path =
      "/home/dessis-aux23/Documentos/maestria/pipesAndFilters/src/main/java/com/mcc/KWIC_file.txt";

  private final String path2 =
      "/home/dessis-aux23/Documentos/maestria/pipesAndFilters/src/main/java/com/mcc/KWIC_file_pruebas.txt";

  private KWIC kwic;
  private KWIC kwic2;

  @Before
  public void setUp() {
    kwic = new KWIC();
    kwic2 = new KWIC();
  }

  @Test
  public void isResultEqualToAnotherResult() throws ExecutionException, InterruptedException {
    CompletableFuture<String> future = new CompletableFuture<>();
    CompletableFuture<String> future2 = new CompletableFuture<>();

    kwic.execute(path, future::complete);
    kwic.execute(path, future2::complete);
    Assert.assertEquals(future.get(),future2.get());
  }

  @Test
  public void isResultNotEqualToAnotherResult() throws ExecutionException, InterruptedException {
    CompletableFuture<String> future = new CompletableFuture<>();
    CompletableFuture<String> future2 = new CompletableFuture<>();

    kwic.execute(path, future::complete);
    kwic2.execute(path2, future2::complete);

    Assert.assertNotEquals(future.get(),future2.get());

  }
}
