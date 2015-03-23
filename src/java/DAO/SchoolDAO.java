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
        return allSchools;
    }

    public static void deleteSchool(int schoolID) {
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery(
                "select * from schools where schoolid = ?")
                .addEntity(Schools.class)
                .setParameter(0, schoolID);
            
        List<Schools> allSchools = query.list();
        Schools school = allSchools.get(0);
        System.out.print(school.getSchoolname());
        session.getTransaction().begin();
        session.delete(school);
        session.getTransaction().commit();

    }
}
