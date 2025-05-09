package com.project.lotto.config;
import com.project.lotto.user.entity.User;
import com.project.lotto.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 관리자 계정 없을 때 자동 생성
 */
@Component
@RequiredArgsConstructor
public class DataInitalizer implements CommandLineRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AdminProperties adminProperties;


  @Override
  public void run(String[] args) throws Exception {
    String adminEmail = adminProperties.getEmail();
    if (userRepository.existsByUsername(adminEmail)) {
      User admin = User.builder()
          .username(adminEmail)
          .password(passwordEncoder.encode(adminProperties.getPassword()))
          .provider(adminProperties.getProvider())
          .providerId(null)
          .userRole(User.ROLE_ADMIN)
          .build();
      userRepository.save(admin);
      System.out.println("기본 관리자 계정 생성 : " + adminEmail);
    }
  }
}
