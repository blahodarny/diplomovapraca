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

import org.blahodarny.formalnemetody.antlr.api.expr.NameExpr;
import org.blahodarny.formalnemetody.antlr.api.visitor.GenericVisitor;
import org.blahodarny.formalnemetody.antlr.api.visitor.VoidVisitor;

/**
 * This class represents a import declaration. Imports are optional for the
 * {@link CompilationUnit}.
 * The ImportDeclaration is constructed following the syntax:<br>
 *
 * @author Julio Vilmar Gesser
 */
public final class ImportDeclaration extends Node {

    private NameExpr name;

    private boolean static_;

    private boolean asterisk;

    public ImportDeclaration() {
    }

    public ImportDeclaration(NameExpr name, boolean isStatic, boolean isAsterisk) {
        this.name = name;
        this.static_ = isStatic;
        this.asterisk = isAsterisk;
    }

    public ImportDeclaration(int beginLine, int beginColumn, int endLine, int endColumn, NameExpr name, boolean isStatic, boolean isAsterisk) {
        super(beginLine, beginColumn, endLine, endColumn);
        this.name = name;
        this.static_ = isStatic;
        this.asterisk = isAsterisk;
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
     * Retrieves the name of the import.
     *
     * @return the name of the import
     */
    public NameExpr getName() {
        return name;
    }

    /**
     * Return if the import ends with "*".
     *
     * @return <code>true</code> if the import ends with "*", <code>false</code>
     *         otherwise
     */
    public boolean isAsterisk() {
        return asterisk;
    }

    /**
     * Return if the import is static.
     *
     * @return <code>true</code> if the import is static, <code>false</code>
     *         otherwise
     */
    public boolean isStatic() {
        return static_;
    }

    /**
     * Sets if this import is asterisk.
     *
     * @param asterisk
     *            <code>true</code> if this import is asterisk
     */
    public void setAsterisk(boolean asterisk) {
        this.asterisk = asterisk;
    }

    /**
     * Sets the name this import.
     *
     * @param name
     *            the name to set
     */
    public void setName(NameExpr name) {
        this.name = name;
    }

    /**
     * Sets if this import is static.
     *
     * @param static_
     *            <code>true</code> if this import is static
     */
    public void setStatic(boolean static_) {
        this.static_ = static_;
    }

}
