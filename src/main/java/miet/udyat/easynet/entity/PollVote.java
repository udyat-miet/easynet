package miet.udyat.easynet.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class PollVote {

  @EmbeddedId
  private PrimaryKey id = new PrimaryKey();

  @Column(nullable = false)
  private Integer weight;

  public User getUser() {
    return id.user;
  }

  public Poll getPoll() {
    return id.poll;
  }

  public void setUser(User user) {
    id.user = user;
  }

  public void setPoll(Poll poll) {
    id.poll = poll;
  }

  @Embeddable
  @EqualsAndHashCode
  private static class PrimaryKey implements Serializable {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;
  }
}
