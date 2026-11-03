package com.aitu.inspection;
public class ProjectStatisticsVisitor implements ProjectVisitor {
    private int files,lines,tests,dependencies,complexitySum,sourceCount;
    public int getTotalFiles(){return files;} public int getLinesOfCode(){return lines;} public int getTests(){return tests;}
    public int getDependencies(){return dependencies;} public double getAverageComplexity(){return sourceCount==0?0:(double)complexitySum/sourceCount;}
    public double getTestSourceRatio(){return sourceCount==0?0:(double)tests/sourceCount;}
    public void visit(SourceCodeFile f){files++;lines+=f.getLines();sourceCount++;complexitySum+=f.getComplexity();}
    public void visit(TestFile f){files++;lines+=f.getLines();tests++;}
    public void visit(ConfigurationFile f){files++;}
    public void visit(DependencyFile f){files++;dependencies+=f.getDependencies().size();}
    public void visit(Directory d){d.getChildren().forEach(e->e.accept(this));}
}