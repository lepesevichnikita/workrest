package org.klaster.restapi.controller;

/*
 * workrest
 *
 * 28.02.2020
 *
 */

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletResponse;
import org.klaster.domain.model.entity.FileInfo;
import org.klaster.restapi.service.DefaultFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * FileInfoController
 *
 * @author Nikita Lepesevich
 */

@RestController
@RequestMapping("/file")
public class FileInfoController {

  @Autowired
  private DefaultFileService defaultFileService;

  @PostMapping
  @PreAuthorize("hasAuthority('USER')")
  public ResponseEntity<FileInfo> upload(@RequestParam("file") MultipartFile file) throws IOException {
    FileInfo savedFileInfo = defaultFileService.saveFile(file.getInputStream(), file.getOriginalFilename());
    return new ResponseEntity<>(savedFileInfo, HttpStatus.CREATED);
  }


  @GetMapping(value = "/{id}")
  public void getFile(@PathVariable("id") long id, HttpServletResponse response) throws IOException {
    InputStream foundFileInputStream = defaultFileService.findFirstById(id);
    StreamUtils.copy(foundFileInputStream, response.getOutputStream());
    response.flushBuffer();
  }
}
