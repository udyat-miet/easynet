package miet.udyat.easynet.model;

public class UploadMasterModelAndView extends BaseModelAndView {

  public UploadMasterModelAndView(boolean success, String errorMessage) {
    super("upload_masters");
    addObject("isShowingErrorBox", !success && errorMessage != null);
    addObject("isShowingSuccessBox", success);
    addObject("errorMessage", errorMessage);
  }

  public UploadMasterModelAndView() {
    this(false, null);
  }
}
