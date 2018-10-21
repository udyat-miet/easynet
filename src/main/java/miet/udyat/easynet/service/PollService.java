package miet.udyat.easynet.service;

import miet.udyat.easynet.entity.Poll;
import miet.udyat.easynet.entity.PollVote;
import miet.udyat.easynet.entity.repository.PollRepository;
import miet.udyat.easynet.entity.repository.UserRepository;
import miet.udyat.easynet.entity.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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

  public String save(Poll poll) {
    if (poll.getQuestion().trim().isEmpty())
      return "Candidate question is required field!";
    if (poll.getQuestion().length() < 16)
      return "Candidate question must be atleast 16 characters long!";
    if (poll.getOptions() != 2 && poll.getOptions() != 3)
      return "Invalid number of options!";
    pollRepository.save(poll);
    return null;
  }

  public String save(PollVote vote) {
    if (vote.getWeight() < -1 || vote.getWeight() > 1)
      return "Invalid vote value!";
    if (vote.getPoll() == null)
      return "Poll not found!";
    if (vote.getUser() == null)
      return "User not found!";
    voteRepository.save(vote);
    return null;
  }

  public Poll getById(Integer id) {
    return pollRepository.findById(id).orElse(null);
  }

  public List<Poll> getLatest(int page) {
    return entityManager.createQuery("select p from Poll p order by p.createdTimestamp desc", Poll.class)
        .setFirstResult(page * 10)
        .setMaxResults(10)
        .getResultList();
  }
}
