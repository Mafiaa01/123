package ru.kata.spring.boot_security.demo.repositories.impl;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class UserRepoImpl implements UserRepo {
    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    public void addUser(User user) {
        getEntityManager().persist(user);

    }

    @Override
    public void deleteUser(Long id) {
        User user = getEntityManager().find(User.class, id);
        if (user != null) {
            Query query = getEntityManager().createNativeQuery("DELETE FROM user_roles r WHERE r.user_id = :userIdToDelete");
            query.setParameter("userIdToDelete", id);
            query.executeUpdate();
            getEntityManager().remove(user);
        } else {
            System.out.println("User with id " + id + " not found.");
        }
    }


    @Override
    public void editUser(User user) {
        getEntityManager().merge(user);
    }

    @Override
    public User getUserById(Long id) {
        return getEntityManager().find(User.class, id);
    }

    @Override
    public List<User> getAllUsers() {
        return getEntityManager().createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public User getUserByUsername(String username) {
        return getEntityManager().createQuery("select u from User u where u.username = :username", User.class).setParameter("username", username).getSingleResult();
    }
}
