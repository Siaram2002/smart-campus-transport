package com.accessManagementSystem.repository;



import com.accessManagementSystem.entities.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {

    List<AccessLog> findByStudentId(String studentId);

    List<AccessLog> findByDriverId(String driverId);
}

