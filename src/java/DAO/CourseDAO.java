/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Hibernate.HibernateUtil;
import Mapping.POJO.Courses;
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
}
