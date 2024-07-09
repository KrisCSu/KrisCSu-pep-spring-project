package com.example.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entity.*;


public interface MessageRepository extends JpaRepository<Message, Integer>{
    List<Message> findByPostedBy(Integer accountId);
}
