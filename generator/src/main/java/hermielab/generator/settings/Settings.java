package hermielab.generator.settings;

import hermielab.generator.settings.containers.GeneratorInformationElement;

import java.util.*;

public class Settings {

    private Map<String,Map<String,GeneratorInformationElement>> internalMap; //Type,ID,Element

    public Settings(){
        internalMap = new HashMap<>();
    }

    public Settings(GeneratorInformationElement... elements){
        this(Arrays.asList(elements));
    }

    public Settings(Collection<GeneratorInformationElement> elements){
        this();
        addElements(elements);
    }

    public Map<String,Map<String,GeneratorInformationElement>> getAllSettings(){
        return internalMap;
    }

    public void setAllSettings(Map<String,Map<String,GeneratorInformationElement>> settings) {
        this.internalMap = settings;
    }

    public Map<String,GeneratorInformationElement> getSettingsByType(String type) {
        if (internalMap.get(type) == null) {
            return new HashMap<>();
        } else {
            return internalMap.get(type);
        }
    }

    public void addElement(GeneratorInformationElement element){
        internalMap.putIfAbsent(element.getType(), new HashMap<>());
        internalMap.get(element.getType()).put(element.getID(), element);
    }

    public void addReference(String type, String ID){
        internalMap.putIfAbsent(type, new HashMap<>());
        internalMap.get(type).put(ID, null);
    }

    public void addElements(Collection<GeneratorInformationElement> elements){
        for (GeneratorInformationElement element: elements){
            addElement(element);
        }
    }

    public GeneratorInformationElement getSettingsByTypeAndID(String type, String id){
        getSettingsByType(type).putIfAbsent(id, null);
        return getSettingsByType(type).get(id);
    }

    public boolean containsElement(GeneratorInformationElement element) {
        if (internalMap.keySet().contains(element.getType()) &&
                internalMap.get(element.getType()).keySet().contains(element.getID())) {
            return Objects.equals(element, getSettingsByTypeAndID(element.getType(), element.getID()));
        } else {
            return false;
        }
    }

    public boolean containsReference(String type, String ID) {
        if (internalMap.keySet().contains(type) &&
                internalMap.get(type).keySet().contains(ID)) {
            return internalMap.get(type).get(ID) == null;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Settings settings = (Settings) o;

        return internalMap != null ? internalMap.equals(settings.internalMap) : settings.internalMap == null;
    }

    @Override
    public int hashCode() {
        return internalMap != null ? internalMap.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "internalMap=" + internalMap +
                '}';
    }

    public int size(){
        int size = 0;
        for (String key: getAllSettings().keySet()){
            size += getAllSettings().get(key).keySet().size();
        }
        return size;
    }
}
