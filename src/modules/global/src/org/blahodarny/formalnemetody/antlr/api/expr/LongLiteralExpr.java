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

/**
 * @author Julio Vilmar Gesser
 */
public class LongLiteralExpr extends StringLiteralExpr {

    private static final String UNSIGNED_MIN_VALUE = "9223372036854775808";

    protected static final String MIN_VALUE = "-" + UNSIGNED_MIN_VALUE + "L";

    public LongLiteralExpr() {
    }

    public LongLiteralExpr(String value) {
        super(value);
    }

    public LongLiteralExpr(int beginLine, int beginColumn, int endLine, int endColumn, String value) {
        super(beginLine, beginColumn, endLine, endColumn, value);
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    public final boolean isMinValue() {
        return value != null && //
                value.length() == 20 && //
                value.startsWith(UNSIGNED_MIN_VALUE) && //
                (value.charAt(19) == 'L' || value.charAt(19) == 'l');
    }
}
