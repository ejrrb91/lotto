package com.project.lotto.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpDto {

  /**
   * 이메일
   */
  @Email
  @NotBlank
  private String username;

  @NotBlank
  @Size(min = 8, max = 30, message = "비밀번호는 8자 이상 30자 이하여야 합니다.")
//  @Pattern(
//      regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()]).+$",
//      message = "비밀번호는 대문자, 소문자, 숫자, 특수문자를 모두 포함해야 합니다."
//  )
  private String password;

}
