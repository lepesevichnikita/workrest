package org.klaster.webapplication.controller;

/*
 * workrest
 *
 * 13.02.2020
 *
 */

import org.klaster.domain.model.context.ApplicationUser;
import org.klaster.webapplication.dto.LoginInfoDTO;
import org.klaster.webapplication.service.ApplicationUserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * RegistrationController
 *
 * @author Nikita Lepesevich
 */

@Controller
@RequestMapping("/register")
public class RegistrationController {

  @Autowired
  private ApplicationUserRegistrationService applicationUserRegistrationService;

  @GetMapping
  public ModelAndView getForm() {
    return new ModelAndView("register_form", "loginInfoDTO", new LoginInfoDTO());
  }

  @PostMapping
  public ModelAndView registerUserWithLoginInfo(LoginInfoDTO loginInfoDTO) {
    ApplicationUser newApplicationUser = new ApplicationUser();
    newApplicationUser.setLoginInfo(loginInfoDTO.toLoginInfo());
    applicationUserRegistrationService.registerUser(newApplicationUser);
    loginInfoDTO.setPassword("");
    return new ModelAndView("register_form", "loginInfoDTO", loginInfoDTO);
  }

}
