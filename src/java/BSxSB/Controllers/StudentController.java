/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BSxSB.Controllers;

import DAO.CourseDAO;
import DAO.FriendshipsDAO;
import DAO.SchoolDAO;
import DAO.StudentDAO;
import DAO.CourseDAO;
import DAO.GenerationcriteriaDAO;
import DAO.RegistrationDAO;
import DAO.ScheduleBlockDAO;
import Mapping.POJO.Courses;
import Mapping.POJO.Generationcriteria;
import Mapping.POJO.Scheduleblocks;
import Mapping.POJO.Schools;
import Mapping.POJO.Students;
import java.lang.annotation.Annotation;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
        if (!currentStudent.getLoggedin()) {
            StudentDAO.setLoggedIn(name);
        }
        Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
        List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
        model.addAttribute("schoolyears", schoolyears);
        model.addAttribute("student", currentStudent);
        return "student";

    }

    @RequestMapping(value = "/studentmanagefriends", method = RequestMethod.GET)
    public String manageFriends(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        List<Students> friendrequests = StudentDAO.getFriendRequests(currentStudent.getStudentid());
        Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
        List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
        model.addAttribute("schoolyears", schoolyears);
        model.addAttribute("friendrequests", friendrequests);
        return "studentmanagefriends";
    }

    @RequestMapping(value = "/studentassignedcourses", method = RequestMethod.GET)
    public String assignedCourses(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        List<Courses> studentCourses = CourseDAO.getCoursesForStudent(currentStudent.getStudentid());
        Schools school = SchoolDAO.getSchool(currentStudent.getSchoolid());
        List<List<Courses[]>> semesters = new ArrayList<>();
        List<Students> friends = StudentDAO.getFriends(currentStudent.getStudentid());
        for (Courses course : studentCourses) {
            for (Students friend : friends) {
                List<Courses> friendCourses = CourseDAO.getCoursesForStudent(friend.getStudentid());
                for (Courses friendCourse : friendCourses) {
                    if (friendCourse.getCourseid() == (course.getCourseid())) {
                        if (course.getFriends() != null) {
                            course.setFriends(course.getFriends() + " " + friend.getFirstname() + " " + friend.getLastname());
                        } else {
                            course.setFriends(friend.getFirstname() + " " + friend.getLastname());
                        }
                    }
                }
            }
        }
        for (int s = 0; s < school.getNumsemesters(); s++) {
            List<Courses[]> schedule = new ArrayList<>();
            for (int i = 0; i < school.getNumperiods(); i++) {
                Courses[] period = new Courses[7];
                for (Courses course : studentCourses) {
                    Scheduleblocks sb = ScheduleBlockDAO.getScheduleBlock(course.getScheduleblockid());
                    if (sb.getPeriod() == i + 1) {
                        String[] days = sb.getDays().split(",");
                        String[] semester = course.getSemester().split(",");
                        for (String sem : semester) {
                            if (Integer.parseInt(sem) == s + 1) {
                                for (String day : days) {
                                    period[Integer.parseInt(day) - 1] = course;
                                }
                            }
                        }
                    }
                }
                schedule.add(period);
            }
            semesters.add(schedule);
        }
        Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
        List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
        model.addAttribute("schoolyears", schoolyears);
        model.addAttribute("semester", semesters);
        return "studentassignedcourses";
    }

    @RequestMapping(value = "/studentcourseofferings", method = RequestMethod.GET)
    public String courseOfferings(Model model, @RequestParam(value = "year") String year) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        int schoolid = currentStudent.getSchoolid();
        Schools sc = SchoolDAO.getSchool(schoolid);
        String schoolName = sc.getSchoolname();
        Schools schoolYear = SchoolDAO.getSchoolByNameYear(schoolName, year);
        int schoolYearID = schoolYear.getSchoolid();
        List<Courses> courses = CourseDAO.getCourseOfferingForSchool(schoolYearID);
        Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
        List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
        List<Scheduleblocks> scheduleblocks = new ArrayList<>();
        for (Courses course : courses) {
            scheduleblocks.add(ScheduleBlockDAO.getScheduleBlock(course.getScheduleblockid()));
        }
        model.addAttribute("scheduleblocks", scheduleblocks);
        model.addAttribute("schoolyears", schoolyears);
        model.addAttribute("courses", courses);
        return "studentcourseofferings";
    }

    @RequestMapping(value = "/studentdisplayfriends", method = RequestMethod.GET)
    public String displayFriends(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        List<Students> friends = StudentDAO.getFriends(currentStudent.getStudentid());
        Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
        List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
        model.addAttribute("schoolyears", schoolyears);
        model.addAttribute("friends", friends);
        return "studentdisplayfriends";
    }

    @RequestMapping(value = "/studenteditassigned", method = RequestMethod.GET)
    public String editAssigned(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        List<Courses> courses = CourseDAO.getCoursesForStudent(currentStudent.getStudentid());
        List<Scheduleblocks> scheduleblocks = new ArrayList<Scheduleblocks>();
        for (Courses course : courses) {
            scheduleblocks.add(ScheduleBlockDAO.getScheduleBlock(course.getScheduleblockid()));
        }
        Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
        List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
        model.addAttribute("schoolyears", schoolyears);
        model.addAttribute("scheduleblocks", scheduleblocks);
        model.addAttribute("courses", courses);
        return "studenteditassigned";
    }

    @RequestMapping(value = "/removeassign", method = RequestMethod.POST)
    public String removeAssigned(Model model, @RequestParam(value = "id") int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        RegistrationDAO.removereg(id, currentStudent.getStudentid());
        CourseDAO.decrementCourseStudentsAndDelete(id);
        List<Courses> courses = CourseDAO.getCoursesForStudent(currentStudent.getStudentid());
        List<Scheduleblocks> scheduleblocks = new ArrayList<Scheduleblocks>();
        for (Courses course : courses) {
            scheduleblocks.add(ScheduleBlockDAO.getScheduleBlock(course.getScheduleblockid()));
        }
        Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
        List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
        model.addAttribute("schoolyears", schoolyears);
        model.addAttribute("scheduleblocks", scheduleblocks);
        model.addAttribute("courses", courses);
        return "studenteditassigned";
    }

    @RequestMapping(value = "/studententercourses", method = RequestMethod.GET)
    public String enterCourses(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
        List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
        model.addAttribute("schoolyears", schoolyears);
        model.addAttribute("numSemesters", currentSchool.getNumsemesters());
        model.addAttribute("numPeriods", currentSchool.getNumperiods());
        model.addAttribute("numDays", currentSchool.getNumdays());
        return "studententercourses";
    }

    @RequestMapping(value = "/courseCheck.html", method = RequestMethod.POST)
    public @ResponseBody
    String courseCheck(@ModelAttribute(value = "identifier") String identifier,
            @ModelAttribute(value = "schoolname") String schoolname,
            @ModelAttribute(value = "academicyear") String academicyear,
            BindingResult result) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        int schoolid = currentStudent.getSchoolid();
        Courses course = CourseDAO.getCourseUsingID(schoolid, identifier);
        String courseName = "";
        if (course != null) {
            courseName = course.getCoursename();
        }
        return courseName;
    }

    @RequestMapping(value = "/submitassigned", method = RequestMethod.POST)
    public String submitAssigned(Model model, @RequestParam(value = "courseidentifier") String courseidentifier,
            @RequestParam(value = "coursename") String coursename,
            @RequestParam(value = "instructor") String instructor,
            @RequestParam(value = "semesters") String[] semesters,
            @RequestParam(value = "period") String period,
            @RequestParam(value = "days") String[] days) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        int schoolid = currentStudent.getSchoolid();
        //Build the semester string
        String semString = "";
        for (int i = 0; i < semesters.length; i++) {
            semString += semesters[i];
            semString += ",";
        }
        //Get rid of last ','
        semString = semString.substring(0, semString.length() - 1);
        //Build the scheduleblock days
        String daysString = "";
        for (int i = 0; i < days.length; i++) {
            daysString += days[i];
            daysString += ",";
        }
        daysString = daysString.substring(0, daysString.length() - 1);
        int periodInt = Integer.parseInt(period);
        Scheduleblocks sb = ScheduleBlockDAO.getScheduleBlock(schoolid, periodInt, daysString);
        if (sb == null) {
            model.addAttribute("sbinvalid", "Scheduleblock provided is invalid.");
            // return error msg
        } else {
            Courses c = CourseDAO.getCourse(courseidentifier, coursename, sb.getScheduleblockid(), schoolid, instructor, semString);
            if (c != null) {
                RegistrationDAO.addRegistration(c.getCourseid(), currentStudent.getStudentid());
                CourseDAO.incrementCourseStudents(c.getCourseid());
                model.addAttribute("halfsuccess", "Course already exists, you have been successfully added to the course roster.");
            } else {
                int sbid = sb.getScheduleblockid();
                Courses newCourse = new Courses(schoolid, coursename, courseidentifier, instructor, sbid, semString);
                newCourse.setNumstudents(1);
                int studentid = currentStudent.getStudentid();
                CourseDAO.addCourse(newCourse, studentid);
                model.addAttribute("success", "New course successfully added.");
            }
        }

        Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
        List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
        model.addAttribute("schoolyears", schoolyears);
        model.addAttribute("numSemesters", currentSchool.getNumsemesters());
        model.addAttribute("numPeriods", currentSchool.getNumperiods());
        model.addAttribute("numDays", currentSchool.getNumdays());
        return "studententercourses";
    }

    @RequestMapping(value = "/studentgeneratecourses", method = RequestMethod.GET)
    public String generateCourses(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
        List<Courses> courses = CourseDAO.getCourseOfferingForSchool(currentSchool.getSchoolid());
        List<Scheduleblocks> scheduleblocks = new ArrayList<>();
        for (Courses course : courses) {
            scheduleblocks.add(ScheduleBlockDAO.getScheduleBlock(course.getScheduleblockid()));
        }
        List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
        Generationcriteria gencriteria = GenerationcriteriaDAO.getGenerationCriteria(currentStudent.getStudentid());
        String[] courseids = gencriteria.getCourseids().split(",");
        List<Courses> genCourses = new ArrayList<>();
        List<Scheduleblocks> genscheduleblocks = new ArrayList<>();
        for (String courseid : courseids) {
            Courses genCourse = CourseDAO.getCourse(Integer.parseInt(courseid));
            genCourses.add(genCourse);
            genscheduleblocks.add(ScheduleBlockDAO.getScheduleBlock(genCourse.getScheduleblockid()));
        }
        if (gencriteria.getLunch() != null && !gencriteria.getLunch().isEmpty()) {
            String[] lunch = gencriteria.getLunch().split(",");
            model.addAttribute("lunch", lunch);
        }
        String lunchrange = currentSchool.getLunchrange();
        model.addAttribute("lunchrange", lunchrange);
        int numdays = currentSchool.getNumdays();
        String lunchdays = lunchToText(numdays);
        String[] lunchdays2 = lunchdays.split(",");
        model.addAttribute("lunchdays", lunchdays2);
        model.addAttribute("genscheduleblocks", genscheduleblocks);
        model.addAttribute("gencourses", genCourses);
        model.addAttribute("schoolyears", schoolyears);
        model.addAttribute("scheduleblocks", scheduleblocks);
        model.addAttribute("courses", courses);
        return "studentgeneratecourses";
    }

    @RequestMapping(value = "/adddesiredcourse", method = RequestMethod.POST)
    public String adddesiredcourses(Model model, @RequestParam("id") String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        GenerationcriteriaDAO.addDesiredCourses(currentStudent.getStudentid(), id);
        Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
        List<Courses> courses = CourseDAO.getCourseOfferingForSchool(currentSchool.getSchoolid());
        List<Scheduleblocks> scheduleblocks = new ArrayList<>();
        for (Courses course : courses) {
            scheduleblocks.add(ScheduleBlockDAO.getScheduleBlock(course.getScheduleblockid()));
        }
        List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
        Generationcriteria gencriteria = GenerationcriteriaDAO.getGenerationCriteria(currentStudent.getStudentid());
        String[] courseids = gencriteria.getCourseids().split(",");
        List<Courses> genCourses = new ArrayList<>();
        List<Scheduleblocks> genscheduleblocks = new ArrayList<>();
        for (String courseid : courseids) {
            Courses genCourse = CourseDAO.getCourse(Integer.parseInt(courseid));
            genCourses.add(genCourse);
            genscheduleblocks.add(ScheduleBlockDAO.getScheduleBlock(genCourse.getScheduleblockid()));
        }
        if (gencriteria.getLunch() != null && !gencriteria.getLunch().isEmpty()) {
            String[] lunch = gencriteria.getLunch().split(",");
            model.addAttribute("lunch", lunch);
        }
        String lunchrange = currentSchool.getLunchrange();
        model.addAttribute("lunchrange", lunchrange);
        int numdays = currentSchool.getNumdays();
        String lunchdays = lunchToText(numdays);
        String[] lunchdays2 = lunchdays.split(",");
        model.addAttribute("lunchdays", lunchdays2);
        model.addAttribute("genscheduleblocks", genscheduleblocks);
        model.addAttribute("gencourses", genCourses);
        model.addAttribute("schoolyears", schoolyears);
        model.addAttribute("scheduleblocks", scheduleblocks);
        model.addAttribute("courses", courses);
        return "studentgeneratecourses";
    }

    @RequestMapping(value = "/removedesiredcourse", method = RequestMethod.POST)
    public String removedesiredcourses(Model model, @RequestParam("id") String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        GenerationcriteriaDAO.removeDesiredCourses(currentStudent.getStudentid(), id);
        Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
        List<Courses> courses = CourseDAO.getCourseOfferingForSchool(currentSchool.getSchoolid());
        List<Scheduleblocks> scheduleblocks = new ArrayList<>();
        for (Courses course : courses) {
            scheduleblocks.add(ScheduleBlockDAO.getScheduleBlock(course.getScheduleblockid()));
        }
        List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
        Generationcriteria gencriteria = GenerationcriteriaDAO.getGenerationCriteria(currentStudent.getStudentid());
        String[] courseids = gencriteria.getCourseids().split(",");
        List<Courses> genCourses = new ArrayList<>();
        List<Scheduleblocks> genscheduleblocks = new ArrayList<>();
        for (String courseid : courseids) {
            Courses genCourse = CourseDAO.getCourse(Integer.parseInt(courseid));
            genCourses.add(genCourse);
            genscheduleblocks.add(ScheduleBlockDAO.getScheduleBlock(genCourse.getScheduleblockid()));
        }
        if (gencriteria.getLunch() != null && !gencriteria.getLunch().isEmpty()) {
            String[] lunch = gencriteria.getLunch().split(",");
            model.addAttribute("lunch", lunch);
        }
        String lunchrange = currentSchool.getLunchrange();
        model.addAttribute("lunchrange", lunchrange);
        int numdays = currentSchool.getNumdays();
        String lunchdays = lunchToText(numdays);
        String[] lunchdays2 = lunchdays.split(",");
        model.addAttribute("lunchdays", lunchdays2);
        model.addAttribute("genscheduleblocks", genscheduleblocks);
        model.addAttribute("gencourses", genCourses);
        model.addAttribute("schoolyears", schoolyears);
        model.addAttribute("scheduleblocks", scheduleblocks);
        model.addAttribute("courses", courses);
        return "studentgeneratecourses";
    }

    @RequestMapping(value = "/removelunch", method = RequestMethod.POST)
    public String removelunch(Model model, @RequestParam("lunch") String lunchday) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
        GenerationcriteriaDAO.removeLunch(currentStudent.getStudentid(), lunchday);
        List<Courses> courses = CourseDAO.getCourseOfferingForSchool(currentSchool.getSchoolid());
        List<Scheduleblocks> scheduleblocks = new ArrayList<>();
        for (Courses course : courses) {
            scheduleblocks.add(ScheduleBlockDAO.getScheduleBlock(course.getScheduleblockid()));
        }
        List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
        Generationcriteria gencriteria = GenerationcriteriaDAO.getGenerationCriteria(currentStudent.getStudentid());
        String[] courseids = gencriteria.getCourseids().split(",");
        List<Courses> genCourses = new ArrayList<>();
        List<Scheduleblocks> genscheduleblocks = new ArrayList<>();
        for (String courseid : courseids) {
            Courses genCourse = CourseDAO.getCourse(Integer.parseInt(courseid));
            genCourses.add(genCourse);
            genscheduleblocks.add(ScheduleBlockDAO.getScheduleBlock(genCourse.getScheduleblockid()));
        }
        if (gencriteria.getLunch() != null && !gencriteria.getLunch().isEmpty()) {
            String[] lunch = gencriteria.getLunch().split(",");
            model.addAttribute("lunch", lunch);
        }
        String lunchrange = currentSchool.getLunchrange();
        model.addAttribute("lunchrange", lunchrange);
        int numdays = currentSchool.getNumdays();
        String lunchdays = lunchToText(numdays);
        String[] lunchdays2 = lunchdays.split(",");
        model.addAttribute("lunchdays", lunchdays2);
        model.addAttribute("genscheduleblocks", genscheduleblocks);
        model.addAttribute("gencourses", genCourses);
        model.addAttribute("schoolyears", schoolyears);
        model.addAttribute("scheduleblocks", scheduleblocks);
        model.addAttribute("courses", courses);
        return "studentgeneratecourses";
    }

    @RequestMapping(value = "/addlunch", method = RequestMethod.POST)
    public String addlunch(Model model, @RequestParam("lunch") String lunchday) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        GenerationcriteriaDAO.addLunch(currentStudent.getStudentid(), lunchday);
        Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
        List<Courses> courses = CourseDAO.getCourseOfferingForSchool(currentSchool.getSchoolid());
        List<Scheduleblocks> scheduleblocks = new ArrayList<>();
        for (Courses course : courses) {
            scheduleblocks.add(ScheduleBlockDAO.getScheduleBlock(course.getScheduleblockid()));
        }
        List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
        Generationcriteria gencriteria = GenerationcriteriaDAO.getGenerationCriteria(currentStudent.getStudentid());
        String[] courseids = gencriteria.getCourseids().split(",");
        List<Courses> genCourses = new ArrayList<>();
        List<Scheduleblocks> genscheduleblocks = new ArrayList<>();
        for (String courseid : courseids) {
            Courses genCourse = CourseDAO.getCourse(Integer.parseInt(courseid));
            genCourses.add(genCourse);
            genscheduleblocks.add(ScheduleBlockDAO.getScheduleBlock(genCourse.getScheduleblockid()));
        }
        if (gencriteria.getLunch() != null && !gencriteria.getLunch().isEmpty()) {
            String[] lunch = gencriteria.getLunch().split(",");
            model.addAttribute("lunch", lunch);
        }
        String lunchrange = currentSchool.getLunchrange();
        model.addAttribute("lunchrange", lunchrange);
        int numdays = currentSchool.getNumdays();
        String lunchdays = lunchToText(numdays);
        String[] lunchdays2 = lunchdays.split(",");
        model.addAttribute("lunchdays", lunchdays2);
        model.addAttribute("genscheduleblocks", genscheduleblocks);
        model.addAttribute("gencourses", genCourses);
        model.addAttribute("schoolyears", schoolyears);
        model.addAttribute("scheduleblocks", scheduleblocks);
        model.addAttribute("courses", courses);
        return "studentgeneratecourses";
    }

    @RequestMapping(value = "/studentviewgenerated", method = RequestMethod.GET)
    public String viewGenerated(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
        List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
        model.addAttribute("schoolyears", schoolyears);
        return "studentviewgenerated";
    }

    @RequestMapping(value = "/generateschedule", method = RequestMethod.GET)
    public String generateSchedule(Model model, @RequestParam("instructors") String instructors) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
        Generationcriteria gencriteria = GenerationcriteriaDAO.getGenerationCriteria(currentStudent.getStudentid());
        String[] courseids = gencriteria.getCourseids().split(",");
        List<Courses> genCourses = new ArrayList<>();
        List<Scheduleblocks> genscheduleblocks = new ArrayList<>();
        for (String courseid : courseids) {
            Courses genCourse = CourseDAO.getCourse(Integer.parseInt(courseid));
            genCourses.add(genCourse);
            genscheduleblocks.add(ScheduleBlockDAO.getScheduleBlock(genCourse.getScheduleblockid()));
        }
        List<List<Courses>> conflictCourses = new ArrayList<>();
        String lunches = gencriteria.getLunch();
        String[] lunch = lunches.split(",");
        for (int i = 0; i < lunch.length; i++) {
            if (lunch[i].equals("monday")) {
                lunch[i] = "1";
            } else if (lunch[i].equals("tuesday")) {
                lunch[i] = "2";
            } else if (lunch[i].equals("wednesday")) {
                lunch[i] = "3";
            } else if (lunch[i].equals("thursday")) {
                lunch[i] = "4";
            } else if (lunch[i].equals("friday")) {
                lunch[i] = "5";
            } else if (lunch[i].equals("saturday")) {
                lunch[i] = "6";
            } else if (lunch[i].equals("sunday")) {
                lunch[i] = "7";
            }
        }
        String lunchperiods = currentSchool.getLunchrange();
        String[] temp = lunchperiods.split("-");
        int length = Integer.parseInt(temp[1]) - Integer.parseInt(temp[0]);
        int[] lunchperiod = new int[length + 1];
        int low = Integer.parseInt(temp[0]);
        for (int i = 0; i <= length; i++) {
            lunchperiod[i] = low;
            low++;
        }
        for (int i = 0; i < genscheduleblocks.size(); i++) {
            Scheduleblocks firstBlock = genscheduleblocks.get(i);
            conflictLunch:
            {
                for (int period : lunchperiod) {
                    if (firstBlock.getPeriod().equals(period)) {
                        String[] gendays = firstBlock.getDays().split(",");
                        for (String days : lunch) {
                            for (String genday : gendays) {
                                if (days.equals(genday)) {
                                    List<Courses> conflictCourse = new ArrayList<>();
                                    conflictCourse.add(genCourses.get(i));
                                    Courses lunchCourse = new Courses(0, "lunch", "", "", 0);
                                    conflictCourse.add(lunchCourse);
                                    conflictCourses.add(conflictCourse);
                                    break conflictLunch;
                                }
                            }
                        }
                    }
                }
            }
            for (int i2 = i + 1; i2 < genscheduleblocks.size(); i2++) {
                Scheduleblocks secondBlock = genscheduleblocks.get(i2);
                checkConflict:
                {
                    if (firstBlock.getPeriod().equals(secondBlock.getPeriod())) {
                        String[] days = firstBlock.getDays().split(",");
                        String[] days2 = secondBlock.getDays().split(",");
                        for (String d : days) {
                            for (String d2 : days2) {
                                if (d.equals(d2)) {
                                    Courses genCourse = genCourses.get(i);
                                    Courses genCourse2 = genCourses.get(i2);
                                    String[] semesters = genCourse.getSemester().split(",");
                                    String[] semesters2 = genCourse2.getSemester().split(",");
                                    for (String s : semesters) {
                                        for (String s2 : semesters2) {
                                            if (s.equals(s2)) {
                                                List<Courses> conflictCourse = new ArrayList<>();
                                                conflictCourse.add(genCourse);
                                                conflictCourse.add(genCourse2);
                                                conflictCourses.add(conflictCourse);
                                                break checkConflict;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (conflictCourses.isEmpty()) {
            String[] instructor = instructors.split(",");
            for (int i = 0; i < genCourses.size(); i++) {
                Courses course1 = genCourses.get(i);
                String courseiden = course1.getCourseidentifier();
                for (int i2 = i + 1; i2 < genCourses.size(); i2++) {
                    Courses course2 = genCourses.get(i2);
                    checkCourse:
                    {
                        if (courseiden.equals(course2.getCourseidentifier())) {
                            if (instructors.isEmpty()) {
                                List<Students> friends = StudentDAO.getFriends(currentStudent.getStudentid());
                                int friendcourse1 = 0;
                                int friendcourse2 = 0;
                                for (Students friend : friends) {
                                    if (RegistrationDAO.isRegistered(course1, friend)) {
                                        friendcourse1++;
                                    } else if (RegistrationDAO.isRegistered(course2, friend)) {
                                        friendcourse2++;
                                    }
                                }
                                if (friendcourse1 >= friendcourse2) {
                                    genCourses.remove(i2);
                                    break checkCourse;
                                } else if (friendcourse1 < friendcourse2) {
                                    genCourses.remove(i);
                                    break checkCourse;
                                } else {
                                    genCourses.remove(i);
                                    break checkCourse;
                                }
                            } else {
                                for (String inst : instructor) {
                                    if (course1.getInstructor().equals(inst)) {
                                        genCourses.remove(i2);
                                        break checkCourse;
                                    } else if (course2.getInstructor().equals(inst)) {
                                        genCourses.remove(i);
                                        break checkCourse;
                                    } else {
                                        List<Students> friends = StudentDAO.getFriends(currentStudent.getStudentid());
                                        int friendcourse1 = 0;
                                        int friendcourse2 = 0;
                                        for (Students friend : friends) {
                                            if (RegistrationDAO.isRegistered(course1, friend)) {
                                                friendcourse1++;
                                            } else if (RegistrationDAO.isRegistered(course2, friend)) {
                                                friendcourse2++;
                                            }
                                        }
                                        if (friendcourse1 >= friendcourse2) {
                                            genCourses.remove(i2);
                                            break checkCourse;
                                        } else if (friendcourse1 < friendcourse2) {
                                            genCourses.remove(i);
                                            break checkCourse;
                                        } else {
                                            genCourses.remove(i);
                                            break checkCourse;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            List<List<Courses[]>> semesters = new ArrayList<>();
            List<Students> friends = StudentDAO.getFriends(currentStudent.getStudentid());
            for (Courses course : genCourses) {
                for (Students friend : friends) {
                    List<Courses> friendCourses = CourseDAO.getCoursesForStudent(friend.getStudentid());
                    for (Courses friendCourse : friendCourses) {
                        if (friendCourse.getCourseid() == (course.getCourseid())) {
                            if (course.getFriends() != null) {
                                course.setFriends(course.getFriends() + " " + friend.getFirstname() + " " + friend.getLastname());
                            } else {
                                course.setFriends(friend.getFirstname() + " " + friend.getLastname());
                            }
                        }
                    }
                }
            }
            for (int s = 0; s < currentSchool.getNumsemesters(); s++) {
                List<Courses[]> schedule = new ArrayList<>();
                for (int i = 0; i < currentSchool.getNumperiods(); i++) {
                    Courses[] period = new Courses[7];
                    for (Courses course : genCourses) {
                        Scheduleblocks sb = ScheduleBlockDAO.getScheduleBlock(course.getScheduleblockid());
                        if (sb.getPeriod() == i + 1) {
                            String[] days = sb.getDays().split(",");
                            String[] semester = course.getSemester().split(",");
                            for (String sem : semester) {
                                if (Integer.parseInt(sem) == s + 1) {
                                    for (String day : days) {
                                        period[Integer.parseInt(day) - 1] = course;
                                    }
                                }
                            }
                        }
                    }
                    schedule.add(period);
                }
                semesters.add(schedule);
            }
            model.addAttribute("semester", semesters);
        } else {
            model.addAttribute("conflictCourses", conflictCourses);
        }

        List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
        model.addAttribute("schoolyears", schoolyears);
        return "studentviewgenerated";
    }

    @RequestMapping(value = "/acceptfriend", method = RequestMethod.POST)
    public String acceptfriend(Model model, @RequestParam(value = "id") int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        FriendshipsDAO.acceptfriend(currentStudent.getStudentid(), id);
        List<Students> friendrequests = StudentDAO.getFriendRequests(currentStudent.getStudentid());
        Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
        List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
        model.addAttribute("schoolyears", schoolyears);
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
        Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
        List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
        model.addAttribute("schoolyears", schoolyears);
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
        Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
        List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
        model.addAttribute("schoolyears", schoolyears);
        model.addAttribute("friends", friends);
        return "studentdisplayfriends";
    }

    @RequestMapping(value = "/addfriend", method = RequestMethod.POST)
    public String addfriend(Model model, @RequestParam(value = "email") String email) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        Students friend = StudentDAO.getStudent(email);
        if (friend == null) {
            model.addAttribute("msg", "The email you have entered doesn't belong to any student");
        } else if (friend.getStudentid() == currentStudent.getStudentid()) {
            model.addAttribute("msg", "Can't friend yourself...");
        } else if (friend.getSchoolid() != currentStudent.getSchoolid()) {
            model.addAttribute("msg", "Can not add a student from different school");
        } else {
            model.addAttribute("msg", FriendshipsDAO.addfriend(friend, currentStudent));
        }
        List<Students> friendrequests = StudentDAO.getFriendRequests(currentStudent.getStudentid());
        Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
        List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
        model.addAttribute("schoolyears", schoolyears);
        model.addAttribute("friendrequests", friendrequests);
        return "studentmanagefriends";
    }

    public String lunchToText(int numdays) {
        String lunchdays = "";
        if (numdays == 1) {
            lunchdays = "monday,";
        } else if (numdays == 2) {
            lunchdays = "monday,tuesday,";
        } else if (numdays == 3) {
            lunchdays = "monday,tuesday,wednesday,";
        } else if (numdays == 4) {
            lunchdays = "monday,tuesday,wednesday,thursday,";
        } else if (numdays == 5) {
            lunchdays = "monday,tuesday,wednesday,thursday,friday,";
        } else if (numdays == 6) {
            lunchdays = "monday,tuesday,wednesday,thursday,friday,saturday,";
        } else if (numdays == 7) {
            lunchdays = "monday,tuesday,wednesday,thursday,friday,saturday,sunday,";
        }
        return lunchdays;
    }
}
