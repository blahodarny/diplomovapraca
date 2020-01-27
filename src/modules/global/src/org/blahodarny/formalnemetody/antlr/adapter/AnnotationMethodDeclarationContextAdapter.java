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
package org.blahodarny.formalnemetody.antlr.adapter;

import org.blahodarny.formalnemetody.antlr.Java7Parser;
import org.blahodarny.formalnemetody.antlr.api.body.AnnotationMemberDeclaration;
import org.blahodarny.formalnemetody.antlr.api.body.BodyDeclaration;

public class AnnotationMethodDeclarationContextAdapter implements Adapter<BodyDeclaration, Java7Parser.AnnotationMethodDeclarationContext> {
    public BodyDeclaration adapt(Java7Parser.AnnotationMethodDeclarationContext context, AdapterParameters adapterParameters) {
        /*
            annotationMethodDeclaration
                :    modifiers type Identifier LPAREN RPAREN (DEFAULT elementValue)? SEMI
                ;
         */

        AnnotationMemberDeclaration annotationMemberDeclaration = new AnnotationMemberDeclaration();
        AdapterUtil.setModifiers(context.modifiers(), annotationMemberDeclaration, adapterParameters);
        AdapterUtil.setComments(annotationMemberDeclaration, context, adapterParameters);
        AdapterUtil.setPosition(annotationMemberDeclaration, context);
        annotationMemberDeclaration.setType(Adapters.getTypeContextAdapter().adapt(context.type(), adapterParameters));
        annotationMemberDeclaration.setName(context.Identifier().getText());

        if (context.elementValue() != null) {
            annotationMemberDeclaration.setDefaultValue(Adapters.getElementValueContextAdapter().adapt(context.elementValue(), adapterParameters));
        }

        return annotationMemberDeclaration;
    }
}
