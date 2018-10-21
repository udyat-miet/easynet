package miet.udyat.easynet.model;

import miet.udyat.easynet.Application;
import miet.udyat.easynet.entity.User;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class BaseModelAndView extends ModelAndView {

  public BaseModelAndView(String templatePath, String pageTitle) {
    super(templatePath);
    String requestURL;
    try {
      requestURL = new URL(ServletUriComponentsBuilder.fromCurrentRequest().toUriString()).getPath();
    } catch (MalformedURLException e) {
      requestURL = "/";
    }
    addObject("title", pageTitle);
    addObject("requestURL", requestURL);
    addObject("currentYear", Calendar.getInstance().get(Calendar.YEAR));
    User currentUser = Application.getLoggedInUser();
    if (currentUser != null) {
      addObject("currentUser", currentUser);
      switch (currentUser.getAuthority()) {
        case "user":
          addObject("isUserGuest", false);
          addObject("isUserModerator", false);
          addObject("isUserAdmin", false);
          break;
        case "moderator":
          addObject("isUserGuest", false);
          addObject("isUserModerator", true);
          addObject("isUserAdmin", false);
          break;
        case "admin":
          addObject("isUserGuest", false);
          addObject("isUserModerator", true);
          addObject("isUserAdmin", true);
          break;
      }
    } else {
      addObject("currentUser", "guest");
      addObject("isUserGuest", true);
      addObject("isUserModerator", false);
      addObject("isUserAdmin", false);
    }
  }
}
