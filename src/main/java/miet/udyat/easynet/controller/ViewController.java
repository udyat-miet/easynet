package miet.udyat.easynet.controller;

import miet.udyat.easynet.Application;
import miet.udyat.easynet.entity.User;
import miet.udyat.easynet.entity.View;
import miet.udyat.easynet.model.BaseModelAndView;
import miet.udyat.easynet.service.CategoryService;
import miet.udyat.easynet.service.ViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/view")
public class ViewController {

  @Autowired
  private ViewService viewService;

  @Autowired
  private CategoryService categoryService;

  @GetMapping(path = "/all")
  ModelAndView list(@RequestParam(name = "p", defaultValue = "0") Integer page) {
    BaseModelAndView modelAndView = new BaseModelAndView("view/list", "Views Galore");
    modelAndView.addObject("views", viewService.getLatest(page));
    modelAndView.addObject("pageNumber", page);
    return modelAndView;
  }

  @GetMapping(path = "/add")
  ModelAndView add() {
    return new BaseModelAndView("view/add", "Share views");
  }

  @PostMapping(path = "/add")
  ModelAndView add(@RequestParam(name = "content") String content,
                   @RequestParam(name = "categories") String categories) {
    User currentUser = Application.getLoggedInUser();
    if (currentUser != null) {
      View view = new View();
      view.setContent(content);
      view.setCategoryList(categoryService.parseCategoryString(categories));
      view.setOwner(Application.getLoggedInUser());
      view.setApproved("admin moderator".contains(currentUser.getAuthority()));
      String errorMessage = viewService.save(view);
      if (errorMessage != null) {
        BaseModelAndView modelAndView = new BaseModelAndView("view/add", "Share views");
        modelAndView.addObject("errorMessage", errorMessage);
        modelAndView.addObject("contentValue", content);
        modelAndView.addObject("categoriesValue", categories);
        return modelAndView;
      }
    }
    return new ModelAndView("redirect:/view/all");
  }

  @GetMapping(path = "/delete/{id}")
  ModelAndView delete(@PathVariable(name = "id") Integer id) {
    User currentUser = Application.getLoggedInUser();
    if (currentUser != null) {
      View view = viewService.getById(id);
      if (view != null &&
          ((int) currentUser.getId() == view.getOwner().getId() || "admin moderator".contains(currentUser.getAuthority()))) {
        viewService.deleteById(view.getId());
      }
    }

    return new ModelAndView("redirect:/view/all");
  }

  @GetMapping(path = "/approve/{id}")
  ModelAndView approve(@PathVariable(name = "id") Integer id) {
    View view = viewService.getById(id);
    User currentUser = Application.getLoggedInUser();
    if (currentUser != null && "admin moderator".contains(currentUser.getAuthority()) && view != null) {
      view.setApproved(true);
      viewService.save(view);
    }
    return new ModelAndView("redirect:/view/review");
  }

  @GetMapping(path = "/review")
  ModelAndView review(@RequestParam(name = "p", defaultValue = "0") Integer page) {
    BaseModelAndView modelAndView = new BaseModelAndView("view/list", "Review views");
    modelAndView.addObject("views", viewService.getUnapproved(page));
    modelAndView.addObject("pageNumber", page);
    modelAndView.addObject("enableReviewActions", true);
    return modelAndView;
  }
}
