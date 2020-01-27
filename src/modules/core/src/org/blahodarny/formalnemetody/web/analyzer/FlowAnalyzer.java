package org.blahodarny.formalnemetody.web.analyzer;

import org.blahodarny.formalnemetody.antlr.api.CompilationUnit;
import org.blahodarny.formalnemetody.antlr.api.body.*;
import org.blahodarny.formalnemetody.antlr.api.expr.*;
import org.blahodarny.formalnemetody.antlr.api.stmt.*;
import org.blahodarny.formalnemetody.antlr.api.type.ClassOrInterfaceType;
import org.blahodarny.formalnemetody.model.Dependency;
import org.blahodarny.formalnemetody.model.ctx.BodyContext;
import org.blahodarny.formalnemetody.model.ctx.ClassContext;
import org.blahodarny.formalnemetody.model.ctx.Context;
import org.blahodarny.formalnemetody.model.ctx.MethodContext;
import org.blahodarny.formalnemetody.model.values.SecurityAnnotation;
import org.blahodarny.formalnemetody.model.values.Var;

import java.util.*;
import java.util.stream.Collectors;

public class FlowAnalyzer {

    private CompilationUnit compilationUnit = null;
    private List<ClassContext> klassContexts;
    private Set<Dependency> dependecies;
    private StringBuilder resolvedDeps = new StringBuilder();

    public FlowAnalyzer(CompilationUnit compilationUnit) {
        this.compilationUnit = compilationUnit;
        this.dependecies = new LinkedHashSet<>();
        this.klassContexts = new ArrayList<>();
    }

    public String analyze() {
        List<AnnotationDeclaration> annotations = getAnnotattionDeclaration(compilationUnit);
        List<ClassOrInterfaceDeclaration> classes = getClasses(compilationUnit);

        for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : classes) {
            klassContexts.add(createClassContext(classOrInterfaceDeclaration));
        }

        return processMainClass();
    }

    private String processMainClass() {
        ClassContext mainClass = getMainMethodClassContext(klassContexts);
        MethodContext mainContext = mainClass.getMethod("main");
        List<Statement> mainStatements = mainContext.getBodyStatements();
        Map<String, SecurityAnnotation> paramMap = new HashMap<>();
        paramMap.put("args", null);
        BodyContext mainExecContext = mainContext.createBlockContext(mainClass, paramMap, new HashMap<>());

        List<Dependency> allFuck = new ArrayList<>();
        try {
            for (Statement statement : mainStatements) {
                List<Dependency> deps = proccessStatement(statement, mainExecContext, null);
                if(deps != null){
                    allFuck.addAll(deps);
                }
            }

            DependecyResolver resolver = new DependecyResolver(allFuck);
            String resolved = resolver.analyze();
            resolvedDeps.append(resolved);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resolvedDeps.toString();
    }

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
        List<ConstructorDeclaration> constructors = getConstructorDeclarations(klass);

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

        for (ConstructorDeclaration constructor : Optional.ofNullable(constructors).orElse(new ArrayList<>())) {
            String name = constructor.getName();
            MethodContext method = new MethodContext(name, context);
            context.addChild(method);

            method.setBodyStatements(Optional.ofNullable(constructor.getBlock()).map(BlockStmt::getStmts).orElse(new ArrayList<>()));
            for (Parameter parameter : Optional.ofNullable(constructor.getParameters()).orElse(new ArrayList<>())) {
                Var var = new Var(parameter.getId().getName(), parameter.getType().toString(), null);
                method.addValue(var);
                method.addParam(parameter.getId().getName());
            }
        }
        return context;
    }

    private ClassContext getMainMethodClassContext(List<ClassContext> klasses) {
        return getMethodClassContext(klasses, "main");
    }

    private ClassContext getMethodClassContext(List<ClassContext> klasses, String name) {
        return klasses.stream()
                .filter(classContext -> classContext.getMethods().containsKey(name))
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

    private List<ConstructorDeclaration> getConstructorDeclarations(ClassOrInterfaceDeclaration klass) {
        return klass.getMembers()
                .stream()
                .filter(member -> member instanceof ConstructorDeclaration)
                .map(member -> (ConstructorDeclaration) member)
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

    private ClassContext getClassContextFromActContext(Context context) {
        Context parent = context;
        while (parent.getParent() != null && !(parent instanceof ClassContext)) {
            parent = parent.getParent();
        }
        if (parent instanceof ClassContext) {
            return (ClassContext) parent;
        }
        throw new IllegalStateException("klass context does not exists");
    }

    /*\
    TODO
    stmt
    \*/

    private List<Dependency> proccessStatement(Statement statement, Context context, List<Dependency> dependencies) {
        if (statement instanceof EmptyStmt) {
            return dependencies;
        } else if (statement instanceof BlockStmt) {
            return proccessBlockStmt((BlockStmt) statement, context, dependencies);
        } else if (statement instanceof ExpressionStmt) {
            return proccessExpressionStmt((ExpressionStmt) statement, context, dependencies);
        } else if (statement instanceof IfStmt) {
            return proccessIfStmt((IfStmt) statement, context, dependencies);
        } else if (statement instanceof WhileStmt) {
            return proccessWhileStatment((WhileStmt) statement, context, dependencies);
        } else if (statement instanceof DoStmt) {
            return proccessDoStatment((DoStmt) statement, context, dependencies);
        } else if (statement instanceof SwitchStmt) {
            return null;//TODO
        } else if (statement instanceof ReturnStmt) {
            return proccessReturnStmt((ReturnStmt) statement, context, dependencies);
        }
        return dependencies;
    }

    private List<Dependency> proccessBlockStmt(BlockStmt statement, Context context, List<Dependency> dependencies) {
        BodyContext blockContext = new BodyContext("block", context);
        List<Dependency> blockDeps = new ArrayList<>();
        for (Statement stmts : statement.getStmts()) {
            List<Dependency> stmtDeps = proccessStatement(stmts, blockContext, dependencies);
            blockDeps.addAll(stmtDeps);
        }
        return blockDeps.isEmpty() ? null : blockDeps;
    }

    private List<Dependency> proccessExpressionStmt(ExpressionStmt statement, Context context, List<Dependency> dependencies) {
        Expression expression = statement.getExpression();
        return proccessExpression(expression, context, dependencies);
    }

    private List<Dependency> proccessIfStmt(IfStmt statement, Context context, List<Dependency> dependencies) {
        Expression condition = statement.getCondition();
        Statement thenStmt = statement.getThenStmt();
        Statement elseStmt = statement.getElseStmt();

        List<Dependency> conditionVars = proccessExpression(condition, context, dependencies);

        List<Dependency> varsDependantOnCondition1 = proccessStatement(thenStmt, context, conditionVars);
        List<Dependency> varsDependantOnCondition2 = proccessStatement(elseStmt, context, conditionVars);
        List<Dependency> varsDependantOnCondition = new ArrayList<>();
        if (varsDependantOnCondition1 != null) {
            varsDependantOnCondition.addAll(varsDependantOnCondition1);
        }
        if (varsDependantOnCondition2 != null) {
            varsDependantOnCondition.addAll(varsDependantOnCondition2);
        }

        return varsDependantOnCondition;
    }

    private List<Dependency> proccessWhileStatment(WhileStmt statement, Context context, List<Dependency> dependencies) {
        Expression condition = statement.getCondition();
        Statement body = statement.getBody();

        List<Dependency> conditionVars = proccessExpression(condition, context, dependencies);

        return proccessStatement(body, context, conditionVars);
    }

    private List<Dependency> proccessDoStatment(DoStmt statement, Context context, List<Dependency> dependencies) {
        Expression condition = statement.getCondition();
        Statement body = statement.getBody();

        List<Dependency> conditionVars = proccessExpression(condition, context, dependencies);

        return proccessStatement(body, context, conditionVars);
    }

    private List<Dependency> proccessReturnStmt(ReturnStmt statement, Context context, List<Dependency> dependencies) {
        Expression returnExprs = statement.getExpr();

        List<Dependency> returnDeps = proccessExpression(returnExprs, context, dependencies);

        SecurityAnnotation ann = null;

        for(Dependency dependency : returnDeps){ //todo do hlbky
            if(dependency.getVariable() != null && dependency.getVariable().getSecurityAnnotation() !=null){
                if(ann == null){
                    ann = dependency.getVariable().getSecurityAnnotation();
                }else if(ann.isLessRestrictive(dependency.getVariable().getSecurityAnnotation())){
                    ann = dependency.getVariable().getSecurityAnnotation();
                }
            }
        }

        Var variable = new Var("*return_" + context.getName(), "", ann);
        Dependency var = new Dependency();
        var.setVariable(variable);
        var.addSubDependecy(returnDeps);

        return Collections.singletonList(var);
    }

    /*\
    TODO expressions
    \*/

    private List<Dependency> proccessExpression(Expression expression, Context context, List<Dependency> dependencies) {
        if (expression instanceof LiteralExpr) {
            return dependencies;
        } else if (expression instanceof EnclosedExpr) {
            return processEnclosedexpr((EnclosedExpr) expression, context, dependencies);
        } else if (expression instanceof NameExpr) {
            return processNameExpr((NameExpr) expression, context, dependencies);
        } else if (expression instanceof AssignExpr) {
            return processAssignExpr((AssignExpr) expression, context, dependencies);
        } else if (expression instanceof UnaryExpr) {
            return processUnaryExpr((UnaryExpr) expression, context, dependencies);
        } else if (expression instanceof BinaryExpr) {
            return processBinaryExpr((BinaryExpr) expression, context, dependencies);
        } else if (expression instanceof ObjectCreationExpr) {
            return processObjectCreationExpr((ObjectCreationExpr) expression, context, dependencies);
        } else if (expression instanceof ConditionalExpr) {
            return processConditionalExpr((ConditionalExpr) expression, context, dependencies);
        } else if (expression instanceof MethodCallExpr) {
            return processMethodCallExpr((MethodCallExpr) expression, context, dependencies);
        } else if (expression instanceof VariableDeclarationExpr) {
            return proccessVariableDeclarationExpr((VariableDeclarationExpr) expression, context, dependencies);
        }
        return dependencies;
    }

    private List<Dependency> processEnclosedexpr(EnclosedExpr expression, Context context, List<Dependency> dependencies) {
        Expression inner = expression.getInner();

        return proccessExpression(inner, context, dependencies);
    }

    private List<Dependency> processNameExpr(NameExpr expression, Context context, List<Dependency> dependencies) {
        String name = expression.getName();
        Var variable = context.getValue(name);
        Dependency var = new Dependency();
        var.setVariable(variable);
        var.addSubDependecy(dependencies);

        return Collections.singletonList(var);
    }

    private List<Dependency> processAssignExpr(AssignExpr expression, Context context, List<Dependency> dependencies) {
        Expression target = expression.getTarget();
        Expression value = expression.getValue();

        List<Dependency> epxrVars = proccessExpression(value, context, dependencies);

        return proccessExpression(target, context, epxrVars);
    }

    private List<Dependency> processUnaryExpr(UnaryExpr expression, Context context, List<Dependency> dependencies) {
        Expression unaryExpr = expression.getExpr();

        return proccessExpression(unaryExpr, context, dependencies);
    }

    private List<Dependency> processBinaryExpr(BinaryExpr expression, Context context, List<Dependency> dependencies) {
        Expression left = expression.getLeft();
        Expression right = expression.getRight();

        List<Dependency> expressionDeps = new ArrayList<>();
        List<Dependency> leftDeps = proccessExpression(left, context, dependencies);
        List<Dependency> rightDeps = proccessExpression(right, context, dependencies);
        if (leftDeps != null) {
            expressionDeps.addAll(leftDeps);
        }
        if (rightDeps != null) {
            expressionDeps.addAll(rightDeps);
        }

        return expressionDeps;
    }

    private List<Dependency> processConditionalExpr(ConditionalExpr expression, Context context, List<Dependency> dependencies) {
        Expression condition = expression.getCondition();
        Expression thenExpr = expression.getThenExpr();
        Expression elseExpr = expression.getElseExpr();

        List<Dependency> conditionVars = proccessExpression(condition, context, dependencies);

        List<Dependency> varsDependantOnCondition1 = proccessExpression(thenExpr, context, conditionVars);
        List<Dependency> varsDependantOnCondition2 = proccessExpression(elseExpr, context, conditionVars);
        List<Dependency> varsDependantOnCondition = new ArrayList<>();
        if (varsDependantOnCondition1 != null) {
            varsDependantOnCondition.addAll(varsDependantOnCondition1);
        }
        if (varsDependantOnCondition2 != null) {
            varsDependantOnCondition.addAll(varsDependantOnCondition2);
        }

        return varsDependantOnCondition;
    }

    private List<Dependency> processMethodCallExpr(MethodCallExpr expression, Context context, List<Dependency> dependencies) {
        String name = expression.getName();
        Expression scope = expression.getScope(); //todo??
        ClassContext classContext;
        Context execContext;

        if (scope != null && !(scope instanceof ThisExpr)) {
            List<Dependency> scopeDeps = proccessExpression(scope, context, null);
            classContext = scopeDeps.get(0).getVariable().getClassExecContext();
            execContext = classContext;
        } else {
            classContext = getClassContextFromActContext(context);
            execContext = context;
        }

        List<Expression> params = expression.getArgs();
        MethodContext methodContext = classContext.getMethod(name);

        Map<String, SecurityAnnotation> paramsMap = new HashMap<>();
        Map<String, List<Dependency>> paramsDependecies = new HashMap<>();
        for (int i = 0; i < Optional.ofNullable(params).orElse(new ArrayList<>()).size(); i++) {
            String paramName = methodContext.getParamByIndex(i);
            Var param = methodContext.getValue(paramName);
            Expression paramExpression = params.get(i);
            List<Dependency> paramDeps = proccessExpression(paramExpression, context, dependencies);
            if (paramDeps == null) {
                paramsMap.put(param.getName(), null);
            }
            if (paramDeps != null && paramDeps.isEmpty()) {
                paramsMap.put(param.getName(), null);
            }
            if (paramDeps != null && !paramDeps.isEmpty()) {
                Var var = paramDeps.get(0).getVariable();
                paramsMap.put(param.getName(), var.getSecurityAnnotation());
                paramsDependecies.put(param.getName(), paramDeps);
            }
        }

        BodyContext methodExecContext = methodContext.createBlockContext(execContext, paramsMap, paramsDependecies);
        List<Dependency> methodExecDeps = new ArrayList<>();
        for (Statement statement : methodContext.getBodyStatements()) {
            Optional.ofNullable(proccessStatement(statement, methodExecContext, null))
                    .ifPresent(methodExecDeps::addAll);
        }
        DependecyResolver resolver = new DependecyResolver(methodExecDeps);
        String resolved = resolver.analyze();
        resolvedDeps.append(resolved);

        List<Dependency> returnDeps = methodExecDeps.stream()
                .filter(dependency -> dependency.getVariable().getName().startsWith("*return"))
                .collect(Collectors.toList());

        return returnDeps;
    }

    private List<Dependency> processObjectCreationExpr(ObjectCreationExpr expression, Context context, List<Dependency> dependencies) {
        Expression scope = expression.getScope(); //todo??
        List<Expression> params = expression.getArgs();
        ClassOrInterfaceType klassDecl = expression.getType();

        ClassContext klassContext = getMethodClassContext(klassContexts, klassDecl.getName());
        MethodContext methodContext = klassContext.getMethod(klassDecl.getName());

        Map<String, SecurityAnnotation> paramsMap = new HashMap<>();
        Map<String, List<Dependency>> paramsDependecies = new HashMap<>();
        for (int i = 0; i < Optional.ofNullable(params).orElse(new ArrayList<>()).size(); i++) {
            String paramName = methodContext.getParamByIndex(i);
            Var param = methodContext.getValue(paramName);
            Expression paramExpression = params.get(i);
            List<Dependency> paramDeps = proccessExpression(paramExpression, context, dependencies);
            if (paramDeps == null) {
                paramsMap.put(param.getName(), null);
            }
            if (paramDeps != null && paramDeps.isEmpty()) {
                paramsMap.put(param.getName(), null);
            }
            if (paramDeps != null && !paramDeps.isEmpty()) {
                Var var = paramDeps.get(0).getVariable();
                paramsMap.put(param.getName(), var.getSecurityAnnotation());
                paramsDependecies.put(param.getName(), paramDeps);
            }
        }
        BodyContext methodExecContext = methodContext.createBlockContext(klassContext, paramsMap, paramsDependecies);

        List<Dependency> methodExecDeps = new ArrayList<>();
        for (Statement statement : methodContext.getBodyStatements()) {
            Optional.ofNullable(proccessStatement(statement, methodExecContext, null))
                    .ifPresent(methodExecDeps::addAll);
        }

        DependecyResolver resolver = new DependecyResolver(methodExecDeps);
        String resolved = resolver.analyze();
        resolvedDeps.append(resolved);

        return dependencies;
    }


    private List<Dependency> proccessVariableDeclarationExpr(VariableDeclarationExpr expression, Context context, List<Dependency> dependencies) {
        String type = Optional.ofNullable(expression.getType())
                .map(Object::toString)
                .orElse("");

        List<AnnotationExpr> annotationExprs = expression.getAnnotations();
        SecurityAnnotation securityAnnotation = getAnnotation(annotationExprs);
        List<VariableDeclarator> vars = expression.getVars();

        List<Dependency> varInitDeps = new ArrayList<>();

        for (VariableDeclarator declarator : vars) {
            Expression initExpr = declarator.getInit();
            Optional.ofNullable(proccessExpression(initExpr, context, dependencies))
                    .ifPresent(varInitDeps::addAll);
        }

        List<Dependency> varsDpes = new ArrayList<>();
        for (VariableDeclarator declarator : vars) {
            String name = Optional.ofNullable(declarator.getId())
                    .map(VariableDeclaratorId::getName)
                    .orElse("");

            Var localVar = new Var(name, type, securityAnnotation);
            context.addValue(localVar);
            Dependency dependency = new Dependency();
            dependency.setVariable(localVar);
            dependency.addSubDependecy(varInitDeps);
            varsDpes.add(dependency);
            klassContexts.stream()
                    .filter(classContext -> classContext.getName().equals(type))
                    .findFirst()
                    .ifPresent(localVar::setClassExecContext);
        }

        return varsDpes;
    }
}