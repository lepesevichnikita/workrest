package org.klaster.restapi.controller;

/*
 * workrest
 *
 * 28.02.2020
 *
 */

import java.io.FileInputStream;
import java.io.IOException;
import org.klaster.domain.model.entity.FileInfo;
import org.klaster.restapi.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
  private FileService defaultFileService;

  @PostMapping
  public ResponseEntity<FileInfo> upload(@RequestParam("file") MultipartFile file) throws IOException {
    return new ResponseEntity<>(defaultFileService.saveFile(file.getInputStream()), HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<InputStreamResource> findByName(@PathVariable long id) throws IOException {
    InputStreamResource result = new InputStreamResource(new FileInputStream(defaultFileService.findFirstById(id)));
    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add("Content-Length", String.valueOf(result.contentLength()));
    return new ResponseEntity<>(result, headers, HttpStatus.OK);
  }

}
