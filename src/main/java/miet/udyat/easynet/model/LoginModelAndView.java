package miet.udyat.easynet.model;

public class LoginModelAndView extends BaseModelAndView {

  public LoginModelAndView(boolean isShowingErrorBox, boolean isShowingLogoutBox) {
    super("login");
    addObject("isShowingErrorBox", isShowingErrorBox);
    addObject("isShowingLogoutBox", isShowingLogoutBox);
  }
}
