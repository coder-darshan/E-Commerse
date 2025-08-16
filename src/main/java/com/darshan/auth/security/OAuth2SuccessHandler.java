package com.darshan.auth.security;
import com.darshan.auth.entity.User;
import com.darshan.auth.enums.Role;
import com.darshan.auth.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    public OAuth2SuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Extract details (works for Google/GitHub with standard claims)
        if (authentication.getPrincipal() instanceof DefaultOAuth2User oAuth2User) {
            Map<String, Object> attrs = oAuth2User.getAttributes();
            String email = (String) attrs.getOrDefault("email", "");
            String name  = (String) attrs.getOrDefault("name", "");

            if (email != null && !email.isBlank()) {
                userRepository.findByEmail(email).orElseGet(() -> {
                    User u = new User();
                    u.setEmail(email);
                    u.setUsername(name != null && !name.isBlank() ? name : email);
                    u.setEnabled(true);           // oauth users are verified by provider
                    u.setRole(Role.USER);
                    return userRepository.save(u);
                });
            }
        }

        // For now, return 200 with a simple message. (Later we’ll redirect + issue JWT.)
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\":\"OAuth2 login successful. Complete JWT hookup in Step 9.\"}");
    }
}