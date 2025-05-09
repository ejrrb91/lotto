package com.project.lotto.user.repository;

import com.project.lotto.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
  Optional<User> findByProviderAndProviderId(String provider, String providerId);

  boolean existsByUsername(String username);

}
