package miet.udyat.easynet.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@Entity
public class Anniversary {

  @EmbeddedId
  private PrimaryKey id = new PrimaryKey();

  public long getAge() {
    return ChronoUnit.YEARS.between(this.id.date.toLocalDate(), LocalDate.now());
  }

  public Date getDate() {
    return id.date;
  }

  public void setDate(Date date) {
    if (id == null)
      id = new PrimaryKey();
    id.date = date;
  }

  public User getUser() {
    return id.user;
  }

  public void setUser(User user) {
    if (id == null)
      id = new PrimaryKey();
    id.user = user;
  }

  @Embeddable
  @EqualsAndHashCode
  public static class PrimaryKey implements Serializable {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private Date date;
  }
}
