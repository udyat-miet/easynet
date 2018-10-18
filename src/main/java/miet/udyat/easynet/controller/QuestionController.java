package miet.udyat.easynet.controller;

import miet.udyat.easynet.entity.Question;
import miet.udyat.easynet.entity.QuestionAnswer;
import miet.udyat.easynet.entity.User;
import miet.udyat.easynet.entity.repository.UserRepository;
import miet.udyat.easynet.model.BaseModelAndView;
import miet.udyat.easynet.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
@RequestMapping(path = "/question")
public class QuestionController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private QuestionService questionService;

  @GetMapping(path = "/ask")
  ModelAndView ask() {
    return new BaseModelAndView("question/ask", "Ask a question");
  }

  @PostMapping(path = "/ask")
  ModelAndView ask(Principal principal,
                   @RequestParam(name = "title") String title,
                   @RequestParam(name = "description") String description,
                   @RequestParam(name = "categories") String categories) {
    Question question = new Question();
    String errorMessage = questionService.save(question, title, description, categories, principal.getName());
    if (errorMessage != null) {
      BaseModelAndView modelAndView = new BaseModelAndView("question/ask", "Ask a question");
      modelAndView.addObject("errorMessage", errorMessage);
      modelAndView.addObject("titleValue", title);
      modelAndView.addObject("descriptionValue", description);
      modelAndView.addObject("categoriesValue", categories);
      return modelAndView;
    }
    return new ModelAndView("redirect:/question/" + question.getId());
  }

  @GetMapping(path = "/{id}")
  ModelAndView view(@PathVariable(name = "id") Integer id,
                    @RequestParam(name = "a", defaultValue = "0") String isAnswered) {
    Question question = questionService.getQuestionById(id);
    BaseModelAndView modelAndView = new BaseModelAndView("question/view", "404");
    if (question != null) {
      modelAndView.addObject("title", question.getTitle());
      modelAndView.addObject("question", question);
      modelAndView.addObject("isAnswered", isAnswered.equals("1"));
      return modelAndView;
    }
    modelAndView.addObject("isQuestionNotFound", true);
    return modelAndView;
  }

  @PostMapping(path = "/{id}")
  ModelAndView acceptAnswer(Principal principal,
                            @PathVariable(name = "id") Integer id,
                            @RequestParam(name = "answer") String answerContent) {
    Question question = questionService.getQuestionById(id);
    if (question != null) {
      String error = questionService.addAnswer(question, answerContent, principal.getName());
      if (error == null)
        return new ModelAndView("redirect:/question/" + id + "#answer-form?a=1");
      BaseModelAndView modelAndView = new BaseModelAndView("question/view", question.getTitle());
      modelAndView.addObject("question", question);
      modelAndView.addObject("answerErrorMessage", error);
      modelAndView.addObject("answerContent", answerContent);
      return modelAndView;
    }
    BaseModelAndView modelAndView = new BaseModelAndView("question/view", "404 - Not Found");
    modelAndView.addObject("isQuestionNotFound", true);
    return modelAndView;
  }

  @GetMapping(path = "/delete/{id}")
  ModelAndView delete(Principal principal,
                      @PathVariable(name = "id") Integer id) {
    Question question = questionService.getQuestionById(id);
    User currentUser = userRepository.findByUsername(principal.getName());
    if (question != null &&
        (currentUser.getAuthority().equals("moderator")
            || currentUser.getAuthority().equals("admin")
            || currentUser.getId() == question.getOwner().getId())) {
      questionService.deleteQuestionById(id);
    }
    return new ModelAndView("redirect:/questions");
  }

  @GetMapping(path = "/answer/delete/{id}")
  ModelAndView deleteAnswer(Principal principal,
                            @PathVariable(name = "id") Integer id) {
    QuestionAnswer answer = questionService.getAnswerById(id);
    User currentUser = userRepository.findByUsername(principal.getName());
    if (answer != null &&
        (currentUser.getAuthority().equals("moderator")
            || currentUser.getAuthority().equals("admin")
            || currentUser.getId() == answer.getOwner().getId())) {
      questionService.deleteAnswerById(id);
    }
    return new ModelAndView("redirect:/question/" + answer.getQuestion().getId());
  }

  @GetMapping(path = "/review")
  ModelAndView review(@RequestParam(name = "p", defaultValue = "0") Integer page) {
    BaseModelAndView modelAndView = new BaseModelAndView("question/list", "Review questions");
    modelAndView.addObject("questions", questionService.getUnapproved(page));
    modelAndView.addObject("pageNumber", page);
    modelAndView.addObject("enableReviewActions", true);
    return modelAndView;
  }

  @GetMapping(path = "/approve/{id}")
  ModelAndView approve(Principal principal, @PathVariable(name = "id") Integer id) {
    Question question = questionService.getQuestionById(id);
    User currentUser = userRepository.findByUsername(principal.getName());
    if("admin moderator".contains(currentUser.getAuthority()) && question != null) {
      question.setApproved(true);
      questionService.save(question);
    }
    return new ModelAndView("redirect:/question/review");
  }

  @GetMapping(path = "/all")
  ModelAndView list(@RequestParam(name = "p", defaultValue = "0") Integer page) {
    BaseModelAndView modelAndView = new BaseModelAndView("question/list", "Latest Questions");
    modelAndView.addObject("questions", questionService.getLatest(page));
    modelAndView.addObject("pageNumber", page);
    return modelAndView;
  }
}
