import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class DFA {
    public static final String EPSILON = "ε";
    private Set<CompoundState> states;
    private Set<String> alphabet;
    private Map<OrderedPair<CompoundState, String>, CompoundState> transition; // Structure (current state +
    // alphabet-element)->(state that will be reached)
    private State initialState;
    private Set<CompoundState> acceptStates;
    private NFA nfa;

    public DFA(NFA nfa) {
        this.nfa = nfa;
        this.states = new HashSet<>();
        CompoundState nullState=new CompoundState(false);
        states.add(nullState);
        this.initialState = nfa.getInitialState();
        this.transition = new HashMap<>();
        this.alphabet=nfa.getAlphabet().keySet();
        this.acceptStates=new HashSet<>();

        // Build Epsilon Closures for every State
        Map<State, Set<State>> allEpsilonClosures = new HashMap<>();
        for (State state : nfa.getStates().keySet()) {
            allEpsilonClosures.put(state, makeEpsilonClosureForState(state));
        }

        // Add initial to transition functions
        Queue<CompoundState> processQueue = new LinkedList<>();
        Set<State> ecOfInitial = allEpsilonClosures.get(initialState);
        for (String str : nfa.getAlphabet().keySet()) {
            CompoundState symbolTransitions = new CompoundState(false);
            for (State state : ecOfInitial) {
                Map<State, State> t = nfa.getTransition().get(new OrderedPair<State, String>(state, str));
                if (t != null) {
                    Set<State> transitionStates = t.keySet();
                    for (State closureIndex : transitionStates) {
                        symbolTransitions.addAllStates(allEpsilonClosures.get(closureIndex));
                       
                    }
                }
            }
            CompoundState compoundState = new CompoundState(ecOfInitial,true);
            if(statesContains(compoundState)){
                compoundState=findCompoundState(compoundState);
            }else{
                this.states.add(compoundState);
            }
            if(!symbolTransitions.getStates().isEmpty() && !statesContains(symbolTransitions)){
                processQueue.add(symbolTransitions);
                this.states.add(symbolTransitions);
            }
            if(symbolTransitions.getStates().isEmpty()){
                symbolTransitions=nullState;
                
            }
            if(statesContains(symbolTransitions)){
                symbolTransitions=findCompoundState(symbolTransitions);
            }
           
            transition.put(new OrderedPair<CompoundState, String>(compoundState, str), symbolTransitions);
        }

        while (!processQueue.isEmpty()) {
            Set<State> ecOfNext = new HashSet<>();
            Set<State> cur = processQueue.remove().getStates();
            for (State state : cur) {
                ecOfNext.addAll(allEpsilonClosures.get(state));
            }
            for (String str : nfa.getAlphabet().keySet()) {
                CompoundState symbolTransitions = new CompoundState(false);
                for (State state : ecOfNext) {
                    Map<State, State> t = nfa.getTransition().get(new OrderedPair<State, String>(state, str));
                    if (t != null) {
                        Set<State> transitionStates = t.keySet();
                        for (State closureIndex : transitionStates) {
                            symbolTransitions.addAllStates(allEpsilonClosures.get(closureIndex));
                            
                        }
                    }
                }
                CompoundState compoundState = new CompoundState(ecOfNext,false);
                if(statesContains(compoundState)){
                    compoundState=findCompoundState(compoundState);
                }else{
                    this.states.add(compoundState);
                }
              
                if(!symbolTransitions.getStates().isEmpty() && !statesContains(symbolTransitions)){
                    processQueue.add(symbolTransitions);
                    this.states.add(symbolTransitions);
                }
                if(symbolTransitions.getStates().isEmpty()){
                    symbolTransitions=nullState;
                    
                }
                if(statesContains(symbolTransitions)){
                    symbolTransitions=findCompoundState(symbolTransitions);
                }
                
                transition.put(new OrderedPair<CompoundState, String>(compoundState, str), symbolTransitions);
            }
        }

        for (String symbol : alphabet) {
            transition.put(new OrderedPair<CompoundState, String>(nullState, symbol), nullState);
        }

        buildAcceptStates();
        renameStates();
    }

    private Set<State> makeEpsilonClosureForState(State s) {
        Set<State> epsilonClosure = new HashSet();
        epsilonClosure.add(s);
        epsilonClosure.addAll(getEpsilonTransitions(s));
        return epsilonClosure;

    }

    private Set<State> unionEpsilonClosures(State s1, State s2, Map<State, Set<State>> allEpsilonClosures) {
        Set<State> union = new HashSet<>();
        union.addAll(allEpsilonClosures.get(s1));
        union.addAll(allEpsilonClosures.get(s2));
        return union;
    }

    private Set<State> getEpsilonTransitions(State s) {

        Set<State> epsilonTransitions = new HashSet<>();
        Map<State, State> trans = (nfa.getTransition().get(new OrderedPair<State, String>(s, EPSILON)));
        if (trans != null) {
            epsilonTransitions.addAll(trans.keySet());
        }
        Set<State> newEpsilonTransitions = new HashSet<>();
        newEpsilonTransitions.addAll(epsilonTransitions);
        for (State state : epsilonTransitions) {
            newEpsilonTransitions.addAll(getEpsilonTransitions(state));
        }
        return newEpsilonTransitions;
    }

    public Set<CompoundState> getStates() {
        return states;
    }

    public Set<String> getAlphabet() {
        return alphabet;
    }

    public Set<CompoundState> getAcceptStates() {
        return acceptStates;
    }

    public Map<OrderedPair<CompoundState, String>, CompoundState> getTransition() {
        return transition;
    }

    private boolean statesContains(CompoundState cs){
        for (CompoundState compoundState : states) {
            if(compoundState.hashCode()==cs.hashCode()){
                return true;
            }
        }
        return false;
    }

    private CompoundState findCompoundState(CompoundState cs){
        for (CompoundState compoundState : states) {
            if(compoundState.hashCode()==cs.hashCode()){
                return compoundState;
            }
        }
       
        return null;
    }

    private void buildAcceptStates(){
        for (CompoundState compoundState : states) {
            if(compoundState.isFinal()){
                acceptStates.add(compoundState);
            }
        }
    }

    public void renameStates(){
        int num=0;
        for (CompoundState compoundState : states) {
            compoundState.setName("q"+num);
            num++;
        }
    }

    public Map<OrderedPair<CompoundState, String>, CompoundState> getStateTransitions(CompoundState cs) {
        Map<OrderedPair<CompoundState, String>, CompoundState> stateTransitions = new HashMap<>();
        for (OrderedPair<CompoundState, String> op : transition.keySet()) {
          if (op.getObj1().equals(cs)) {
            stateTransitions.put(op, transition.get(op));
          }
        }
        return stateTransitions;
      }

      public String toXML(){
        String out="";
        out+="<States>\n";
        for (CompoundState state : states) {
            out+="\t<"+state.getName()+">\n";
        }
        out+="<\\States>";

        return out;
      }

}
