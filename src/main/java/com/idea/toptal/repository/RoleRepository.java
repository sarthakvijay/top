package com.idea.toptal.repository;

import java.util.Optional;

import com.idea.toptal.models.ERole;
import com.idea.toptal.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  /**
   * Finding the role using the ERole object.
   * @param name
   * @return
   */
  Optional<Role> findByName(ERole name);
}
