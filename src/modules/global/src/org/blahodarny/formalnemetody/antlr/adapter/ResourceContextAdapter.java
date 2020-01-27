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
import org.blahodarny.formalnemetody.antlr.api.body.Resource;
import org.blahodarny.formalnemetody.antlr.api.body.VariableDeclaratorId;

public class ResourceContextAdapter implements Adapter<Resource, Java7Parser.ResourceContext> {
    public Resource adapt(Java7Parser.ResourceContext context, AdapterParameters adapterParameters) {
        /*
            resource
            :    variableModifiers? type Identifier EQ expression                       // {VariableModifier} ReferenceType VariableDeclaratorId = Expression
            ;
        */

        Resource resource = new Resource();
        AdapterUtil.setComments(resource, context, adapterParameters);
        AdapterUtil.setPosition(resource, context);

        if (context.variableModifiers() != null) {
            AdapterUtil.setVariableModifiers(context.variableModifiers(), resource, adapterParameters);
        }

        resource.setType(Adapters.getTypeContextAdapter().adapt(context.type(), adapterParameters));

        VariableDeclaratorId variableDeclaratorId = new VariableDeclaratorId();
        variableDeclaratorId.setName(context.Identifier().getText());
        resource.setId(variableDeclaratorId);

        resource.setExpression(Adapters.getExpressionContextAdapter().adapt(context.expression(), adapterParameters));

        return resource;
    }
}
