package miet.udyat.easynet.entity.repository;

import miet.udyat.easynet.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

  List<User> findByUsername(String username);
  List<User> findByBirthday(Date date);
}
