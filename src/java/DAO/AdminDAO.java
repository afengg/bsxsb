/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Hibernate.HibernateUtil;
import Mapping.POJO.Admins;
import Mapping.POJO.Students;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author lun
 */
public class AdminDAO {

    private static Session session;

    public static void setLoggedIn(String email) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        Query query = session.createSQLQuery(
                "SELECT * FROM admins WHERE email = ?")
                .addEntity(Admins.class)
                .setString(0, email);
        List<Admins> allAdmins = query.list();
        Admins admin = allAdmins.get(0);
        admin.setLoggedin(true);
        session.update(admin);
        session.getTransaction().commit();
        session.close();
    }

    public static void setLoggedOut(String email) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        Query query = session.createSQLQuery(
                "SELECT * FROM admins WHERE email = ?")
                .addEntity(Admins.class)
                .setString(0, email);
        List<Admins> allAdmins = query.list();
        Admins admin = allAdmins.get(0);
        admin.setLoggedin(false);
        session.update(admin);
        session.getTransaction().commit();
        session.close();
    }

    public static Admins getAdmin(String email) {
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery(
                "SELECT * FROM admins WHERE email = ?")
                .addEntity(Admins.class)
                .setString(0, email);
        List<Admins> allAdmins = query.list();
        if (allAdmins.isEmpty()) {
            return null;
        }
        Admins admin = allAdmins.get(0);
        session.close();
        return admin;
    }
}
