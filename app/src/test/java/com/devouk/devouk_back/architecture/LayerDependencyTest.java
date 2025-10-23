package com.devouk.devouk_back.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com.devouk.devouk_back")
public class LayerDependencyTest {

  @ArchTest
  static final ArchRule domain_should_not_depend_on_infra_or_app =
      noClasses()
          .that()
          .resideInAPackage("..domain..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage("..infra..", "..app..");

  @ArchTest
  static final ArchRule infra_should_not_depend_on_app =
      noClasses()
          .that()
          .resideInAPackage("..infra..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage("..app..");
}
