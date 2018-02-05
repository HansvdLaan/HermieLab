package component;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;
import java.util.Map;

public class GeneratedSpecContainer {

    private Map<String,Map<String,MethodSpec>> internalMethodMap;
    private Map<String,Map<String,FieldSpec>> internalFieldMap;
    private Map<String,TypeSpec> otherComponents;

    public GeneratedSpecContainer(){
            internalMethodMap = new HashMap<>();
            internalFieldMap = new HashMap<>();
            otherComponents = new HashMap<>();
    }

    public Map<String,Map<String,MethodSpec>> getAllMethods(){
        return internalMethodMap;
    }

    public void setAllMethods(Map<String,Map<String,MethodSpec>> methods) {
        this.internalMethodMap = methods;
    }

    public Map<String,MethodSpec> getMethodsByType(String type) {
        internalMethodMap.putIfAbsent(type, new HashMap<>());
        return internalMethodMap.get(type);
    }

    public void setMethodsByType(String type, Map<String,MethodSpec> methods){
        internalMethodMap.put(type, methods);
    }

    public void addMethodsByType(String type, Map<String,MethodSpec> methods){
        getMethodsByType(type).putAll(methods);
    }

    public MethodSpec getMethodsByTypeAndID(String type, String id){
        getMethodsByType(type).putIfAbsent(id, null);
        return getMethodsByType(type).get(id);
    }

    public void setMethodsByTypeAndID(String type, String id, MethodSpec methods){
        getMethodsByType(type).put(id, methods);
    }

    public void addMethodsByTypeAndID(String type, String id, MethodSpec methods){
        getMethodsByType(type).put(id, methods);
    }

    public Map<String,Map<String,FieldSpec>> getAllFields(){
        return internalFieldMap;
    }

    public void setAllFields(Map<String,Map<String,FieldSpec>> fields) {
        this.internalFieldMap = fields;
    }

    public Map<String,FieldSpec> getFieldsByType(String type) {
        internalFieldMap.putIfAbsent(type, new HashMap<>());
        return internalFieldMap.get(type);
    }

    public void setFieldsByType(String type, Map<String,FieldSpec> fields){
        internalFieldMap.put(type, fields);
    }

    public void addFieldsByType(String type, Map<String,FieldSpec> fields){
        getFieldsByType(type).putAll(fields);
    }

    public FieldSpec getFieldsByTypeAndID(String type, String id){
        getFieldsByType(type).putIfAbsent(id, null);
        return getFieldsByType(type).get(id);
    }

    public void setFieldsByTypeAndID(String type, String id, FieldSpec fields){
        getFieldsByType(type).put(id, fields);
    }

    public void addFieldsByTypeAndID(String type, String id, FieldSpec fields){
        getFieldsByType(type).put(id, fields);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneratedSpecContainer that = (GeneratedSpecContainer) o;

        if (internalMethodMap != null ? !internalMethodMap.equals(that.internalMethodMap) : that.internalMethodMap != null)
            return false;
        return internalFieldMap != null ? internalFieldMap.equals(that.internalFieldMap) : that.internalFieldMap == null;
    }

    @Override
    public int hashCode() {
        int result = internalMethodMap != null ? internalMethodMap.hashCode() : 0;
        result = 31 * result + (internalFieldMap != null ? internalFieldMap.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GeneratedSpecContainer{" +
                "internalMethodMap=" + internalMethodMap +
                ", internalFieldMap=" + internalFieldMap +
                '}';
    }

    public Map<String, TypeSpec> getComponents() {
        return otherComponents;
    }

    public void addComponent(String id, TypeSpec component) {
        otherComponents.put(id,component);
    }

    public void setComponents(Map<String, TypeSpec> otherComponents) {
        this.otherComponents = otherComponents;
    }
}
