package learningsetup;

import net.automatalib.words.Alphabet;

/**
 * Created by Hans on 2-3-2018.
 */
public class AbstractExperiment {

    private String ID;
    private AutomatonType automatonType;
    private String membershipOracle;
    private String equivilanceOracle;
    private Alphabet alphabet;

    public AbstractExperiment(String ID, AutomatonType automatonType, String membershipOracle, String equivilanceOracle, Alphabet alphabet) {
        this.ID = ID;
        this.automatonType = automatonType;
        this.membershipOracle = membershipOracle;
        this.equivilanceOracle = equivilanceOracle;
        this.alphabet = alphabet;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public AutomatonType getAutomatonType() {
        return automatonType;
    }

    public void setAutomatonType(AutomatonType automatonType) {
        this.automatonType = automatonType;
    }

    public String getMembershipOracle() {
        return membershipOracle;
    }

    public void setMembershipOracle(String membershipOracle) {
        this.membershipOracle = membershipOracle;
    }

    public String getEquivilanceOracle() {
        return equivilanceOracle;
    }

    public void setEquivilanceOracle(String equivilanceOracle) {
        this.equivilanceOracle = equivilanceOracle;
    }

    public Alphabet getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(Alphabet alphabet) {
        this.alphabet = alphabet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractExperiment that = (AbstractExperiment) o;

        if (getID() != null ? !getID().equals(that.getID()) : that.getID() != null) return false;
        if (getAutomatonType() != that.getAutomatonType()) return false;
        if (getMembershipOracle() != null ? !getMembershipOracle().equals(that.getMembershipOracle()) : that.getMembershipOracle() != null)
            return false;
        if (getEquivilanceOracle() != null ? !getEquivilanceOracle().equals(that.getEquivilanceOracle()) : that.getEquivilanceOracle() != null)
            return false;
        return getAlphabet() != null ? getAlphabet().equals(that.getAlphabet()) : that.getAlphabet() == null;
    }

    @Override
    public int hashCode() {
        int result = getID() != null ? getID().hashCode() : 0;
        result = 31 * result + (getAutomatonType() != null ? getAutomatonType().hashCode() : 0);
        result = 31 * result + (getMembershipOracle() != null ? getMembershipOracle().hashCode() : 0);
        result = 31 * result + (getEquivilanceOracle() != null ? getEquivilanceOracle().hashCode() : 0);
        result = 31 * result + (getAlphabet() != null ? getAlphabet().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AbstractExperiment{" +
                "ID='" + ID + '\'' +
                ", automatonType=" + automatonType +
                ", membershipOracle='" + membershipOracle + '\'' +
                ", equivilanceOracle='" + equivilanceOracle + '\'' +
                ", alphabet=" + alphabet +
                '}';
    }
}
