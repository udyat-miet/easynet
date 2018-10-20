package miet.udyat.easynet.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.format.datetime.DateFormatter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Data
@Entity
public class Question {

  private static final DateFormatter DEFAULT_DATE_FORMAT = new DateFormatter("MMM dd, yy HH:mm");

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Integer id;

  @Column(nullable = false, length = 128)
  private String title;

  @Column(nullable = false, length = 1024)
  private String description;

  @Column(updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  @Setter(AccessLevel.NONE)
  private Timestamp createdTimestamp;

  @Column(nullable = false)
  private boolean isApproved;

  @ManyToMany
  @JoinTable(name = "question_category")
  private List<Category> categoryList = new ArrayList<>();

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User owner;

  @OneToMany(orphanRemoval = true)
  @JoinColumn(name = "question_id")
  private List<QuestionAnswer> answers = new ArrayList<>();

  public String getCategoryString() {
    String retVal = "";
    for (Category category : categoryList)
      retVal += ", " + category.getName();
    return retVal.substring(2);
  }

  public String getCreatedTime() {
    return DEFAULT_DATE_FORMAT.print(createdTimestamp, Locale.getDefault());
  }

  public String getShortDescription() {
    return description.length() < 128 ? description : description.substring(0, 125) + "...";
  }
}
