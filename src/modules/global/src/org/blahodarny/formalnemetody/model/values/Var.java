package org.blahodarny.formalnemetody.model.values;

import org.blahodarny.formalnemetody.model.Dependency;
import org.blahodarny.formalnemetody.model.ctx.ClassContext;

import java.util.List;

public class Var {

    private String name;
    private String type;
    private SecurityAnnotation securityAnnotation;
    private List<Dependency> execContextDependecies;
    private ClassContext classExecContext;

    public Var(String name, String type, SecurityAnnotation securityAnnotation) {
        this.name = name;
        this.type = type;
        this.securityAnnotation = securityAnnotation;
    }

    public SecurityAnnotation getSecurityAnnotation() {
        return securityAnnotation;
    }

    public void setSecurityAnnotation(SecurityAnnotation securityAnnotation) {
        this.securityAnnotation = securityAnnotation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Dependency> getExecContextDependecies() {
        return execContextDependecies;
    }

    public void setExecContextDependecies(List<Dependency> execContextDependecies) {
        this.execContextDependecies = execContextDependecies;
    }

    public void setClassExecContext(ClassContext klassContext){
        classExecContext = klassContext;
    }

    public ClassContext getClassExecContext() {
        return classExecContext;
    }
}
