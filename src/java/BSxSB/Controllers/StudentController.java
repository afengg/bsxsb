/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BSxSB.Controllers;

import DAO.StudentDAO;
import Mapping.POJO.Student;
import java.lang.annotation.Annotation;
import java.util.List;
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
      StudentDAO studentdao = new StudentDAO();
      List list=studentdao.allStudent();
      model.addAttribute("student", list.get(0));
      model.addAttribute("msg", "msg");
      return "student";

}
   @RequestMapping(value="/managefriends",method=RequestMethod.GET)
    public void studentPages(Model model){
      Student lun = new Student();
      lun.setEmail("asfsas");
      model.addAttribute("msg", "msg");

}

}
