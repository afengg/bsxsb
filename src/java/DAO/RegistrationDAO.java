/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;
import Hibernate.HibernateUtil;
import Mapping.POJO.Registrations;
import Mapping.POJO.RegistrationsId;
import java.util.List;
import org.hibernate.Session;
/**
 *
 * @author alvin
 */
public class RegistrationDAO {
    private static Session session;
    public static void removereg(int courseid,int studentid){
        session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        RegistrationsId regID = new RegistrationsId(courseid,studentid);
        Registrations reg = new Registrations(regID);
        session.delete(reg);
        session.getTransaction().commit();
        session.close();
    }
    
    public static void addRegistration(int courseid, int studentid){
                session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        RegistrationsId regID = new RegistrationsId(courseid,studentid);
        Registrations reg = new Registrations(regID);
        session.save(reg);
        session.getTransaction().commit();
        session.close();
    }
}
