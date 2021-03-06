/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Hibernate.HibernateUtil;
import Mapping.POJO.Schools;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author alvin
 */
public class SchoolDAO {

    private static Session session;

    public static List<Schools> allSchools() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.getTransaction().begin();
        Query query = session.createSQLQuery(
                "SELECT * FROM schools")
                .addEntity(Schools.class);
        List<Schools> allSchools = query.list();
        session.getTransaction().commit();
        return allSchools;
    }

    public static Schools getSchool(int schoolID) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.getTransaction().begin();
        Query query = session.createSQLQuery("SELECT * FROM schools WHERE schoolid = ?")
                .addEntity(Schools.class)
                .setInteger(0, schoolID);
        List<Schools> schools = query.list();
        session.getTransaction().commit();
        if (schools.isEmpty()) {
            return null;
        } else {
            return schools.get(0);
        }
    }

    public static List<Schools> getSchoolSameName(String schoolName) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.getTransaction().begin();
        Query query = session.createSQLQuery("SELECT * FROM schools WHERE schoolname = ?")
                .addEntity(Schools.class)
                .setString(0, schoolName);
        List<Schools> schools = query.list();
        session.getTransaction().commit();
        return schools;
    }

    public static void deleteSchool(int schoolID) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.getTransaction().begin();
        Query query = session.createSQLQuery(
                "select * from schools where schoolid = ?")
                .addEntity(Schools.class)
                .setParameter(0, schoolID);
        List<Schools> allSchools = query.list();
        Schools school = allSchools.get(0);
        session.delete(school);

        session.getTransaction().commit();
    }

    public static Schools getSchoolByNameYear(String schoolName, String academicYear) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.getTransaction().begin();
        Query query = session.createSQLQuery("SELECT * FROM schools WHERE schoolname = ? AND academicyear = ?")
                .addEntity(Schools.class)
                .setParameter(0, schoolName)
                .setParameter(1, academicYear);
        List<Schools> schools = query.list();
        session.getTransaction().commit();
        if (schools.isEmpty()) {
            return null;
        } else {
            return schools.get(0);
        }
    }

    public static void addSchool(String schoolName, String academicYear, int semesters, int days, int periods, String lunchRange) {
        Schools newSchool = new Schools();
        newSchool.setSchoolname(schoolName);
        newSchool.setAcademicyear(academicYear);
        newSchool.setNumdays(days);
        newSchool.setLunchrange(lunchRange);
        newSchool.setNumperiods(periods);
        newSchool.setNumsemesters(semesters);
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.getTransaction().begin();
        session.save(newSchool);

        session.getTransaction().commit();
    }

    public static void editSchool(int schoolID, String schoolName, String academicYear, int semesters, int days, int periods, String lunchRange) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.getTransaction().begin();
        Schools school = (Schools) session.get(Schools.class, schoolID);
        school.setAcademicyear(academicYear);
        school.setLunchrange(lunchRange);
        school.setNumperiods(periods);
        school.setNumdays(days);
        school.setSchoolname(schoolName);
        school.setNumsemesters(semesters);
        session.save(school);

        session.getTransaction().commit();

    }
}
