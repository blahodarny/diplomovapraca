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
import org.blahodarny.formalnemetody.antlr.api.body.BodyDeclaration;
import org.blahodarny.formalnemetody.antlr.api.body.EmptyMemberDeclaration;

public class InterfaceBodyDeclarationContextAdapter implements Adapter<BodyDeclaration, Java7Parser.InterfaceBodyDeclarationContext> {
    public BodyDeclaration adapt(Java7Parser.InterfaceBodyDeclarationContext context, AdapterParameters adapterParameters) {

        /*
        interfaceBodyDeclaration
            :   interfaceFieldDeclaration
            |   interfaceMethodDeclaration
            |   interfaceDeclaration
            |   classDeclaration
            |   SEMI
            ;
        */

        if (context.interfaceFieldDeclaration() != null) {
            return Adapters.getInterfaceFieldDeclarationContextAdapter().adapt(context.interfaceFieldDeclaration(), adapterParameters);
        } else if (context.interfaceMethodDeclaration() != null) {
            return Adapters.getInterfaceMethodDeclarationContextAdapter().adapt(context.interfaceMethodDeclaration(), adapterParameters);
        } else if (context.interfaceDeclaration() != null) {
            return Adapters.getInterfaceDeclarationContextAdapter().adapt(context.interfaceDeclaration(), adapterParameters);
        } else if (context.classDeclaration() != null) {
            return Adapters.getClassDeclarationContextAdapter().adapt(context.classDeclaration(), adapterParameters);
        } else if (context.defaultInterfaceMethodDeclaration() != null) {
            return Adapters.getDefaultInterfaceMethodDeclarationContextAdapter().adapt(context.defaultInterfaceMethodDeclaration(), adapterParameters);
        } else if (context.SEMI() != null) {
            BodyDeclaration bodyDeclaration = new EmptyMemberDeclaration();
            AdapterUtil.setComments(bodyDeclaration, context, adapterParameters);
            AdapterUtil.setPosition(bodyDeclaration, context);
            return bodyDeclaration;
        }

        return null;
    }
}
