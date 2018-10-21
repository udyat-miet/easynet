package miet.udyat.easynet.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.datetime.DateFormatter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

@Data
@Entity
public class View {

  private static final DateFormatter DEFAULT_DATE_FORMAT = new DateFormatter("MMM dd, yy HH:mm");

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Integer id;

  @Column(nullable = false, length = 511)
  private String content;

  @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  @Setter(AccessLevel.NONE)
  private Timestamp createdTimestamp;

  @Column(nullable = false)
  @ColumnDefault("0")
  private boolean isApproved;

  @ManyToMany
  @JoinTable(name = "view_category")
  private List<Category> categoryList;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id")
  private User owner;

  public String getCategoryString() {
    String retVal = "";
    for (Category category : categoryList)
      retVal += ", " + category.getName();
    return retVal.substring(2);
  }

  public String getCreatedTime() {
    return DEFAULT_DATE_FORMAT.print(createdTimestamp, Locale.getDefault());
  }
}
