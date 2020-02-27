package org.klaster.restapi.factory;

/*
 * workrest
 *
 * 27.02.2020
 *
 */

import com.github.javafaker.Faker;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.klaster.domain.builder.DefaultFileInfoBuilder;
import org.klaster.domain.builder.FileInfoBuilder;
import org.klaster.domain.model.entity.FileInfo;

/**
 * RandomFileInfoFactory
 *
 * @author Nikita Lepesevich
 */

public class RandomFileInfoFactory {

  private static RandomFileInfoFactory instance;
  private FileInfoBuilder defaultFileInfoBuilder;
  private Faker faker;

  private RandomFileInfoFactory() throws NoSuchAlgorithmException {
    faker = Faker.instance(SecureRandom.getInstanceStrong());
    defaultFileInfoBuilder = new DefaultFileInfoBuilder();
  }

  public static RandomFileInfoFactory getInstance() throws NoSuchAlgorithmException {
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
    return Integer.toHexString(faker.number()
                                    .numberBetween(startRange, endRange));
  }

  private String getPath() {
    return faker.file()
                .fileName();
  }

  public FileInfo build() {
    defaultFileInfoBuilder.reset();
    return defaultFileInfoBuilder.setMd5(getMd5())
                                 .setPath(getPath())
                                 .build();
  }

}
