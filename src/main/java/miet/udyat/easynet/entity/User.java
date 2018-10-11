package miet.udyat.easynet.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;

@Data
@Entity
public class User implements UserDetails {

  @Id
  private Integer id;

  @Column(nullable = false, unique = true, length = 32)
  private String username;

  @Column(nullable = false, length = 64)
  private String password;

  @Column(nullable = false, length = 64)
  private String name;

  @Column(nullable = false, length = 16, columnDefinition = "CHAR(10) DEFAULT 'user'")
  private String authority;

  @Column
  private Date birthday;

  @ManyToOne(optional = false)
  @JoinColumn(name = "store_id")
  private Store store;

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(() -> authority);
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  public long getAge() {
    return ChronoUnit.YEARS.between(this.birthday.toLocalDate(), LocalDate.now());
  }

  public void setPassword(String newPassword) {
    this.password = new BCryptPasswordEncoder().encode(newPassword);
  }
}