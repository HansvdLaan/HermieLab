package settings.transformations;

import settings.Settings;
import settings.containers.GeneratorInformationElement;

import java.util.Set;

public class TransformationResult {

    private Settings newSettings;
    private Set<GeneratorInformationElement> generatedElements;

    public TransformationResult(Settings newSettings, Set<GeneratorInformationElement> generatedElements) {
        this.newSettings = newSettings;
        this.generatedElements = generatedElements;
    }

    public Settings getNewSettings() {
        return newSettings;
    }

    public void setNewSettings(Settings newSettings) {
        this.newSettings = newSettings;
    }

    public Set<GeneratorInformationElement> getGeneratedElements() {
        return generatedElements;
    }

    public void setGeneratedElements(Set<GeneratorInformationElement> generatedElements) {
        this.generatedElements = generatedElements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransformationResult that = (TransformationResult) o;

        if (getNewSettings() != null ? !getNewSettings().equals(that.getNewSettings()) : that.getNewSettings() != null)
            return false;
        return getGeneratedElements() != null ? getGeneratedElements().equals(that.getGeneratedElements()) : that.getGeneratedElements() == null;
    }

    @Override
    public int hashCode() {
        int result = getNewSettings() != null ? getNewSettings().hashCode() : 0;
        result = 31 * result + (getGeneratedElements() != null ? getGeneratedElements().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TransformationResult{" +
                "newSettings=" + newSettings +
                ", generatedElements=" + generatedElements +
                '}';
    }
}
