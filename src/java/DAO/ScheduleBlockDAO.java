/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;
import Hibernate.HibernateUtil;
import Mapping.POJO.Scheduleblocks;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
/**
 *
 * @author alvin
 */
public class ScheduleBlockDAO {
    private static Session session;
    
    public List<Scheduleblocks> getSchoolsScheduleBlocks(int schoolid){
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createSQLQuery(
                "SELECT * FROM scheduleblocks WHERE scheduleblocks.schoolid = schoolid")
                .addEntity(Scheduleblocks.class);
        List<Scheduleblocks> schoolScheduleBlocks = query.list();
        return schoolScheduleBlocks;
    }

}
