package miet.udyat.easynet.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.format.datetime.DateFormatter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Locale;

@Data
@Entity
public class QuestionAnswer {

  private static final DateFormatter DEFAULT_DATE_FORMAT = new DateFormatter("MMM dd, yy HH:mm");

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Integer id;

  @Column(length = 1024)
  private String content;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User owner;

  @ManyToOne(optional = false)
  @JoinColumn(name = "question_id", nullable = false)
  private Question question;

  @Column(updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  @Setter(AccessLevel.NONE)
  private Timestamp createdTimestamp;

  public String getCreatedTime() {
    return DEFAULT_DATE_FORMAT.print(createdTimestamp, Locale.getDefault());
  }
}
