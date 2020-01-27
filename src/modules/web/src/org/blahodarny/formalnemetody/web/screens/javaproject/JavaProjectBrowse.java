package org.blahodarny.formalnemetody.web.screens.javaproject;

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.screen.*;
import org.blahodarny.formalnemetody.entity.JavaFile;
import org.blahodarny.formalnemetody.entity.JavaProject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@UiController("formalnemetody_JavaProject.browse")
@UiDescriptor("java-project-browse.xml")
@LookupComponent("javaProjectsTable")
@LoadDataBeforeShow
public class JavaProjectBrowse extends StandardLookup<JavaProject> {

    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Metadata metadata;

    @Subscribe("javaProjectsTable.create")
    public void onJavaProjectsTableCreate(Action.ActionPerformedEvent event) {
        screenBuilders.editor(JavaProject.class, this)
                .withInitializer(this::initJavaProjectWithSecurityType)
                .build()
                .show();
    }

    private void initJavaProjectWithSecurityType(JavaProject javaProject){
        List<JavaFile> sources = new ArrayList<>();
        sources.add(getNewAnnotationFile(javaProject));
        javaProject.setSources(sources);
    }

    private JavaFile getNewAnnotationFile(JavaProject javaProject) {
        JavaFile securityTypeInterface = metadata.create(JavaFile.class);
        securityTypeInterface.setTitle("SecurityType");
        securityTypeInterface.setSource("@interface SecurityType {\n" +
                "\tString OwnerPrincipal () default \"\";\n" +
                "\tString[] ReaderPrincipal() default \"\";\n" +
                "}");
        securityTypeInterface.setProject(javaProject);
        return securityTypeInterface;
    }


}