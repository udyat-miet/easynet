package miet.udyat.easynet.service;

import miet.udyat.easynet.Application;
import miet.udyat.easynet.entity.Store;
import miet.udyat.easynet.entity.Todo;
import miet.udyat.easynet.entity.repository.StoreRepository;
import miet.udyat.easynet.entity.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class TodoService {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private TodoRepository todoRepository;

  public Todo getById(Integer id) {
    return todoRepository.findById(id).orElse(null);
  }

  public Store getStoreById(Integer id) {
    return storeRepository.findById(id).orElse(null);
  }

  public Iterable<Store> getAllStores() {
    return storeRepository.findAll();
  }

  public String save(Todo todo) {
    if (todo.getTask().trim().isEmpty())
      return "Task field is empty!";
    if (todo.getPriority() < 0 || todo.getPriority() > 2)
      return "Invalid priority value!";
    if (todo.getStore() == null)
      return "Invalid store!";
    if (todo.getUser() == null)
      return "Invalid user!";
    todoRepository.save(todo);
    return null;
  }

  public List<Todo> getLatest(Integer page) {
    return entityManager.createQuery("select t from Todo t where t.user = :user order by t.id desc", Todo.class)
        .setParameter("user", Application.getLoggedInUser())
        .setFirstResult(page * 20)
        .setMaxResults(20)
        .getResultList();
  }

  public void deleteById(Integer id) {
    todoRepository.deleteById(id);
  }
}
