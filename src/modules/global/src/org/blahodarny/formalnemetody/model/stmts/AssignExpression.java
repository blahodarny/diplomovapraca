package org.blahodarny.formalnemetody.model.stmts;

import java.util.List;

public class AssignExpression extends Expression {

    private String target;
    private List<String> values;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
