/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpringSecurity;

import DAO.AdminDAO;
import DAO.StudentDAO;
import Mapping.POJO.Admins;
import Mapping.POJO.Students;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author lun
 */
public class UserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String string) throws UsernameNotFoundException {
        Students student = StudentDAO.getStudent(string);

        if (student != null) {
            if (student.getLoggedin()) {
                throw new UsernameNotFoundException("User Logged In");
            }
            List roles = new ArrayList();
            roles.add(new SimpleGrantedAuthority("ROLE_USER"));
            return new User(student.getEmail(), student.getPassword(), true, true, true, true, roles);
        }
        Admins admin = AdminDAO.getAdmin(string);
        if (admin != null) {
            if (admin.getLoggedin()) {
                throw new UsernameNotFoundException("User Logged In");
            }
            List roles = new ArrayList();
            roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            return new User(admin.getEmail(), admin.getPassword(), true, true, true, true, roles);
        }
        throw new UsernameNotFoundException("User not found");
    }

}
