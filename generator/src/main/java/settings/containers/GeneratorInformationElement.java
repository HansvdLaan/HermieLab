package settings.containers;

import net.automatalib.commons.util.Pair;
import settings.transformations.exceptions.IncorrectElementException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Hans on 31-12-2017.
 */
public class GeneratorInformationElement implements Comparable<GeneratorInformationElement>{

    private String id;
    private String type;
    private Map<String,Object> singleAttributes;
    private Map<String, List<Object>> ordenedAttributes;
    private List<Pair<String,String>> parents;
    private List<Pair<String,String>> children;


    public GeneratorInformationElement(String type, String id){
        this.id = id;
        this.type = type;
        singleAttributes = new HashMap<>();
        ordenedAttributes = new HashMap<>();
        singleAttributes.put("type",type);
        singleAttributes.put("id",id);
        parents = new LinkedList<>();
        children = new LinkedList<>();
    }

    public GeneratorInformationElement(String type, String id, GeneratorInformationElement parent){
        this(type,id);
        if (parent != null) {
            this.parents.add(new Pair<>(parent.getType(), parent.getID()));
        }
    }

    public GeneratorInformationElement(String type, String id, Map<String, Object> attributes){
        this(type,id);
        for (String attributeType: attributes.keySet()){
            if (!(Objects.equals(attributeType, "id") || Objects.equals(attributeType, "type"))) {
                addAttribute(attributeType, attributes.get(attributeType));
            }
        }
    }

    public GeneratorInformationElement(String type, String id, GeneratorInformationElement parent, Map<String, Object> attributes){
        this(type,id,parent);
        for (String attributeType: attributes.keySet()){
            if (!(Objects.equals(attributeType, "id") || Objects.equals(attributeType, "type"))) {
                addAttribute(attributeType, attributes.get(attributeType));
            }
        }
    }

    public GeneratorInformationElement(String type, String id, GeneratorInformationElement parent,  Object... attributes){
        this(type,id,parent);
        for (int i = 0; i < attributes.length; i += 2){
            String attributeType = String.valueOf(attributes[i]);
            this.addAttribute(attributeType, attributes[i+1]);
        }
    }

    public GeneratorInformationElement(String type, String id, GeneratorInformationElement parent, Map<String,Object> singleAttributes, Map<String,List<Object>> ordenedAttributes){
        this(type,id,parent);
        this.getSingleAttributes().putAll(singleAttributes);
        this.getOrdenedAttributes().putAll(ordenedAttributes);
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        overwriteAttribute("type",type);
    }

    public void addAttribute(String type, Object value) {
        if (Objects.equals(type, "type") || Objects.equals(type, "id")) {
            throw new IncorrectElementException("cannot edit " + type + " of an Generator Information Element");
        }
        if (!singleAttributes.containsKey(type) && !ordenedAttributes.containsKey(type)) {
            if (value instanceof List) {
                ordenedAttributes.put(type, new LinkedList<Object>( (Collection) value));
            } else {
                singleAttributes.put(type, value);
            }
        }
        else if (ordenedAttributes.containsKey(type)) {
            ordenedAttributes.get(type).add(value);
        } else if (singleAttributes.containsKey(type)) {
            List<Object> attributes = new LinkedList<>(Arrays.asList(singleAttributes.get(type)));
            attributes.add(value);
            singleAttributes.remove(type);
            ordenedAttributes.put(type, attributes);
        }
    }

    public List<Object> getAttribute(String type) {
        if (singleAttributes.containsKey(type)) {
            return new ArrayList<>(Arrays.asList(singleAttributes.get(type)));
        } else if (ordenedAttributes.containsKey(type)) {
            List<Object> safeOrdenedAttributes = new LinkedList<>(); //with null values removed
            for (Object o : this.ordenedAttributes.get(type)) {
                if (o != null) {
                    safeOrdenedAttributes.add(o);
                }
            }
            return safeOrdenedAttributes;
        } else {
            return new ArrayList<>();
        }
    }

    public boolean hasAttribute(String type) {
        return singleAttributes.containsKey(type) || ordenedAttributes.containsKey(type);
    }

    public List<String> getStringAttribute(String type) {
        System.out.println("nullCheck: " + singleAttributes ==  null || ordenedAttributes == null);
        if (singleAttributes.containsKey(type)) {
            return new ArrayList<>(Collections.singletonList(singleAttributes.get(type).toString()));
        } else if (ordenedAttributes.containsKey(type)){
            System.out.println("I WAS HERE!");
            System.out.println("ATTR:" + ordenedAttributes.get(type));
            return getAttribute(type)
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        } else {
            return new LinkedList<>();
        }
    }

    public void overwriteAttribute(String type, Object value) {
        if (ordenedAttributes.containsKey(type)) {
            ordenedAttributes.remove(type);
        }
        singleAttributes.put(type,value);
    }

    public void overwriteAttribute(String type, List<Object> values) {
        singleAttributes.remove(type);
        ordenedAttributes.remove(type);
        ordenedAttributes.put(type,new LinkedList<>(values));
    }

    public void removeAttribute(String type) {
        singleAttributes.remove(type);
        ordenedAttributes.remove(type);
    }
    public void addSingleAttribute(String type, Object value) {
        singleAttributes.put(type,value);
    }

    public void removeSingleAttribute(String type) {
        singleAttributes.remove(type);
    }

    public void addToListedAttributes(String type, Object value) {
        if (!ordenedAttributes.containsKey(type)){
            ordenedAttributes.put(type, new LinkedList<>());
        }
        ordenedAttributes.get(type).add(value);
    }

    public void addToListedAttributes(String type, Object value, int index) {
        if (!ordenedAttributes.containsKey(type)){
            ordenedAttributes.put(type, new LinkedList<>());
        }
        List<?> attributes = ordenedAttributes.get(type);
        int amountOfAttributes = attributes.size();
        for (int i = 0; i < index; i++) {
            if(i >= amountOfAttributes) {
                attributes.add(null);
            }
        }
        ordenedAttributes.get(type).add(index, value);
    }

    public void removeListedAttribute(String type, int index) {
        ordenedAttributes.get(type).remove(index);
    }

    public void removeListedAttribute(String type, Object value) {
        ordenedAttributes.get(type).remove(value);
    }

    public Map<String, List<Object>> getOrdenedAttributes() {
        return ordenedAttributes;
    }

    public Map<String, Object> getAttributeSubset(String regex){
        Map subset = new HashMap();
        subset.putAll(singleAttributes.keySet().stream()
                .filter(key -> key.matches(regex))
                .collect(Collectors.toMap(Function.identity(), singleAttributes::get)));
        subset.putAll(ordenedAttributes.keySet().stream()
                .filter(key -> key.matches(regex))
                .collect(Collectors.toMap(Function.identity(), ordenedAttributes::get)));
        return subset;
    }

    public Map<String, Object> getSingleAttributes() {
        return singleAttributes;
    }

    public void setSingleAttributes(Map<String, Object> singleAttributes) {
        this.singleAttributes = singleAttributes;
    }

    public void setOrdenedAttributes(Map<String, List<Object>> ordenedAttributes) {
        this.ordenedAttributes = ordenedAttributes;
    }

    public Map<String, Object> getAttributes(){
        Map<String,Object> map = new HashMap();
        map.putAll(getSingleAttributes());
        map.putAll(getOrdenedAttributes());
        return map;
    }
    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
        overwriteAttribute("id",id);
    }

    public List<Pair<String,String>> getParents() {
        return this.parents;
    }

    public boolean hasParents(){
        return !this.parents.isEmpty();
    }

    public void addParent(GeneratorInformationElement parent){
        this.parents.add(new Pair<>(parent.getType(), parent.getID()));
    }

    public void setParents(LinkedList<GeneratorInformationElement> parents) {
        for (GeneratorInformationElement parent: parents){
            this.parents.add(new Pair<>(parent.getType(), parent.getID()));
        }
    }

    public List<Pair<String, String>> getChildren() {
        return children;
    }

    public boolean hasChildren() {
        return !this.children.isEmpty();
    }

    public void addChild(GeneratorInformationElement child) {
        this.children.add(new Pair<>(child.getType(), child.getID()));
    }

    public void setChildren(LinkedList<GeneratorInformationElement> children) {
        for (GeneratorInformationElement child: children){
            this.children.add(new Pair<>(child.getType(), child.getID()));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneratorInformationElement element = (GeneratorInformationElement) o;

        if (id != null ? !id.equals(element.id) : element.id != null) return false;
        if (getType() != null ? !getType().equals(element.getType()) : element.getType() != null) return false;
        if (getSingleAttributes() != null ? !getSingleAttributes().equals(element.getSingleAttributes()) : element.getSingleAttributes() != null)
            return false;
        if (getOrdenedAttributes() != null ? !getOrdenedAttributes().equals(element.getOrdenedAttributes()) : element.getOrdenedAttributes() != null)
            return false;
        return getParents() != null ? getParents().equals(element.getParents()) : element.getParents() == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getSingleAttributes() != null ? getSingleAttributes().hashCode() : 0);
        result = 31 * result + (getOrdenedAttributes() != null ? getOrdenedAttributes().hashCode() : 0);
        result = 31 * result + (getParents() != null ? getParents().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GeneratorInformationElement{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", singleAttributes=" + singleAttributes +
                ", ordenedAttributes=" + ordenedAttributes +
                ", parents=" + parents +
                '}';
    }

    @Override
    public int compareTo(GeneratorInformationElement element) {
        int result = this.getType().compareTo(element.getType());
        if (result != 0) {
            return result;
        } else  {
            return this.getID().compareTo(element.getID());
        }
    }
}
