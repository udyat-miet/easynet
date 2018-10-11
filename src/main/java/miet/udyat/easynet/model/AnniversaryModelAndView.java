package miet.udyat.easynet.model;

import miet.udyat.easynet.entity.repository.AnniversaryRepository;

import java.sql.Date;

public class AnniversaryModelAndView extends BaseModelAndView {

  public AnniversaryModelAndView(AnniversaryRepository anniversaryRepository, Date date) {
    super("anniversary");
    Date nextDate = new Date(date.getTime() + 24 * 60 * 60 * 1000),
        prevDate = new Date(date.getTime() - 24 * 60 * 60 * 1000);
    addObject("currentDay", date.toString());
    addObject("next_day", nextDate.toString());
    addObject("prev_day", prevDate.toString());
    if (getModel().get("title") == null)
      addObject("title", "Anniversary");
    addObject("anniversaries", anniversaryRepository.getByDate(date));
  }

  public AnniversaryModelAndView(AnniversaryRepository anniversaryRepository) {
    this(anniversaryRepository, new Date(System.currentTimeMillis()));
  }

  @Override
  String getPageTitle() {
    return "Anniversary";
  }
}
