package miet.udyat.easynet.entity.repository;

import miet.udyat.easynet.entity.Anniversary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;

public interface AnniversaryRepository extends CrudRepository<Anniversary, Anniversary.PrimaryKey> {

  @Query("select a from Anniversary a where day(a.id.date) = day(?1) and month(a.id.date) = month(?1)")
  List<Anniversary> findByDate(Date date);
}
