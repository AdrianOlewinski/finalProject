package pl.coderslab;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pl.coderslab.service.SpringDataUserDetailsService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String redirectURL = request.getContextPath();

        String role = userDetails.getAuthorities().stream().
                map(GrantedAuthority::getAuthority).findFirst().get();


        if (role.equals("ROLE_ADMIN")) {
            redirectURL = "admin/test";
        } else if (role.equals("ROLE_USER")) {
            redirectURL = "user/workingtime";
        }
        response.sendRedirect(redirectURL);

    }

}
