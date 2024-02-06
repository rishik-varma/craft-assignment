package com.craft.demo.repositories;

import com.craft.demo.models.enitities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    public User findByUsernameAndPassword(String username, String password);
}
