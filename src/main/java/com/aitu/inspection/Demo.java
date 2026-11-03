
package com.aitu.inspection;

import java.util.*;

public class Demo {

    public static Directory sampleProject() {

        Directory root = new Directory("fintech-project");

        Directory src = new Directory("src")
                .add(new SourceCodeFile(
                        "PaymentService.java", 180, 12, true, true))
                .add(new SourceCodeFile(
                        "UserService.java", 120, 7, false, false));

        Directory tests = new Directory("tests")
                .add(new TestFile(
                        "PaymentServiceTest.java", 90, "PaymentService.java"));

        Directory config = new Directory("config")
                .add(new ConfigurationFile(
                        "application.properties", true, false));

        root.add(src)
                .add(tests)
                .add(config)
                .add(new DependencyFile(
                        "pom.xml",
                        Map.of(
                                "spring-core", "6.1.2",
                                "old-lib", "1.0"
                        ),
                        Set.of("old-lib"),
                        Set.of("bad-lib")
                ));

        return root;
    }

    public static void main(String[] args) {

        Directory project = sampleProject();

        // 1. Security Audit Visitor
        SecurityAuditVisitor security = new SecurityAuditVisitor();
        project.accept(security);

        System.out.println("=== SECURITY AUDIT ===");
        System.out.println(security.report());

        // 2. Project Statistics Visitor
        ProjectStatisticsVisitor stats = new ProjectStatisticsVisitor();
        project.accept(stats);

        System.out.println("\n=== PROJECT STATISTICS ===");
        System.out.println("Files: " + stats.getTotalFiles());
        System.out.println("Lines of code: " + stats.getLinesOfCode());
        System.out.println("Tests: " + stats.getTests());
        System.out.println("Dependencies: " + stats.getDependencies());

        // 3. Quality Gate Visitor
        QualityGateVisitor gate = new QualityGateVisitor();
        project.accept(gate);

        System.out.println("\n=== QUALITY GATE ===");
        System.out.println(gate.report());

        // 4. Compliance Visitor
        ComplianceVisitor compliance = new ComplianceVisitor();
        project.accept(compliance);

        // Check test coverage after visiting all files
        compliance.checkTestCoverage();

        System.out.println("\n=== COMPLIANCE CHECK ===");
        System.out.println(
                "Compliance: "
                + (compliance.isCompliant() ? "PASS" : "FAIL")
        );
        System.out.println("Violations: " + compliance.getViolations());

        // 5. Technical Debt Visitor
        TechnicalDebtVisitor debt = new TechnicalDebtVisitor();
        project.accept(debt);

        System.out.println("\n=== TECHNICAL DEBT ===");
        System.out.println(debt.report());
    }
}
