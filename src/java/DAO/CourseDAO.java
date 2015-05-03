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
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.getTransaction().begin();
        Query query = session.createSQLQuery("SELECT * FROM courses, registrations, scheduleblocks WHERE studentid = ? AND courses.courseid = registrations.courseid AND "
                + "courses.scheduleblockid = scheduleblocks.scheduleblockid ORDER BY period ASC")
                .addEntity(Courses.class)
                .setInteger(0, studentid);
        List<Courses> studentsCourses = query.list();
        session.getTransaction().commit();
        return studentsCourses;
    }

    public static List<Courses> getCourseOfferingForSchool(int schoolid) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.getTransaction().begin();
        Query query = session.createSQLQuery("SELECT * FROM courses WHERE schoolid=?")
                .addEntity(Courses.class)
                .setInteger(0, schoolid);
        List<Courses> studentsCourses = query.list();
        session.getTransaction().commit();
        return studentsCourses;
    }

    public static Courses getCourseUsingID(int schoolid, String courseidentifier) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
         session.getTransaction().begin();
        Query query = session.createSQLQuery("SELECT * FROM courses where schoolid=? AND courseidentifier=?")
                .addEntity(Courses.class)
                .setParameter(0, schoolid)
                .setParameter(1, courseidentifier);
        List<Courses> courses = query.list();
        session.getTransaction().commit();
        if (courses.isEmpty()) {
            return null;
        } else {
            return courses.get(0);
        }
    }

    public static Courses getCourse(int courseid) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
         session.getTransaction().begin();
        Query query = session.createSQLQuery("SELECT * FROM courses WHERE courseid=?")
                .addEntity(Courses.class)
                .setInteger(0, courseid);
        List<Courses> courses = query.list();
        Courses course = courses.get(0);
        session.getTransaction().commit();
        return course;
    }

    public static Courses getCourse(String courseIdentifier, String courseName, int scheduleblockid, int schoolid, String instructor, String semesters) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
         session.getTransaction().begin();
        Query query = session.createSQLQuery("SELECT * FROM courses WHERE courseidentifier = ? AND coursename = ? AND scheduleblockid = ? AND schoolid = ? AND instructor = ? AND semester = ?")
                .addEntity(Courses.class)
                .setParameter(0, courseIdentifier)
                .setParameter(1, courseName)
                .setParameter(2, scheduleblockid)
                .setParameter(3, schoolid)
                .setParameter(4, instructor)
                .setParameter(5, semesters);
        List<Courses> course = query.list();
        session.getTransaction().commit();
        if (course.isEmpty()) {
            return null;
        } else {
            return course.get(0);
        }
    }

    public static void incrementCourseStudents(int courseID) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.getTransaction().begin();
        Courses course = (Courses) session.get(Courses.class, courseID);
        course.incrementStudents();
        session.update(course);

        session.getTransaction().commit();
    }

    public static void decrementCourseStudentsAndDelete(int courseID) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.getTransaction().begin();
        Courses course = (Courses) session.get(Courses.class, courseID);
        course.decrementStudents();
        if (course.getNumstudents() == 0) {
            session.delete(course);
        } else {
            session.update(course);
        }
        session.getTransaction().commit();
    }

    public static void addCourse(Courses newCourse, int studentid) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.getTransaction().begin();
        session.save(newCourse);

        //retrieve this course
        Query query = session.createSQLQuery("SELECT * FROM courses WHERE courseidentifier = ? AND coursename = ? AND scheduleblockid = ? AND schoolid = ? AND instructor = ? AND semester = ?")
                .addEntity(Courses.class)
                .setParameter(0, newCourse.getCourseidentifier())
                .setParameter(1, newCourse.getCoursename())
                .setParameter(2, newCourse.getScheduleblockid())
                .setParameter(3, newCourse.getSchoolid())
                .setParameter(4, newCourse.getInstructor())
                .setParameter(5, newCourse.getSemester());
        List<Courses> course = query.list();
        if (course == null) {
        } else {
            Courses c = course.get(0);
            RegistrationsId registrationId = new RegistrationsId(c.getCourseid(), studentid);
            Registrations registration = new Registrations(registrationId);
            session.save(registration);

        }
        session.getTransaction().commit();
    }

    public static void deleteCourse(int courseid) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.getTransaction().begin();
        Query query = session.createSQLQuery("SELECT * FROM courses WHERE courseid = ?")
                .addEntity(Courses.class)
                .setParameter(0, courseid);
        List<Courses> courses = query.list();
        Courses course = courses.get(0);
        session.delete(course);

        session.getTransaction().commit();
    }

}
