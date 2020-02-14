package org.klaster.webapplication.controller;

/*
 * workrest
 *
 * 13.02.2020
 *
 */

import org.klaster.domain.model.context.User;
import org.klaster.webapplication.dto.LoginInfoDTO;
import org.klaster.webapplication.service.UserRegistrationService;
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
  private UserRegistrationService userRegistrationService;

  @GetMapping
  public ModelAndView getForm() {
    return new ModelAndView("registration/form");
  }

  @PostMapping
  public ModelAndView registerUserWithLoginInfo(LoginInfoDTO loginInfoDTO) {
    User newUser = new User();
    newUser.setLoginInfo(loginInfoDTO.toLoginInfo());
    userRegistrationService.createUser(newUser);
    return new ModelAndView("registration/form");
  }

}
