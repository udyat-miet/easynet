package miet.udyat.easynet.entity.repository;

import miet.udyat.easynet.entity.View;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewRepository extends CrudRepository<View, Integer> {
}
