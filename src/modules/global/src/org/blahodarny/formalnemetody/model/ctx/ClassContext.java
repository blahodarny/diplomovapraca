package org.blahodarny.formalnemetody.model.ctx;

import java.util.HashMap;
import java.util.Map;

public class ClassContext extends Context {

    private Map<String, MethodContext> methods;

    public ClassContext(String name, Context parent) {
        super(name, parent);
        methods = new HashMap<>();
    }

    @Override
    public void addChild(Context child) {
        super.addChild(child);
        if(child instanceof MethodContext && child.getName() != null && !child.getName().isEmpty()){
            methods.put(child.getName(), (MethodContext) child);
        }
    }

    public Map<String, MethodContext> getMethods() {
        return methods;
    }

    public void setMethods(Map<String, MethodContext> methods) {
        this.methods = methods;
    }

    public void addExpression(MethodContext body){
        methods.put(body.name, body);
    }

    public MethodContext getMethod(String name){
        MethodContext method = methods.get(name);
        return method;
    }
}
