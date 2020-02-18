package org.klaster.webapplication.controller;

/*
 * workrest
 *
 * 14.02.2020
 *
 */

import org.klaster.webapplication.dto.LoginInfoDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * AuthorizationController
 *
 * @author Nikita Lepesevich
 */

@Controller
@RequestMapping("/login")
public class AuthorizationController {

  @GetMapping
  public ModelAndView getForm() {
    return new ModelAndView("login_form", "loginInfoDTO", new LoginInfoDTO());
  }

}
