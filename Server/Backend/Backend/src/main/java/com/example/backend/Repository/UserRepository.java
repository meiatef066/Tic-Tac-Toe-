package com.example.backend.Repository;

import com.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
     //List<User> findByUsername( String username);
     List<User> findByStatus( String status);
     User findByUsername( String username);
}
