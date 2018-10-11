package miet.udyat.easynet.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Store {

  @Id
  private Integer id;

  @Column(nullable = false)
  private String location;
}
