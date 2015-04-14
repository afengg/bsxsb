/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Hibernate.HibernateUtil;
import Mapping.POJO.Friendships;
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

    public static void setLoggedIn(String email) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        Query query = session.createSQLQuery(
                "SELECT * FROM students WHERE email = ?")
                .addEntity(Students.class)
                .setString(0, email);
        List<Students> allStudents = query.list();
        Students student = allStudents.get(0);
        student.setLoggedin(true);
        session.update(student);
        session.getTransaction().commit();
        session.close();
    }

    public static void setLoggedOut(String email) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        Query query = session.createSQLQuery(
                "SELECT * FROM students WHERE email = ?")
                .addEntity(Students.class)
                .setString(0, email);
        List<Students> allStudents = query.list();
        Students student = allStudents.get(0);
        student.setLoggedin(false);
        session.update(student);
        session.getTransaction().commit();
        session.close();
    }

    public static void register(String firstName, String lastName, String email, String password, String school) {
        //the String school is actually in the form: [schoolname] - [academicyear]
        //so we need to split it into these two parts to query for the school id
        String[] schoolparts = school.split(" - ");
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery(
                "SELECT schoolid FROM schools WHERE schoolname = " + "'" + schoolparts[0] + "'"
                + " AND academicyear = " + "'" + schoolparts[1] + "'"
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
        session.close();
        /**
         * query = session.createSQLQuery( "INSERT INTO students (email,
         * firstname, lastname, password, schoolid) VALUES (:a, :b, :c, :d,
         * :e)") .setParameter("a", email) .setParameter("b", firstName)
         * .setParameter("c", lastName) .setParameter("d", password)
         * .setParameter("e", schoolid); query.executeUpdate();
         */
    }

    public static List<Students> allStudent() {
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery(
                "SELECT * FROM students")
                .addEntity(Students.class);
        List<Students> allStudents = query.list();
        session.close();
        return allStudents;
    }

    public static List<Students> getAcceptedAccounts() {
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery(
                "SELECT * FROM students where approved =1")
                .addEntity(Students.class);
        List<Students> allStudents = query.list();
        session.close();
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
        session.close();
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
        session.close();
        return student;
    }

    public static List<Students> getAccountRequests() {
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery(
                "SELECT * FROM students WHERE approved = 0")
                .addEntity(Students.class);
        List<Students> allStudents = query.list();
        session.close();
        return allStudents;
    }

    public static List<Students> getFriendRequests(int id) {
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery(
                "SELECT * FROM students INNER JOIN friendships ON friendships.friend1=? AND students.studentid=friend2 AND friendships.accepted=0")
                .addEntity(Students.class)
                .setInteger(0, id);
        List<Students> allStudents = query.list();
        session.close();
        return allStudents;
    }

    public static List<Students> getFriends(int id) {
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery(
                "SELECT * FROM students INNER JOIN friendships ON ((friendships.friend1=? AND studentid=friend2) OR (friendships.friend2=? AND studentid=friend1)) AND accepted=1")
                .addEntity(Students.class)
                .setInteger(0, id)
                .setInteger(1, id);
        List<Students> allStudents = query.list();
        session.close();
        return allStudents;
    }

    public static void acceptAccount(String email) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        Query query = session.createSQLQuery(
                "SELECT * FROM students WHERE email = ?")
                .addEntity(Students.class)
                .setString(0, email);
        List<Students> allStudents = query.list();
        Students student = allStudents.get(0);
        student.setApproved(true);
        session.update(student);
        session.getTransaction().commit();
        session.close();
    }

    public static void deleteAccount(String email) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        Query query = session.createSQLQuery(
                "SELECT * FROM students WHERE email = ?")
                .addEntity(Students.class)
                .setString(0, email);
        List<Students> allStudents = query.list();
        Students student = allStudents.get(0);
        session.delete(student);
        session.getTransaction().commit();
        session.close();
    }

    public static void acceptAllAccount() {
        session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        Query query = session.createSQLQuery(
                "SELECT * FROM students")
                .addEntity(Students.class);
        List<Students> allStudents = query.list();
        for (Students student : allStudents) {
            student.setApproved(true);
        }
        for (Students student : allStudents) {
            session.update(student);
        }
        session.getTransaction().commit();
        session.close();
    }
}
