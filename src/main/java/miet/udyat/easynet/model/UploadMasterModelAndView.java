package miet.udyat.easynet.model;

public class UploadMasterModelAndView extends BaseModelAndView {

  public UploadMasterModelAndView(boolean success, String errorMessage) {
    super("upload_masters");
    addObject("isShowingSuccessBox", success);
    addObject("errorMessage", errorMessage);
  }

  public UploadMasterModelAndView() {
    this(false, null);
  }

  @Override
  String getPageTitle() {
    return "Upload master records";
  }
}