/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Hibernate.HibernateUtil;
import Mapping.POJO.Courses;
import Mapping.POJO.Friendships;
import Mapping.POJO.Generationcriteria;
import Mapping.POJO.Students;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author lun
 */
public class GenerationcriteriaDAO {

    private static Session session;

    public static Generationcriteria getGenerationCriteria(int studentID) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.getTransaction().begin();
        Query query = session.createSQLQuery(
                "SELECT * FROM generationcriteria where studentid = ?")
                .addEntity(Generationcriteria.class)
                .setInteger(0, studentID);
        List<Generationcriteria> gencriterias = query.list();
        session.getTransaction().commit();
        if(!gencriterias.isEmpty()){
        Generationcriteria gencriteria = gencriterias.get(0);
        return gencriteria;
        }
        return null;
    }
    public static void addDesiredCourses(int studentid, String courseid) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.getTransaction().begin();
        Query query = session.createSQLQuery(
                "SELECT * FROM generationcriteria WHERE studentid = ?")
                .addEntity(Generationcriteria.class)
                .setInteger(0, studentid);
        List<Generationcriteria> gencriterias = query.list();
        Generationcriteria gencriteria = gencriterias.get(0);
        gencriteria.setCourseids(gencriteria.getCourseids() + courseid + ",");
        session.update(gencriteria);
         
        session.getTransaction().commit();
    }

    public static void removeDesiredCourses(int studentid, String courseid) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.getTransaction().begin();
        Query query = session.createSQLQuery(
                "SELECT * FROM generationcriteria WHERE studentid = ?")
                .addEntity(Generationcriteria.class)
                .setInteger(0, studentid);
        List<Generationcriteria> gencriterias = query.list();
        Generationcriteria gencriteria = gencriterias.get(0);
        StringBuilder sb = new StringBuilder(gencriteria.getCourseids());
        sb.delete(sb.indexOf(courseid), sb.indexOf(courseid) + courseid.length()+1);
        String newcourseids = sb.toString();
        gencriteria.setCourseids(newcourseids);
        session.update(gencriteria);
         
        session.getTransaction().commit();
    }

    public static void addLunch(int studentid, String lunch) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.getTransaction().begin();
        Query query = session.createSQLQuery(
                "SELECT * FROM generationcriteria WHERE studentid = ?")
                .addEntity(Generationcriteria.class)
                .setInteger(0, studentid);
        List<Generationcriteria> gencriterias = query.list();
        Generationcriteria gencriteria = gencriterias.get(0);
        if(gencriteria.getLunch()==null){
             gencriteria.setLunch(lunch + ",");
        }
        else{
             gencriteria.setLunch(gencriteria.getLunch() + lunch + ",");
        }
        session.update(gencriteria);
         
        session.getTransaction().commit();
    }

    public static void removeLunch(int studentid, String lunch) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.getTransaction().begin();
        Query query = session.createSQLQuery(
                "SELECT * FROM generationcriteria WHERE studentid = ?")
                .addEntity(Generationcriteria.class)
                .setInteger(0, studentid);
        List<Generationcriteria> gencriterias = query.list();
        Generationcriteria gencriteria = gencriterias.get(0);
        StringBuilder sb = new StringBuilder(gencriteria.getLunch());
        if(lunch.equals("monday") || lunch.equals("sunday") || lunch.equals("friday")){
                sb.delete(sb.indexOf(lunch), sb.indexOf(lunch) + 7);
        }
        else if(lunch.equals("tuesday")){
                sb.delete(sb.indexOf(lunch), sb.indexOf(lunch) + 8);
        }
        else if(lunch.equals("thursday") || lunch.equals("saturday")){
                sb.delete(sb.indexOf(lunch), sb.indexOf(lunch) + 9);
        }
        else if(lunch.equals("wednesday")){
                sb.delete(sb.indexOf(lunch), sb.indexOf(lunch) + 10);
        }
        String newlunch = sb.
                toString();
        gencriteria.setLunch(newlunch);
        session.update(gencriteria);
         
        session.getTransaction().commit();
    }
}
