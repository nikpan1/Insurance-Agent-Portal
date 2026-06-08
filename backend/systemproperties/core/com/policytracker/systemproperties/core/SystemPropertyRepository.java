package com.policytracker.systemproperties.core;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemPropertyRepository extends JpaRepository<SystemPropertyEntity, Long> {

    Optional<SystemPropertyEntity> findByCustomerIdAndPropertyKey(Long customerId, String propertyKey);
}
