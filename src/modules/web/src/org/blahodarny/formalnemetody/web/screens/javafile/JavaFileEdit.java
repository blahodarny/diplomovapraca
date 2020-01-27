package org.blahodarny.formalnemetody.web.screens.javafile;

import com.haulmont.cuba.gui.components.SourceCodeEditor;
import com.haulmont.cuba.gui.screen.*;
import org.blahodarny.formalnemetody.entity.JavaFile;

import javax.inject.Inject;

@UiController("formalnemetody_JavaFile.edit")
@UiDescriptor("java-file-edit.xml")
@EditedEntityContainer("javaFileDc")
@LoadDataBeforeShow
public class JavaFileEdit extends StandardEditor<JavaFile> {

    @Inject
    private SourceCodeEditor sourceCodeEditor;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        sourceCodeEditor.setValue(getEditedEntity().getSource());
    }

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        getEditedEntity().setSource(sourceCodeEditor.getRawValue());
    }

}