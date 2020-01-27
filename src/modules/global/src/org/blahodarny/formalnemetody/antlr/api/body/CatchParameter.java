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
package org.blahodarny.formalnemetody.antlr.api.body;

import org.blahodarny.formalnemetody.antlr.api.Node;
import org.blahodarny.formalnemetody.antlr.api.expr.AnnotationExpr;
import org.blahodarny.formalnemetody.antlr.api.type.Type;
import org.blahodarny.formalnemetody.antlr.api.visitor.GenericVisitor;
import org.blahodarny.formalnemetody.antlr.api.visitor.VoidVisitor;

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public final class CatchParameter extends Node {

    private int modifiers;

    private List<AnnotationExpr> annotations;

    private List<Type> typeList;

    private VariableDeclaratorId id;

    public CatchParameter() {
    }

    public CatchParameter(List<Type> typeList, VariableDeclaratorId id) {
        this.typeList = typeList;
        this.id = id;
    }

    public CatchParameter(int modifiers, List<Type> typeList, VariableDeclaratorId id) {
        this.modifiers = modifiers;
        this.typeList = typeList;
        this.id = id;
    }

    public CatchParameter(int beginLine, int beginColumn, int endLine, int endColumn, int modifiers, List<AnnotationExpr> annotations, List<Type> typeList, VariableDeclaratorId id) {
        super(beginLine, beginColumn, endLine, endColumn);
        this.modifiers = modifiers;
        this.annotations = annotations;
        this.typeList = typeList;
        this.id = id;
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    public List<AnnotationExpr> getAnnotations() {
        return annotations;
    }

    public VariableDeclaratorId getId() {
        return id;
    }

    /**
     * Return the modifiers of this parameter declaration.
     *
     * @see ModifierSet
     * @return modifiers
     */
    public int getModifiers() {
        return modifiers;
    }

    public List<Type> getTypeList() {
        return typeList;
    }

    public void setAnnotations(List<AnnotationExpr> annotations) {
        this.annotations = annotations;
    }

    public void setId(VariableDeclaratorId id) {
        this.id = id;
    }

    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    public void setTypeList(List<Type> typeList) {
        this.typeList = typeList;
    }
}
