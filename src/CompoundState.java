import java.util.HashSet;
import java.util.Set;

public class CompoundState {
    private Set<State> states;
    private boolean isFinal;
    private boolean isInitial;
    private String name;

    public CompoundState(Set<State> stateSet,boolean isInitial){
        this.name="";
        this.states=new HashSet<>();
        this.isFinal=false;
        this.isInitial=isInitial;
        for (State state : stateSet) {
            addState(state);
        }
    }

    public boolean isFinal() {
        return isFinal;
    }

    public boolean isInitial() {
        return isInitial;
    }

    public CompoundState(boolean isInitial){
        this.name="";
        this.states=new HashSet<>();
        this.isFinal=false;
        this.isInitial=isInitial;

    }

    public void addState(State s){
        if(s.isFinal()){
            isFinal=true;
        }
        states.add(s);
    }

    public void addAllStates(Set<State> allStates){
        for (State state : allStates) {
          addState(state);
        }
    }

    public boolean equals(CompoundState cs){
        return this.states.equals(cs.states);
    }

    public boolean contains(State state){
        return states.contains(state);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        
        if(states.size()==0){
            return "Ø";
        }
        if(this.name!=""){
            return this.name;
        }
        String out="";
        for (State state : states) {
            out+=state.getName()+",";
        }
       
        return out;
    }

    public Set<State> getStates() {
        return states;
    }

    @Override
    public int hashCode() {
        int sum=0;
        for (State state : states) {
            sum+=state.hashCode();
        }
        return sum;
    }

    @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompoundState s = (CompoundState) o;
    
    return this.getName().equals(s.getName());
    
  }
}
