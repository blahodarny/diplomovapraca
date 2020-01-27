package org.blahodarny.formalnemetody.service;

import org.blahodarny.formalnemetody.antlr.api.CompilationUnit;
import org.blahodarny.formalnemetody.antlr.api.body.*;
import org.blahodarny.formalnemetody.antlr.api.expr.AnnotationExpr;
import org.blahodarny.formalnemetody.antlr.api.expr.MemberValuePair;
import org.blahodarny.formalnemetody.antlr.api.expr.NormalAnnotationExpr;
import org.blahodarny.formalnemetody.model.ctx.ClassContext;
import org.blahodarny.formalnemetody.model.values.SecurityAnnotation;
import org.blahodarny.formalnemetody.model.values.Var;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service(ConvertToModelService.NAME)
public class ConvertToModelServiceBean implements ConvertToModelService {

    @Override
    public void convert(CompilationUnit compilationUnit) {
        List<ClassContext> classContexts = new ArrayList<>();

        List<ClassOrInterfaceDeclaration> classes = getClasses(compilationUnit);
        for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : classes) {
            classContexts.add(createClassContext(classOrInterfaceDeclaration));
        }

        System.out.println();

    }

    private ClassContext createClassContext(ClassOrInterfaceDeclaration klass) {
        ClassContext context = new ClassContext(klass.getName(), null);
        List<FieldDeclaration> fields = getFieldDeclarations(klass);
        for (FieldDeclaration fieldDeclaration : fields) {
            String type = fieldDeclaration.getType().toString();
            for (VariableDeclarator declarator : fieldDeclaration.getVariables()) {
                Var var = new Var(declarator.toString(), type, getAnnotation(fieldDeclaration.getAnnotations()));
                context.addValue(var);
            }
        }
        return context;
    }

    private SecurityAnnotation getAnnotation(List<AnnotationExpr> exprs) {
        List<NormalAnnotationExpr> securityTypeAnnotations = exprs.stream()
                .filter(annotationExpr -> annotationExpr instanceof NormalAnnotationExpr)
                .filter(annotationExpr -> "SecurityType".equals(annotationExpr.getName()))
                .map(annotationExpr -> (NormalAnnotationExpr) annotationExpr)
                .collect(Collectors.toList());

        if (!securityTypeAnnotations.isEmpty()) {
            List<MemberValuePair> pairs = securityTypeAnnotations.get(0).getPairs();
            if (pairs.size() == 2) {
                MemberValuePair first = pairs.get(0);
                MemberValuePair second = pairs.get(0);
                if ("OwnerPrincipal".equals(first.getName()) && "ReaderPrincipal".equals(second.getName())) {
                    String owner = first.getValue().toString();
                    String[] readers = second.getValue()
                            .toString()
                            .replaceAll("\\{", "")
                            .replaceAll("\\}", "")
                            .split(",");

                    Set<String> readersSet = Arrays.stream(readers)
                            .map(s -> s.trim())
                            .collect(Collectors.toSet());
                    return new SecurityAnnotation(owner, readersSet);

                } else if ("OwnerPrincipal".equals(second.getName()) && "ReaderPrincipal".equals(first.getName())) {
                    String owner = second.getValue().toString();
                    String[] readers = first.getValue()
                            .toString()
                            .replaceAll("\\{", "")
                            .replaceAll("\\}", "")
                            .split(",");

                    Set<String> readersSet = Arrays.stream(readers)
                            .map(s -> s.trim())
                            .collect(Collectors.toSet());
                    return new SecurityAnnotation(owner, readersSet);
                }
            }
        }
        return null;
    }

    private List<ClassOrInterfaceDeclaration> getClasses(CompilationUnit compilationUnit) {
        return compilationUnit.getTypes()
                .stream()
                .filter(typeDeclaration -> typeDeclaration instanceof ClassOrInterfaceDeclaration)
                .map(typeDeclaration -> (ClassOrInterfaceDeclaration) typeDeclaration)
                .collect(Collectors.toList());
    }

    private List<FieldDeclaration> getFieldDeclarations(ClassOrInterfaceDeclaration klass) {
        return klass.getMembers()
                .stream()
                .filter(member -> member instanceof FieldDeclaration)
                .map(member -> (FieldDeclaration) member)
                .collect(Collectors.toList());
    }
}