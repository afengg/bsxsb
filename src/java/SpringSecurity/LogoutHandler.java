/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpringSecurity;

import DAO.AdminDAO;
import DAO.StudentDAO;
import Mapping.POJO.Students;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

/**
 *
 * @author lun
 */
public class LogoutHandler extends SimpleUrlLogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        Students student = StudentDAO.getStudent(authentication.getName());
        if (student != null) {
            StudentDAO.setLoggedOut(authentication.getName());
        } else {
            AdminDAO.setLoggedOut(authentication.getName());
        }

        super.onLogoutSuccess(request, response, authentication);
    }
}
