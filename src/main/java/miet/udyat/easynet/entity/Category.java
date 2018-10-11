package miet.udyat.easynet.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Category {

  @Id
  private Integer id;

  @Column(nullable = false, unique = true)
  private String name;
}

