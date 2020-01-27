package org.blahodarny.formalnemetody.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.CurrencyValue;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NamePattern("%s|title")
@Table(name = "FORMALNEMETODY_JAVA_FILE")
@Entity(name = "formalnemetody_JavaFile")
public class JavaFile extends StandardEntity {
    private static final long serialVersionUID = -3537498118702349134L;

    @NotNull
    @Column(name = "TITLE", nullable = false)
    @CurrencyValue(currency = ".java")
    protected String title;

    @NotNull
    @Lob
    @Column(name = "SOURCE", nullable = false)
    protected String source;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PROJECT_ID")
    protected JavaProject project;

    public JavaProject getProject() {
        return project;
    }

    public void setProject(JavaProject project) {
        this.project = project;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}