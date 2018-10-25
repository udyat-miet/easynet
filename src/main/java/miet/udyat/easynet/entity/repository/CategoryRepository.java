package miet.udyat.easynet.entity.repository;

import miet.udyat.easynet.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {

  Category findByName(String name);

  List<Category> findAllByNameIn(List<String> names);
}
