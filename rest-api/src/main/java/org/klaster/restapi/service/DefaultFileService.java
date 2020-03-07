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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import org.klaster.domain.builder.general.FileInfoBuilder;
import org.klaster.domain.model.entity.FileInfo;
import org.klaster.domain.repository.FileInfoRepository;
import org.klaster.domain.util.MessageUtil;
import org.klaster.restapi.properties.FilesProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

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
  public DefaultFileService(FileInfoBuilder defaultFileInfoBuilder, FileInfoRepository fileInfoRepository, FilesProperties filesProperties)
      throws IOException {
    this.defaultFileInfoBuilder = defaultFileInfoBuilder;
    this.fileInfoRepository = fileInfoRepository;
    this.filesProperties = filesProperties;
    Files.createDirectories(this.filesProperties.getOutputFolder()
                                                .toPath());
  }

  @Transactional
  public FileInfo saveFile(InputStream inputStream, String outputFileName) throws IOException {
    LocalDateTime createdAt = LocalDateTime.now();
    String md5DigestAsHex = DigestUtils.md5DigestAsHex(inputStream);
    Path targetFolderPath = Paths.get(filesProperties.getOutputFolder()
                                                     .getCanonicalPath(), String.valueOf(createdAt.getNano()));
    Path resultFilePath = writeFileIntoFolder(inputStream, outputFileName, targetFolderPath);
    defaultFileInfoBuilder.setMd5(md5DigestAsHex)
                          .setPath(resultFilePath.toFile()
                                                 .getCanonicalPath())
                          .setCreatedAt(createdAt);
    return fileInfoRepository.save(defaultFileInfoBuilder.build());
  }

  public FileInputStream findFirstById(long id) throws FileNotFoundException {
    FileInfo foundFileInfo = fileInfoRepository.findById(id)
                                               .orElseThrow(() -> new EntityNotFoundException(MessageUtil.getEntityByIdNotFoundMessage(
                                                   FileInfo.class,
                                                   id)));
    File foundFile = new File(foundFileInfo.getPath());
    return new FileInputStream(foundFile);
  }

  @Transactional
  public FileInfo deleteById(long id) throws IOException {
    FileInfo removedFileInfo = fileInfoRepository.findById(id)
                                                 .orElseThrow(() -> new EntityNotFoundException(MessageUtil.getEntityByIdNotFoundMessage(
                                                     FileInfo.class,
                                                     id)));
    File deletedFile = new File(removedFileInfo.getPath());
    Files.delete(deletedFile.toPath());
    fileInfoRepository.delete(removedFileInfo);
    return removedFileInfo;
  }

  private Path writeFileIntoFolder(InputStream inputStream, String outputFileName, Path targetFolderPath) throws IOException {
    Files.createDirectories(targetFolderPath);
    Path resultFilePath = Paths.get(targetFolderPath.toString(), outputFileName);
    Files.copy(inputStream, resultFilePath);
    return resultFilePath;
  }
}