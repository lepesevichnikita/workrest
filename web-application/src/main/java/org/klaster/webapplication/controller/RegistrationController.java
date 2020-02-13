package org.klaster.webapplication.controller;

/*
 * workrest
 *
 * 13.02.2020
 *
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
  private ModelAndView modelAndView;


  public ModelAndView getForm() {
    modelAndView.setViewName("registrationForm");
    return modelAndView;
  }

}
