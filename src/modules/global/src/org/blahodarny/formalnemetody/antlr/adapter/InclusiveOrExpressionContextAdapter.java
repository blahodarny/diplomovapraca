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
import org.blahodarny.formalnemetody.antlr.api.expr.BinaryExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.Expression;

public class InclusiveOrExpressionContextAdapter implements Adapter<Expression, Java7Parser.InclusiveOrExpressionContext> {
    public Expression adapt(Java7Parser.InclusiveOrExpressionContext context, AdapterParameters adapterParameters) {
        return AdapterUtil.handleExpression(Adapters.getExclusiveOrExpressionContextAdapter(), context.exclusiveOrExpression(), BinaryExpr.Operator.binOr, adapterParameters);
    }
}
