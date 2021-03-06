/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BSxSB.Controllers;

import DAO.AdminDAO;
import DAO.StudentDAO;
import DAO.SchoolDAO;
import DAO.ScheduleBlockDAO;
import Email.EmailNotification;
import Mapping.POJO.Admins;
import Mapping.POJO.Scheduleblocks;
import Mapping.POJO.Schools;
import Mapping.POJO.Students;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.portlet.ModelAndView;
import java.util.logging.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author lun
 */
@Controller
public class AdminController {

    static final Logger logger = Logger.getLogger(AdminController.class.getName());

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model) {
        try{
            Handler handler = new FileHandler("%tBSxSBAdminSchools.log",true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            logger.info("Admin Viewing List of Schools.");      
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            Admins admin = AdminDAO.getAdmin(name);
            if (!admin.getLoggedin()) {
                AdminDAO.setLoggedIn(name);
            }
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
            logger.info("Schools successfully updated to model.");
            handler.close();
            logger.removeHandler(handler);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                logger.log(Level.SEVERE, null, ex);
            }   
            return "admin";
    }
    @RequestMapping(value = "/admineditschool", method = RequestMethod.POST)
    public String editRequest(Model model, @RequestParam(value = "schoolID") String schoolID){  
        Schools school = SchoolDAO.getSchool(Integer.parseInt(schoolID));
        List<Scheduleblocks> scheduleBlocks = ScheduleBlockDAO.getSchoolsScheduleBlocks(school.getSchoolid());
            String SB2Strings = "";
            for (Scheduleblocks sb : scheduleBlocks) {
                SB2Strings += "#" + sb.toString();
            }
            SB2Strings = SB2Strings.substring(1);
            school.setScheduleblocks(SB2Strings);
        model.addAttribute("school", school);
        model.addAttribute("currentsemesters", String.valueOf(school.getNumsemesters()));
        model.addAttribute("currentdays", String.valueOf(school.getNumdays()));
        model.addAttribute("currentperiods", String.valueOf(school.getNumperiods()));
        
        return "admineditschool";
    }
    @RequestMapping(value = "/admineditscheduleblocks", method = RequestMethod.POST)
    public String editScheduleBlocks(Model model, @RequestParam(value="schoolID") String schoolID){
    try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBAdminScheduleBlocks.log",true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            logger.info("Admin Viewing List of School's Schedule Blocks.");    
        int schoolID2 = Integer.parseInt(schoolID);
        Schools school = SchoolDAO.getSchool(schoolID2);
        List<Scheduleblocks> sbs = ScheduleBlockDAO.getSchoolsScheduleBlocks(schoolID2);
        model.addAttribute("school", school);
        model.addAttribute("scheduleblocks", sbs);
            logger.info("School's schedule blocks successfully updated to model.");
            handler.close();
            logger.removeHandler(handler);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                logger.log(Level.SEVERE, null, ex);
            }   
        return "admineditscheduleblocks";
        
    }
    
    @RequestMapping(value = "/addscheduleblock", method = RequestMethod.POST)
    public String addScheduleBlock(Model model,
                                                @RequestParam(value="schoolid") String schoolID,
                                                @RequestParam(value="period") String period,
                                                @RequestParam(value="days") String[] days){
    try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBAdminScheduleBlocks.log",true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            logger.info("Admin Viewing List of School's Schedule Blocks.");           
        int schoolID2 = Integer.parseInt(schoolID);
        if(period.isEmpty() || days.length == 0){
            model.addAttribute("sbempty", "A field is empty");
        }
        int periodInt = Integer.parseInt(period);
        String daysString = "";
        for(String x : days){
            daysString += x;
            daysString += ",";
        }
        daysString = daysString.substring(0, daysString.length()-1);
        Scheduleblocks sb = ScheduleBlockDAO.getScheduleBlock(schoolID2, periodInt, daysString);
        if(sb != null){
            model.addAttribute("sbexists", "This scheduleblock exists");
            logger.info("Error: SB exists");
        }
        else{
        ScheduleBlockDAO.addScheduleBlock(schoolID2, periodInt, daysString);
        logger.info("Scheduleblock with period " + periodInt + " and days " + daysString + " added to school.");
        }
            handler.close();
        logger.removeHandler(handler);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                logger.log(Level.SEVERE, null, ex);
            }   
        return editScheduleBlocks(model, schoolID);
    }
    
    @RequestMapping(value = "/deletescheduleblock", method = RequestMethod.POST)
    public String deleteScheduleBlocks(Model model, @RequestParam(value="scheduleblockID") String scheduleblockID,
                                                       @RequestParam(value="schoolid") String schoolid) {
            try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBAdminScheduleBlocks.log",true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            logger.info("Admin Viewing List of School's Schedule Blocks.");    
        int sbid = Integer.parseInt(scheduleblockID);
        ScheduleBlockDAO.deleteScheduleBlock(sbid);
        logger.info("Scheduleblock " + sbid + " was deleted.");
        handler.close();
        logger.removeHandler(handler);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                logger.log(Level.SEVERE, null, ex);
            }   
        return editScheduleBlocks(model, schoolid);
    }
    @RequestMapping(value = "/editschool", method = RequestMethod.POST)
    public String editSchool(Model model, @RequestParam(value="schoolID") String schoolID,
                                           @RequestParam(value="schoolname") String schoolName,
                                           @RequestParam(value="academicyear") String academicYear,
                                           @RequestParam(value="numsemesters") String numSemesters,
                                           @RequestParam(value="numdays") String numDays,
                                           @RequestParam(value="numperiods") String numPeriods,
                                           @RequestParam(value="lunchrange") String lunchRange){
        try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBAdminSchools.log",true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            logger.info("Admin Viewing List of School's Schedule Blocks.");    
        boolean valid = true;
        if (schoolName.isEmpty() || academicYear.isEmpty() || numSemesters.isEmpty() || numPeriods.isEmpty() || lunchRange.isEmpty()) {
            model.addAttribute("fillout", "Please fill out all Required Fields");
            valid = false;
        }
        int schoolid2 = Integer.parseInt(schoolID); 
        String academicYearRegex = "[0-9]{4}-[0-9]{4}";
        String lunchRangeRegex = "[0-9]-[0-9]";
        int periods = Integer.parseInt(numPeriods);
        int days = Integer.parseInt(numDays);
        int semesters = Integer.parseInt(numSemesters);
        /*
        String legalBlockRegex = "(<[1-" + periods + "];([1-" + days + "](,[1-" + days + "]){0," + days + "})>)"
               + "(#<[1-" + periods + "];([1-" + days + "](,[1-" + days + "]){0," + days + "})>)*";
                */
        if (!academicYear.matches(academicYearRegex)) {
            model.addAttribute("ayregex", "Academic Year is invalid.");
            logger.info("Error: invalid academic year.");
            valid = false;
        }
       if (!lunchRange.matches(lunchRangeRegex)) {
            model.addAttribute("lrregex", "Lunch Range is invalid.");
            logger.info("Error: invalid lunch range.");
            valid = false;
        }
        if (valid == true) {
            SchoolDAO.editSchool(schoolid2, schoolName, academicYear, semesters, days, periods, lunchRange);
            //Check if scheduleblock string changed. If it didn't, then do NOT delete
            //Delete all existing scheduleblocks
 
            model.addAttribute("added", "School has been successfully edited.");
            logger.info("School was successfully edited");
        }
                logger.removeHandler(handler);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                logger.log(Level.SEVERE, null, ex);
            }  
        return editRequest(model, schoolID);
    }
    @RequestMapping(value = "/deleteschool", method = RequestMethod.POST)
    public String deleteSchool(Model model, @RequestParam(value = "schoolID") int schoolID) {
        SchoolDAO.deleteSchool(schoolID);
        try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBAdminSchools.log");
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
        logger.info("Admin Viewing List of Schools.");
        SchoolDAO schoolDAO = new SchoolDAO();
        ScheduleBlockDAO scheduleBlockDAO = new ScheduleBlockDAO();
        List<Schools> schools = schoolDAO.allSchools();
        logger.info("Returning list of schools..." + schools.size() + " schools found.");
        for (Schools school : schools) {
            List<Scheduleblocks> scheduleBlocks = scheduleBlockDAO.getSchoolsScheduleBlocks(school.getSchoolid());
            String SB2Strings = "";
            for (Scheduleblocks sb : scheduleBlocks) {
                SB2Strings += sb.toString();
            }
            school.setScheduleblocks(SB2Strings);
        }
        model.addAttribute("school", schools);
        logger.info("Schools successfully added to model.");
        handler.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return "admin";
    }

    @RequestMapping(value = "/acceptaccount", method = RequestMethod.POST)
    public String acceptAccount(Model model, @RequestParam(value = "email") String email) {
        try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBAdminStudentAccts.log");
            logger.addHandler(handler);
            handler.setFormatter(new SimpleFormatter());            
        StudentDAO.acceptAccount(email);
        Students student = StudentDAO.getStudent(email);
        EmailNotification.sendEmail(student.getEmail(), student.getFirstname());
        List<Students> accountrequests = StudentDAO.getAccountRequests();
        model.addAttribute("accountrequests", accountrequests);
        logger.info("Successfully accepted: " + email);
        logger.info("Account successfully updated to model");        
        handler.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return "adminmanagerequests";
        
    }

    @RequestMapping(value = "/rejectaccount", method = RequestMethod.POST)
    public String rejectAccount(Model model, @RequestParam(value = "email") String email) {
        try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBAdminStudentAccts.log");
            logger.addHandler(handler);
            handler.setFormatter(new SimpleFormatter());  
        StudentDAO.deleteAccount(email);
        List<Students> accountrequests = StudentDAO.getAccountRequests();
        model.addAttribute("accountrequests", accountrequests);
        logger.info("Successfully rejected: " + email);
        logger.info("Accounts successfully updated to model");
        handler.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return "adminmanagerequests";
    }

    @RequestMapping(value = "/acceptallaccount", method = RequestMethod.POST)
    public String acceptAllAccount(Model model) {
    try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBAdminStudentAccts.log");
            logger.addHandler(handler);
            handler.setFormatter(new SimpleFormatter());  
        StudentDAO.acceptAllAccount();
        List<Students> accountrequests = StudentDAO.getAccountRequests();
        model.addAttribute("accountrequests", accountrequests);
        logger.info("Successfully accepted all accounts");
        logger.info("Accounts successfully updated to model");
        handler.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return "adminmanagerequests";
    }

    @RequestMapping(value = "/deleteaccount", method = RequestMethod.POST)
    public String deleteAccount(Model model, @RequestParam(value = "email") String email) {
    try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBAdminStudentAccts.log");
            logger.addHandler(handler);
            handler.setFormatter(new SimpleFormatter());  
        StudentDAO.deleteAccount(email);
        List<Students> allStudents = StudentDAO.getAcceptedAccounts();
        model.addAttribute("allstudents", allStudents);
        logger.info("Successfully deleted: " + email);
        logger.info("Accounts successfully updated to model");        
        handler.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return "adminmanageaccounts";
    }

    @RequestMapping(value = "/adminaddschool", method = RequestMethod.GET)
    public String adminaddSchool(Model model) {
        return "adminaddschool";
    }

    @RequestMapping(value = "/addschool", method = RequestMethod.POST)
    public String addSchool(Model model, @RequestParam(value = "schoolname") String schoolName,
            @RequestParam(value = "academicyear") String academicYear,
            @RequestParam(value = "numsemesters") String numSemesters,
            @RequestParam(value = "numperiods") String numPeriods,
            @RequestParam(value = "numdays") String numDays,
            @RequestParam(value = "legalblocks") String legalBlocks,
            @RequestParam(value = "lunchrange") String lunchRange) {
        try {
            //Initialize the file that the logger writes to.
            Handler handler = new FileHandler("%tBSxSBAddSchool.log",true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
        boolean valid = true;
        if (schoolName.isEmpty() || academicYear.isEmpty() || numSemesters.isEmpty() || numPeriods.isEmpty() || legalBlocks.isEmpty() || lunchRange.isEmpty()) {
            model.addAttribute("fillout", "Please fill out all Required Fields");
            valid = false;
        }
        Schools school = SchoolDAO.getSchoolByNameYear(schoolName, academicYear);
        String academicYearRegex = "[0-9]{4}-[0-9]{4}";
        String lunchRangeRegex = "[0-9]-[0-9]";
        int periods = Integer.parseInt(numPeriods);
        int days = Integer.parseInt(numDays);
        int semesters = Integer.parseInt(numSemesters);
        String legalBlockRegex = "(<[1-" + periods + "];([1-" + days + "](,[1-" + days + "]){0," + days + "})>)"
                + "(#<[1-" + periods + "];([1-" + days + "](,[1-" + days + "]){0," + days + "})>)*";
        if (!academicYear.matches(academicYearRegex)) {
            model.addAttribute("ayregex", "Academic Year is invalid");
            logger.info("Invalid Academic Year");
            valid = false;
        }
        if (!lunchRange.matches(lunchRangeRegex)) {
            model.addAttribute("lrregex", "Lunch Range is invalid");
            logger.info("Invalid Lunch Range");
            valid = false;
        }
        if (periods <= 9) {
            if (!legalBlocks.matches(legalBlockRegex)) {
                model.addAttribute("lbregex", "Legal Block set is invalid");
                logger.info("Invalid Legal Block");                
                valid = false;
            }
        }
        if (school != null) {
            model.addAttribute("taken", "There is already a school with this name and academic year.");
            logger.info("Invalid name and academic year");
            valid = false;
        }
        if (valid == true) {
            SchoolDAO.addSchool(schoolName, academicYear, semesters, days, periods, lunchRange);
            Schools tempSchool = SchoolDAO.getSchoolByNameYear(schoolName, academicYear);
            int schoolIDForSB = tempSchool.getSchoolid();
            //Add all scheduleblocks
            String[] lbArray = legalBlocks.split("#");
            //Array of strings in the format ({1;1,2,3}
            for (String s : lbArray) {
                String temp = s.substring(1, s.length() - 1);
                String[] tempArray = temp.split(";");
                int pd = Integer.parseInt(tempArray[0]);
                ScheduleBlockDAO.addScheduleBlock(schoolIDForSB, pd, tempArray[1]);
            }

            model.addAttribute("added", "School has been successfully added.");
            logger.info("Successfully added school " + schoolName);
        }
        handler.close();
        logger.removeHandler(handler);
        // Scheduleblocks are in the form of <period;day1,day2..>#<period;day1,day2..>.
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return "adminaddschool";
    }

    @RequestMapping(value = "/adminmanageaccounts", method = RequestMethod.GET)
    public String manageAccounts(Model model) {
        List<Students> allStudents = StudentDAO.getAcceptedAccounts();
        model.addAttribute("allstudents", allStudents);
        return "adminmanageaccounts";
    }

    @RequestMapping(value = "/adminmanagerequests", method = RequestMethod.GET)
    public String manageRequests(Model model) {
        List<Students> accountrequests = StudentDAO.getAccountRequests();
        model.addAttribute("accountrequests", accountrequests);
        return "adminmanagerequests";
    }

}
