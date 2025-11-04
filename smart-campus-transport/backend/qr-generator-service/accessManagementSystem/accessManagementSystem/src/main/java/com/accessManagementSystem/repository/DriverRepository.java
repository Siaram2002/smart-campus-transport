package com.accessManagementSystem.repository;

import com.accessManagementSystem.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

	Optional<Driver> findByDriverId(String driverId);

	Optional<Driver> findByEmail(String email);

	Optional<Driver> findByPhone(String phone);

	Optional<Driver> findByUsername(String username);

}
