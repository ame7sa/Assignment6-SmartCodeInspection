package com.aitu.inspection;
import java.util.*;
public class SecurityAuditVisitor implements ProjectVisitor {
    private final List<String> issues=new ArrayList<>();
    public List<String> getIssues(){return Collections.unmodifiableList(issues);}
    public int getScore(){return Math.max(0,100-issues.size()*20);}
    public String report(){return "Security score: "+getScore()+"/100\\nIssues: "+issues;}
    public void visit(SourceCodeFile f){if(f.hasHardcodedCredentials())issues.add(f.getName()+": hardcoded credentials");if(f.isHighRisk())issues.add(f.getName()+": high-risk source file");}
    public void visit(TestFile f){}
    public void visit(ConfigurationFile f){if(f.isInsecure())issues.add(f.getName()+": insecure configuration");if(f.hasExposedCredentials())issues.add(f.getName()+": exposed credentials");}
    public void visit(DependencyFile f){if(f.hasVulnerableDependencies())issues.add(f.getName()+": vulnerable dependency");}
    public void visit(Directory d){d.getChildren().forEach(e->e.accept(this));}
}