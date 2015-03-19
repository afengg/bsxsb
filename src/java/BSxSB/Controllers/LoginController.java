/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BSxSB.Controllers;

import DAO.AdminDAO;
import DAO.ScheduleBlockDAO;
import DAO.SchoolDAO;
import DAO.StudentDAO;
import Mapping.POJO.Admins;
import Mapping.POJO.Scheduleblocks;
import Mapping.POJO.Schools;
import Mapping.POJO.Students;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.ModelAndView;

/**
 *
 * @author lun
 */
@Controller
public class LoginController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String login(Model model,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        if (currentStudent != null) {
            model.addAttribute("student", currentStudent);
            return "student";
        }
        Admins currentAdmin = AdminDAO.getAdmin(name);
        if (currentAdmin != null) {
            SchoolDAO schoolDAO = new SchoolDAO();
            ScheduleBlockDAO scheduleBlockDAO = new ScheduleBlockDAO();
            List<Schools> schools = schoolDAO.allSchools();
            for (Schools school : schools) {
                List<Scheduleblocks> scheduleBlocks = scheduleBlockDAO.getSchoolsScheduleBlocks(school.getSchoolid());
                String SB2Strings = "";
                for (Scheduleblocks sb : scheduleBlocks) {
                    SB2Strings += sb.toString();
                }
                school.setScheduleblocks(SB2Strings);
            }
            model.addAttribute("school", schools);
            return "admin";
        }
        List<Schools> schools = SchoolDAO.allSchools();
        if(schools!=null){
        model.addAttribute("school", schools);
        }
        return "index";

    }

}
