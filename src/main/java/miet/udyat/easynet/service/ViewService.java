package miet.udyat.easynet.service;

import lombok.NonNull;
import miet.udyat.easynet.entity.Category;
import miet.udyat.easynet.entity.View;
import miet.udyat.easynet.entity.repository.CategoryRepository;
import miet.udyat.easynet.entity.repository.ViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

@Service
public class ViewService {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private ViewRepository viewRepository;

  public List<View> getLatest(int page) {
    return entityManager.createQuery("select v from View v where v.isApproved != 0 order by v.createdTimestamp desc", View.class)
        .setFirstResult(page * 10)
        .setMaxResults(10)
        .getResultList();
  }

  public List<View> getUnapproved(Integer page) {
    return entityManager.createQuery("select v from View v where v.isApproved = 0 order by v.createdTimestamp desc", View.class)
        .setFirstResult(page * 10)
        .setMaxResults(10)
        .getResultList();
  }

  public List<Category> parseCategoryString(@NonNull String categories) {
    return categoryRepository.findByNameIn(
        Arrays.asList(categories.replace(", ", ",").split(","))
    );
  }

  public String save(View view) {
    if (view.getContent().trim().isEmpty())
      return "View field cannot be empty!";
    if (view.getCategoryList().isEmpty())
      return "At least one valid category is required!";
    if (view.getOwner() == null)
      return "Invalid user!";
    viewRepository.save(view);
    return null;
  }

  public View getById(Integer viewId) {
    return viewRepository.findById(viewId).orElse(null);
  }

  public void deleteById(Integer id) {
    viewRepository.deleteById(id);
  }
}
