package com.nickng.ccd.controller;

import com.nickng.ccd.dto.ConfigDto;
import com.nickng.ccd.exception.InvalidJsonException;
import com.nickng.ccd.model.ConfigModel;
import com.nickng.ccd.model.LinkModel;
import com.nickng.ccd.service.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ConfigController {

  private final ConfigService configService;

  @GetMapping("/reload-config")
  public String reloadConfig() {
    try {
      ConfigService.reloadConfig();
      return "Ok";
    } catch (FileNotFoundException e) {
      log.error("Khong tim thay file config", e);
      return "Khong tim thay file config";
    } catch (InvalidJsonException e) {
      log.error("File config khong dung dinh dang", e);
      return "File config khong dung dinh dang";
    }
  }

  @PostMapping("/update-config")
  public String saveConfig(@RequestBody ConfigDto configDto) {
    try {
      configService.updateConfig(configDto);
      log.info("Update config successfully");
      return "Ok";
    } catch (Exception e) {
      log.error("Error updating config.", e);
      return e.getMessage();
    }
  }

  @PostMapping("/update-links")
  public String saveLinks(@RequestBody Set<LinkModel> linkSet) {
    ConfigModel configObject = new ConfigModel();
    configObject.setLinks(linkSet);
    try {
      configService.updateConfig(configObject);
      reloadConfig();
      log.info("Update config successfully");
      return "Ok";
    } catch (Exception e) {
      log.error("Error updating links.", e);
      return e.getMessage();
    }
  }

}
