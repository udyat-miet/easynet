package miet.udyat.easynet.service;

import miet.udyat.easynet.entity.Anniversary;
import miet.udyat.easynet.entity.Category;
import miet.udyat.easynet.entity.Store;
import miet.udyat.easynet.entity.User;
import miet.udyat.easynet.entity.repository.AnniversaryRepository;
import miet.udyat.easynet.entity.repository.CategoryRepository;
import miet.udyat.easynet.entity.repository.StoreRepository;
import miet.udyat.easynet.entity.repository.UserRepository;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;

@Service
public class UploadMasterService {

  @Autowired
  private AnniversaryRepository anniversaryRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private UserRepository userRepository;

  public void saveAnniversaryMaster(CSVRecord record) {
    Optional<User> user = userRepository.findById(Integer.valueOf(record.get(0)));
    if (user.isPresent()) {
      Anniversary anniversary = new Anniversary();
      anniversary.setUser(user.get());
      anniversary.setDate(Date.valueOf(record.get(2)));
      anniversaryRepository.save(anniversary);
    }
  }

  public void saveBirthdayMaster(CSVRecord record) {
    Optional<User> optionalUser = userRepository.findById(Integer.valueOf(record.get(0)));
    User user;
    if (optionalUser.isPresent()) {
      user = optionalUser.get();
    } else {
      user = new User();
      user.setId(Integer.valueOf(record.get(0)));
      user.setUsername(record.get(0));
      user.setPassword(record.get(1));
      user.setAuthority("user");
    }
    user.setName(record.get(1));
    user.setBirthday(Date.valueOf(record.get(2)));
    Optional<Store> store = storeRepository.findById(Integer.valueOf(record.get(3)));
    if (store.isPresent())
      user.setStore(store.get());
    userRepository.save(user);
  }

  public void saveCategoryMaster(CSVRecord record) {
    Category category = new Category();
    category.setId(Integer.valueOf(record.get(0)));
    category.setName(record.get(1));
    categoryRepository.save(category);
  }

  public void saveStoreMaster(CSVRecord record) {
    Store store = new Store();
    store.setId(Integer.valueOf(record.get(0)));
    store.setLocation(record.get(1));
    storeRepository.save(store);
  }
}
