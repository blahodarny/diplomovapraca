package org.blahodarny.formalnemetody.model.stmts;

import java.util.List;

public class IfExpression extends Expression{

    private List<Expression> thenExpressions;
    private List<Expression> elseExpressions;
    private List<String> conditionValues;

    public List<Expression> getThenExpressions() {
        return thenExpressions;
    }

    public void setThenExpressions(List<Expression> thenExpressions) {
        this.thenExpressions = thenExpressions;
    }

    public List<Expression> getElseExpressions() {
        return elseExpressions;
    }

    public void setElseExpressions(List<Expression> elseExpressions) {
        this.elseExpressions = elseExpressions;
    }

    public List<String> getConditionValues() {
        return conditionValues;
    }

    public void setConditionValues(List<String> conditionValues) {
        this.conditionValues = conditionValues;
    }
}
