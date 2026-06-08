package com.policytracker.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ModularArchitectureTest {

    @Test
    void clientModuleMustNotDependOnDomainCorePackages() {
        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.policytracker");

        noClasses()
                .that().resideInAPackage("..client..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "..audit.core..",
                        "..externalinsurance.core..",
                        "..systemproperties.core..",
                        "..referencedataimport.core.."
                )
                .check(importedClasses);
    }
}
