package miet.udyat.easynet;

import miet.udyat.easynet.entity.repository.AnniversaryRepository;
import miet.udyat.easynet.entity.repository.UserRepository;
import miet.udyat.easynet.model.AnniversaryModelAndView;
import miet.udyat.easynet.model.BirthdaysModelAndView;
import miet.udyat.easynet.model.LoginModelAndView;
import miet.udyat.easynet.model.UploadMasterModelAndView;
import miet.udyat.easynet.service.UploadMasterService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Principal;
import java.sql.Date;

@org.springframework.stereotype.Controller
class Controller {

  @Autowired
  private AnniversaryRepository anniversaryRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UploadMasterService uploadMasterService;

  @GetMapping(path = "/login")
  ModelAndView login(Principal principal,
                     @RequestParam(name = "error", defaultValue = "false") String isShowingErrorBox,
                     @RequestParam(name = "logout", defaultValue = "false") String isShowingLogoutBox) {
    if (principal != null)
      return new ModelAndView("redirect:/");
    return new LoginModelAndView(isShowingErrorBox.equals("true"), isShowingLogoutBox.equals("true"));
  }

  @GetMapping(path = "/upload_masters")
  ModelAndView uploadMasters() {
    return new UploadMasterModelAndView();
  }

  @PostMapping(path = "/upload_masters")
  ModelAndView uploadMasters(@RequestParam(name = "master_type") String masterType,
                             @RequestParam(name = "file") MultipartFile file) {
    try {
      if (!file.getContentType().equalsIgnoreCase("text/csv")) {
        throw new IOException("Was expecting text/csv; Got - " + file.getContentType());
      }
      Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(new InputStreamReader(file.getInputStream()));
      switch(masterType) {
        case "store-master":
          records.forEach(uploadMasterService::saveStoreMaster);
          break;
        case "birthday-master":
          records.forEach(uploadMasterService::saveBirthdayMaster);
          break;
        case "anniversary-master":
          records.forEach(uploadMasterService::saveAnniversaryMaster);
          break;
        case "category-master":
          records.forEach(uploadMasterService::saveCategoryMaster);
          break;
        default:
          return new UploadMasterModelAndView(false, "Invalid master record type!");
      }
      return new UploadMasterModelAndView(true, null);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      return new UploadMasterModelAndView(false, "Error parsing CSV records!");
    }
  }

  @GetMapping(path = "/birthdays")
  ModelAndView birthdays() {
    return new BirthdaysModelAndView(userRepository);
  }

  @GetMapping(path = "/birthdays/{date}")
  ModelAndView birthdaysOn(@PathVariable(value = "date") Date date) {
    return new BirthdaysModelAndView(userRepository, date);
  }

  @GetMapping(path = "/anniversary")
  ModelAndView anniversary() {
    return new AnniversaryModelAndView(anniversaryRepository);
  }

  @GetMapping(path = "/anniversary/{date}")
  ModelAndView anniversaryOn(@PathVariable(value = "date") Date date) {
    return new AnniversaryModelAndView(anniversaryRepository, date);
  }
}
