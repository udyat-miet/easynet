package miet.udyat.easynet.service;

import miet.udyat.easynet.entity.Poll;
import miet.udyat.easynet.entity.PollVote;
import miet.udyat.easynet.entity.User;
import miet.udyat.easynet.entity.repository.PollRepository;
import miet.udyat.easynet.entity.repository.UserRepository;
import miet.udyat.easynet.entity.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Service
public class PollService {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PollRepository pollRepository;

  @Autowired
  private VoteRepository voteRepository;

  public String save(Poll poll, String question, int options) {
    if (question.isEmpty())
      return "Candidate question is required field!";
    if (question.length() < 16)
      return "Candidate question must be atleast 16 characters long!";
    if (options != 2 && options != 3)
      return "Invalid number of options!";
    poll.setQuestion(question);
    poll.setOptions(options);
    pollRepository.save(poll);
    return null;
  }

  public List<Poll> getLatest(int page) {
    return entityManager.createQuery("select p from Poll p order by p.createdTimestamp desc", Poll.class)
        .setFirstResult(page * 10)
        .setMaxResults(10)
        .getResultList();
  }

  public void addVote(Integer pollId, int weight, String username) {
    User user = userRepository.findByUsername(username);
    Optional<Poll> poll = pollRepository.findById(pollId);
    if (user == null || !poll.isPresent() || weight < -1 || weight > 1)
      return;
    PollVote vote = new PollVote();
    vote.setPoll(poll.get());
    vote.setUser(user);
    vote.setWeight(weight);
    poll.get().getVotes().add(vote);
    voteRepository.save(vote);
  }
}
