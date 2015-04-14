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
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery(
                "SELECT * FROM generationcriteria where studentid = ?")
                .addEntity(Generationcriteria.class)
                .setInteger(0, studentID);
        List<Generationcriteria> gencriterias = query.list();
        Generationcriteria gencriteria = gencriterias.get(0);
        session.close();
        return gencriteria;

    }
}
