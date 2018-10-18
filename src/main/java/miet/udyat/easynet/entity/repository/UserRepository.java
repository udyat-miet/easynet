package miet.udyat.easynet.entity.repository;

import miet.udyat.easynet.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

  @Nullable
  User findByUsername(String username);

  @Query("select u from User u where day(u.birthday) = day(?1) and month(u.birthday) = month(?1)")
  List<User> getByBirthDate(Date date);
}
