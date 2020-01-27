package org.blahodarny.formalnemetody.model.stmts;

import java.util.List;

public class MethodCallExpression extends Expression {

    private String name;
    private List<String> params;


    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
