package miet.udyat.easynet;

import miet.udyat.easynet.entity.repository.AnniversaryRepository;
import miet.udyat.easynet.entity.repository.UserRepository;
import miet.udyat.easynet.model.LoginModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@org.springframework.stereotype.Controller
class Controller {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AnniversaryRepository anniversaryRepository;

  @GetMapping(path = "/login")
  ModelAndView login(Principal principal,
                     @RequestParam(name = "error", defaultValue = "false") String isShowingErrorBox,
                     @RequestParam(name = "logout", defaultValue = "false") String isShowingLogoutBox) {
    if (principal != null)
      return new ModelAndView("redirect:/");
    return new LoginModelAndView(isShowingErrorBox.equals("true"), isShowingLogoutBox.equals("true"));
  }
}
