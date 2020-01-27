package org.blahodarny.formalnemetody.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@NamePattern("%s|title")
@Table(name = "FORMALNEMETODY_JAVA_PROJECT")
@Entity(name = "formalnemetody_JavaProject")
public class JavaProject extends StandardEntity {
    private static final long serialVersionUID = -97024132015981130L;

    @NotNull
    @Column(name = "TITLE", nullable = false)
    protected String title;

    @OneToMany(mappedBy = "project")
    protected List<JavaFile> sources;

    public List<JavaFile> getSources() {
        return sources;
    }

    public void setSources(List<JavaFile> sources) {
        this.sources = sources;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}