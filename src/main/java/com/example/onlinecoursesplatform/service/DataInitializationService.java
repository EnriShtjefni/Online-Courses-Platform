package com.example.onlinecoursesplatform.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DataInitializationService implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void run(String... args) {
        insertRoles();
        insertUser("admin", "admin", 1L);
        insertUser("user", "user", 2L);
    }

    @Transactional
    public void insertRoles() {
        Long existingRoleCount = (Long) entityManager.createQuery(
                        "SELECT COUNT(r) FROM Role r WHERE r.roleName IN ('Administrator', 'SimpleUser')")
                .getSingleResult();

        if (existingRoleCount == 0) {
            String nativeQuery = "INSERT INTO role (role_name) VALUES ('Administrator'), ('SimpleUser')";
            Query query = entityManager.createNativeQuery(nativeQuery);
            query.executeUpdate();
        } else {
            System.out.println("Roles already exist.");
        }
    }

    @Transactional
    public void insertUser(String username, String rawPassword, Long roleId) {
        Long existingUserCount = (Long) entityManager.createQuery(
                        "SELECT COUNT(u) FROM Users u WHERE u.userName = :username")
                .setParameter("username", username)
                .getSingleResult();

        if (existingUserCount == 0) {
            String encodedPassword = passwordEncoder.encode(rawPassword);
            String nativeQuery = "INSERT INTO users (user_name, password, role_id) VALUES (:username, :password, :roleId)";
            Query query = entityManager.createNativeQuery(nativeQuery)
                    .setParameter("username", username)
                    .setParameter("password", encodedPassword)
                    .setParameter("roleId", roleId);

            query.executeUpdate();
        } else {
            System.out.println("User with username " + username + " already exists.");
        }
    }
}

