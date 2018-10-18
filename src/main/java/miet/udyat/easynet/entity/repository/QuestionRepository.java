package miet.udyat.easynet.entity.repository;

import miet.udyat.easynet.entity.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;

public interface QuestionRepository extends CrudRepository<Question, Integer> {

  @Nullable
  Question findByTitle(String title);
}
