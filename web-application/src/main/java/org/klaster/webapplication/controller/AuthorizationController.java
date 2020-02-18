package org.klaster.webapplication.controller;

/*
 * workrest
 *
 * 14.02.2020
 *
 */

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * AuthorizationController
 *
 * @author Nikita Lepesevich
 */

@Controller
@RequestMapping("/login")
public class AuthorizationController {

  @GetMapping
  public String getForm(Model model, String error, String logout) {
    if (error != null) {
      model.addAttribute("error", "Your username and password is invalid.");
    }

    if (logout != null) {
      model.addAttribute("message", "You have been logged out successfully.");
    }

    return "login";
  }

}
