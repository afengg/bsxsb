/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;



import Hibernate.HibernateUtil;
import Mapping.POJO.Student;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author lun
 */
public class StudentDAO {
    public static List<Student> getStudent(){
        Session session = HibernateUtil.getSessionFactory().openSession();
                Query query = session.createSQLQuery(
                "SELECT * FROM student")
                .addEntity(Student.class);
          List<Student> allStudents = query.list();
          session.close();
        return allStudents;
    }
}
