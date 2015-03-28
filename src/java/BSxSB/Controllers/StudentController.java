/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BSxSB.Controllers;

import DAO.FriendshipsDAO;
import DAO.SchoolDAO;
import DAO.StudentDAO;
import Mapping.POJO.Schools;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.ModelAndView;

/**
 *
 * @author lun
 */
@Controller
public class StudentController {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(Model model, @RequestParam(value = "firstName") String firstName,
            @RequestParam(value = "lastName") String lastName,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "school") String school) {
        List<Schools> schools = SchoolDAO.allSchools();
        if (schools != null) {
            model.addAttribute("school", schools);
        }
        Students student = StudentDAO.getStudent(email);
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            model.addAttribute("fillout", "Please fill out all Required Fields");
        } else if (student != null) {
            model.addAttribute("taken", "The email address is taken");
        } else {
            StudentDAO.register(firstName, lastName, email, password, school);
            model.addAttribute("registered", "You have been successfully registered. Please Login");
        }
        return "index";
    }

    @RequestMapping(value = "/student", method = RequestMethod.GET)
    public String studentPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        model.addAttribute("student", currentStudent);
        return "student";

    }

    @RequestMapping(value = "/studentmanagefriends", method = RequestMethod.GET)
    public String manageFriends(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        List<Students> friendrequests = StudentDAO.getFriendRequests(currentStudent.getStudentid());
        model.addAttribute("friendrequests", friendrequests);
        return "studentmanagefriends";
    }

    @RequestMapping(value = "/studentassignedcourses", method = RequestMethod.GET)
    public String assignedCourses(Model model) {
        return "studentassignedcourses";
    }

    @RequestMapping(value = "/studentcourseofferings", method = RequestMethod.GET)
    public String courseOfferings(Model model) {
        return "studentcourseofferings";
    }

    @RequestMapping(value = "/studentdisplayfriends", method = RequestMethod.GET)
    public String displayFriends(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        List<Students> friends = StudentDAO.getFriends(currentStudent.getStudentid());
        model.addAttribute("friends", friends);
        return "studentdisplayfriends";
    }

    @RequestMapping(value = "/studenteditassigned", method = RequestMethod.GET)
    public String editAssigned(Model model) {
        return "studenteditassigned";
    }

    @RequestMapping(value = "/studententercourses", method = RequestMethod.GET)
    public String enterCourses(Model model) {
        return "studententercourses";
    }

    @RequestMapping(value = "/studentgeneratecourses", method = RequestMethod.GET)
    public String generateCourses(Model model) {
        return "studentgeneratecourses";
    }

    @RequestMapping(value = "/studentviewgenerated", method = RequestMethod.GET)
    public String viewGenerated(Model model) {
        return "studentviewgenerated";
    }

    @RequestMapping(value = "/acceptfriend", method = RequestMethod.POST)
    public String acceptfriend(Model model, @RequestParam(value = "id") int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        FriendshipsDAO.acceptfriend(currentStudent.getStudentid(), id);
        List<Students> friendrequests = StudentDAO.getFriendRequests(currentStudent.getStudentid());
        model.addAttribute("friendrequests", friendrequests);
        return "studentmanagefriends";
    }

    @RequestMapping(value = "/rejectfriend", method = RequestMethod.POST)
    public String rejectfriend(Model model, @RequestParam(value = "id") int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        FriendshipsDAO.deletefriend(currentStudent.getStudentid(), id);
        List<Students> friendrequests = StudentDAO.getFriendRequests(currentStudent.getStudentid());
        model.addAttribute("friendrequests", friendrequests);
        return "studentmanagefriends";
    }

    @RequestMapping(value = "/unfriend", method = RequestMethod.POST)
    public String unfriend(Model model, @RequestParam(value = "id") int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        FriendshipsDAO.deletefriend(currentStudent.getStudentid(), id);
        List<Students> friends = StudentDAO.getFriends(currentStudent.getStudentid());
        model.addAttribute("friends", friends);
        return "studentdisplayfriends";
    }

    @RequestMapping(value = "/addfriend", method = RequestMethod.POST)
    public String addfriend(Model model, @RequestParam(value = "email") String email) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        Students friend = StudentDAO.getStudent(email);
        if(friend==null){
             model.addAttribute("msg", "The email you have entered doesn't belong to any student");
        }
        else if(friend.getSchoolid()!=currentStudent.getStudentid()) {
            model.addAttribute("msg", "Can not add a student from different school");
        }
        else
        {
        model.addAttribute("msg", FriendshipsDAO.addfriend(friend, currentStudent));
        }
        List<Students> friendrequests = StudentDAO.getFriendRequests(currentStudent.getStudentid());
        model.addAttribute("friendrequests", friendrequests);
        return "studentmanagefriends";
    }
}
