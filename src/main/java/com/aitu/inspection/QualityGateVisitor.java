
package com.aitu.inspection;

public class QualityGateVisitor implements ProjectVisitor {

    private final int maxComplexity;
    private final double minTestRatio;

    private boolean securityPass = true;
    private boolean complexityPass = true;
    private boolean testsPass = true;

    public QualityGateVisitor() {
        this(10, 0.6);
    }

    public QualityGateVisitor(int maxComplexity, double minTestRatio) {
        this.maxComplexity = maxComplexity;
        this.minTestRatio = minTestRatio;
    }

    public boolean isSecurityPass() {
        return securityPass;
    }

    public boolean isComplexityPass() {
        return complexityPass;
    }

    public boolean isTestsPass() {
        return testsPass;
    }

    public boolean isPassed() {
        return securityPass && complexityPass && testsPass;
    }

    @Override
    public void visit(SourceCodeFile file) {
        if (file.getComplexity() > maxComplexity) {
            complexityPass = false;
        }

        if (file.hasHardcodedCredentials() || file.isHighRisk()) {
            securityPass = false;
        }
    }

    @Override
    public void visit(TestFile file) {
        // Test files are checked through project statistics.
    }

    @Override
    public void visit(ConfigurationFile file) {
        if (file.isInsecure()) {
            securityPass = false;
        }
    }

    @Override
    public void visit(DependencyFile file) {
        if (file.hasVulnerableDependencies()) {
            securityPass = false;
        }
    }

    @Override
    public void visit(Directory directory) {
        int sourceCount = 0;
        int testCount = 0;

        for (ProjectElement element : directory.getChildren()) {
            element.accept(this);

            if (element instanceof SourceCodeFile) {
                sourceCount++;
            } else if (element instanceof TestFile) {
                testCount++;
            }
        }

        if (sourceCount > 0) {
            double ratio = (double) testCount / sourceCount;

            if (ratio < minTestRatio) {
                testsPass = false;
            }
        }
    }

    public String report() {
        return "Security: " + (isSecurityPass() ? "PASS" : "FAIL") + "\n"
                + "Complexity: " + (isComplexityPass() ? "PASS" : "FAIL") + "\n"
                + "Tests: " + (isTestsPass() ? "PASS" : "FAIL") + "\n"
                + "FINAL RESULT: " + (isPassed() ? "PASSED" : "FAILED");
    }
}
