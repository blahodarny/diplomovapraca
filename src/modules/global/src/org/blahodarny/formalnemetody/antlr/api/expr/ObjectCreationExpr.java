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

import org.blahodarny.formalnemetody.antlr.api.body.BodyDeclaration;
import org.blahodarny.formalnemetody.antlr.api.type.ClassOrInterfaceType;
import org.blahodarny.formalnemetody.antlr.api.type.Type;
import org.blahodarny.formalnemetody.antlr.api.visitor.GenericVisitor;
import org.blahodarny.formalnemetody.antlr.api.visitor.VoidVisitor;

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public final class ObjectCreationExpr extends Expression {

    private Expression scope;

    private ClassOrInterfaceType type;

    private List<Type> typeArgs;

    private List<Expression> args;

    private List<BodyDeclaration> anonymousClassBody;

    public ObjectCreationExpr() {
    }

    public ObjectCreationExpr(Expression scope, ClassOrInterfaceType type, List<Expression> args) {
        this.scope = scope;
        this.type = type;
        this.args = args;
    }

    public ObjectCreationExpr(int beginLine, int beginColumn, int endLine, int endColumn, Expression scope, ClassOrInterfaceType type, List<Type> typeArgs, List<Expression> args, List<BodyDeclaration> anonymousBody) {
        super(beginLine, beginColumn, endLine, endColumn);
        this.scope = scope;
        this.type = type;
        this.typeArgs = typeArgs;
        this.args = args;
        this.anonymousClassBody = anonymousBody;
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    public List<BodyDeclaration> getAnonymousClassBody() {
        return anonymousClassBody;
    }

    public List<Expression> getArgs() {
        return args;
    }

    public Expression getScope() {
        return scope;
    }

    public ClassOrInterfaceType getType() {
        return type;
    }

    public List<Type> getTypeArgs() {
        return typeArgs;
    }

    public void setAnonymousClassBody(List<BodyDeclaration> anonymousClassBody) {
        this.anonymousClassBody = anonymousClassBody;
    }

    public void setArgs(List<Expression> args) {
        this.args = args;
    }

    public void setScope(Expression scope) {
        this.scope = scope;
    }

    public void setType(ClassOrInterfaceType type) {
        this.type = type;
    }

    public void setTypeArgs(List<Type> typeArgs) {
        this.typeArgs = typeArgs;
    }

}
