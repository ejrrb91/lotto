package com.project.lotto.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangeRoleDto {

  /**
   * "ROLE_USER" 또는 "ROLE_ADMIN"
   */
  @Pattern(regexp = "ROLE_USER|ROLE_ADMIN")
  private String role;
}
