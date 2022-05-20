package com.example.demo.test.api;

import com.example.demo.test.entity.VerificationEntity;
import com.example.demo.test.repository.VerificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class VerificationController {
    private final VerificationRepository verificationRepository;

    @GetMapping(value = "/test/db2")
    public void getOne2() throws InterruptedException {
        while (true) {
            Thread.sleep(800L);
            List<VerificationEntity> entityList = verificationRepository.findAll();
            System.out.println();
        }

    }

}
