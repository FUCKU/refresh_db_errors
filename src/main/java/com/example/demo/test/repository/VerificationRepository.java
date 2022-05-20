package com.example.demo.test.repository;


import com.example.demo.test.entity.VerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * This is the JPA repository for {@link VerificationEntity}
 */
public interface VerificationRepository<T> extends JpaRepository<VerificationEntity, String> {


}
