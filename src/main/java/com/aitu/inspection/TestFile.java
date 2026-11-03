package com.aitu.inspection;
public class TestFile implements ProjectElement {
    private final String name; private final int lines; private final String testedModule;
    public TestFile(String name,int lines,String testedModule){this.name=name;this.lines=lines;this.testedModule=testedModule;}
    public String getName(){return name;} public int getLines(){return lines;} public String getTestedModule(){return testedModule;}
    @Override public void accept(ProjectVisitor visitor){visitor.visit(this);}
}