package org.blahodarny.formalnemetody.web.screens.javaproject;

import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TextArea;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.screen.*;
import org.blahodarny.formalnemetody.antlr.JavaParser;
import org.blahodarny.formalnemetody.antlr.api.CompilationUnit;
import org.blahodarny.formalnemetody.entity.JavaFile;
import org.blahodarny.formalnemetody.entity.JavaProject;
import org.blahodarny.formalnemetody.web.analyzer.FlowAnalyzer;

import javax.inject.Inject;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@UiController("formalnemetody_JavaProject.edit")
@UiDescriptor("java-project-edit.xml")
@EditedEntityContainer("javaProjectDc")
@LoadDataBeforeShow
public class JavaProjectEdit extends StandardEditor<JavaProject> {

    @Inject
    private Table<JavaFile> sourcesTable;

    @Inject
    private CollectionPropertyContainer<JavaFile> sourceDc;

    @Inject
    private Dialogs dialogs;
    @Inject
    private TextArea<String> resultArea;
    @Inject
    private ScreenBuilders screenBuilders;

    @Subscribe("sourcesTable.edit")
    public void onSourcesTableEdit(Action.ActionPerformedEvent event) {
        screenBuilders.editor(sourcesTable)
                .withOpenMode(OpenMode.NEW_TAB)
                .build()
                .show();
    }

    @Subscribe("sourcesTable.parse")
    public void onSourcesTableParse(Action.ActionPerformedEvent event) {
        resultArea.setValue(null);
        String source = Optional.ofNullable(sourceDc.getMutableItems())
                .orElse(new ArrayList<>())
                .stream()
                .map(JavaFile::getSource)
                .collect(Collectors.joining("\n\n"));

        CompilationUnit compilationUnit = null;
        try {
            compilationUnit = JavaParser.parse(source);

            FlowAnalyzer analyzer = new FlowAnalyzer(compilationUnit);
            String vysledok = analyzer.analyze();

            if(vysledok != null && vysledok.isEmpty()){
                vysledok = "Everything seems fine.";
            }
            resultArea.setValue(vysledok);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}