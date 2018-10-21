package miet.udyat.easynet.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Data
@Entity
public class Todo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Integer id;

  @Column(nullable = false, length = 128)
  private String task;

  @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
  private short priority;

  @ManyToOne(optional = false)
  private User user;

  @ManyToOne(optional = false)
  private Store store;

  @Column(nullable = false)
  @ColumnDefault("0")
  private boolean isCompleted;
}
