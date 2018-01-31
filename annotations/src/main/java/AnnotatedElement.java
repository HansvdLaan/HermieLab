import javax.lang.model.element.Element;
import java.util.Map;

public class AnnotatedElement implements Comparable{

    private String id;
    private Element element;
    private Map<String,String> parameters;
    private int order = -2;


    public AnnotatedElement(Element element, Map<String,String> parameters){
        this.element = element;
        this.parameters = parameters;
    }

    public AnnotatedElement(Element element, Map<String,String> parameters, String id){
        this.element = element;
        this.parameters = parameters;
        this.id = id;
    }

    public AnnotatedElement(Element element, Map<String,String> parameters, int order){
        this.element = element;
        this.parameters = parameters;
        this.order = order;
    }

    public AnnotatedElement(Element element, Map<String,String> parameters, String id, int order) {
        this.element = element;
        this.parameters = parameters;
        this.id = id;
        this.order = order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnnotatedElement that = (AnnotatedElement) o;

        if (getOrder() != that.getOrder()) return false;
        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getElement() != null ? !getElement().equals(that.getElement()) : that.getElement() != null)
            return false;
        return getParameters() != null ? getParameters().equals(that.getParameters()) : that.getParameters() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getElement() != null ? getElement().hashCode() : 0);
        result = 31 * result + (getParameters() != null ? getParameters().hashCode() : 0);
        result = 31 * result + getOrder();
        return result;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof AnnotatedElement) {
            AnnotatedElement ce = (AnnotatedElement) o;
            if (order < ce.getOrder()) {
                return -1;
            } else if (order > ce.getOrder()) {
                return 1;
            } else {
                return 0;
            }
        } else {
            throw new IllegalArgumentException("parameter should be instance of AnnotatedElement");
        }
    }
}
