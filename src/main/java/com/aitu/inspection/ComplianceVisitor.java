
package com.aitu.inspection;

import java.util.*;

public class ComplianceVisitor implements ProjectVisitor {

    private final List<String> violations = new ArrayList<>();
    private final Set<String> sourceModules = new HashSet<>();
    private final Set<String> testedModules = new HashSet<>();

    public List<String> getViolations() {
        return Collections.unmodifiableList(violations);
    }

    public boolean isCompliant() {
        return violations.isEmpty();
    }

    public void visit(SourceCodeFile f) {
        sourceModules.add(f.getName());
    }

    public void visit(TestFile f) {
        testedModules.add(f.getTestedModule());
    }

    public void visit(ConfigurationFile f) {
        if (f.hasExposedCredentials()) {
            violations.add(f.getName() + ": credentials must not be exposed");
        }
    }

    public void visit(DependencyFile f) {
        if (f.hasUnspecifiedVersions()) {
            violations.add(f.getName() + ": dependency version is unspecified");
        }

        if (f.hasProhibitedDependencies()) {
            violations.add(f.getName() + ": prohibited dependency detected");
        }
    }

    public void visit(Directory d) {
        d.getChildren().forEach(e -> e.accept(this));
    }

    public void checkTestCoverage() {
        for (String source : sourceModules) {
            String moduleName = source.endsWith(".java")
                    ? source.substring(0, source.length() - 5)
                    : source;

            if (!testedModules.contains(source)
                    && !testedModules.contains(moduleName)) {
                violations.add(source + ": production source has no matching test");
            }
        }
    }
}
