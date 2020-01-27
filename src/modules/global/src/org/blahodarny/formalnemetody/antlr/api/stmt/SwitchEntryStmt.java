/*
 * Copyright (C) 2015 Julio Vilmar Gesser and Mike DeHaan
 *
 * This file is part of antlr-java-parser.
 *
 * antlr-java-parser is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * antlr-java-parser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with antlr-java-parser.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.blahodarny.formalnemetody.antlr.api.stmt;

import org.blahodarny.formalnemetody.antlr.api.expr.Expression;
import org.blahodarny.formalnemetody.antlr.api.visitor.GenericVisitor;
import org.blahodarny.formalnemetody.antlr.api.visitor.VoidVisitor;

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public final class SwitchEntryStmt extends Statement {

    private Expression label;

    private List<Statement> stmts;

    public SwitchEntryStmt() {
    }

    public SwitchEntryStmt(Expression label, List<Statement> stmts) {
        this.label = label;
        this.stmts = stmts;
    }

    public SwitchEntryStmt(int beginLine, int beginColumn, int endLine, int endColumn, Expression label, List<Statement> stmts) {
        super(beginLine, beginColumn, endLine, endColumn);
        this.label = label;
        this.stmts = stmts;
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    public Expression getLabel() {
        return label;
    }

    public List<Statement> getStmts() {
        return stmts;
    }

    public void setLabel(Expression label) {
        this.label = label;
    }

    public void setStmts(List<Statement> stmts) {
        this.stmts = stmts;
    }
}
