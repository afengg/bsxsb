/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Hibernate.HibernateUtil;
import Mapping.POJO.Friendships;
import Mapping.POJO.FriendshipsId;
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
