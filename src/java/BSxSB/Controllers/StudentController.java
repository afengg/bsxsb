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
   @RequestMapping(value="/studentmanagefriends",method=RequestMethod.GET)
    public String manageFriends(Model model){
      return "studentmanagefriends";
}
    
    @RequestMapping(value="/studentassignedcourses",method=RequestMethod.GET)
    public String assignedCourses(Model model){
        return "studentassignedcourses";
    }
    
    @RequestMapping(value="/studentcourseofferings",method=RequestMethod.GET)
    public String courseOfferings(Model model){
        return "studentcourseofferings";
    }
    
    @RequestMapping(value="/studentdisplayfriends",method=RequestMethod.GET)
    public String displayFriends(Model model){
        return "studentdisplayfriends";
    }
    
    @RequestMapping(value="/studenteditassigned",method=RequestMethod.GET)
    public String editAssigned(Model model){
        return "studenteditassigned";
    }
    
    @RequestMapping(value="/studententercourses",method=RequestMethod.GET)
    public String enterCourses(Model model){
        return "studententercourses";
    }
    
    @RequestMapping(value="/studentgeneratecourses",method=RequestMethod.GET)
    public String generateCourses(Model model){
        return "studentgeneratecourses";
    }
    
    @RequestMapping(value="/studentviewgenerated",method=RequestMethod.GET)
    public String viewGenerated(Model model){
        return "studentviewgenerated";
    }
}
