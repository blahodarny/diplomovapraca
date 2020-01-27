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

import java.util.List;

/**
 * @author Julio Vilmar Gesser
 */
public abstract class BodyDeclaration extends Node {

    private JavadocComment javaDoc;

    private List<AnnotationExpr> annotations;

    public BodyDeclaration() {
    }

    public BodyDeclaration(List<AnnotationExpr> annotations, JavadocComment javaDoc) {
        this.javaDoc = javaDoc;
        this.annotations = annotations;
    }

    public BodyDeclaration(int beginLine, int beginColumn, int endLine, int endColumn, List<AnnotationExpr> annotations, JavadocComment javaDoc) {
        super(beginLine, beginColumn, endLine, endColumn);
        this.javaDoc = javaDoc;
        this.annotations = annotations;
    }

    public final JavadocComment getJavaDoc() {
        return javaDoc;
    }

    public final List<AnnotationExpr> getAnnotations() {
        return annotations;
    }

    public final void setJavaDoc(JavadocComment javaDoc) {
        this.javaDoc = javaDoc;
    }

    public final void setAnnotations(List<AnnotationExpr> annotations) {
        this.annotations = annotations;
    }

}
