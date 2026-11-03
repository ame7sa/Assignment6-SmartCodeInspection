package com.aitu.inspection;
import java.util.*;
public class DependencyFile implements ProjectElement {
    private final String name; private final Map<String,String> dependencies; private final Set<String> vulnerable; private final Set<String> prohibited;
    public DependencyFile(String name,Map<String,String> dependencies,Set<String> vulnerable,Set<String> prohibited){
        this.name=name;this.dependencies=new LinkedHashMap<>(dependencies);this.vulnerable=new HashSet<>(vulnerable);this.prohibited=new HashSet<>(prohibited);
    }
    public String getName(){return name;} public Map<String,String> getDependencies(){return Collections.unmodifiableMap(dependencies);}
    public boolean hasVulnerableDependencies(){return dependencies.keySet().stream().anyMatch(vulnerable::contains);}
    public boolean hasProhibitedDependencies(){return dependencies.keySet().stream().anyMatch(prohibited::contains);}
    public boolean hasUnspecifiedVersions(){return dependencies.values().stream().anyMatch(v->v==null||v.isBlank()||v.equalsIgnoreCase("latest"));}
    @Override public void accept(ProjectVisitor visitor){visitor.visit(this);}
}