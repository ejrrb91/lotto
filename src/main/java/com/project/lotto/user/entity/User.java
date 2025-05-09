package com.project.lotto.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

  @Id
  @GeneratedValue
  private Long id;

  /**
   * 로컬 : 이메일, 소셜 : provider + '_' + providerId
   */
  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  /**
   * "local", "google"
   */
  @Column(nullable = false)
  private String provider;

  /**
   * 로컬 : "", 구글 : 구글Id
   */
  @Column(nullable = false)
  private String providerId;

  /**
   * 일반 : "ROLE_USER", 관리자 : "ROLE_ADMIN"
   */
  @Builder.Default
  @Column(nullable = false)
  private String userRole = "ROLE_USER";

  public static final String ROLE_USER = "ROLE_USER";
  public static final String ROLE_ADMIN = "ROLE_ADMIN";

  public SimpleGrantedAuthority getRoleAuthority() {
    return new SimpleGrantedAuthority(this.userRole);
  }
}
