/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BSxSB.Controllers;

import DAO.StudentDAO;
import DAO.SchoolDAO;
import DAO.ScheduleBlockDAO;
import Mapping.POJO.Scheduleblocks;
import Mapping.POJO.Schools;
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
public class AdminController {
    
    @RequestMapping(value="/admin",method=RequestMethod.GET)
    public String adminPage(Model model){
        SchoolDAO schoolDAO = new SchoolDAO();
        ScheduleBlockDAO scheduleBlockDAO = new ScheduleBlockDAO();
        List<Schools> schools = schoolDAO.allSchools();
        for(Schools school: schools){
            List<Scheduleblocks> scheduleBlocks = scheduleBlockDAO.getSchoolsScheduleBlocks(school.getSchoolid());
            String SB2Strings = "";
            for(Scheduleblocks sb : scheduleBlocks){
                SB2Strings += sb.toString();
            }
            school.setScheduleblocks(SB2Strings);
        }
        model.addAttribute("school", schools);
        return "admin";
    }
    
    @RequestMapping(value="/adminaddschool",method=RequestMethod.GET)
    public String addSchool(Model model){    
      return "adminaddschool";     
}
    
    @RequestMapping(value="/adminmanageacounts",method=RequestMethod.GET)
    public String manageAccounts(Model model){    
      return "adminmanageaccounts";     
}
    
    @RequestMapping(value="/adminmanagerequests",method=RequestMethod.GET)
    public String manageRequests(Model model){    
      return "adminmanagerequests";     
}

}
