package miet.udyat.easynet.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.sql.Date;

@Data
@Entity
@IdClass(Anniversary.PrimaryKey.class)
public class Anniversary {

  @Id
  private Integer userId;

  @Id
  private Date date;

  public static class PrimaryKey implements Serializable {
    private Integer userId;
    private Date date;
  }
}
