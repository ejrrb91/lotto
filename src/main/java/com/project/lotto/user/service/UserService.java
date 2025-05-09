package com.project.lotto.user.service;

import com.project.lotto.user.entity.User;

public interface UserService {

  /**
   * usernamee으로 사용자 조회
   *
   * @throws org.springframework.security.core.userdetails.UsernameNotFoundException
   */

  User findByUsername(String username);
}

