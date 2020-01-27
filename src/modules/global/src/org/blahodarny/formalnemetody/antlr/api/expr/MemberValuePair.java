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
package org.blahodarny.formalnemetody.antlr.api.expr;

import org.blahodarny.formalnemetody.antlr.api.Node;
import org.blahodarny.formalnemetody.antlr.api.visitor.GenericVisitor;
import org.blahodarny.formalnemetody.antlr.api.visitor.VoidVisitor;

/**
 * @author Julio Vilmar Gesser
 */
public final class MemberValuePair extends Node {

    private String name;

    private Expression value;

    public MemberValuePair() {
    }

    public MemberValuePair(String name, Expression value) {
        this.name = name;
        this.value = value;
    }

    public MemberValuePair(int beginLine, int beginColumn, int endLine, int endColumn, String name, Expression value) {
        super(beginLine, beginColumn, endLine, endColumn);
        this.name = name;
        this.value = value;
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    public String getName() {
        return name;
    }

    public Expression getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Expression value) {
        this.value = value;
    }

}
