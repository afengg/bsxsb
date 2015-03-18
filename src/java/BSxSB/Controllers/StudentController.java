/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BSxSB.Controllers;

import DAO.StudentDAO;
import Mapping.POJO.Students;
import java.lang.annotation.Annotation;
import java.security.Principal;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.portlet.ModelAndView;

/**
 *
 * @author lun
 */
@Controller
public class StudentController  {
    @RequestMapping(value="/student",method=RequestMethod.GET)
    public String studentPage(Model model){
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      String name = auth.getName();
      Students currentStudent = StudentDAO.getStudent(name);
      model.addAttribute("student", currentStudent);
      return "student";

}
   @RequestMapping(value="/managefriends",method=RequestMethod.GET)
    public String manageFriends(Model model){
      return "managefriends";
}

}
