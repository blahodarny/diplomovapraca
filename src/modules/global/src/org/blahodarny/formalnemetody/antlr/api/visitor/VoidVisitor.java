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
package org.blahodarny.formalnemetody.antlr.api.visitor;

import org.blahodarny.formalnemetody.antlr.api.BlockComment;
import org.blahodarny.formalnemetody.antlr.api.Comment;
import org.blahodarny.formalnemetody.antlr.api.CompilationUnit;
import org.blahodarny.formalnemetody.antlr.api.ImportDeclaration;
import org.blahodarny.formalnemetody.antlr.api.LineComment;
import org.blahodarny.formalnemetody.antlr.api.PackageDeclaration;
import org.blahodarny.formalnemetody.antlr.api.TypeParameter;
import org.blahodarny.formalnemetody.antlr.api.body.AnnotationDeclaration;
import org.blahodarny.formalnemetody.antlr.api.body.AnnotationMemberDeclaration;
import org.blahodarny.formalnemetody.antlr.api.body.CatchParameter;
import org.blahodarny.formalnemetody.antlr.api.body.ClassOrInterfaceDeclaration;
import org.blahodarny.formalnemetody.antlr.api.body.ConstructorDeclaration;
import org.blahodarny.formalnemetody.antlr.api.body.EmptyMemberDeclaration;
import org.blahodarny.formalnemetody.antlr.api.body.EmptyTypeDeclaration;
import org.blahodarny.formalnemetody.antlr.api.body.EnumConstantDeclaration;
import org.blahodarny.formalnemetody.antlr.api.body.EnumDeclaration;
import org.blahodarny.formalnemetody.antlr.api.body.FieldDeclaration;
import org.blahodarny.formalnemetody.antlr.api.body.InitializerDeclaration;
import org.blahodarny.formalnemetody.antlr.api.body.JavadocComment;
import org.blahodarny.formalnemetody.antlr.api.body.MethodDeclaration;
import org.blahodarny.formalnemetody.antlr.api.body.Parameter;
import org.blahodarny.formalnemetody.antlr.api.body.Resource;
import org.blahodarny.formalnemetody.antlr.api.body.VariableDeclarator;
import org.blahodarny.formalnemetody.antlr.api.body.VariableDeclaratorId;
import org.blahodarny.formalnemetody.antlr.api.expr.ArrayAccessExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.ArrayCreationExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.ArrayInitializerExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.AssignExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.BinaryExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.BooleanLiteralExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.CastExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.CharLiteralExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.ClassExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.ConditionalExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.DoubleLiteralExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.EnclosedExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.FieldAccessExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.InstanceOfExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.IntegerLiteralExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.IntegerLiteralMinValueExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.LambdaExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.LongLiteralExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.LongLiteralMinValueExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.MarkerAnnotationExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.MemberValuePair;
import org.blahodarny.formalnemetody.antlr.api.expr.MethodCallExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.MethodReferenceExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.NameExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.NormalAnnotationExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.NullLiteralExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.ObjectCreationExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.QualifiedNameExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.SingleMemberAnnotationExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.StringLiteralExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.SuperExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.ThisExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.UnaryExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.VariableDeclarationExpr;
import org.blahodarny.formalnemetody.antlr.api.stmt.AssertStmt;
import org.blahodarny.formalnemetody.antlr.api.stmt.BlockStmt;
import org.blahodarny.formalnemetody.antlr.api.stmt.BreakStmt;
import org.blahodarny.formalnemetody.antlr.api.stmt.CatchClause;
import org.blahodarny.formalnemetody.antlr.api.stmt.ContinueStmt;
import org.blahodarny.formalnemetody.antlr.api.stmt.DoStmt;
import org.blahodarny.formalnemetody.antlr.api.stmt.EmptyStmt;
import org.blahodarny.formalnemetody.antlr.api.stmt.ExplicitConstructorInvocationStmt;
import org.blahodarny.formalnemetody.antlr.api.stmt.ExpressionStmt;
import org.blahodarny.formalnemetody.antlr.api.stmt.ForStmt;
import org.blahodarny.formalnemetody.antlr.api.stmt.ForeachStmt;
import org.blahodarny.formalnemetody.antlr.api.stmt.IfStmt;
import org.blahodarny.formalnemetody.antlr.api.stmt.LabeledStmt;
import org.blahodarny.formalnemetody.antlr.api.stmt.ReturnStmt;
import org.blahodarny.formalnemetody.antlr.api.stmt.SwitchEntryStmt;
import org.blahodarny.formalnemetody.antlr.api.stmt.SwitchStmt;
import org.blahodarny.formalnemetody.antlr.api.stmt.SynchronizedStmt;
import org.blahodarny.formalnemetody.antlr.api.stmt.ThrowStmt;
import org.blahodarny.formalnemetody.antlr.api.stmt.TryStmt;
import org.blahodarny.formalnemetody.antlr.api.stmt.TypeDeclarationStmt;
import org.blahodarny.formalnemetody.antlr.api.stmt.WhileStmt;
import org.blahodarny.formalnemetody.antlr.api.type.ClassOrInterfaceType;
import org.blahodarny.formalnemetody.antlr.api.type.PrimitiveType;
import org.blahodarny.formalnemetody.antlr.api.type.ReferenceType;
import org.blahodarny.formalnemetody.antlr.api.type.VoidType;
import org.blahodarny.formalnemetody.antlr.api.type.WildcardType;

/**
 * @author Julio Vilmar Gesser
 */
public interface VoidVisitor<A> {

    //- Compilation Unit ----------------------------------

    public void visit(CompilationUnit n, A arg);

    public void visit(PackageDeclaration n, A arg);

    public void visit(ImportDeclaration n, A arg);

    public void visit(TypeParameter n, A arg);

    public void visit(LineComment n, A arg);

    public void visit(BlockComment n, A arg);

    //- Body ----------------------------------------------

    public void visit(ClassOrInterfaceDeclaration n, A arg);

    public void visit(Comment n, A arg);

    public void visit(EnumDeclaration n, A arg);

    public void visit(EmptyTypeDeclaration n, A arg);

    public void visit(EnumConstantDeclaration n, A arg);

    public void visit(AnnotationDeclaration n, A arg);

    public void visit(AnnotationMemberDeclaration n, A arg);

    public void visit(FieldDeclaration n, A arg);

    public void visit(VariableDeclarator n, A arg);

    public void visit(VariableDeclaratorId n, A arg);

    public void visit(ConstructorDeclaration n, A arg);

    public void visit(MethodDeclaration n, A arg);

    public void visit(Parameter n, A arg);

    public void visit(CatchParameter n, A arg);

    public void visit(Resource n, A arg);

    public void visit(EmptyMemberDeclaration n, A arg);

    public void visit(InitializerDeclaration n, A arg);

    public void visit(JavadocComment n, A arg);

    //- Type ----------------------------------------------

    public void visit(ClassOrInterfaceType n, A arg);

    public void visit(PrimitiveType n, A arg);

    public void visit(ReferenceType n, A arg);

    public void visit(VoidType n, A arg);

    public void visit(WildcardType n, A arg);

    //- Expression ----------------------------------------

    public void visit(ArrayAccessExpr n, A arg);

    public void visit(ArrayCreationExpr n, A arg);

    public void visit(ArrayInitializerExpr n, A arg);

    public void visit(AssignExpr n, A arg);

    public void visit(BinaryExpr n, A arg);

    public void visit(CastExpr n, A arg);

    public void visit(ClassExpr n, A arg);

    public void visit(ConditionalExpr n, A arg);

    public void visit(EnclosedExpr n, A arg);

    public void visit(FieldAccessExpr n, A arg);

    public void visit(InstanceOfExpr n, A arg);

    public void visit(StringLiteralExpr n, A arg);

    public void visit(IntegerLiteralExpr n, A arg);

    public void visit(LongLiteralExpr n, A arg);

    public void visit(IntegerLiteralMinValueExpr n, A arg);

    public void visit(LongLiteralMinValueExpr n, A arg);

    public void visit(CharLiteralExpr n, A arg);

    public void visit(DoubleLiteralExpr n, A arg);

    public void visit(BooleanLiteralExpr n, A arg);

    public void visit(NullLiteralExpr n, A arg);

    public void visit(LambdaExpr n, A arg);

    public void visit(MethodCallExpr n, A arg);

    public void visit(NameExpr n, A arg);

    public void visit(ObjectCreationExpr n, A arg);

    public void visit(QualifiedNameExpr n, A arg);

    public void visit(ThisExpr n, A arg);

    public void visit(SuperExpr n, A arg);

    public void visit(UnaryExpr n, A arg);

    public void visit(VariableDeclarationExpr n, A arg);

    public void visit(MarkerAnnotationExpr n, A arg);

    public void visit(SingleMemberAnnotationExpr n, A arg);

    public void visit(NormalAnnotationExpr n, A arg);

    public void visit(MemberValuePair n, A arg);

    public void visit(MethodReferenceExpr n, A arg);

    //- Statements ----------------------------------------

    public void visit(ExplicitConstructorInvocationStmt n, A arg);

    public void visit(TypeDeclarationStmt n, A arg);

    public void visit(AssertStmt n, A arg);

    public void visit(BlockStmt n, A arg);

    public void visit(LabeledStmt n, A arg);

    public void visit(EmptyStmt n, A arg);

    public void visit(ExpressionStmt n, A arg);

    public void visit(SwitchStmt n, A arg);

    public void visit(SwitchEntryStmt n, A arg);

    public void visit(BreakStmt n, A arg);

    public void visit(ReturnStmt n, A arg);

    public void visit(IfStmt n, A arg);

    public void visit(WhileStmt n, A arg);

    public void visit(ContinueStmt n, A arg);

    public void visit(DoStmt n, A arg);

    public void visit(ForeachStmt n, A arg);

    public void visit(ForStmt n, A arg);

    public void visit(ThrowStmt n, A arg);

    public void visit(SynchronizedStmt n, A arg);

    public void visit(TryStmt n, A arg);

    public void visit(CatchClause n, A arg);

}
