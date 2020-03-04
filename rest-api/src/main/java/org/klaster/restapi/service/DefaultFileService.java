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
import java.time.LocalDateTime;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import org.klaster.domain.builder.general.FileInfoBuilder;
import org.klaster.domain.model.entity.FileInfo;
import org.klaster.domain.repository.FileInfoRepository;
import org.klaster.domain.util.MessageUtil;
import org.klaster.restapi.properties.FilesProperties;
import org.klaster.restapi.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DefaultFileService
 *
 * @author Nikita Lepesevich
 */

@Service
@Transactional
public class DefaultFileService {

  private FileInfoBuilder defaultFileInfoBuilder;

  private FileInfoRepository fileInfoRepository;

  private FilesProperties filesProperties;

  @Autowired
  public DefaultFileService(FileInfoBuilder defaultFileInfoBuilder, FileInfoRepository fileInfoRepository, FilesProperties filesProperties) throws IOException {
    this.defaultFileInfoBuilder = defaultFileInfoBuilder;
    this.fileInfoRepository = fileInfoRepository;
    this.filesProperties = filesProperties;
    initializeOutputFolder();
  }

  @Transactional
  public FileInfo saveFile(InputStream inputStream, String outputFileName) throws IOException {
    LocalDateTime createdAt = LocalDateTime.now();
    File targetFolder = FileUtil.createSubFolderIfNotExists(filesProperties.getOutputFolder(), String.valueOf(createdAt.getNano()));
    File resultFile = FileUtil.makeChildItem(targetFolder, outputFileName);
    Files.copy(inputStream, resultFile.toPath());
    defaultFileInfoBuilder.setMd5(FileUtil.getHexMd5OfInputStream(inputStream))
                          .setPath(resultFile.getCanonicalPath())
                          .setCreatedAt(createdAt);
    return fileInfoRepository.save(defaultFileInfoBuilder.build());
  }

  public FileInputStream findFirstById(long id) throws FileNotFoundException {
    FileInfo foundFileInfo = fileInfoRepository.findById(id)
                                               .orElseThrow(() -> new EntityNotFoundException(MessageUtil.getEntityByIdNotFoundMessage(FileInfo.class, id)));
    File foundFile = new File(foundFileInfo.getPath());
    return new FileInputStream(foundFile);
  }

  @Transactional
  public FileInfo deleteById(long id) throws IOException {
    FileInfo removedFileInfo = fileInfoRepository.findById(id)
                                                 .orElseThrow(() -> new EntityNotFoundException(MessageUtil.getEntityByIdNotFoundMessage(FileInfo.class, id)));
    File deletedFile = new File(removedFileInfo.getPath());
    Files.delete(deletedFile.toPath());
    fileInfoRepository.delete(removedFileInfo);
    return removedFileInfo;
  }

  private void initializeOutputFolder() throws IOException {
    File outputFolder = filesProperties.getOutputFolder();
    if (!outputFolder.exists()) {
      Files.createDirectory(filesProperties.getOutputFolder()
                                           .toPath());
    }
  }
}
