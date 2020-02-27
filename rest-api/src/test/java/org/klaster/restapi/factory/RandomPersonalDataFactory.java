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
import org.klaster.domain.builder.DefaultPersonalDataBuilder;
import org.klaster.domain.builder.PersonalDataBuilder;
import org.klaster.domain.model.entity.FileInfo;
import org.klaster.domain.model.entity.PersonalData;

/**
 * RandomPersonalDataFactory
 *
 * @author Nikita Lepesevich
 */

public class RandomPersonalDataFactory {

  private static RandomPersonalDataFactory instance;

  private PersonalDataBuilder defaultPersonalDataBuilder;
  private RandomFileInfoFactory randomFileInfoFactory;
  private Faker faker;

  private RandomPersonalDataFactory() throws NoSuchAlgorithmException {
    faker = Faker.instance(SecureRandom.getInstanceStrong());
    defaultPersonalDataBuilder = new DefaultPersonalDataBuilder();
    randomFileInfoFactory = RandomFileInfoFactory.getInstance();
  }

  public static RandomPersonalDataFactory getInstance() throws NoSuchAlgorithmException {
    if (instance == null) {
      synchronized (RandomPersonalDataFactory.class) {
        instance = new RandomPersonalDataFactory();
      }
    }
    return instance;
  }

  private String getFirstName() {
    return faker.name()
                .firstName();
  }

  private String getLastName() {
    return faker.name()
                .lastName();
  }


  private String getDocumentName() {
    return faker.book()
                .title();
  }

  private String getDocumentNumber() {
    return faker.idNumber()
                .validSvSeSsn();
  }

  private FileInfo getDocumentScan() {
    return randomFileInfoFactory.build();
  }

  public PersonalData build() {
    defaultPersonalDataBuilder.reset();
    return defaultPersonalDataBuilder.setDocumentName(getDocumentName())
                                     .setDocumentNumber(getDocumentNumber())
                                     .setDocumentScan(getDocumentScan())
                                     .setFirstName(getFirstName())
                                     .setLastName(getLastName())
                                     .build();
  }
}
