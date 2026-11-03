package com.aitu.inspection;
public class SourceCodeFile implements ProjectElement {
    private final String name; private final int lines, complexity; private final boolean hardcodedCredentials, highRisk;
    public SourceCodeFile(String name,int lines,int complexity,boolean hardcodedCredentials,boolean highRisk){
        this.name=name; this.lines=lines; this.complexity=complexity; this.hardcodedCredentials=hardcodedCredentials; this.highRisk=highRisk;
    }
    public String getName(){return name;} public int getLines(){return lines;} public int getComplexity(){return complexity;}
    public boolean hasHardcodedCredentials(){return hardcodedCredentials;} public boolean isHighRisk(){return highRisk;}
    @Override public void accept(ProjectVisitor visitor){visitor.visit(this);}
}