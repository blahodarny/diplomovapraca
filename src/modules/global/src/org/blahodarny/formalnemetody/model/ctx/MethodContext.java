package org.blahodarny.formalnemetody.model.ctx;

import org.blahodarny.formalnemetody.antlr.api.stmt.Statement;
import org.blahodarny.formalnemetody.model.Dependency;
import org.blahodarny.formalnemetody.model.values.SecurityAnnotation;
import org.blahodarny.formalnemetody.model.values.Var;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MethodContext extends Context {

    private List<Statement> bodyStatements;
    private List<String> params;

    public MethodContext(String name, Context parent) {
        super(name, parent);
        params = new ArrayList<>();
    }

    public List<Statement> getBodyStatements() {
        return bodyStatements;
    }

    public void setBodyStatements(List<Statement> bodyStatements) {
        this.bodyStatements = bodyStatements;
    }

    public void addParam(String name){
        params.add(name);
    }

    public String getParamByIndex(int index){
        return params.get(index);
    }

    public BodyContext createBlockContext(Context execContext, Map<String, SecurityAnnotation> params, Map<String, List<Dependency>> paramsDependecies){
        BodyContext bodyContext = new BodyContext(name+"_execution" , execContext);
        for(Map.Entry<String, SecurityAnnotation> entry : params.entrySet()){
            Var var = getValue(entry.getKey());
            Var execVar = new Var(var.getName(), var.getType(), entry.getValue());
            execVar.setExecContextDependecies(paramsDependecies.get(entry.getKey()));
            bodyContext.addValue(execVar);
        }
        return bodyContext;
    }
}
