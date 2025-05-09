package com.project.lotto.security;

import com.project.lotto.user.entity.User;
import com.project.lotto.user.repository.UserRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
    OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
    String provider = oAuth2UserRequest.getClientRegistration().getRegistrationId();
    String providerId = oAuth2User.getAttribute("sub");

    User user = userRepository.findByProviderAndProviderId(provider, providerId)
        .orElseGet(() -> {
          User user1 = User.builder()
              .username(oAuth2User.getAttribute("email"))
              .password("")
              .provider(provider)
              .providerId(providerId)
              .userRole(User.ROLE_USER)
              .build();
          return userRepository.save(user1);
        });
    return new DefaultOAuth2User(
        Collections.singleton(user.getRoleAuthority()),
        oAuth2User.getAttributes(),
        "sub"
    );
  }

}
