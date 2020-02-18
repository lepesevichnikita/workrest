package org.klaster.webapplication.controller;

/*
 * workrest
 *
 * 18.02.2020
 *
 */

import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.webapplication.dto.LoginInfoDTO;
import org.klaster.webapplication.repository.RoleRepository;
import org.klaster.webapplication.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * SystemAdministratorController
 *
 * @author Nikita Lepesevich
 */

@Controller
@RequestMapping("/administrators")
public class AdministratorsController {

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private AdministratorService administratorService;

  @GetMapping
  public ModelAndView getAllAdministrators() {
    return new ModelAndView("system_administrator/all", "administrators", administratorService.getAllAdministrators());
  }

  @GetMapping("/new")
  public ModelAndView getNewAdministratorForm() {
    return new ModelAndView("system_administrator/new", "loginInfoDTO", new LoginInfoDTO());
  }

  @PostMapping
  public ModelAndView createAdministrator(LoginInfoDTO loginInfoDTO) {
    LoginInfo loginInfo = loginInfoDTO.toLoginInfo();
    administratorService.registerAdministrator(loginInfo);
    return new ModelAndView("redirect:/");
  }
}
