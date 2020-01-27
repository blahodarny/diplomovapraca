package org.blahodarny.formalnemetody.web.analyzer;

import org.blahodarny.formalnemetody.antlr.api.CompilationUnit;
import org.blahodarny.formalnemetody.antlr.api.body.*;
import org.blahodarny.formalnemetody.antlr.api.expr.*;
import org.blahodarny.formalnemetody.antlr.api.stmt.*;
import org.blahodarny.formalnemetody.model.Dependency;
import org.blahodarny.formalnemetody.model.ctx.BodyContext;
import org.blahodarny.formalnemetody.model.ctx.ClassContext;
import org.blahodarny.formalnemetody.model.ctx.Context;
import org.blahodarny.formalnemetody.model.ctx.MethodContext;
import org.blahodarny.formalnemetody.model.values.SecurityAnnotation;
import org.blahodarny.formalnemetody.model.values.Var;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class OldFlowAnalyzer {

    private CompilationUnit compilationUnit = null;
    private List<ClassContext> klassContexts;
    private Set<Dependency> dependecies;

    public OldFlowAnalyzer(CompilationUnit compilationUnit) {
        this.compilationUnit = compilationUnit;
        this.dependecies = new LinkedHashSet<>();
        this.klassContexts = new ArrayList<>();
    }

    public void analyze(){
        List<AnnotationDeclaration> annotations = getAnnotattionDeclaration(compilationUnit);
        List<ClassOrInterfaceDeclaration> classes = getClasses(compilationUnit);

        for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : classes) {
            klassContexts.add(createClassContext(classOrInterfaceDeclaration));
        }

        processMainClass();
        System.out.println();
    }

    private void processMainClass() {
        ClassContext mainClass = getMainMethodContext(klassContexts);
        MethodContext mainContext = mainClass.getMethod("main");

        for (Statement statement : mainContext.getBodyStatements()) {
            proccessStatement(statement, mainContext, null, null);
        }
        System.out.println();
    }
    /*
    private void proccessStatement(Statement statement, Context context) {
        if (statement instanceof BlockStmt) {
            for (Statement s : ((BlockStmt) statement).getStmts()) {
                proccessStatement(s, context);
            }
        } else if (statement instanceof ExpressionStmt) {
            processExpression(((ExpressionStmt) statement).getExpression(), context);
        } else if (statement instanceof WhileStmt) {
            List<String> conditionVars = processExpression(((WhileStmt) statement).getCondition(), context);
            List<Var> dependeantVals = conditionVars.stream()
                    .map(context::getValue)
                    .collect(Collectors.toList());
            ExpressionContext whileContext = new ExpressionContext("while", context);
            whileContext.setDependantVals(dependeantVals);
            proccessStatement(((WhileStmt) statement).getBody(), whileContext);
        } else if (statement instanceof IfStmt) {
            List<String> conditionVars = processExpression(((IfStmt) statement).getCondition(), context);
            List<Var> dependeantVals = conditionVars.stream()
                    .map(context::getValue)
                    .collect(Collectors.toList());
            ExpressionContext whileContext = new ExpressionContext("while", context);
            whileContext.setDependantVals(dependeantVals);
            proccessStatement(((IfStmt) statement).getThenStmt(), whileContext);
            proccessStatement(((IfStmt) statement).getElseStmt(), whileContext);
        }
    }
    private List<String> processExpression(Expression expression, Context context) {
        List<String> vars = new ArrayList<>();
        if (expression instanceof LiteralExpr) {
            return new ArrayList<>();
        } else if (expression instanceof NameExpr) {
            vars.add(((NameExpr) expression).getName());
            return vars;
        } else if (expression instanceof BinaryExpr) {
            vars.addAll(processExpression(((BinaryExpr) expression).getLeft(), context));
            vars.addAll(processExpression(((BinaryExpr) expression).getRight(), context));
            return vars;
        } else if (expression instanceof UnaryExpr) {
            vars.addAll(processExpression(((UnaryExpr) expression).getExpr(), context));
            return vars;
        } else if (expression instanceof VariableDeclarationExpr) {
            String type = ((VariableDeclarationExpr) expression).getType().toString();
            SecurityAnnotation annotation = getAnnotation(((VariableDeclarationExpr) expression).getAnnotations());
            for (VariableDeclarator expr : ((VariableDeclarationExpr) expression).getVars()) {
                Var val = new Var(expr.getId().getName(), type, annotation);
                context.addValue(val);
            }
            return vars;
        } else if (expression instanceof AssignExpr) {
            vars.addAll(processExpression(((AssignExpr) expression).getValue(), context));
            List<String> targetVars = new ArrayList<>(processExpression(((AssignExpr) expression).getTarget(), context));
            //todo skus pozeriet ci values neporusuje target
        } else if (expression instanceof MethodCallExpr){
        } else if (expression instanceof ObjectCreationExpr){
        } else if (expression instanceof ConditionalExpr){
        }
        return vars;
    }
     */

    private ClassContext createClassContext(ClassOrInterfaceDeclaration klass) {
        ClassContext context = new ClassContext(klass.getName(), null);
        List<FieldDeclaration> fields = getFieldDeclarations(klass);
        for (FieldDeclaration fieldDeclaration : Optional.ofNullable(fields).orElse(new ArrayList<>())) {
            String type = fieldDeclaration.getType().toString();
            for (VariableDeclarator declarator : Optional.ofNullable(fieldDeclaration.getVariables()).orElse(new ArrayList<>())) {
                Var var = new Var(declarator.getId().getName(), type, getAnnotation(fieldDeclaration.getAnnotations()));
                context.addValue(var);
            }
        }

        List<MethodDeclaration> methods = getMethodDeclarations(klass);

        for (MethodDeclaration methodDeclaration : Optional.ofNullable(methods).orElse(new ArrayList<>())) {
            String name = methodDeclaration.getName();
            MethodContext method = new MethodContext(name, context);
            context.addChild(method);

            method.setBodyStatements(Optional.ofNullable(methodDeclaration.getBody()).map(BlockStmt::getStmts).orElse(new ArrayList<>()));
            for (Parameter parameter : Optional.ofNullable(methodDeclaration.getParameters()).orElse(new ArrayList<>())) {
                Var var = new Var(parameter.getId().getName(), parameter.getType().toString(), null);
                method.addValue(var);
                method.addParam(parameter.getId().getName());
            }
        }
        return context;
    }

    private ClassContext getMainMethodContext(List<ClassContext> klasses) {
        return klasses.stream()
                .filter(classContext -> classContext.getMethods().containsKey("main"))
                .findFirst()
                .orElse(null);
    }

    private void setValuesFromVarDeclarationStatement(List<Statement> statements, BodyContext context) {
        List<VariableDeclarationExpr> varDeclarations = getVarDeclrExprs(statements);
        for (VariableDeclarationExpr declarationExpr : varDeclarations) {
            String type = declarationExpr.getType().toString();
            for (VariableDeclarator declarator : declarationExpr.getVars()) {
                Var var = new Var(declarator.getId().getName(), type, getAnnotation(declarationExpr.getAnnotations()));
                context.addValue(var);
            }
        }
    }

    private SecurityAnnotation getAnnotation(List<AnnotationExpr> exprs) {
        List<NormalAnnotationExpr> securityTypeAnnotations = exprs.stream()
                .filter(annotationExpr -> annotationExpr instanceof NormalAnnotationExpr)
                .filter(annotationExpr -> "SecurityType".equals(annotationExpr.getName().getName()))
                .map(annotationExpr -> (NormalAnnotationExpr) annotationExpr)
                .collect(Collectors.toList());

        if (!securityTypeAnnotations.isEmpty()) {
            List<MemberValuePair> pairs = securityTypeAnnotations.get(0).getPairs();
            if (pairs.size() == 2) {
                MemberValuePair first = pairs.get(0);
                MemberValuePair second = pairs.get(1);
                if ("OwnerPrincipal".equals(first.getName()) && "ReaderPrincipal".equals(second.getName())) {
                    String owner = first.getValue()
                            .toString()
                            .replaceAll("\"", "");

                    String[] readers = second.getValue()
                            .toString()
                            .replaceAll("\\{", "")
                            .replaceAll("\\}", "")
                            .replaceAll("\"", "")
                            .split(",");

                    Set<String> readersSet = Arrays.stream(readers)
                            .map(String::trim)
                            .collect(Collectors.toSet());

                    return new SecurityAnnotation(owner, readersSet);

                } else if ("OwnerPrincipal".equals(second.getName()) && "ReaderPrincipal".equals(first.getName())) {
                    String owner = second.getValue()
                            .toString()
                            .replaceAll("\"", "");

                    String[] readers = first.getValue()
                            .toString()
                            .replaceAll("\\{", "")
                            .replaceAll("\\}", "")
                            .replaceAll("\"", "")
                            .split(",");

                    Set<String> readersSet = Arrays.stream(readers)
                            .map(String::trim)
                            .collect(Collectors.toSet());

                    return new SecurityAnnotation(owner, readersSet);
                }
            }
        }
        return null;
    }

    private List<AnnotationDeclaration> getAnnotattionDeclaration(CompilationUnit compilationUnit) {
        return compilationUnit.getTypes()
                .stream()
                .filter(typeDeclaration -> typeDeclaration instanceof AnnotationDeclaration)
                .map(typeDeclaration -> (AnnotationDeclaration) typeDeclaration)
                .collect(Collectors.toList());
    }

    private List<ClassOrInterfaceDeclaration> getClasses(CompilationUnit compilationUnit) {
        return compilationUnit.getTypes()
                .stream()
                .filter(typeDeclaration -> typeDeclaration instanceof ClassOrInterfaceDeclaration)
                .map(typeDeclaration -> (ClassOrInterfaceDeclaration) typeDeclaration)
                .collect(Collectors.toList());
    }

    private List<MethodDeclaration> getMethodDeclarations(ClassOrInterfaceDeclaration klass) {
        return klass.getMembers()
                .stream()
                .filter(member -> member instanceof MethodDeclaration)
                .map(member -> (MethodDeclaration) member)
                .collect(Collectors.toList());
    }

    private List<FieldDeclaration> getFieldDeclarations(ClassOrInterfaceDeclaration klass) {
        return klass.getMembers()
                .stream()
                .filter(member -> member instanceof FieldDeclaration)
                .map(member -> (FieldDeclaration) member)
                .collect(Collectors.toList());
    }

    private List<VariableDeclarationExpr> getVarDeclrExprs(List<Statement> statements) {
        return statements
                .stream()
                .filter(statement -> statement instanceof ExpressionStmt)
                .map(statement -> (ExpressionStmt) statement)
                .map(ExpressionStmt::getExpression)
                .filter(statement -> statement instanceof VariableDeclarationExpr)
                .map(statement -> (VariableDeclarationExpr) statement)
                .collect(Collectors.toList());
    }

    private ClassContext getClassContextFromActContext(Context context){
        Context parent = context;
        while (parent.getParent() != null && !(parent instanceof ClassContext)){
            parent = parent.getParent();
        }
        if(parent instanceof ClassContext){
            return (ClassContext)parent;
        }
        throw new IllegalStateException("klass context does not exists");
    }

    //todo
    /*
    private List<org.blahodarny.formalnemetody.model.stmts.Expression> getExpressionFromStatement(Statement statement, Context context) {
        List<org.blahodarny.formalnemetody.model.stmts.Expression> expressions = new ArrayList<>();
        if (statement instanceof ExpressionStmt) {
            ExpressionStmt exprStmt = (ExpressionStmt) statement;
            Expression expression = exprStmt.getExpression();
            if (expression instanceof VariableDeclarationExpr) {
                //do nothing
            } else if (expression instanceof AssignExpr) {
                AssignExpr assignExpr = (AssignExpr) expression;
                List<String> target = getValueNamesAffectedByExpression(assignExpr.getTarget());
                List<String> value = getValueNamesAffectedByExpression(assignExpr.getValue());  //pozriet sa ci je to tupy assing alebo volanie metody / instancia
                AssignExpression assignExpression = new AssignExpression();
                assignExpression.setTarget(Optional.ofNullable(target).map(strings -> strings.stream().findFirst().orElse(null)).orElse(null));
                assignExpression.setValues(value);
                expressions.add(assignExpression);
            } else if (expression instanceof MethodCallExpr) {
                MethodCallExpr methodCallExpr = (MethodCallExpr) expression;
                List<String> args = new ArrayList<>();
                for (Expression e : Optional.ofNullable(methodCallExpr.getArgs()).orElse(new ArrayList<>())) {
                    args.addAll(getValueNamesAffectedByExpression(e));
                }
                MethodCallExpression methodCallExpression = new MethodCallExpression();
                methodCallExpression.setName(methodCallExpr.getName());
                methodCallExpression.setParams(args);
                expressions.add(methodCallExpression);
            } else if (expression instanceof UnaryExpr) {
                UnaryExpr unaryExpr = (UnaryExpr) expression;
                List<String> target = getValueNamesAffectedByExpression(unaryExpr.getExpr());
                List<String> value = new ArrayList<>(target);
                AssignExpression assignExpression = new AssignExpression();
                assignExpression.setTarget(Optional.ofNullable(target).map(strings -> strings.stream().findFirst().orElse(null)).orElse(null));
                assignExpression.setValues(value);
                expressions.add(assignExpression);
            } else if (expression instanceof ObjectCreationExpr) {
                ObjectCreationExpr objectCreationExpr = (ObjectCreationExpr) expression;
                List<String> args = new ArrayList<>();
                for (Expression e : Optional.ofNullable(objectCreationExpr.getArgs()).orElse(new ArrayList<>())) {
                    args.addAll(getValueNamesAffectedByExpression(e));
                }
                MethodCallExpression methodCallExpression = new MethodCallExpression();
                methodCallExpression.setName(objectCreationExpr.getType().getName());
                methodCallExpression.setParams(args);
                expressions.add(methodCallExpression);
            }
        } else if (statement instanceof IfStmt) {
            IfStmt ifStmt = (IfStmt) statement;
            IfExpression ifExpression = new IfExpression();
            ifExpression.setConditionValues(getValueNamesAffectedByExpression(ifStmt.getCondition()));
            ifExpression.setThenExpressions(getExpressionFromStatement(ifStmt.getThenStmt(), context));
            ifExpression.setElseExpressions(getExpressionFromStatement(ifStmt.getElseStmt(), context));
            expressions.add(ifExpression);
        } else if (statement instanceof WhileStmt) {
            WhileStmt whileStmt = (WhileStmt) statement;
            WhileExpression whileExpression = new WhileExpression();
            whileExpression.setConditionValues(getValueNamesAffectedByExpression(whileStmt.getCondition()));
            whileExpression.setExpressions(getExpressionFromStatement(whileStmt.getBody(), context));
            expressions.add(whileExpression);
        } else if (statement instanceof DoStmt) {
            DoStmt doStmt = (DoStmt) statement;
            WhileExpression whileExpression = new WhileExpression();
            whileExpression.setConditionValues(getValueNamesAffectedByExpression(doStmt.getCondition()));
            whileExpression.setExpressions(getExpressionFromStatement(doStmt.getBody(), context));
            expressions.add(whileExpression);
        } else if (statement instanceof ReturnStmt) {
            ReturnStmt returnStmt = (ReturnStmt) statement;
            List<String> target = getValueNamesAffectedByExpression(returnStmt.getExpr());
            List<String> value = new ArrayList<>(target);
            AssignExpression assignExpression = new AssignExpression();
            assignExpression.setTarget(Optional.ofNullable(target).map(strings -> strings.stream().findFirst().orElse(null)).orElse(null));
            assignExpression.setValues(value);
            expressions.add(assignExpression);
        } else if (statement instanceof BlockStmt) {
            BlockStmt blockStmt = (BlockStmt) statement;
            for (Statement innerStatement : Optional.ofNullable(blockStmt.getStmts()).orElse(new ArrayList<>())) {
                List<org.blahodarny.formalnemetody.model.stmts.Expression> innerExpressions = getExpressionFromStatement(innerStatement, context);
                expressions.addAll(innerExpressions);
            }
        }
        return expressions;
    }
    private List<String> getValueNamesAffectedByExpression(Expression Expression) {
        List<String> valueNames = new ArrayList<>();
        if (Expression instanceof BinaryExpr) {
            BinaryExpr binaryExpr = (BinaryExpr) Expression;
            valueNames.addAll(getValueNamesAffectedByExpression(binaryExpr.getLeft()));
            valueNames.addAll(getValueNamesAffectedByExpression(binaryExpr.getRight()));
        }
        if (Expression instanceof InstanceOfExpr) {
            InstanceOfExpr instanceOfExpr = (InstanceOfExpr) Expression;
            valueNames.addAll(getValueNamesAffectedByExpression(instanceOfExpr.getExpr()));
        }
        if (Expression instanceof ObjectCreationExpr) {
            ObjectCreationExpr objectCreationExpr = (ObjectCreationExpr) Expression;
            valueNames.add(objectCreationExpr.toString());
        }
        if (Expression instanceof NameExpr) {
            NameExpr nameExpr = (NameExpr) Expression;
            valueNames.add(nameExpr.getName());
        }
        if (Expression instanceof LiteralExpr) {
            //do nothing
        }
        if (Expression instanceof UnaryExpr) {
            UnaryExpr unaryExpr = (UnaryExpr) Expression;
            valueNames.addAll(getValueNamesAffectedByExpression(unaryExpr.getExpr()));
        }
        return valueNames;
    }
    */

    /*\
    TODO
    stmt
    \*/

    private void proccessStatement(Statement statement, Context context, Dependency dependancy, Consumer<Var> dependencySetter) {
        if (statement instanceof EmptyStmt) {
            //todo do nothing its empty
        } else if (statement instanceof BlockStmt) {
            proccessBlockStmt((BlockStmt) statement, context, dependancy, dependencySetter);
        } else if (statement instanceof ExpressionStmt) {
            proccessExpressionStmt((ExpressionStmt) statement, context, dependancy, dependencySetter);
        } else if (statement instanceof IfStmt) {
            proccessIfStmt((IfStmt) statement, context, dependancy, dependencySetter);
        } else if (statement instanceof WhileStmt) {
            proccessWhileStatment((WhileStmt)statement, context, dependancy, dependencySetter);
        } else if (statement instanceof DoStmt) {
            proccessDoStatment((DoStmt) statement, context, dependancy, dependencySetter);
        } else if (statement instanceof SwitchStmt) {
            //
        }
    }

    private void proccessBlockStmt(BlockStmt statement, Context context, Dependency dependancy, Consumer<Var> dependencySetter) {
        BodyContext blockContext = new BodyContext("block", context);
        for (Statement stmts : statement.getStmts()) {
            proccessStatement(stmts, blockContext, dependancy, dependencySetter);
        }
    }

    private void proccessExpressionStmt(ExpressionStmt statement, Context context, Dependency dependancy, Consumer<Var> dependencySetter) {
        Expression expression = statement.getExpression();
        proccessExpression(expression, context, dependancy, dependencySetter);
    }

    private void proccessIfStmt(IfStmt statement, Context context, Dependency dependancy, Consumer<Var> dependencySetter) {
        Expression condition = statement.getCondition();
        Statement thenStmt = statement.getThenStmt();
        Statement elseStmt = statement.getElseStmt();

        Dependency ifDependency =  new Dependency();
        Dependency ifSubDependency = new Dependency();
        ifDependency.addSubDependecy(ifSubDependency);

        proccessExpression(condition, context, ifDependency, ifDependency::setVariable);

        proccessStatement(thenStmt, context, ifSubDependency, ifSubDependency::setVariable);
        proccessStatement(elseStmt, context, ifSubDependency, ifSubDependency::setVariable);

        if(dependancy != null){
            dependancy.addSubDependecy(ifDependency);
        }else{
            dependecies.add(ifDependency);
        }
    }

    private void proccessWhileStatment(WhileStmt statement, Context context, Dependency dependancy, Consumer<Var> dependencySetter){
        Expression condition = statement.getCondition(); //all vars
        Statement body = statement.getBody();// all vars dependant na vars v condition

        Dependency whileDependency = new Dependency();
        Dependency whileSubDependency = new Dependency();
        whileDependency.addSubDependecy(whileSubDependency);

        proccessExpression(condition, context, whileDependency, whileDependency::setVariable);
        proccessStatement(body, context, whileSubDependency, whileSubDependency::setVariable);

        if(dependancy != null){
            dependancy.addSubDependecy(whileDependency);
        }else{
            dependecies.add(whileDependency);
        }
    }

    private void proccessDoStatment(DoStmt statement, Context context, Dependency dependancy, Consumer<Var> dependencySetter){
        Expression condition = statement.getCondition(); //all vars
        Statement body = statement.getBody();// all vars dependant na vars v condition

        Dependency doDependency = new Dependency();
        Dependency doSubDependency = new Dependency();
        doDependency.addSubDependecy(doSubDependency);

        proccessExpression(condition, context, doDependency, doDependency::setVariable);
        proccessStatement(body, context, doSubDependency, doSubDependency::setVariable);

        if(dependancy != null){
            dependancy.addSubDependecy(doDependency);
        }else{
            dependecies.add(doDependency);
        }
    }

    /*\
    TODO expressions
    \*/

    private void proccessExpression(Expression expression, Context context, Dependency dependancy, Consumer<Var> dependencySetter) {
        if (expression instanceof LiteralExpr) {
            //todo do nothing its literal
        } else if (expression instanceof EnclosedExpr) {
            processEnclosedexpr((EnclosedExpr) expression, context, dependancy, dependencySetter);
        } else if (expression instanceof NameExpr) {
            processNameExpr((NameExpr) expression, context, dependancy, dependencySetter);
        } else if (expression instanceof AssignExpr) {
            processAssignExpr((AssignExpr) expression, context, dependancy, dependencySetter);
        } else if (expression instanceof UnaryExpr) {
            processUnaryExpr((UnaryExpr) expression, context, dependancy, dependencySetter);
        } else if (expression instanceof BinaryExpr) {
            processBinaryExpr((BinaryExpr) expression, context, dependancy, dependencySetter);
        } else if(expression instanceof ObjectCreationExpr){
            processObjectCreationExpr((ObjectCreationExpr) expression, context, dependancy, dependencySetter);
        } else if(expression instanceof ConditionalExpr){
            processConditionalExpr((ConditionalExpr) expression, context, dependancy, dependencySetter);
        } else if(expression instanceof MethodCallExpr){
            processMethodCallExpr((MethodCallExpr) expression, context, dependancy, dependencySetter);
        } else if(expression instanceof VariableDeclarationExpr){
            proccessVariableDeclarationExpr((VariableDeclarationExpr) expression, context, dependancy, dependencySetter);
        }

    }

    private void processEnclosedexpr(EnclosedExpr expression, Context context, Dependency dependancy, Consumer<Var> dependencySetter){
        Expression inner = expression.getInner();

        proccessExpression(inner, context, dependancy, dependencySetter);
    }

    private void processNameExpr(NameExpr expression, Context context, Dependency dependancy, Consumer<Var> dependencySetter) {
        String name = expression.getName();
        Var variable = context.getValue(name);
        if(dependancy != null){
            dependencySetter.accept(variable);
        }
    }

    private void processAssignExpr(AssignExpr expression, Context context, Dependency dependancy, Consumer<Var> dependencySetter) {
        Expression target = expression.getTarget();
        Expression value = expression.getValue();

        Dependency assignDependency = new Dependency();
        Dependency assignSubDependency = new Dependency();
        assignDependency.addSubDependecy(assignSubDependency);

        proccessExpression(value, context, assignDependency, assignDependency::setVariable);
        proccessExpression(target, context, assignSubDependency, assignSubDependency::setVariable);

        if(dependancy != null){
            dependancy.addSubDependecy(assignDependency);
        }else{
            dependecies.add(assignDependency);
        }

    }

    private void processUnaryExpr(UnaryExpr expression, Context context, Dependency dependancy, Consumer<Var> dependencySetter) {
        Expression unaryExpr = expression.getExpr();

        proccessExpression(unaryExpr, context, dependancy, dependencySetter);
    }

    private void processBinaryExpr(BinaryExpr expression, Context context, Dependency dependancy, Consumer<Var> dependencySetter) {
        Expression left = expression.getLeft();
        Expression right = expression.getRight();

        proccessExpression(left, context, dependancy, dependencySetter);
        proccessExpression(right, context, dependancy, dependencySetter);
    }

    private void processObjectCreationExpr(ObjectCreationExpr expression, Context context, Dependency dependancy, Consumer<Var> dependencySetter) {
        List<Expression> args = expression.getArgs();

        for(Expression e: args){
            proccessExpression(e, context, dependancy, dependencySetter);
        }
    }

    private void processConditionalExpr(ConditionalExpr expression, Context context, Dependency dependancy, Consumer<Var> dependencySetter) {
        Expression condition = expression.getCondition();//all vars
        Expression thenExpr = expression.getThenExpr(); //all vars dependant on condition vars
        Expression elseExpr = expression.getElseExpr();//all vars dependant on condition vars

        Dependency conditionDependency = new Dependency();
        Dependency conditionSubDependency = new Dependency();
        conditionDependency.addSubDependecy(conditionSubDependency);

        proccessExpression(condition, context, conditionDependency, conditionDependency::setVariable);
        proccessExpression(thenExpr, context, conditionSubDependency, conditionSubDependency::setVariable);
        proccessExpression(elseExpr, context, conditionSubDependency, conditionSubDependency::setVariable);

        if(dependancy != null){
            dependancy.addSubDependecy(conditionDependency);
        }else{
            dependecies.add(conditionDependency);
        }
    }

    private void processMethodCallExpr(MethodCallExpr expression, Context context, Dependency dependancy, Consumer<Var> dependencySetter) {
        Expression scope = expression.getScope(); //todo??
/*
        String name = expression.getName();
        List<Expression> params = expression.getArgs();

        ClassContext classContext = getClassContextFromActContext(context);
        BodyContext methodContext = classContext.getMethod(name);
        if(methodContext == null){
            throw new IllegalStateException("method does not exists");
        }
        BodyContext methodExecContext = new BodyContext("methodExecContext", methodContext);

        Dependency paramDependency = new Dependency();
        for(int i = 0; i < Optional.ofNullable(params).orElse(new ArrayList<>()).size(); i++){
            //TODO treba spracovat vsetky param expresny - vratit nejaku dpendency ktora sa potom prepne do
            //TODO kontextu exekucie mothody - treba nejak premaovat parametre metody
            //TODO aby sa cely ten strom dpendcy nejak nastavil podla toho ake security typa maju args
            //TODO a este aj tie zavyslosti nad args - v expressions
            //proccessExpression(params.get(i), context, );

            String paramName = methodContext.getParamByIndex(i);
            Var paramVar = methodContext.getValue(paramName);

            //Var methodExecVar = new Var()
            //methodExecContext.addValue();
        }

        List<Statement> methodStatements =  methodContext.getBodyStatements();
        for(Statement statement : methodStatements){
            proccessStatement(statement, methodExecContext, paramDependency, dependencySetter);
        }

        if(dependancy != null){
            dependancy.addSubDependecy(paramDependency);
        }else{
            dependecies.add(paramDependency);
        }

 */
    }

    private void proccessVariableDeclarationExpr(VariableDeclarationExpr expression, Context context, Dependency dependancy, Consumer<Var> dependencySetter){
        String type = Optional.ofNullable(expression.getType()).map(Object::toString).orElse("");

        List<AnnotationExpr> annotationExprs =  expression.getAnnotations();
        SecurityAnnotation securityAnnotation = getAnnotation(annotationExprs);

        List<VariableDeclarator> vars = expression.getVars();

        Dependency initDependency = new Dependency();
        Dependency initSubDependency = new Dependency();
        initDependency.addSubDependecy(initSubDependency);

        for(VariableDeclarator declarator : vars){
            String name = Optional.ofNullable(declarator.getId()).map(VariableDeclaratorId::getName).orElse("");
            Var localVar = new Var(name, type, securityAnnotation);
            context.addValue(localVar);
            initSubDependency.setVariable(localVar);
        }

        for(VariableDeclarator declarator : vars){
            Expression initExpr = declarator.getInit();
            proccessExpression(initExpr, context, initDependency, initDependency::setVariable);
        }
        if(dependancy != null){
            dependancy.addSubDependecy(initDependency);
        }else{
            dependecies.add(initDependency);
        }
    }
}