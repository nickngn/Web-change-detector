package com.projects.ccd.controller;

import java.io.FileNotFoundException;
import java.util.Set;

import com.projects.ccd.dto.ConfigDto;
import com.projects.ccd.exception.InvalidJsonException;
import com.projects.ccd.model.ConfigModel;
import com.projects.ccd.model.LinkModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.projects.ccd.service.ConfigService;

@RestController
public class ConfigController {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConfigService configService;

  @GetMapping("/reload-config")
  public String reloadConfig() {
    try {
      ConfigService.reloadConfig();
      return "Ok";
    } catch (FileNotFoundException e) {
      logger.error("Khong tim thay file config", e);
      return "Khong tim thay file config";
    } catch (InvalidJsonException e) {
      logger.error("File config khong dung dinh dang", e);
      return "File config khong dung dinh dang";
    }
  }

  @PostMapping("/update-config")
  public String saveConfig(@RequestBody ConfigDto configDto) {
    try {
      configService.updateConfig(configDto);
      logger.info("Update config successfully");
      return "Ok";
    } catch (Exception e) {
      logger.error("Error updating config.", e);
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
      logger.info("Update config successfully");
      return "Ok";
    } catch (Exception e) {
      logger.error("Error updating links.", e);
      return e.getMessage();
    }
  }

}
