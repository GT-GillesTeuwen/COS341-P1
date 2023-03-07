import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StateGroup {
    private Set<CompoundState> statesInGroup;

    public StateGroup(){
        this.statesInGroup=new HashSet<>();
    }

    public StateGroup(Set<CompoundState> set){
        this.statesInGroup=set;
    }

    public Set<CompoundState> getStatesInGroup() {
        return statesInGroup;
    }

    public boolean contains(CompoundState cs){
        return statesInGroup.contains(cs);
    }

    
}
