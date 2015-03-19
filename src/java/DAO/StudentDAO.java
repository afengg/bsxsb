/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Hibernate.HibernateUtil;
import Mapping.POJO.Students;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author lun
 */
public class StudentDAO {

    private static Session session;

    public static void register(String firstName, String lastName, String email, String password, String school) {
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery(
                "INSERT INTO students values (?,?,?,?)")
                .setParameter(0, email)
                .setParameter(1, firstName)
                .setParameter(2, lastName)
                .setParameter(3, password);
        query.executeUpdate();
    }
    public static List<Students> allStudent() {
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery(
                "SELECT * FROM students")
                .addEntity(Students.class);
        List<Students> allStudents = query.list();
        return allStudents;
    }

    public Students loginStudent(String email, String password) {
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery(
                "SELECT * FROM students WHERE email = ? AND password =?")
                .addEntity(Students.class)
                .setString(0, email)
                .setString(1, password);
        List<Students> allStudents = query.list();
        if (allStudents.isEmpty()) {
            return null;
        }
        Students student = allStudents.get(0);
        return student;
    }

    public static Students getStudent(String email) {
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery(
                "SELECT * FROM students WHERE email = ?")
                .addEntity(Students.class)
                .setString(0, email);
        List<Students> allStudents = query.list();
        if (allStudents.isEmpty()) {
            return null;
        }
        Students student = allStudents.get(0);
        return student;
    }

}
