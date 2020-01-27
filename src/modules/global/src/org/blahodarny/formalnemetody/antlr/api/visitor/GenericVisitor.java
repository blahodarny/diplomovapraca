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
public interface GenericVisitor<R, A> {

    //- Compilation Unit ----------------------------------

    public R visit(CompilationUnit n, A arg);

    public R visit(PackageDeclaration n, A arg);

    public R visit(ImportDeclaration n, A arg);

    public R visit(TypeParameter n, A arg);

    //- Body ----------------------------------------------

    public R visit(ClassOrInterfaceDeclaration n, A arg);

    //public R visit(Comment n, A arg);

    public R visit(EnumDeclaration n, A arg);

    public R visit(EmptyTypeDeclaration n, A arg);

    public R visit(EnumConstantDeclaration n, A arg);

    public R visit(AnnotationDeclaration n, A arg);

    public R visit(AnnotationMemberDeclaration n, A arg);

    public R visit(FieldDeclaration n, A arg);

    public R visit(VariableDeclarator n, A arg);

    public R visit(VariableDeclaratorId n, A arg);

    public R visit(ConstructorDeclaration n, A arg);

    public R visit(MethodDeclaration n, A arg);

    public R visit(Parameter n, A arg);

    public R visit(CatchParameter n, A arg);

    public R visit(Resource n, A arg);

    public R visit(EmptyMemberDeclaration n, A arg);

    public R visit(InitializerDeclaration n, A arg);

    //- Type ----------------------------------------------

    public R visit(ClassOrInterfaceType n, A arg);

    public R visit(PrimitiveType n, A arg);

    public R visit(ReferenceType n, A arg);

    public R visit(VoidType n, A arg);

    public R visit(WildcardType n, A arg);

    //- Expression ----------------------------------------

    public R visit(ArrayAccessExpr n, A arg);

    public R visit(ArrayCreationExpr n, A arg);

    public R visit(ArrayInitializerExpr n, A arg);

    public R visit(AssignExpr n, A arg);

    public R visit(BinaryExpr n, A arg);

    public R visit(CastExpr n, A arg);

    public R visit(ClassExpr n, A arg);

    public R visit(ConditionalExpr n, A arg);

    public R visit(EnclosedExpr n, A arg);

    public R visit(FieldAccessExpr n, A arg);

    public R visit(InstanceOfExpr n, A arg);

    public R visit(StringLiteralExpr n, A arg);

    public R visit(IntegerLiteralExpr n, A arg);

    public R visit(LongLiteralExpr n, A arg);

    public R visit(IntegerLiteralMinValueExpr n, A arg);

    public R visit(LongLiteralMinValueExpr n, A arg);

    public R visit(CharLiteralExpr n, A arg);

    public R visit(DoubleLiteralExpr n, A arg);

    public R visit(BooleanLiteralExpr n, A arg);

    public R visit(NullLiteralExpr n, A arg);

    public R visit(LambdaExpr n, A arg);

    public R visit(MethodCallExpr n, A arg);

    public R visit(NameExpr n, A arg);

    public R visit(ObjectCreationExpr n, A arg);

    public R visit(QualifiedNameExpr n, A arg);

    public R visit(ThisExpr n, A arg);

    public R visit(SuperExpr n, A arg);

    public R visit(UnaryExpr n, A arg);

    public R visit(VariableDeclarationExpr n, A arg);

    public R visit(MarkerAnnotationExpr n, A arg);

    public R visit(SingleMemberAnnotationExpr n, A arg);

    public R visit(NormalAnnotationExpr n, A arg);

    public R visit(MemberValuePair n, A arg);

    public R visit(MethodReferenceExpr n, A arg);

    //- Statements ----------------------------------------

    public R visit(ExplicitConstructorInvocationStmt n, A arg);

    public R visit(TypeDeclarationStmt n, A arg);

    public R visit(AssertStmt n, A arg);

    public R visit(BlockStmt n, A arg);

    public R visit(LabeledStmt n, A arg);

    public R visit(EmptyStmt n, A arg);

    public R visit(ExpressionStmt n, A arg);

    public R visit(SwitchStmt n, A arg);

    public R visit(SwitchEntryStmt n, A arg);

    public R visit(BreakStmt n, A arg);

    public R visit(ReturnStmt n, A arg);

    public R visit(IfStmt n, A arg);

    public R visit(WhileStmt n, A arg);

    public R visit(ContinueStmt n, A arg);

    public R visit(DoStmt n, A arg);

    public R visit(ForeachStmt n, A arg);

    public R visit(ForStmt n, A arg);

    public R visit(ThrowStmt n, A arg);

    public R visit(SynchronizedStmt n, A arg);

    public R visit(TryStmt n, A arg);

    public R visit(CatchClause n, A arg);

}
