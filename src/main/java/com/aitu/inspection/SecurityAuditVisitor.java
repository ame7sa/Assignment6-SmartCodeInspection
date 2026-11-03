package com.aitu.inspection;

import java.util.ArrayList;
import java.util.List;

public class SecurityAuditVisitor implements ProjectVisitor {

    private final List<String> issues = new ArrayList<>();
    private int score = 100;

    public int getScore() {
        return Math.max(0, score);
    }

    public List<String> getIssues() {
        return issues;
    }

    public boolean isPassed() {
        return getScore() >= 70;
    }

    @Override
    public void visit(SourceCodeFile file) {
        if (file.hasHardcodedCredentials()) {
            issues.add(file.getName() + ": hardcoded credentials");
            score -= 40;
        }

        if (file.isHighRisk()) {
            issues.add(file.getName() + ": high-risk source file");
            score -= 20;
        }
    }

    @Override
    public void visit(TestFile file) {
        // No security checks for test files.
    }

    @Override
    public void visit(ConfigurationFile file) {
        if (file.isInsecure()) {
            issues.add(file.getName() + ": insecure configuration");
            score -= 20;
        }
    }

    @Override
    public void visit(DependencyFile file) {
        if (file.hasVulnerableDependencies()) {
            issues.add(file.getName() + ": vulnerable dependency");
            score -= 20;
        }
    }

    @Override
    public void visit(Directory directory) {
        for (ProjectElement element : directory.getChildren()) {
            element.accept(this);
        }
    }

    public String report() {
        return "Security score: " + getScore() + "/100\n"
                + "Issues: " + issues;
    }
}