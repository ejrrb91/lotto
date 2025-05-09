package com.project.lotto.user.service;

import com.project.lotto.user.dto.LoginDto;
import com.project.lotto.user.dto.SignUpDto;

public interface AuthService {
  void register(SignUpDto signUpDto);

  String login(LoginDto loginDto);

}
