package miet.udyat.easynet.controller;

import miet.udyat.easynet.Application;
import miet.udyat.easynet.entity.Poll;
import miet.udyat.easynet.entity.PollVote;
import miet.udyat.easynet.model.BaseModelAndView;
import miet.udyat.easynet.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/poll")
public class PollController {

  @Autowired
  private PollService pollService;

  @GetMapping(path = "/setup")
  ModelAndView setup() {
    return new BaseModelAndView("poll/setup", "Setup poll");
  }

  @PostMapping(path = "/setup")
  ModelAndView setup(@RequestParam(name = "candidate") String candidateQuestion,
                     @RequestParam(name = "options", defaultValue = "2") Integer votingOptions) {
    Poll poll = new Poll();
    poll.setQuestion(candidateQuestion);
    poll.setOptions(votingOptions);
    String error = pollService.save(poll);
    if (error != null) {
      BaseModelAndView modelAndView = new BaseModelAndView("poll/setup", "Setup poll");
      modelAndView.addObject("errorMessage", error);
      modelAndView.addObject("candidateValue", candidateQuestion);
      modelAndView.addObject("optionsValue", votingOptions);
      return modelAndView;
    }
    return new ModelAndView("redirect:/poll/all");
  }

  @GetMapping(path = "/all")
  ModelAndView all(@RequestParam(name = "p", defaultValue = "0") Integer page) {
    BaseModelAndView modelAndView = new BaseModelAndView("poll/list", "Polls");
    modelAndView.addObject("polls", pollService.getLatest(page));
    modelAndView.addObject("pageNumber", page);
    return modelAndView;
  }

  @PostMapping(path = "/vote/{id}")
  ModelAndView vote(@PathVariable(name = "id") Integer pollId,
                    @RequestParam(name = "page") Integer pageNumber,
                    @RequestParam(name = "weight") Integer weight) {
    PollVote vote = new PollVote();
    vote.setWeight(weight);
    vote.setPoll(pollService.getById(pollId));
    vote.setUser(Application.getLoggedInUser());
    pollService.save(vote);
    return new ModelAndView("redirect:/poll/all?p=" + pageNumber);
  }
}
