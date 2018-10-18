package miet.udyat.easynet.controller;

import miet.udyat.easynet.entity.repository.AnniversaryRepository;
import miet.udyat.easynet.entity.repository.UserRepository;
import miet.udyat.easynet.model.BaseModelAndView;
import miet.udyat.easynet.service.UploadMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
import java.sql.Date;

@Controller
public class RootController {

  @Autowired
  private AnniversaryRepository anniversaryRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UploadMasterService uploadMasterService;


  @GetMapping(path = "/login")
  ModelAndView login(Principal principal,
                     @RequestParam(name = "s", defaultValue = "0") Integer status) {
    if (principal != null) {
      return new ModelAndView("redirect:/");
    }
    BaseModelAndView modelAndView = new BaseModelAndView("login", "Login");
    modelAndView.addObject("isShowingErrorBox", status == 1);
    modelAndView.addObject("isShowingLogoutBox", status == 2);
    return modelAndView;
  }

  @GetMapping(path = "/upload_masters")
  ModelAndView uploadMasters() {
    return new BaseModelAndView("upload_masters", "Upload master record");
  }

  @PostMapping(path = "/upload_masters")
  ModelAndView uploadMasters(@RequestParam(name = "master_type") String masterType,
                             @RequestParam(name = "file") MultipartFile file) {
    BaseModelAndView modelAndView = new BaseModelAndView("upload_masters", "Upload master record");
    try {
      if (uploadMasterService.saveMasterRecord(masterType, file)) {
        modelAndView.addObject("isShowingSuccessBox", true);
        return modelAndView;
      }
      modelAndView.addObject("errorMessage", "Invalid master record type!");
    } catch (IOException e) {
      modelAndView.addObject("errorMessage", "Error parsing CSV records!");
    }
    return modelAndView;
  }

  @GetMapping(path = "/birthdays")
  ModelAndView birthdays() {
    return birthdaysOn(null);
  }

  @GetMapping(path = "/birthdays/{date}")
  ModelAndView birthdaysOn(@PathVariable(name = "date") Date date) {
    if (date == null)
      date = new Date(System.currentTimeMillis());
    Date nextDate = new Date(date.getTime() + 24 * 60 * 60 * 1000),
        prevDate = new Date(date.getTime() - 24 * 60 * 60 * 1000);
    BaseModelAndView modelAndView = new BaseModelAndView("birthdays", "Birthdays");
    modelAndView.addObject("currentDay", date.toString());
    modelAndView.addObject("nextDay", nextDate.toString());
    modelAndView.addObject("prevDay", prevDate.toString());
    modelAndView.addObject("birthdayUsers", userRepository.getByBirthDate(date));
    return modelAndView;
  }

  @GetMapping(path = "/anniversary")
  ModelAndView anniversary() {
    return anniversaryOn(null);
  }

  @GetMapping(path = "/anniversary/{date}")
  ModelAndView anniversaryOn(@PathVariable(name = "date") Date date) {
    if (date == null)
      date = new Date(System.currentTimeMillis());
    Date nextDate = new Date(date.getTime() + 24 * 60 * 60 * 1000),
        prevDate = new Date(date.getTime() - 24 * 60 * 60 * 1000);
    BaseModelAndView modelAndView = new BaseModelAndView("anniversary", "Anniversary");
    modelAndView.addObject("currentDay", date.toString());
    modelAndView.addObject("nextDay", nextDate.toString());
    modelAndView.addObject("prevDay", prevDate.toString());
    modelAndView.addObject("anniversaries", anniversaryRepository.findByDate(date));
    return modelAndView;
  }
}
