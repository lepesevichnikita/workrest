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
import org.apache.commons.io.FileExistsException;
import org.klaster.domain.builder.general.FileInfoBuilder;
import org.klaster.domain.model.entity.FileInfo;
import org.klaster.domain.repository.FileInfoRepository;
import org.klaster.domain.util.FileUtil;
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
    final long timeStamp = FileUtil.getTimeStampFromLocalDateTime(createdAt);
    File targetFolder = new File(filesProperties.getOutputFolder(), String.valueOf(timeStamp));
    File targetFile = new File(outputFileName);
    File writtenFile = writeFileIntoFolder(inputStream, targetFile, targetFolder);
    String md5DigestAsHex = DigestUtils.md5DigestAsHex(new FileInputStream(writtenFile));
    defaultFileInfoBuilder.setMd5(md5DigestAsHex)
                          .setPath(writtenFile.getCanonicalPath())
                          .setCreatedAt(createdAt);
    return fileInfoRepository.save(defaultFileInfoBuilder.build());
  }

  public FileInputStream findFirstById(long id) throws FileNotFoundException {
    FileInfo foundFileInfo = fileInfoRepository.findById(id)
                                               .orElseThrow(() -> new EntityNotFoundException(MessageUtil.getEntityByIdNotFoundMessage(
                                                   FileInfo.class,
                                                   id)));
    Path foundFilePath = Paths.get(foundFileInfo.getPath());
    if (Files.notExists(foundFilePath)) {
      throw new FileNotFoundException();
    }
    return new FileInputStream(foundFilePath.toFile());
  }

  @Transactional
  public FileInfo deleteById(long id) throws IOException {
    FileInfo removedFileInfo = fileInfoRepository.findById(id)
                                                 .orElseThrow(() -> new EntityNotFoundException(MessageUtil.getEntityByIdNotFoundMessage(
                                                     FileInfo.class,
                                                     id)));
    File deletedFile = new File(removedFileInfo.getPath());
    fileInfoRepository.delete(removedFileInfo);
    if (Files.notExists(deletedFile.toPath())) {
      throw new FileNotFoundException();
    }
    Files.delete(deletedFile.toPath());
    return removedFileInfo;
  }

  private File writeFileIntoFolder(InputStream inputStream, File targetFile, File targetFolder) throws IOException {
    Files.createDirectories(targetFolder.toPath());
    File resultFile = new File(targetFolder, targetFile.getName());
    if (resultFile.exists()) {
      throw new FileExistsException();
    }
    Files.copy(inputStream, resultFile.toPath());
    return resultFile;
  }
}