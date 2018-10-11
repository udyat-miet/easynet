package miet.udyat.easynet.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@Entity
@IdClass(Anniversary.PrimaryKey.class)
public class Anniversary {

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id")
  private User user;

  @Id
  private Date date;

  public long getAge() {
    return ChronoUnit.YEARS.between(this.date.toLocalDate(), LocalDate.now());
  }

  public static class PrimaryKey implements Serializable {
    private User user;
    private Date date;
  }
}
