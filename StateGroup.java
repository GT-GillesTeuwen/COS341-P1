import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StateGroup {
    private Set<CompoundState> statesInGroup;
    private boolean isFinal;
    private boolean isInitial;
    private String name;

    public StateGroup() {
        this.statesInGroup = new HashSet<>();
        setFinalFromStates();
        name = "";
    }

    public StateGroup(Set<CompoundState> set) {
        this.statesInGroup = set;
        setFinalFromStates();
        name = "";
    }

    public void setInitial(boolean isInitial) {
        this.isInitial = isInitial;
    }

    public Set<CompoundState> getStatesInGroup() {
        return statesInGroup;
    }

    public boolean contains(CompoundState cs) {
        return statesInGroup.contains(cs);
    }

    public String getName() {

        if (name.equals("")) {

            String out = "";
            for (CompoundState compoundState : statesInGroup) {
                out += compoundState.getName() + ",";
            }
            return out;
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void setFinalFromStates() {
        for (CompoundState compoundState : statesInGroup) {
            if (compoundState.isFinal()) {
            
            this.isFinal = true;
            return;
            }
        }
        this.isFinal = false;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public boolean isInitial() {
        return isInitial;
    }

}
