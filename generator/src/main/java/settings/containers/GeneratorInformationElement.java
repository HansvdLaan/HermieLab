package settings.containers;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Hans on 31-12-2017.
 */
public class GeneratorInformationElement{

    private String id;
    private String type;
    private Map<String,Object> data;


    public GeneratorInformationElement(String type, String id, Map<String,Object> data){
        this.id = id;
        this.type = type;
        this.data = data;
        data.put("type",type);
        data.put("id",id);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Map<String, Object> getDataSubset(String regex){
        return data.keySet().stream()
                .filter(key -> key.matches(regex))
                .collect(Collectors.toMap(Function.identity(), data::get));
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneratorInformationElement element = (GeneratorInformationElement) o;

        if (getId() != null ? !getId().equals(element.getId()) : element.getId() != null) return false;
        if (getType() != null ? !getType().equals(element.getType()) : element.getType() != null) return false;
        return getData() != null ? getData().equals(element.getData()) : element.getData() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getData() != null ? getData().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GeneratorInformationElement{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", data=" + data +
                '}';
    }
}
