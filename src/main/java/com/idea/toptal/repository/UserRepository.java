package com.idea.toptal.repository;

import java.util.Optional;

import com.idea.toptal.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * find user using the username (not primary key)
   * @param username
   * @return
   */
  Optional<User> findByUsername(String username);

  /**
   * checking does user already exists with given username, (enforcing the unique username constraint)
   * @param username
   * @return
   */
  Boolean existsByUsername(String username);

  /**
   * Checking that user with given email alreadt ecists or not.
   * @param email
   * @return
   */
  Boolean existsByEmail(String email);
}
