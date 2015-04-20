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
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery(
                "SELECT * FROM schools")
                .addEntity(Schools.class);
        List<Schools> allSchools = query.list();
        session.close();
        return allSchools;
    }

    public static Schools getSchool(int schoolID) {
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery("SELECT * FROM schools WHERE schoolid = ?")
                .addEntity(Schools.class)
                .setInteger(0, schoolID);
        List<Schools> schools = query.list();
        session.close();
        if(schools.isEmpty()){
            return null;
        }
        else{
            return schools.get(0);
        }
    }

    public static List<Schools> getSchoolSameName(String schoolName) {
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery("SELECT * FROM schools WHERE schoolname = ?")
                .addEntity(Schools.class)
                .setString(0, schoolName);
        List<Schools> schools = query.list();
        session.close();
        return schools;
    }

    public static void deleteSchool(int schoolID) {
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery(
                "select * from schools where schoolid = ?")
                .addEntity(Schools.class)
                .setParameter(0, schoolID);
        List<Schools> allSchools = query.list();
        Schools school = allSchools.get(0);
        session.getTransaction().begin();
        session.delete(school);
        session.getTransaction().commit();
        session.close();
    }

    public static Schools getSchoolByNameYear(String schoolName, String academicYear) {
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery("SELECT * FROM schools WHERE schoolname = ? AND academicyear = ?")
                .addEntity(Schools.class)
                .setParameter(0, schoolName)
                .setParameter(1, academicYear);
        List<Schools> schools = query.list();
        session.close();
        if(schools.isEmpty()){
            return null;
        }
        else{
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
        session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        session.save(newSchool);
        session.getTransaction().commit();
        session.close();
    }
}
