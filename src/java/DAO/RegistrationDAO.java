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
import Mapping.POJO.Schools;
import Mapping.POJO.Students;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author alvin
 */
public class RegistrationDAO {
    
    private static Session session;
    
    public static void removereg(int courseid, int studentid) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        RegistrationsId regID = new RegistrationsId(courseid, studentid);
        Registrations reg = new Registrations(regID);
        session.delete(reg);
        session.getTransaction().commit();
        session.close();
    }
    
    public static boolean isRegistered(Courses course, Students student) {
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery(
                "SELECT * FROM registrations where courseid=? AND studentid=?")
                .addEntity(Registrations.class)
                .setInteger(0, course.getCourseid())
                .setInteger(1, student.getStudentid());
        List<Registrations> reg = query.list();
        session.close();
        if(reg.isEmpty()){
            return false;
        }
        else{
            return true;
        }
    }
    
    public static void addRegistration(int courseid, int studentid) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        RegistrationsId regID = new RegistrationsId(courseid, studentid);
        Registrations reg = new Registrations(regID);
        session.save(reg);
        session.getTransaction().commit();
        session.close();
    }
}
