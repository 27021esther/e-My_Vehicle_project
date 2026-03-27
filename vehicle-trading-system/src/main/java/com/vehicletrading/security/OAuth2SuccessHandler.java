package com.vehicletrading.security;

import com.vehicletrading.enums.UserRole;
import com.vehicletrading.model.User;
import com.vehicletrading.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // GitHub may return null email; fall back to login attribute
        String email = oAuth2User.getAttribute("email");
        String login = oAuth2User.getAttribute("login"); // GitHub username
        String name  = oAuth2User.getAttribute("name");

        // Detect provider: GitHub users have a "login" attribute, Google does not
        String provider = (login != null) ? "github" : "google";

        // Derive a unique username
        String username = (email != null)
                ? email.split("@")[0]
                : (login != null ? login : name.replaceAll("\\s+", "").toLowerCase());

        // Use email if present, otherwise build a placeholder for GitHub
        String resolvedEmail = (email != null) ? email : (username + "@github.com");

        String providerId = oAuth2User.getName();

        User user = userRepository.findByEmail(resolvedEmail).orElseGet(() -> {
            User newUser = User.builder()
                    .username(username)
                    .email(resolvedEmail)
                    .role(UserRole.ROLE_USER)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            return userRepository.save(newUser);
        });

        String token = jwtUtil.generateToken(user);
        getRedirectStrategy().sendRedirect(request, response, "/api/auth/oauth2/success?token=" + token);
    }
}
