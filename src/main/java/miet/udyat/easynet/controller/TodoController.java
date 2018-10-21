package miet.udyat.easynet.controller;

import miet.udyat.easynet.Application;
import miet.udyat.easynet.entity.Todo;
import miet.udyat.easynet.entity.User;
import miet.udyat.easynet.model.BaseModelAndView;
import miet.udyat.easynet.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/todo")
public class TodoController {

  @Autowired
  private TodoService todoService;

  @GetMapping(path = "/all")
  ModelAndView list(@RequestParam(name = "p", defaultValue = "0") Integer page,
                    @RequestParam(name = "e", defaultValue = "-2") Integer editTodoId,
                    @RequestParam(name = "a", defaultValue = "0") Integer actionResponse,
                    @RequestParam(name="t", defaultValue = "") String newTaskValue,
                    @RequestParam(name="p", defaultValue = "0") Integer newPriorityValue,
                    @RequestParam(name="s", defaultValue = "0") Integer newStoreIdValue) {
    BaseModelAndView modelAndView = new BaseModelAndView("todo", "TODOs");
    modelAndView.addObject("todos", todoService.getLatest(page));
    modelAndView.addObject("editTodoId", editTodoId);
    modelAndView.addObject("actionResponse", actionResponse);
    modelAndView.addObject("pageNumber", page);
    if (editTodoId != -2) {
      modelAndView.addObject("stores", todoService.getAllStores());
    }

    if (editTodoId == -1) {
      modelAndView.addObject("newTaskValue", newTaskValue);
      modelAndView.addObject("newPriorityValue", newPriorityValue);
      modelAndView.addObject("newStoreIdValue", newStoreIdValue);
    }
    return modelAndView;
  }

  @PostMapping(path = "/create")
  ModelAndView create(@RequestParam(name = "task") String task,
                      @RequestParam(name = "priority") Short priority,
                      @RequestParam(name = "store_id", defaultValue = "-1") Integer storeId) {
    Todo todo = new Todo();
    todo.setTask(task);
    todo.setPriority(priority);
    todo.setStore(todoService.getStoreById(storeId));
    todo.setUser(Application.getLoggedInUser());
    todo.setCompleted(false);
    if (todoService.save(todo) == null)
      return new ModelAndView("redirect:/todo/all?a=1");
    return new ModelAndView("redirect:/todo/all?a=3&e=-1&t=" + task + "&p=" + priority + "&s=" + storeId);
  }

  @PostMapping(path = "/edit/{id}")
  ModelAndView edit(@PathVariable(name = "id") Integer todoId,
                    @RequestParam(name = "task") String task,
                    @RequestParam(name = "priority") Short priority,
                    @RequestParam(name = "store_id") Integer storeId) {
    Todo todo = todoService.getById(todoId);
    if (todo != null) {
      todo.setTask(task);
      todo.setPriority(priority);
      todo.setStore(todoService.getStoreById(storeId));
      if (todoService.save(todo) == null)
        return new ModelAndView("redirect:/todo/all?a=1");
    }
    return new ModelAndView("redirect:/todo/all?a=3&e=" + todoId + "&t=" + task + "&p=" + priority + "&s=" + storeId);
  }

  @GetMapping(path = "/delete/{id}")
  ModelAndView delete(@PathVariable(name = "id") Integer todoId) {
    User currentUser = Application.getLoggedInUser();
    if (currentUser != null) {
      Todo todo = todoService.getById(todoId);
      if (todo != null && (int) todo.getUser().getId() == currentUser.getId()) {
        todoService.deleteById(todo.getId());
        return new ModelAndView("redirect:/todo/all?a=2");
      }
    }
    return new ModelAndView("redirect:/todo/all?a=4");
  }

  @GetMapping(path = "/completed/{id}")
  ModelAndView markCompleted(@PathVariable(name = "id") Integer todoId,
                             @RequestParam(name = "c", defaultValue = "1") Boolean isCompleted,
                             @RequestParam(name = "p", defaultValue = "0") Integer page) {
    User currentUser = Application.getLoggedInUser();
    if (currentUser != null) {
      Todo todo = todoService.getById(todoId);
      if (todo != null && (int) todo.getUser().getId() == currentUser.getId()) {
        todo.setCompleted(isCompleted);
        todoService.save(todo);
      }
    }
    return new ModelAndView("redirect:/todo/all?p=" + page);
  }
}
