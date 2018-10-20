package miet.udyat.easynet.entity.repository;

import miet.udyat.easynet.entity.PollVote;
import org.springframework.data.repository.CrudRepository;

public interface VoteRepository extends CrudRepository<PollVote, Integer> {
}
