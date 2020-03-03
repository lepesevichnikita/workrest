package org.klaster.restapi.service;

/*
 * workrest
 *
 * 28.02.2020
 *
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import javax.persistence.EntityNotFoundException;
import org.klaster.domain.builder.general.FileInfoBuilder;
import org.klaster.domain.model.entity.FileInfo;
import org.klaster.domain.repository.FileInfoRepository;
import org.klaster.domain.util.MessageUtil;
import org.klaster.restapi.configuration.FilesConfig;
import org.klaster.restapi.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DefaultFileService
 *
 * @author Nikita Lepesevich
 */

@Service
public class DefaultFileService {

  private FileInfoBuilder defaultFileInfoBuilder;

  private FileInfoRepository fileInfoRepository;

  private FilesConfig filesConfig;

  @Autowired
  public DefaultFileService(FileInfoBuilder defaultFileInfoBuilder, FileInfoRepository fileInfoRepository, FilesConfig filesConfig) throws IOException {
    this.defaultFileInfoBuilder = defaultFileInfoBuilder;
    this.fileInfoRepository = fileInfoRepository;
    this.filesConfig = filesConfig;
    initializeOutputFolder();
  }

  public FileInfo saveFile(InputStream inputStream, String outputFileName) throws IOException {
    File resultFile = FileUtil.makeChildItem(filesConfig.getOutputFolder(), outputFileName);
    overwriteFile(resultFile, inputStream);
    defaultFileInfoBuilder.setMd5(FileUtil.getHexMd5OfInputStream(inputStream))
                          .setPath(resultFile.getAbsolutePath());
    return fileInfoRepository.save(defaultFileInfoBuilder.build());
  }

  public FileInputStream findFirstById(long id) throws FileNotFoundException {
    FileInfo foundFileInfo = fileInfoRepository.findById(id)
                                               .orElseThrow(() -> new EntityNotFoundException(MessageUtil.getEntityByIdNotFoundMessage(FileInfo.class, id)));
    File foundFile = new File(foundFileInfo.getPath());
    return new FileInputStream(foundFile);
  }

  public FileInfo deleteById(long id) throws IOException {
    FileInfo removedFileInfo = fileInfoRepository.findById(id)
                                                 .orElseThrow(() -> new EntityNotFoundException(MessageUtil.getEntityByIdNotFoundMessage(FileInfo.class, id)));
    File deletedFile = new File(removedFileInfo.getPath());
    Files.delete(deletedFile.toPath());
    fileInfoRepository.delete(removedFileInfo);
    return removedFileInfo;
  }

  private void initializeOutputFolder() throws IOException {
    File outputFolder = filesConfig.getOutputFolder();
    if (!outputFolder.exists()) {
      Files.createDirectory(filesConfig.getOutputFolder()
                                       .toPath());
    }
  }


  private void overwriteFile(File overwritedFile, InputStream source) throws IOException {
    if (overwritedFile.exists()) {
      Files.delete(overwritedFile.toPath());
    }
    Files.copy(source, overwritedFile.toPath());
  }
}
