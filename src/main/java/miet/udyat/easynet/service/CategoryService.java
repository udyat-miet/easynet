package miet.udyat.easynet.service;

import lombok.NonNull;
import miet.udyat.easynet.entity.Category;
import miet.udyat.easynet.entity.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

@Service
public class CategoryService {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private CategoryRepository categoryRepository;

  public List<Category> getAll(int page) {
    return entityManager.createQuery("select c from Category c order by c.name", Category.class)
        .setFirstResult(page * 30)
        .setMaxResults(30)
        .getResultList();
  }

  public List<Category> parseCategoryString(@NonNull String categories) {
    return categoryRepository.findAllByNameIn(
        Arrays.asList(categories.replace(", ", ",").split(","))
    );
  }

  public Category getById(Integer id) {
    return categoryRepository.findById(id).orElse(null);
  }
}
