package miet.udyat.easynet.service;

import lombok.NonNull;
import miet.udyat.easynet.entity.Category;
import miet.udyat.easynet.entity.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CategoryService {

  @Autowired
  private CategoryRepository categoryRepository;

  public List<Category> parseCategoryString(@NonNull String categories) {
    return categoryRepository.findAllByNameIn(
        Arrays.asList(categories.replace(", ", ",").split(","))
    );
  }
}
