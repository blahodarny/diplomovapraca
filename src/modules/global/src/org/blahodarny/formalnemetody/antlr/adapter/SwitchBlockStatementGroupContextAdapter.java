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
import org.blahodarny.formalnemetody.antlr.api.stmt.Statement;
import org.blahodarny.formalnemetody.antlr.api.stmt.SwitchEntryStmt;

import java.util.LinkedList;
import java.util.List;

public class SwitchBlockStatementGroupContextAdapter implements Adapter<SwitchEntryStmt, Java7Parser.SwitchBlockStatementGroupContext> {
    public SwitchEntryStmt adapt(Java7Parser.SwitchBlockStatementGroupContext context, AdapterParameters adapterParameters) {

        /*
            switchBlockStatementGroup
                :
                    switchLabel
                    (blockStatement
                    )*
                ;

            switchLabel
                :   CASE expression COLON
                |   DEFAULT COLON
                ;
         */

        SwitchEntryStmt switchEntryStmt = new SwitchEntryStmt();
        AdapterUtil.setComments(switchEntryStmt, context, adapterParameters);
        AdapterUtil.setPosition(switchEntryStmt, context);

        if (context.blockStatement() != null && context.blockStatement().size() > 0) {
            List<Statement> blockStmtList = new LinkedList<Statement>();
            for (Java7Parser.BlockStatementContext blockStatementContext : context.blockStatement()) {
                blockStmtList.add(Adapters.getBlockStatementContextAdapter().adapt(blockStatementContext, adapterParameters));
            }
            switchEntryStmt.setStmts(blockStmtList);
        }

        if (context.switchLabel().CASE() != null) {
            switchEntryStmt.setLabel(Adapters.getExpressionContextAdapter().adapt(context.switchLabel().expression(), adapterParameters));
        } else if (context.switchLabel().DEFAULT() != null) {
            // Explicitly setting these null for readability
            switchEntryStmt.setLabel(null);
            switchEntryStmt.setStmts(null);
        }

        return switchEntryStmt;
    }
}
