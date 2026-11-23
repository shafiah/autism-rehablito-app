
package com.autism_rehablito_app.repository;

import com.autism_rehablito_app.entity.User;
import com.autism_rehablito_app.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByMobileNumber(String mobileNumber);

    boolean existsByEmail(String email);

    boolean existsByMobileNumber(String mobileNumber);

    // Find all active Admin users
    List<User> findByRoleAndActiveTrue(Role role);
}
