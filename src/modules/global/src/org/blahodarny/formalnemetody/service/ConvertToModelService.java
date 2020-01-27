package org.blahodarny.formalnemetody.service;

import org.blahodarny.formalnemetody.antlr.api.CompilationUnit;

public interface ConvertToModelService {
    String NAME = "formalnemetody_ConvertToModelService";

    void convert( CompilationUnit compilationUnit);
}