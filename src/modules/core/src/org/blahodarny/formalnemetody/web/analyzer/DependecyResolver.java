package org.blahodarny.formalnemetody.web.analyzer;

import org.blahodarny.formalnemetody.model.Dependency;
import org.blahodarny.formalnemetody.model.values.SecurityAnnotation;
import org.blahodarny.formalnemetody.model.values.Var;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class DependecyResolver {

    private List<Dependency> deps;
    private String response;

    public DependecyResolver(List<Dependency> deps) {
        this.deps = deps;
        response = "";
    }

    public String analyze() {
        for (Dependency dep : deps) {
            Var var = dep.getVariable();
            if (var.getSecurityAnnotation() != null) {
                walkDeps(var.getSecurityAnnotation(), var.getName(), dep);
            }
        }
        return response;
    }

    private void walkDeps(SecurityAnnotation annotation, String name, Dependency dependency) {
        Set<Dependency> innerDeps = dependency.getSubDependecies();
        List<Dependency> execDeps = dependency.getVariable().getExecContextDependecies();
        if (innerDeps != null) {
            for (Dependency innerDep : innerDeps) {
                SecurityAnnotation innerAnno = innerDep.getVariable().getSecurityAnnotation();
                if (annotation.isLessRestrictive(innerAnno)) {
                    String resp = "INFORMATION LEAK: \nFrom var: " + innerDep.getVariable().getName() + " - " + Optional.ofNullable(innerAnno).map(Objects::toString).orElse("") + "\nto var: " + name + " - " + annotation.toString() + "\n\n";
                    if (!response.contains(resp)) {
                        response = response + "" + resp;
                    }
                }
                walkDeps(annotation, name, innerDep);
            }
        }
        if (execDeps != null) {
            for (Dependency execDep : execDeps) {
                SecurityAnnotation innerAnno = execDep.getVariable().getSecurityAnnotation();
                if (annotation.isLessRestrictive(innerAnno)) {
                    String resp = "INFORMATION LEAK: \nFrom var: " + execDep.getVariable().getName() + " - " +  Optional.ofNullable(innerAnno).map(Objects::toString).orElse("")  + "\nto var: " + name + " - " + annotation.toString() + "\n\n";
                    if (!response.contains(resp)) {
                        response = response + "" + resp;
                    }
                }
                walkDeps(annotation, name, execDep);
            }
        }
    }
}
