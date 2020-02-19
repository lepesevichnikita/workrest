package org.klaster.webapplication.controller;

/*
 * workrest
 *
 * 13.02.2020
 *
 */

import org.klaster.webapplication.dto.LoginInfoDTO;
import org.klaster.webapplication.service.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * RegistrationController
 *
 * @author Nikita Lepesevich
 */

@RestController
@RequestMapping("/register")
public class RegistrationController {

  @Autowired
  private ApplicationUserService applicationUserService;

  @PostMapping
  public ModelAndView create(LoginInfoDTO loginInfoDTO) {
    applicationUserService.registerUserByLoginInfo(loginInfoDTO.toLoginInfo());
    return new ModelAndView("register_form", "loginInfoDTO", loginInfoDTO);
  }

}
