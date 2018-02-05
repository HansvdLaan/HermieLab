package settings;

import settings.containers.GeneratorInformationElement;

import java.util.HashMap;
import java.util.Map;

public class Settings {

    private Map<String,Map<String,GeneratorInformationElement>> internalMap;

    public Settings(){
        internalMap = new HashMap<>();
    }

    public Map<String,Map<String,GeneratorInformationElement>> getAllSettings(){
        return internalMap;
    }

    public void setAllSettings(Map<String,Map<String,GeneratorInformationElement>> settings) {
        this.internalMap = settings;
    }

    public Map<String,GeneratorInformationElement> getSettingsByType(String type) {
        internalMap.putIfAbsent(type, new HashMap<>());
        return internalMap.get(type);
    }

    public void setSettingsByType(String type, Map<String,GeneratorInformationElement> settings){
        internalMap.put(type, settings);
    }

    public void addSettingsByType(String type, Map<String,GeneratorInformationElement> settings){
        getSettingsByType(type).putAll(settings);
    }

    public GeneratorInformationElement getSettingsByTypeAndID(String type, String id){
        getSettingsByType(type).putIfAbsent(id, null);
        return getSettingsByType(type).get(id);
    }

    public void setSettingsByTypeAndID(String type, String id, GeneratorInformationElement settings){
        getSettingsByType(type).put(id, settings);
    }

    public void addSettingsByTypeAndID(String type, String id, GeneratorInformationElement settings){
        getSettingsByType(type).put(id, settings);
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
}
