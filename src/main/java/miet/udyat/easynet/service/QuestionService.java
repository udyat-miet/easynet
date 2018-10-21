package miet.udyat.easynet.service;

import lombok.NonNull;
import miet.udyat.easynet.entity.Category;
import miet.udyat.easynet.entity.Question;
import miet.udyat.easynet.entity.QuestionAnswer;
import miet.udyat.easynet.entity.repository.CategoryRepository;
import miet.udyat.easynet.entity.repository.QuestionAnswerRepository;
import miet.udyat.easynet.entity.repository.QuestionRepository;
import miet.udyat.easynet.entity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

@Service
public class QuestionService {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private QuestionAnswerRepository answerRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private UserRepository userRepository;

  public String save(Question question) {
    if (question.getTitle().trim().isEmpty())
      return "Title is a required field!";
    if (question.getTitle().length() < 16)
      return "Title should be atleast 20 characters long!";
    if (question.getDescription().trim().isEmpty())
      return "Description is a required field!";
    if (question.getDescription().length() < 32)
      return "Description should be atleast 32 characters long!";

    if (question.getCategoryList().isEmpty())
      return "Atleast 1 valid category is required!";
    questionRepository.save(question);
    return null;
  }

  public String save(QuestionAnswer questionAnswer) {

    return null;
  }

  @Nullable
  public Question getQuestionById(Integer id) {
    return questionRepository.findById(id).orElse(null);
  }

  @Nullable
  public QuestionAnswer getAnswerById(Integer id) {
    return answerRepository.findById(id).orElse(null);
  }

  public String addAnswer(Question dest, String answerContent, String ownerUsername) {
    if (answerContent.isEmpty()) {
      return "Vote is empty!";
    } if (answerContent.length() < 64) {
      return "Vote must be at least 64 characters long!";
    }
    QuestionAnswer answer = new QuestionAnswer();
    answer.setContent(answerContent);
    answer.setOwner(userRepository.findByUsername(ownerUsername));
    answerRepository.save(answer);
    return null;
  }

  public void deleteQuestionById(Integer id) {
    questionRepository.deleteById(id);
  }

  public void deleteAnswerById(Integer id) {
    answerRepository.deleteById(id);
  }

  public List<Question> getLatest(int page) {
    return entityManager.createQuery("select q from Question q where q.isApproved = 1 order by q.createdTimestamp desc", Question.class)
        .setFirstResult(page * 10)
        .setMaxResults(10)
        .getResultList();
  }

  public List<Question> getUnapproved(int page) {
    return entityManager.createQuery("select q from Question q where q.isApproved = 0 order by q.createdTimestamp", Question.class)
        .setFirstResult(page * 10)
        .setMaxResults(10)
        .getResultList();
  }

  public List<Category> parseCategoryString(@NonNull String categories) {
    return categoryRepository.findByNameIn(
        Arrays.asList(categories.replace(", ", ",").split(","))
    );
  }
}
