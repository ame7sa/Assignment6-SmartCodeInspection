package com.aitu.inspection;
public class TechnicalDebtVisitor implements ProjectVisitor {
    private int debtPoints;
    public int getDebtPoints(){return debtPoints;}
    public String report(){return "Estimated technical debt: "+debtPoints+" points";}
    public void visit(SourceCodeFile f){debtPoints+=Math.max(0,f.getComplexity()-5)+ (f.hasHardcodedCredentials()?10:0);}
    public void visit(TestFile f){}
    public void visit(ConfigurationFile f){if(f.isInsecure()||f.hasExposedCredentials())debtPoints+=5;}
    public void visit(DependencyFile f){if(f.hasVulnerableDependencies())debtPoints+=8;if(f.hasUnspecifiedVersions())debtPoints+=2;}
    public void visit(Directory d){d.getChildren().forEach(e->e.accept(this));}
}