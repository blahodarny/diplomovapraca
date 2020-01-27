package org.blahodarny.formalnemetody.model.stmts;

import java.util.List;

public class WhileExpression extends Expression {

    private List<Expression> expressions;
    private List<String> conditionValues;

    public List<Expression> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<Expression> expressions) {
        this.expressions = expressions;
    }

    public void addExpressions(List<Expression> expressions) {
        this.expressions = expressions;
    }

    public List<String> getConditionValues() {
        return conditionValues;
    }

    public void setConditionValues(List<String> conditionValues) {
        this.conditionValues = conditionValues;
    }
}
