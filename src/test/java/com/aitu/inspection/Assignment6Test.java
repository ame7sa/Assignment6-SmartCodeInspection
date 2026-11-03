package com.aitu.inspection;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class Assignment6Test {

    private Directory project() {
        return Demo.sampleProject();
    }

    @Test
    void securityFindsCredentials() {
        var v = new SecurityAuditVisitor();
        project().accept(v);
        assertTrue(v.getIssues().stream().anyMatch(s -> s.contains("credentials")));
    }

    @Test
    void securityFindsRisk() {
        var v = new SecurityAuditVisitor();
        project().accept(v);
        assertTrue(v.getIssues().stream().anyMatch(s -> s.contains("high-risk")));
    }

    @Test
    void securityFindsVulnerableDependency() {
        var v = new SecurityAuditVisitor();
        project().accept(v);
        assertTrue(v.getIssues().stream().anyMatch(s -> s.contains("vulnerable")));
    }

    @Test
    void statsCountsFiles() {
        var v = new ProjectStatisticsVisitor();
        project().accept(v);
        assertEquals(5, v.getTotalFiles());
    }

    @Test
    void statsCountsLines() {
        var v = new ProjectStatisticsVisitor();
        project().accept(v);
        assertEquals(390, v.getLinesOfCode());
    }

    @Test
    void statsCountsTests() {
        var v = new ProjectStatisticsVisitor();
        project().accept(v);
        assertEquals(1, v.getTests());
    }

    @Test
    void statsCountsDependencies() {
        var v = new ProjectStatisticsVisitor();
        project().accept(v);
        assertEquals(2, v.getDependencies());
    }

    @Test
    void statsAverageComplexity() {
        var v = new ProjectStatisticsVisitor();
        project().accept(v);
        assertEquals(9.5, v.getAverageComplexity());
    }

    @Test
    void qualityGateFailsLowTestRatio() {
        var v = new QualityGateVisitor(10, 0.6);
        project().accept(v);
        assertFalse(v.isTestsPass());
    }

    @Test
    void qualityGatePassesSimpleProject() {
        var d = new Directory("root")
                .add(new SourceCodeFile("A.java", 20, 4, false, false))
                .add(new TestFile("ATest.java", 10, "A.java"));

        var v = new QualityGateVisitor();
        d.accept(v);
        assertTrue(v.isPassed());
    }

    @Test
    void complianceFindsExposedCredentials() {
        var d = new Directory("root")
                .add(new ConfigurationFile("secret.properties", false, true));

        var v = new ComplianceVisitor();
        d.accept(v);
        assertFalse(v.isCompliant());
    }

    @Test
    void nestedDirectoryIsVisited() {
        var d = new Directory("root")
                .add(new Directory("nested")
                .add(new SourceCodeFile("A.java", 5, 2, false, false)));

        var v = new ProjectStatisticsVisitor();
        d.accept(v);
        assertEquals(1, v.getTotalFiles());
    }

    @Test
    void emptyDirectoryHasZeroStats() {
        var v = new ProjectStatisticsVisitor();
        new Directory("empty").accept(v);

        assertEquals(0, v.getTotalFiles());
        assertEquals(0, v.getAverageComplexity());
    }

    @Test
    void technicalDebtVisitorWorks() {
        var v = new TechnicalDebtVisitor();
        project().accept(v);
        assertTrue(v.getDebtPoints() > 0);
    }

    @Test
    void edgeCaseNoSourceRatioIsZero() {
        var v = new ProjectStatisticsVisitor();

        new Directory("root")
                .add(new TestFile("T.java", 10, "X"))
                .accept(v);

        assertEquals(0, v.getTestSourceRatio());
    }
}
