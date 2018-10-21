package miet.udyat.easynet.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Poll {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Integer id;

  @Column(nullable = false, unique = true)
  private String question;

  @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  @Setter(AccessLevel.NONE)
  private Timestamp createdTimestamp;

  @Column(nullable =  false)
  @ColumnDefault("2")
  private Integer options;

  @OneToMany
  @JoinColumn(name = "poll_id")
  @Setter(AccessLevel.NONE)
  private List<PollVote> votes = new ArrayList<>();

  public boolean canUserVote(int userId) {
    if (isVotingExpired())
      return false;
      for (PollVote v : votes) {
        if (v.getUser().getId() == userId)
          return false;
      }
    return true;
  }

  public boolean isVotingExpired() {
    return createdTimestamp.before(new Timestamp(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
  }

  public String[] getStats() {
    int total = votes.size(),
        yes = 0, no = 0, maybe = 0;
    for (PollVote v : votes) {
      switch (v.getWeight()) {
        case -1:
          no++;
          break;
        case 0:
          maybe++;
          break;
        case 1:
          yes++;
          break;
      }
    }
    if (total == 0)
      return new String[] {"0", "0%", "0%",  "0%"};

    return String.format("%d,%.2f%%,%.2f%%,%.2f%%",
        total,
        ((float) yes * 100 / total) ,
        ((float) no * 100 / total),
        ((float) maybe * 100 / total)
    ).split(",");
  }
}
