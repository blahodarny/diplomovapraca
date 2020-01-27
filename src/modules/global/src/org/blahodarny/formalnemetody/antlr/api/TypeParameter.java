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
package org.blahodarny.formalnemetody.antlr.api;

import org.blahodarny.formalnemetody.antlr.api.type.ClassOrInterfaceType;
import org.blahodarny.formalnemetody.antlr.api.visitor.GenericVisitor;
import org.blahodarny.formalnemetody.antlr.api.visitor.VoidVisitor;

import java.util.List;

/**
 * This class represents the declaration of a genetics argument.
 * The TypeParameter is constructed following the syntax:<br>
 *
 * @author Julio Vilmar Gesser
 */
public final class TypeParameter extends Node {

    private String name;

    private List<ClassOrInterfaceType> typeBound;

    public TypeParameter() {
    }

    public TypeParameter(String name, List<ClassOrInterfaceType> typeBound) {
        this.name = name;
        this.typeBound = typeBound;
    }

    public TypeParameter(int beginLine, int beginColumn, int endLine, int endColumn, String name, List<ClassOrInterfaceType> typeBound) {
        super(beginLine, beginColumn, endLine, endColumn);
        this.name = name;
        this.typeBound = typeBound;
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    /**
     * Return the name of the paramenter.
     *
     * @return the name of the paramenter
     */
    public String getName() {
        return name;
    }

    /**
     * Return the list of {@link ClassOrInterfaceType} that this parameter
     * extends. Return <code>null</code> null if there are no type.
     *
     * @return list of types that this paramente extends or <code>null</code>
     */
    public List<ClassOrInterfaceType> getTypeBound() {
        return typeBound;
    }

    /**
     * Sets the name of this type parameter.
     *
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the list o types.
     *
     * @param typeBound
     *            the typeBound to set
     */
    public void setTypeBound(List<ClassOrInterfaceType> typeBound) {
        this.typeBound = typeBound;
    }

}
