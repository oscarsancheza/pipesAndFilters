package com.mcc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class pipesAndFiltersTest {

  private final String path =
      "/home/dessis-aux23/Documentos/maestria/pipesAndFilters/src/main/java/com/mcc/KWIC_file.txt";

  @Before
  public void setUp() {}

  @Test
  public void isResultEqualToAnotherResult() {
    KWIC kwic = new KWIC();
    KWIC kwic2 = new KWIC();

    kwic.execute(
        path,
        data -> {
          kwic2.execute(
              path,
              data1 -> {
                Assert.assertEquals(data, data1);
              });
        });
  }
}
