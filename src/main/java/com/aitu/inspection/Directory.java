package com.aitu.inspection;
import java.util.*;
public class Directory implements ProjectElement {
    private final String name; private final List<ProjectElement> children=new ArrayList<>();
    public Directory(String name){this.name=name;}
    public String getName(){return name;} public List<ProjectElement> getChildren(){return Collections.unmodifiableList(children);}
    public Directory add(ProjectElement element){children.add(Objects.requireNonNull(element));return this;}
    @Override public void accept(ProjectVisitor visitor){visitor.visit(this);}
}