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

import org.blahodarny.formalnemetody.antlr.api.visitor.GenericVisitor;
import org.blahodarny.formalnemetody.antlr.api.visitor.VoidVisitor;

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public final class NormalAnnotationExpr extends AnnotationExpr {

    private List<MemberValuePair> pairs;

    public NormalAnnotationExpr() {
    }

    public NormalAnnotationExpr(NameExpr name, List<MemberValuePair> pairs) {
        this.name = name;
        this.pairs = pairs;
    }

    public NormalAnnotationExpr(int beginLine, int beginColumn, int endLine, int endColumn, NameExpr name, List<MemberValuePair> pairs) {
        super(beginLine, beginColumn, endLine, endColumn);
        this.name = name;
        this.pairs = pairs;
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    public List<MemberValuePair> getPairs() {
        return pairs;
    }

    public void setPairs(List<MemberValuePair> pairs) {
        this.pairs = pairs;
    }

}
