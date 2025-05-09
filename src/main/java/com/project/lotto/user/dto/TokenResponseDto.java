package com.project.lotto.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponseDto {

  /**
   * 발급된 JWT
   */
  private String token;
}
