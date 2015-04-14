/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Hibernate.HibernateUtil;
import Mapping.POJO.Courses;
import Mapping.POJO.Registrations;
import Mapping.POJO.RegistrationsId;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author alvin
 */
public class CourseDAO {

    private static Session session;

    public static List<Courses> getCoursesForStudent(int studentid) {
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery("SELECT * FROM courses, registrations, scheduleblocks WHERE studentid = ? AND courses.courseid = registrations.courseid AND "
                + "courses.scheduleblockid = scheduleblocks.scheduleblockid ORDER BY period ASC")
                .addEntity(Courses.class)
                .setInteger(0, studentid);
        List<Courses> studentsCourses = query.list();
        session.close();
        return studentsCourses;
    }

    public static List<Courses> getCourseOfferingForSchool(int schoolid) {
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery("SELECT * FROM courses WHERE schoolid=?")
                .addEntity(Courses.class)
                .setInteger(0, schoolid);
        List<Courses> studentsCourses = query.list();
        session.close();
        return studentsCourses;
    }

    public static Courses getCourse(int courseid) {
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery("SELECT * FROM courses WHERE courseid=?")
                .addEntity(Courses.class)
                .setInteger(0, courseid);
        List<Courses> courses = query.list();
        Courses course = courses.get(0);
        session.close();
        return course;
    }
    public static Courses getCourse(String courseIdentifier, String courseName, int scheduleblockid, int schoolid, String instructor, String semesters){
                session = HibernateUtil.getSessionFactory().openSession();
                Query query = session.createSQLQuery("SELECT * FROM courses WHERE courseidentifier = ? AND coursename = ? AND scheduleblockid = ? AND schoolid = ? AND instructor = ? AND semester = ?")
                .addEntity(Courses.class)
                .setParameter(0, courseIdentifier)
                .setParameter(1, courseName)
                .setParameter(2, scheduleblockid)
                .setParameter(3, schoolid)
                .setParameter(4, instructor)
                .setParameter(5, semesters);
        List<Courses> course = query.list();
        if(course.isEmpty()){
            return null;
        }
        else{
            return course.get(0);
        }
    }
    public static void addCourse(Courses newCourse, int studentid){
        session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        session.save(newCourse);
        session.getTransaction().commit();
        //retrieve this course
        Query query = session.createSQLQuery("SELECT * FROM courses WHERE courseidentifier = ? AND coursename = ? AND scheduleblockid = ? AND schoolid = ? AND instructor = ?")
                .addEntity(Courses.class)
                .setParameter(0, newCourse.getCourseidentifier())
                .setParameter(1, newCourse.getCoursename())
                .setParameter(2, newCourse.getScheduleblockid())
                .setParameter(3, newCourse.getSchoolid())
                .setParameter(4, newCourse.getInstructor());
        List<Courses> course = query.list();
        if(course.isEmpty()){
            return;
        }
        else{
            Courses c = course.get(0);
            RegistrationsId registrationId = new RegistrationsId(c.getCourseid(), studentid);
            Registrations registration = new Registrations(registrationId);
            session.getTransaction().begin();
            session.save(registration);
            session.getTransaction().commit();
        }
        session.close();
    }
   
}
