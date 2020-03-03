package org.klaster.restapi.factory;

/*
 * workrest
 *
 * 27.02.2020
 *
 */

import java.security.NoSuchAlgorithmException;
import org.klaster.domain.builder.concrete.DefaultPersonalDataBuilder;
import org.klaster.domain.builder.general.PersonalDataBuilder;
import org.klaster.domain.model.entity.FileInfo;
import org.klaster.domain.model.entity.PersonalData;

/**
 * RandomPersonalDataFactory
 *
 * @author Nikita Lepesevich
 */

public class RandomPersonalDataFactory extends AbstractRandomFactory<PersonalData> {

  private static RandomPersonalDataFactory instance;

  private PersonalDataBuilder defaultPersonalDataBuilder;
  private RandomFileInfoFactory randomFileInfoFactory;


  private RandomPersonalDataFactory() throws NoSuchAlgorithmException {
    super();
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
    return getFaker().name()
                     .firstName();
  }

  private String getLastName() {
    return getFaker().name()
                     .lastName();
  }


  private String getDocumentName() {
    return getFaker().book()
                     .title();
  }

  private String getDocumentNumber() {
    return getFaker().idNumber()
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
