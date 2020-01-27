package org.blahodarny.formalnemetody.model;

import org.blahodarny.formalnemetody.model.values.Var;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Dependency {

    private Var variables;
    private Set<Dependency> subDependecies;

    public Dependency() {
        subDependecies = new HashSet<>();
    }

    public void setVariable(Var var){
        variables = var;
    }

    public Var getVariable(){
        return variables;
    }

    public void addSubDependecy(Dependency dependency){
        subDependecies.add(dependency);
    }

    public void addSubDependecy(List<Dependency> dependencies){
        if(dependencies != null){
            for(Dependency dep : dependencies){
                this.subDependecies.add(dep);
            }
        }
    }

    public Set<Dependency> getSubDependecies() {
        return subDependecies;
    }
}
