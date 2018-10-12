package miet.udyat.easynet.model;

import miet.udyat.easynet.entity.repository.UserRepository;

import java.sql.Date;

public class BirthdaysModelAndView extends BaseModelAndView {

  public BirthdaysModelAndView(UserRepository userRepository, Date date) {
    super("birthdays");
    Date nextDate = new Date(date.getTime() + 24 * 60 * 60 * 1000),
        prevDate = new Date(date.getTime() - 24 * 60 * 60 * 1000);
    addObject("currentDay", date.toString());
    addObject("next_day", nextDate.toString());
    addObject("prev_day", prevDate.toString());
    addObject("birthday_users", userRepository.getByBirthdate(date));

  }

  public BirthdaysModelAndView(UserRepository userRepository) {
    this(userRepository, new Date(System.currentTimeMillis()));
  }

  @Override
  String getPageTitle() {
    return "Birthdays";
  }
}
