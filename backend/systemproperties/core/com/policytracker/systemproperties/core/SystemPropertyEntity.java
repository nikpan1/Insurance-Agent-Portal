package com.policytracker.systemproperties.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "system_properties",
        uniqueConstraints = @UniqueConstraint(name = "uk_system_properties_customer_property", columnNames = {"customer_id", "property_key"})
)
@Getter
@Setter
@NoArgsConstructor
public class SystemPropertyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "property_key", nullable = false, length = 120)
    private String propertyKey;

    @Column(name = "property_value", nullable = false, length = 500)
    private String propertyValue;

    @Version
    private Long version;
}
