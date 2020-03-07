package org.klaster.restapi.factory;

/*
 * workrest
 *
 * 27.02.2020
 *
 */

import org.klaster.domain.builder.concrete.DefaultFileInfoBuilder;
import org.klaster.domain.builder.general.FileInfoBuilder;
import org.klaster.domain.model.entity.FileInfo;

/**
 * RandomFileInfoFactory
 *
 * @author Nikita Lepesevich
 */

public class RandomFileInfoFactory extends AbstractRandomFactory<FileInfo> {

  private static RandomFileInfoFactory instance;
  private FileInfoBuilder defaultFileInfoBuilder;

  private RandomFileInfoFactory() {
    super();
    defaultFileInfoBuilder = new DefaultFileInfoBuilder();
  }

  public static RandomFileInfoFactory getInstance() {
    if (instance == null) {
      synchronized (RandomPersonalDataFactory.class) {
        instance = new RandomFileInfoFactory();
      }
    }
    return instance;
  }

  private String getMd5() {
    final int startRange = (int) Math.pow(2, 128);
    final int endRange = (int) Math.pow(2, 129 - 1);
    return Integer.toHexString(getFaker().number()
                                         .numberBetween(startRange, endRange));
  }

  private String getPath() {
    return getFaker().file()
                     .fileName();
  }

  public FileInfo build() {
    defaultFileInfoBuilder.reset();
    return defaultFileInfoBuilder.setMd5(getMd5())
                                 .setPath(getPath())
                                 .build();
  }

}
