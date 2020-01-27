package org.blahodarny.formalnemetody;

import org.blahodarny.formalnemetody.antlr.JavaParser;
import org.blahodarny.formalnemetody.antlr.api.CompilationUnit;
import org.blahodarny.formalnemetody.web.analyzer.FlowAnalyzer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class Tests {

    @Test
    public void test1() throws IOException {
        String exprected = "INFORMATION LEAK: \n" +
                "From var: a - Annotation{ owner: Robert, readers:  Lubos Peter}\n" +
                "to var: b - Annotation{ owner: Robert, readers:  Denisa Lubos Peter Erik}\n\n";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main { public static void main(String[] args) {" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos\"}, OwnerPrincipal = \"Robert\" )" +
                " int a = 1;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " int b = 2;" +
                " b = a; }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test2() throws IOException {
        String exprected = "";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main { public static void main(String[] args) {" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos\"}, OwnerPrincipal = \"Robert\" )" +
                " int a = 1;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " int b = 2;" +
                " a = b; }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test3() throws IOException {
        String exprected = "INFORMATION LEAK: \n" +
                "From var: a - Annotation{ owner: Robert, readers:  Lubos Peter}\n" +
                "to var: b - Annotation{ owner: Robert, readers:  Denisa Lubos Peter Erik}\n\n";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main { public static void main(String[] args) {" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos\"}, OwnerPrincipal = \"Robert\" )" +
                " int a = 1;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " int b = 2;" +
                " while(a == 1){ b++; } }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test4() throws IOException {
        String exprected = "";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main { public static void main(String[] args) {" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos\"}, OwnerPrincipal = \"Robert\" )" +
                " int a = 1;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " int b = 2;" +
                " while(b == 1){ a++; } }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }


    @Test
    public void test5() throws IOException {
        String exprected = "INFORMATION LEAK: \n" +
                "From var: a - Annotation{ owner: Robert, readers:  Lubos Peter}\n" +
                "to var: b - Annotation{ owner: Robert, readers:  Denisa Lubos Peter Erik}\n\n";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main { public static void main(String[] args) {" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos\"}, OwnerPrincipal = \"Robert\" )" +
                " int a = 1;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " int b = 2;" +
                " if(a == 1)b++; }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test6() throws IOException {
        String exprected = "";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main { public static void main(String[] args) {" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos\"}, OwnerPrincipal = \"Robert\" )" +
                " int a = 1;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " int b = 2;" +
                " if(b == 1)a++; }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test7() throws IOException {
        String exprected = "INFORMATION LEAK: \n" +
                "From var: a - Annotation{ owner: Robert, readers:  Lubos Peter}\n" +
                "to var: b - Annotation{ owner: Robert, readers:  Denisa Lubos Peter Erik}\n\n";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main { public static void main(String[] args) {" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos\"}, OwnerPrincipal = \"Robert\" )" +
                " int a = 1;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " int b = 2;" +
                " if(a == 1){ b++; } }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test8() throws IOException {
        String exprected = "";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main { public static void main(String[] args) {" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos\"}, OwnerPrincipal = \"Robert\" )" +
                " int a = 1;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " int b = 2;" +
                " if(b == 1){ a++; } }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test9() throws IOException {
        String exprected = "INFORMATION LEAK: \n" +
                "From var: a - Annotation{ owner: Robert, readers:  Lubos Peter}\n" +
                "to var: b - Annotation{ owner: Robert, readers:  Denisa Lubos Peter Erik}\n\n";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main { public static void main(String[] args) {" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos\"}, OwnerPrincipal = \"Robert\" )" +
                " int a = 1;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " int b = 2;" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos\"}, OwnerPrincipal = \"Robert\" )" +
                " int c = 1;" +
                " if(a == 1) { b++; } else { c--;} }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test10() throws IOException {
        String exprected = "";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main { public static void main(String[] args) {" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos\"}, OwnerPrincipal = \"Robert\" )" +
                " int a = 1;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " int b = 2;" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos\"}, OwnerPrincipal = \"Robert\" )" +
                " int c = 1;" +
                " if(b == 1){ b++; } else { c--;} }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test11() throws IOException {
        String exprected = "INFORMATION LEAK: \n" +
                "From var: a - Annotation{ owner: Robert, readers:  Lubos Peter}\n" +
                "to var: b - Annotation{ owner: Robert, readers:  Denisa Lubos Peter Erik}\n\n";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main { public static void main(String[] args) {" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos\"}, OwnerPrincipal = \"Robert\" )" +
                " int a = 1;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " int b = 2;" +
                " do{ b++; } while(a == 1) }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test12() throws IOException {
        String exprected = "";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main { public static void main(String[] args) {" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos\"}, OwnerPrincipal = \"Robert\" )" +
                " int a = 1;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " int b = 2;" +
                " do{ a++; } while(b == 1) }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test13() throws IOException {
        String exprected = "INFORMATION LEAK: \n" +
                "From var: a - Annotation{ owner: Robert, readers:  Lubos Peter}\n" +
                "to var: b - Annotation{ owner: Robert, readers:  Denisa Lubos Peter Erik}\n\n";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main { public static void main(String[] args) {" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos\"}, OwnerPrincipal = \"Robert\" )" +
                " int a = 1;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " int b = 2;" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos\"}, OwnerPrincipal = \"Robert\" )" +
                " int c = 1;" +
                " while(a == 1){ b++; c = 5; } }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test14() throws IOException {
        String exprected = "";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main { public static void main(String[] args) {" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos\"}, OwnerPrincipal = \"Robert\" )" +
                " int a = 1;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " int b = 2;" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos\"}, OwnerPrincipal = \"Robert\" )" +
                " int c = 1;" +
                " while(b == 1){ a++; c = 5; } }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }
    //TODO TODO
    @Test
    public void test15() throws IOException {
        String exprected = "INFORMATION LEAK: \n" +
                "From var: a - Annotation{ owner: Robert, readers:  Denisa Lubos Peter}\n" +
                "to var: b - Annotation{ owner: Robert, readers:  Denisa Lubos Peter Erik}\n\n";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {"+
                "@SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa\"} )" +
                " private static int a = 1; " +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " private static int b = 2;" +
                " public static void main(String[] args) {" +
                " b = a; }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test16() throws IOException {
        String exprected = "";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {"+
                "@SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa\"} )" +
                " private static int a = 1; " +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " private static int b = 2;" +
                " public static void main(String[] args) {" +
                " a = b; }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test17() throws IOException {
        String exprected = "INFORMATION LEAK: \n" +
                "From var: a - Annotation{ owner: Robert, readers:  Denisa Lubos Peter}\n" +
                "to var: b - Annotation{ owner: Robert, readers:  Denisa Lubos Peter Erik}\n\n";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {"+
                "@SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa\"} )" +
                " private static int a = 1; " +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " private static int b = 2;" +
                " public static void main(String[] args) {" +
                " while(a == 1){ b++; } }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test18() throws IOException {
        String exprected = "";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {"+
                "@SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa\"} )" +
                " private static int a = 1; " +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " private static int b = 2;" +
                " public static void main(String[] args) {" +
                " while(b == 1){ a++; } }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }


    @Test
    public void test19() throws IOException {
        String exprected = "INFORMATION LEAK: \n" +
                "From var: a - Annotation{ owner: Robert, readers:  Denisa Lubos Peter}\n" +
                "to var: b - Annotation{ owner: Robert, readers:  Denisa Lubos Peter Erik}\n\n";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {"+
                "@SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa\"} )" +
                " private static int a = 1; " +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " private static int b = 2;" +
                " public static void main(String[] args) {" +
                " if(a == 1)b++; }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test20() throws IOException {
        String exprected = "";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {"+
                "@SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa\"} )" +
                " private static int a = 1; " +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " private static int b = 2;" +
                " public static void main(String[] args) {" +
                " if(b == 1)a++; }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test21() throws IOException {
        String exprected = "INFORMATION LEAK: \n" +
                "From var: a - Annotation{ owner: Robert, readers:  Denisa Lubos Peter}\n" +
                "to var: b - Annotation{ owner: Robert, readers:  Denisa Lubos Peter Erik}\n\n";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {"+
                "@SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa\"} )" +
                " private static int a = 1; " +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " private static int b = 2;" +
                " public static void main(String[] args) {" +
                " if(a == 1){ b++; } }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test22() throws IOException {
        String exprected = "";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {"+
                "@SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa\"} )" +
                " private static int a = 1; " +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " private static int b = 2;" +
                " public static void main(String[] args) {" +
                " if(b == 1){ a++; } }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test23() throws IOException {
        String exprected = "INFORMATION LEAK: \n" +
                "From var: a - Annotation{ owner: Robert, readers:  Denisa Lubos Peter}\n" +
                "to var: b - Annotation{ owner: Robert, readers:  Denisa Lubos Peter Erik}\n\n";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {"+
                "@SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa\"} )" +
                " private static int a = 1; " +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " private static int b = 2;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa\"} )" +
                " private static int c = 3;" +
                " public static void main(String[] args) {" +
                " if(a == 1) { b++; } else { c--;} }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test24() throws IOException {
        String exprected = "";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {"+
                "@SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa\"} )" +
                " private static int a = 1; " +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " private static int b = 2;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa\"} )" +
                " private static int c = 3;" +
                " public static void main(String[] args) {" +
                " if(b == 1){ b++; } else { c--;} }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test25() throws IOException {
        String exprected = "INFORMATION LEAK: \n" +
                "From var: a - Annotation{ owner: Robert, readers:  Denisa Lubos Peter}\n" +
                "to var: b - Annotation{ owner: Robert, readers:  Denisa Lubos Peter Erik}\n\n";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {"+
                "@SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa\"} )" +
                " private static int a = 1; " +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " private static int b = 2;" +
                " public static void main(String[] args) {" +
                " do{ b++; } while(a == 1) }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test26() throws IOException {
        String exprected = "";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {"+
                "@SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa\"} )" +
                " private static int a = 1; " +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " private static int b = 2;" +
                " public static void main(String[] args) {" +
                " do{ a++; } while(b == 1) }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test27() throws IOException {
        String exprected = "INFORMATION LEAK: \n" +
                "From var: a - Annotation{ owner: Robert, readers:  Denisa Lubos Peter}\n" +
                "to var: b - Annotation{ owner: Robert, readers:  Denisa Lubos Peter Erik}\n\n";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {"+
                "@SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa\"} )" +
                " private static int a = 1; " +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " private static int b = 2;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa\"} )" +
                " private static int c = 3;" +
                " public static void main(String[] args) {" +
                " while(a == 1){ b++; c = 5; } }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test28() throws IOException {
        String exprected = "";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {"+
                "@SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa\"} )" +
                " private static int a = 1; " +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " private static int b = 2;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa\"} )" +
                " private static int c = 3;" +
                " public static void main(String[] args) {" +
                " while(b == 1){ a++; c = 5; } }}");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test29() throws IOException {
        String exprected = "INFORMATION LEAK: \n" +
                "From var: a - Annotation{ owner: Robert, readers:  Denisa Lubos Peter}\n" +
                "to var: b - Annotation{ owner: Robert, readers:  Denisa Lubos Peter Erik}\n\n";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {" +
                " public static void main(String[] args) { this.method();} " +
                " private static void method(){" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos, Denisa\"}, OwnerPrincipal = \"Robert\" )" +
                " int a = 1;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " int b = 2;" +
                " b = a; } }");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test30() throws IOException {
        String exprected = "";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {" +
                " public static void main(String[] args) { this.method();} " +
                " private static void method(){" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos, Denisa\"}, OwnerPrincipal = \"Robert\" )" +
                " int a = 1;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " int b = 2;" +
                " a = b; } }");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }


    @Test
    public void test31() throws IOException {
        String exprected = "INFORMATION LEAK: \n" +
                "From var: a - Annotation{ owner: Robert, readers:  Denisa Lubos Peter}\n" +
                "to var: b - Annotation{ owner: Robert, readers:  Denisa Lubos Peter Erik}\n\n";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {" +
                        " public static void main(String[] args) { this.method();} " +
                        " private static void method(){" +
                        " @SecurityType( ReaderPrincipal = {\"Peter, Lubos, Denisa\"}, OwnerPrincipal = \"Robert\" )" +
                        " int a = 1;" +
                        " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                        " int b = 2;" +
                        " if(a == 2){ b += 1; } } }");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test32() throws IOException {
        String exprected = "";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {" +
                " public static void main(String[] args) { this.method();} " +
                " private static void method(){" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos, Denisa\"}, OwnerPrincipal = \"Robert\" )" +
                " int a = 1;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " int b = 2;" +
                " if(b == 2){ a += 1; } } }");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test33() throws IOException {
        String exprected = "INFORMATION LEAK: \n" +
                "From var: a - Annotation{ owner: Robert, readers:  Denisa Lubos Peter}\n" +
                "to var: b - Annotation{ owner: Robert, readers:  Denisa Lubos Peter Erik}\n\n";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {" +
                " public static void main(String[] args) { method();} " +
                " private static void method(){" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos, Denisa\"}, OwnerPrincipal = \"Robert\" )" +
                " int a = 1;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " int b = 2;" +
                " b = a; } }");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test34() throws IOException {
        String exprected = "";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {" +
                " public static void main(String[] args) { method();} " +
                " private static void method(){" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos, Denisa\"}, OwnerPrincipal = \"Robert\" )" +
                " int a = 1;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " int b = 2;" +
                " a = b; } }");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }


    @Test
    public void test35() throws IOException {
        String exprected = "INFORMATION LEAK: \n" +
                "From var: a - Annotation{ owner: Robert, readers:  Denisa Lubos Peter}\n" +
                "to var: b - Annotation{ owner: Robert, readers:  Denisa Lubos Peter Erik}\n\n";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {" +
                " public static void main(String[] args) { method();} " +
                " private static void method(){" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos, Denisa\"}, OwnerPrincipal = \"Robert\" )" +
                " int a = 1;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " int b = 2;" +
                " if(a == 2){ b += 1; } } }");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test36() throws IOException {
        String exprected = "";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {" +
                " public static void main(String[] args) { method();} " +
                " private static void method(){" +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos, Denisa\"}, OwnerPrincipal = \"Robert\" )" +
                " int a = 1;" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " int b = 2;" +
                " if(b == 2){ a += 1; } } }");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test37() throws IOException {
        String exprected = "INFORMATION LEAK: \n" +
                "From var: par - Annotation{ owner: Robert, readers:  Lubos Peter}\n" +
                "to var: b - Annotation{ owner: Robert, readers:  Denisa Lubos Peter Erik}\n\n" +
                "INFORMATION LEAK: \n" +
                "From var: a - Annotation{ owner: Robert, readers:  Lubos Peter}\n" +
                "to var: b - Annotation{ owner: Robert, readers:  Denisa Lubos Peter Erik}\n\n";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {" +
                " public static void main(String[] args) { " +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos\"}, OwnerPrincipal = \"Robert\" )" +
                " int a = 1;"+
                " method(a);} " +
                " private static void method(int par){" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " int b = 2;" +
                " b = par; } }");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test38() throws IOException {
        String exprected = "";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {" +
                " public static void main(String[] args) { " +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos\"}, OwnerPrincipal = \"Robert\" )" +
                " int a = 1;"+
                " method(a);} " +
                " private static void method(int par){" +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " int b = 2;" +
                " par = b; } }");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test39() throws IOException {
        String exprected = "INFORMATION LEAK: \n" +
                "From var: *return_method_execution - Annotation{ owner: Robert, readers:  Lubos Peter}\n" +
                "to var: b - Annotation{ owner: Robert, readers:  Denisa Lubos Peter Erik}\n\n" +
                "INFORMATION LEAK: \n" +
                "From var: par - Annotation{ owner: Robert, readers:  Lubos Peter}\n" +
                "to var: b - Annotation{ owner: Robert, readers:  Denisa Lubos Peter Erik}\n\n" +
                "INFORMATION LEAK: \n" +
                "From var: a - Annotation{ owner: Robert, readers:  Lubos Peter}\n" +
                "to var: b - Annotation{ owner: Robert, readers:  Denisa Lubos Peter Erik}\n\n";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {" +
                " public static void main(String[] args) { " +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos\"}, OwnerPrincipal = \"Robert\" ) " +
                " int a = 1; " +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"} )" +
                " int b = this.method(a); }" +
                " private static void method(int par){ " +
                " return par; } }");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }

    @Test
    public void test40() throws IOException {
        String exprected = "";

        CompilationUnit compilationUnit = JavaParser.parse("public class Main {" +
                " public static void main(String[] args) { " +
                " @SecurityType( ReaderPrincipal = {\"Peter, Lubos, Denisa, Erik\"}, OwnerPrincipal = \"Robert\" ) " +
                " int a = 1; " +
                " @SecurityType( OwnerPrincipal = \"Robert\", ReaderPrincipal = {\"Peter, Lubos\"} )" +
                " int b = this.method(a); }" +
                " private static void method(int par){ " +
                " return par; } }");

        FlowAnalyzer flowAnalyzer = new FlowAnalyzer(compilationUnit);
        String result =  flowAnalyzer.analyze();

        Assert.assertEquals(exprected, result);
    }
}