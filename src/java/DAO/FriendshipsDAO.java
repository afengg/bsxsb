/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Hibernate.HibernateUtil;
import Mapping.POJO.Friendships;
import Mapping.POJO.FriendshipsId;
import Mapping.POJO.Students;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author lun
 */
public class FriendshipsDAO {

    private static Session session;

    public static void acceptfriend(int userid, int friendid) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        FriendshipsId friendshipid = new FriendshipsId();
        friendshipid.setFriend1(userid);
        friendshipid.setFriend2(friendid);
        Friendships friendship = new Friendships();
        friendship.setId(friendshipid);
        friendship.setAccepted(true);
        session.update(friendship);
        session.getTransaction().commit();
    }

    public static String addfriend(Students addfriend, Students currentStudent) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        List<Students> friends = StudentDAO.getFriends(currentStudent.getStudentid());
        List<Students> friendrequests = StudentDAO.getFriendRequests(currentStudent.getStudentid());
        List<Students> friendrequested = StudentDAO.getFriendRequests(addfriend.getStudentid());
        for (Students friend : friends) {
            if (friend.getStudentid() == addfriend.getStudentid()) {
                return "You are already friends with this student";
            }
        }
        for (Students friend : friendrequests) {
            
            if (friend.getStudentid() == addfriend.getStudentid()) {
                return "This student have already friend requested you.  Accept or Reject the friend request";
            }
        }
        for (Students friend : friendrequested) {
            if (friend.getStudentid()==(currentStudent.getStudentid())) {
                return "You have sent a friend request to this student please wait!";
            }
        }
        FriendshipsId friendshipid = new FriendshipsId();
        friendshipid.setFriend1(addfriend.getStudentid());
        friendshipid.setFriend2(currentStudent.getStudentid());
        Friendships friendship = new Friendships();
        friendship.setId(friendshipid);
        friendship.setAccepted(false);
        session.save(friendship);
        session.getTransaction().commit();
        return "Successfully sent friend request";
    }

    public static void deletefriend(int userid, int friendid) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        FriendshipsId friendshipid = new FriendshipsId();
        friendshipid.setFriend1(userid);
        friendshipid.setFriend2(friendid);
        Friendships friendship = new Friendships();
        friendship.setId(friendshipid);
        session.delete(friendship);
        session.getTransaction().commit();
    }
}
