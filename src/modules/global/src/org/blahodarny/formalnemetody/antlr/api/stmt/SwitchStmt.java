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
public final class SwitchStmt extends Statement {

    private Expression selector;

    private List<SwitchEntryStmt> entries;

    public SwitchStmt() {
    }

    public SwitchStmt(Expression selector, List<SwitchEntryStmt> entries) {
        this.selector = selector;
        this.entries = entries;
    }

    public SwitchStmt(int beginLine, int beginColumn, int endLine, int endColumn, Expression selector, List<SwitchEntryStmt> entries) {
        super(beginLine, beginColumn, endLine, endColumn);
        this.selector = selector;
        this.entries = entries;
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    public List<SwitchEntryStmt> getEntries() {
        return entries;
    }

    public Expression getSelector() {
        return selector;
    }

    public void setEntries(List<SwitchEntryStmt> entries) {
        this.entries = entries;
    }

    public void setSelector(Expression selector) {
        this.selector = selector;
    }
}
