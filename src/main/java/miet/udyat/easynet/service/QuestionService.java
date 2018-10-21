package miet.udyat.easynet.service;

import miet.udyat.easynet.entity.Question;
import miet.udyat.easynet.entity.QuestionAnswer;
import miet.udyat.easynet.entity.repository.QuestionAnswerRepository;
import miet.udyat.easynet.entity.repository.QuestionRepository;
import miet.udyat.easynet.entity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class QuestionService {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private QuestionAnswerRepository answerRepository;

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

  // when you write code while sleeping :(
  public String save(QuestionAnswer answer) {
    if (answer.getContent().trim().isEmpty()) {
      return "Answer field is empty!";
    } if (answer.getContent().length() < 64) {
      return "Vote must be at least 64 characters long!";
    } if (answer.getQuestion() == null) {
      return "Question not found!";
    } if (answer.getOwner() == null)
      return "User not found!";
    answerRepository.save(answer);
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

  public void deleteQuestionById(Integer id) {
    questionRepository.deleteById(id);
  }

  public void deleteAnswerById(Integer id) {
    answerRepository.deleteById(id);
  }

  public List<Question> getLatest(int page) {
    return entityManager.createQuery("select q from Question q where q.isApproved != 0 order by q.createdTimestamp desc", Question.class)
        .setFirstResult(page * 10)
        .setMaxResults(10)
        .getResultList();
  }

  public List<Question> getUnapproved(int page) {
    return entityManager.createQuery("select q from Question q where q.isApproved = 0 order by q.createdTimestamp desc", Question.class)
        .setFirstResult(page * 10)
        .setMaxResults(10)
        .getResultList();
  }
}
