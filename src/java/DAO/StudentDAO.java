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
        //the String school is actually in the form: [schoolname] - [academicyear]
        //so we need to split it into these two parts to query for the school id
        String[] schoolparts = school.split(" - ");
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery(
                "SELECT schoolid FROM schools WHERE schoolname = " + "'" + schoolparts[0] + "'" +
                " AND academicyear = " + "'"+ schoolparts[1] + "'"
        );
        //This query should only return one result, a schoolid int
        session.getTransaction().begin();
        int schoolid = (int) query.uniqueResult();
        Students newStudent = new Students();
        newStudent.setEmail(email);
        newStudent.setFirstname(firstName);
        newStudent.setLastname(lastName);
        newStudent.setSchoolid(schoolid);
        newStudent.setPassword(password);
        newStudent.setRole("ROLE_USER");
        session.save(newStudent);
        session.getTransaction().commit();
        /**
        query = session.createSQLQuery(
                "INSERT INTO students (email, firstname, lastname, password, schoolid) VALUES (:a, :b, :c, :d, :e)")
                .setParameter("a", email)
                .setParameter("b", firstName)
                .setParameter("c", lastName)
                .setParameter("d", password)
                .setParameter("e", schoolid);
        query.executeUpdate(); */
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
