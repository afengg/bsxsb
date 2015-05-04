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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    static final Logger logger = Logger.getLogger(StudentController.class.getName());

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(Model model, @RequestParam(value = "firstName") String firstName,
            @RequestParam(value = "lastName") String lastName,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "school") String school) {
        try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBStudentAccount.log", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            List<Schools> schools = SchoolDAO.allSchools();
            if (schools != null) {
                model.addAttribute("school", schools);
            }
            Students student = StudentDAO.getStudent(email);
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                model.addAttribute("fillout", "Please fill out all Required Fields");
                logger.info("A field was not filled.");
            } else if (student != null) {
                model.addAttribute("taken", "The email address is taken");
                logger.info("Email address was taken.");
            } else {
                StudentDAO.register(firstName, lastName, email, password, school);
                model.addAttribute("registered", "You have been successfully registered. Please Login");
                logger.info("Successfully registered an account with email " + email);
            }
            handler.close();
            logger.removeHandler(handler);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
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

        model.addAttribute("friendrequests", friendrequests);
        return "studentmanagefriends";
    }

    @RequestMapping(value = "/studentassignedcourses", method = RequestMethod.GET)
    public String assignedCourses(Model model) {
        try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBStudentAssignedCourses.log", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
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
            logger.info("Successfully retrieved all friends for each course.");
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
            logger.info("Successfully retrieved schedule for each semester.");
            Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
            List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
            model.addAttribute("schoolyears", schoolyears);
            model.addAttribute("semester", semesters);
            handler.close();
            logger.removeHandler(handler);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return "studentassignedcourses";
    }

    @RequestMapping(value = "/studentcourseofferings", method = RequestMethod.GET)
    public String courseOfferings(Model model, @RequestParam(value = "year") String year) {
        try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBStudentCourseOfferings.log", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
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
                logger.info("Retrieved course: " + course.getCourseidentifier());
            }
            model.addAttribute("scheduleblocks", scheduleblocks);
            model.addAttribute("schoolyears", schoolyears);
            model.addAttribute("courses", courses);
            handler.close();
            logger.removeHandler(handler);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
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
        try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBStudentAssignedCourses.log", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
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
            logger.info("Courses for this student added to model.");
            handler.close();
            logger.removeHandler(handler);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return "studenteditassigned";
    }

    @RequestMapping(value = "/removeassign", method = RequestMethod.POST)
    public String removeAssigned(Model model, @RequestParam(value = "id") int id) {
        try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBStudentAssignedCourses.log", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            Students currentStudent = StudentDAO.getStudent(name);
            RegistrationDAO.removereg(id, currentStudent.getStudentid());
            CourseDAO.decrementCourseStudentsAndDelete(id);
            logger.info("Course successfully deleted");
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
            logger.info("Courses for this student added to model.");
            handler.close();
            logger.removeHandler(handler);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
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
        System.out.println(courseName);
        return courseName;
    }

    @RequestMapping(value = "/courseCheck2.html", method = RequestMethod.POST)
    public @ResponseBody
    String courseCheck2(@ModelAttribute(value = "criteria") String criteria,
            @ModelAttribute(value = "schoolname") String schoolname,
            @ModelAttribute(value = "academicyear") String academicyear,
            BindingResult result) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Students currentStudent = StudentDAO.getStudent(name);
        int schoolid = currentStudent.getSchoolid();
        String[] criteriaparts = criteria.split("/");
        String courseidentifier = criteriaparts[0];
        String coursename = criteriaparts[1];
        String semString = criteriaparts[2];
        int period = Integer.parseInt(criteriaparts[3]);
        String daysString = criteriaparts[4];
        Scheduleblocks sb = ScheduleBlockDAO.getScheduleBlock(schoolid, period, daysString);
        if (sb == null) {
            return "";
        } else {
            int sbid = sb.getScheduleblockid();
            Courses course = CourseDAO.getCourseNoInstructor(courseidentifier, coursename, sbid, schoolid, semString);
            if (course == null) {
                return "";
            } else {
                return course.getInstructor();
            }
        }
    }

    @RequestMapping(value = "/submitassigned", method = RequestMethod.POST)
    public String submitAssigned(Model model, @RequestParam(value = "courseidentifier") String courseidentifier,
            @RequestParam(value = "coursename") String coursename,
            @RequestParam(value = "instructor") String instructor,
            @RequestParam(value = "semesters") String[] semesters,
            @RequestParam(value = "period") String period,
            @RequestParam(value = "days") String[] days) {
        try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBStudentAssignedCourses.log", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            if (coursename.isEmpty() || instructor.isEmpty() || semesters[0].matches("noinput") || period.isEmpty() || days[0].matches("noinput")) {
                model.addAttribute("fieldempty", "Please fill out all required fields.");
                logger.info("Error: Fields are empty");
                handler.close();
                logger.removeHandler(handler);
                return enterCourses(model);
            }
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            Students currentStudent = StudentDAO.getStudent(name);
            int schoolid = currentStudent.getSchoolid();
            //Build the semester string
            String semString = "";
            for (int i = 0; i < semesters.length - 1; i++) {
                semString += semesters[i];
                semString += ",";
            }
            //Get rid of last ','
            semString = semString.substring(0, semString.length() - 1);
            //Build the scheduleblock days
            String daysString = "";
            for (int i = 0; i < days.length - 1; i++) {
                daysString += days[i];
                daysString += ",";
            }
            daysString = daysString.substring(0, daysString.length() - 1);
            int periodInt = Integer.parseInt(period);
            Scheduleblocks sb = ScheduleBlockDAO.getScheduleBlock(schoolid, periodInt, daysString);
            if (sb == null) {
                model.addAttribute("sbinvalid", "Scheduleblock provided is invalid.");
                logger.info("Error: The chosen scheduleblock does not exist for this school");
                // return error msg
            } else {
                Courses c = CourseDAO.getCourse(courseidentifier, coursename, sb.getScheduleblockid(), schoolid, instructor, semString);
                if (c != null) {
                    if (RegistrationDAO.isRegistered(c, currentStudent)) {
                        model.addAttribute("alreadyreg", "You are already registered for this course");
                        logger.info("Error: already registered for this course");
                    } else {
                        RegistrationDAO.addRegistration(c.getCourseid(), currentStudent.getStudentid());
                        CourseDAO.incrementCourseStudents(c.getCourseid());
                        model.addAttribute("halfsuccess", "Course already exists, you have been successfully added to the course roster.");
                        logger.info("Course exists and student has been added to roster");
                    }
                } else {
                    int sbid = sb.getScheduleblockid();
                    Courses newCourse = new Courses(schoolid, coursename, courseidentifier, instructor, sbid, semString);
                    newCourse.setNumstudents(1);
                    int studentid = currentStudent.getStudentid();
                    CourseDAO.addCourse(newCourse, studentid);
                    model.addAttribute("success", "New course successfully added.");
                    logger.info("Course " + courseidentifier + " " + coursename + " successfully added");
                }
            }

            Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
            List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
            model.addAttribute("schoolyears", schoolyears);
            model.addAttribute("numSemesters", currentSchool.getNumsemesters());
            model.addAttribute("numPeriods", currentSchool.getNumperiods());
            model.addAttribute("numDays", currentSchool.getNumdays());
            handler.close();
            logger.removeHandler(handler);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return "studententercourses";
    }

    @RequestMapping(value = "/studentgeneratecourses", method = RequestMethod.GET)
    public String generateCourses(Model model) {
        try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBStudentGenerateCourses.log", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
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

            if (gencriteria!=null) {
                String[] courseids = gencriteria.getCourseids().split(",");
                List<Courses> genCourses = new ArrayList<>();
                List<Scheduleblocks> genscheduleblocks = new ArrayList<>();
                if (!gencriteria.getCourseids().isEmpty()) {
                    for (String courseid : courseids) {
                        Courses genCourse = CourseDAO.getCourse(Integer.parseInt(courseid));
                        genCourses.add(genCourse);
                        genscheduleblocks.add(ScheduleBlockDAO.getScheduleBlock(genCourse.getScheduleblockid()));
                    }
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
                logger.info("Successfully loaded generation criteria.");
                handler.close();
                logger.removeHandler(handler);
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        return "studentgeneratecourses";
    }

    @RequestMapping(value = "/adddesiredcourse", method = RequestMethod.POST)
    public String adddesiredcourses(Model model, @RequestParam("id") String id) {
        try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBStudentGenerateCourses.log", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            Students currentStudent = StudentDAO.getStudent(name);
            GenerationcriteriaDAO.addDesiredCourses(currentStudent.getStudentid(), id);
            logger.info("Course successfully added to generation criteria.");
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
            logger.info("Generation Criteria successfully updated.");
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
            handler.close();
            logger.removeHandler(handler);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return "studentgeneratecourses";
    }

    @RequestMapping(value = "/removedesiredcourse", method = RequestMethod.POST)
    public String removedesiredcourses(Model model, @RequestParam("id") String id) {
        try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBStudentGenerateCourses.log", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            Students currentStudent = StudentDAO.getStudent(name);
            GenerationcriteriaDAO.removeDesiredCourses(currentStudent.getStudentid(), id);
            logger.info("Course sucessfully removed from generation criteria.");
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
            if (!gencriteria.getCourseids().isEmpty()) {
                for (String courseid : courseids) {
                    Courses genCourse = CourseDAO.getCourse(Integer.parseInt(courseid));
                    genCourses.add(genCourse);
                    genscheduleblocks.add(ScheduleBlockDAO.getScheduleBlock(genCourse.getScheduleblockid()));
                }
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
            logger.info("Generation criteria successfully updated.");
            handler.close();
            logger.removeHandler(handler);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return "studentgeneratecourses";
    }

    @RequestMapping(value = "/removelunch", method = RequestMethod.POST)
    public String removelunch(Model model, @RequestParam("lunch") String lunchday) {
        try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBStudentGenerateCourses.log", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            Students currentStudent = StudentDAO.getStudent(name);
            Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
            GenerationcriteriaDAO.removeLunch(currentStudent.getStudentid(), lunchday);
            logger.info(lunchday + " removed from generation crtieria.");
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
            logger.info("Generation criteria successfully updated.");
            handler.close();
            logger.removeHandler(handler);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return "studentgeneratecourses";
    }

    @RequestMapping(value = "/addlunch", method = RequestMethod.POST)
    public String addlunch(Model model, @RequestParam("lunch") String lunchday) {
        try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBStudentGenerateCourses.log", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            Students currentStudent = StudentDAO.getStudent(name);
            GenerationcriteriaDAO.addLunch(currentStudent.getStudentid(), lunchday);
            logger.info(lunchday + " successfully added to Generation Criteria.");
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
            handler.close();
            logger.removeHandler(handler);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return "studentgeneratecourses";
    }

    @RequestMapping(value = "/generateschedule", method = RequestMethod.GET)
    public String generateSchedule(Model model, @RequestParam("instructors") String instructors) {
        try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBStudentGenerateCourses.log", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            Students currentStudent = StudentDAO.getStudent(name);
            Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
            Generationcriteria gencriteria = GenerationcriteriaDAO.getGenerationCriteria(currentStudent.getStudentid());
            String[] courseids = gencriteria.getCourseids().split(",");
            List<Courses> genCourses = new ArrayList<>();
            List<Scheduleblocks> genscheduleblocks = new ArrayList<>();
            if (!gencriteria.getCourseids().isEmpty()) {
                for (String courseid : courseids) {
                    Courses genCourse = CourseDAO.getCourse(Integer.parseInt(courseid));
                    genCourses.add(genCourse);
                    genscheduleblocks.add(ScheduleBlockDAO.getScheduleBlock(genCourse.getScheduleblockid()));
                }
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
            int conlunchcount = 0;
            List<List<Courses>> conflictLunches = new ArrayList<>();
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
                                        conflictLunches.add(conflictCourse);
                                        conlunchcount++;
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
            System.out.print(conlunchcount + " " + lunchperiod.length);
            if (conlunchcount >= lunchperiod.length) {

                for (List<Courses> conlunch : conflictLunches) {
                    System.out.print(conlunch);
                    conflictCourses.add(conlunch);
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
                logger.info("Generated schedule complete.");
            } else {
                model.addAttribute("conflictCourses", conflictCourses);
                logger.info("Conflicting courses displayed.");
            }

            List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
            model.addAttribute("schoolyears", schoolyears);
            handler.close();
            logger.removeHandler(handler);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return "studentviewgenerated";
    }

    @RequestMapping(value = "/acceptfriend", method = RequestMethod.POST)
    public String acceptfriend(Model model, @RequestParam(value = "id") int id) {
        try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBStudentFriends.log", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            Students currentStudent = StudentDAO.getStudent(name);
            FriendshipsDAO.acceptfriend(currentStudent.getStudentid(), id);
            logger.info("Approved friendship between " + currentStudent.getStudentid() + " and " + id);
            List<Students> friendrequests = StudentDAO.getFriendRequests(currentStudent.getStudentid());
            Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
            List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
            model.addAttribute("schoolyears", schoolyears);
            model.addAttribute("friendrequests", friendrequests);
            logger.info("Friend requests updated to model.");
            handler.close();
            logger.removeHandler(handler);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return "studentmanagefriends";
    }

    @RequestMapping(value = "/rejectfriend", method = RequestMethod.POST)
    public String rejectfriend(Model model, @RequestParam(value = "id") int id) {
        try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBStudentFriends.log", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            Students currentStudent = StudentDAO.getStudent(name);
            FriendshipsDAO.deletefriend(currentStudent.getStudentid(), id);
            logger.info("Rejected friendship between " + currentStudent.getStudentid() + " and " + id);
            List<Students> friendrequests = StudentDAO.getFriendRequests(currentStudent.getStudentid());
            Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
            List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
            model.addAttribute("schoolyears", schoolyears);
            model.addAttribute("friendrequests", friendrequests);
            logger.info("Friend requests updated to model.");
            handler.close();
            logger.removeHandler(handler);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return "studentmanagefriends";
    }

    @RequestMapping(value = "/unfriend", method = RequestMethod.POST)
    public String unfriend(Model model, @RequestParam(value = "id") int id) {
        try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBStudentFriends.log", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            Students currentStudent = StudentDAO.getStudent(name);
            FriendshipsDAO.deletefriend(currentStudent.getStudentid(), id);
            logger.info("Successfully deleted friendship between " + currentStudent.getStudentid() + " and " + id);
            List<Students> friends = StudentDAO.getFriends(currentStudent.getStudentid());
            Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
            List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
            model.addAttribute("schoolyears", schoolyears);
            model.addAttribute("friends", friends);
            logger.info("Friends updated to model.");
            handler.close();
            logger.removeHandler(handler);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return "studentdisplayfriends";
    }

    @RequestMapping(value = "/addfriend", method = RequestMethod.POST)
    public String addfriend(Model model, @RequestParam(value = "email") String email) {
        try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBStudentFriends.log", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            Students currentStudent = StudentDAO.getStudent(name);
            Students friend = StudentDAO.getStudent(email);
            if (friend == null) {
                model.addAttribute("msg", "The email you have entered doesn't belong to any student");
                logger.info(email + " does not exist in database.");
            } else if (friend.getStudentid() == currentStudent.getStudentid()) {
                model.addAttribute("msg", "Can't friend yourself...");
                logger.info("This is your email.");
            } else if (friend.getSchoolid() != currentStudent.getSchoolid()) {
                model.addAttribute("msg", "Can not add a student from different school");
                logger.info("This student belongs to a different school");
            } else {
                model.addAttribute("msg", FriendshipsDAO.addfriend(friend, currentStudent));
                logger.info("Successfully created a friend request.");
            }
            List<Students> friendrequests = StudentDAO.getFriendRequests(currentStudent.getStudentid());
            Schools currentSchool = SchoolDAO.getSchool(currentStudent.getSchoolid());
            List<Schools> schoolyears = SchoolDAO.getSchoolSameName(currentSchool.getSchoolname());
            model.addAttribute("schoolyears", schoolyears);
            model.addAttribute("friendrequests", friendrequests);
            logger.info("Friend requests updated to model.");
            handler.close();
            logger.removeHandler(handler);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return "studentmanagefriends";
    }

    @RequestMapping(method = RequestMethod.GET)
    public void doDownload(HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        int BUFFER_SIZE = 4096;
        String filePath = "/WEB-INF/jsp/studentviewgenerated.jsp";
        // get absolute path of the application
        ServletContext context = request.getServletContext();
        String appPath = context.getRealPath("");
        System.out.println("appPath = " + appPath);

        // construct the complete absolute path of the file
        String fullPath = appPath + filePath;
        File downloadFile = new File(fullPath);
        FileInputStream inputStream = new FileInputStream(downloadFile);

        // get MIME type of the file
        String mimeType = context.getMimeType(fullPath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        System.out.println("MIME type: " + mimeType);

        // set content attributes for the response
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());

        // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                downloadFile.getName());
        response.setHeader(headerKey, headerValue);

        // get output stream of the response
        OutputStream outStream = response.getOutputStream();

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;

        // write bytes read from the input stream into the output stream
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outStream.close();

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
