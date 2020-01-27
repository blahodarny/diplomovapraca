package org.blahodarny.formalnemetody.model.ctx;

import org.blahodarny.formalnemetody.model.values.Var;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Context {

    protected String name;
    protected Context parent;
    protected List<Context> childs;
    protected Map<String, Var> values;

    public Context(String name, Context parent) {
        this.name = name;
        this.parent = parent;
        this.values = new HashMap<>();
        this.childs = new ArrayList<>();
    }

    public void addValue(Var var){
        values.put(var.getName(), var);
    }

    public Var getValue(String name){
        Var var = values.get(name);
        if(var == null && parent != null){
            return parent.getValue(name);
        }
        return var;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Context getParent() {
        return parent;
    }

    public void setParent(Context parent) {
        this.parent = parent;
    }

    public List<Context> getChilds() {
        return childs;
    }

    public void addChild(Context child) {
        this.childs.add(child);
    }
}
