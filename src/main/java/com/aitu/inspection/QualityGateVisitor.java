package com.aitu.inspection;
public class QualityGateVisitor implements ProjectVisitor {
    private final ProjectStatisticsVisitor stats=new ProjectStatisticsVisitor();
    private final SecurityAuditVisitor security=new SecurityAuditVisitor();
    private final double maxComplexity,minTestRatio;
    public QualityGateVisitor(){this(10,0.40);}
    public QualityGateVisitor(double maxComplexity,double minTestRatio){this.maxComplexity=maxComplexity;this.minTestRatio=minTestRatio;}
    public boolean isComplexityPass(){return stats.getAverageComplexity()<=maxComplexity;}
    public boolean isTestsPass(){return stats.getTestSourceRatio()>=minTestRatio;}
    public boolean isSecurityPass(){return security.getIssues().isEmpty();}
    public boolean isPassed(){return isComplexityPass()&&isTestsPass()&&isSecurityPass();}
    public String report(){return "Security: "+(isSecurityPass()?"PASS":"FAIL")+"\\nComplexity: "+(isComplexityPass()?"PASS":"FAIL")+"\\nTests: "+(isTestsPass()?"PASS":"FAIL")+"\\nFINAL RESULT: "+(isPassed()?"PASSED":"FAILED");}
    public void visit(SourceCodeFile f){stats.visit(f);security.visit(f);}
    public void visit(TestFile f){stats.visit(f);security.visit(f);}
    public void visit(ConfigurationFile f){stats.visit(f);security.visit(f);}
    public void visit(DependencyFile f){stats.visit(f);security.visit(f);}
    public void visit(Directory d){d.getChildren().forEach(e->e.accept(this));}
}