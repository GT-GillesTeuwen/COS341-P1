import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DFA {
    public static final String EPSILON = "ε";
    private Set<State> states;
    private Set<String> alphabet;
    private Map<OrderedPair<Set<State>, String>, Set<State>> transition; // Structure (current state +
                                                               // alphabet-element)->(state that will be reached)
    private State initialState;
    private Set<State> acceptStates;
    private NFA nfa;

    public DFA(NFA nfa){
        this.nfa=nfa;
        this.states=new HashSet<>();
        for (State state : nfa.getStates().keySet()) {
            states.add(state);
        }
        this.initialState=nfa.getInitialState();
        this.transition=new HashMap<>();
        
        
        Map<State,Set<State>> allEpsilonClosures=new HashMap<>();
        for (State state : states) {
            allEpsilonClosures.put(state,makeEpsilonClosureForState(state));
        }
        System.out.println("a");


        Set<State> ecOfInitial=allEpsilonClosures.get(initialState);
        for (String str : nfa.getAlphabet().keySet()) {
            Set<State> symbolTransitions=new HashSet();
            for (State state : ecOfInitial) {
                Map<State,State> t=nfa.getTransition().get(new OrderedPair<State,String>(state, str));
                if(t !=null){
                Set<State> transitionStates= t.keySet();
               
                    for (State closureIndex : transitionStates) {
                        symbolTransitions.addAll(allEpsilonClosures.get(closureIndex));
                        System.out.println("b");
                    }
                }
                
            }
            transition.put(new OrderedPair<Set<State>,String>(ecOfInitial, str), symbolTransitions);
        }
        System.out.println("c");
        
    }

    private Set<State> makeEpsilonClosureForState(State s){
        Set<State> epsilonClosure=new HashSet();
        epsilonClosure.add(s);
        epsilonClosure.addAll(getEpsilonTransitions(s));
        return epsilonClosure;
        
    }

    private Set<State> getEpsilonTransitions(State s){

        Set<State> epsilonTransitions=new HashSet<>();
        Map<State,State> trans=(nfa.getTransition().get(new OrderedPair<State,String>(s, EPSILON)));
        if(trans!=null){
            epsilonTransitions.addAll(trans.keySet());
        }
        Set<State> newEpsilonTransitions=new HashSet<>();
        newEpsilonTransitions.addAll(epsilonTransitions);
        for (State state : epsilonTransitions) {
            newEpsilonTransitions.addAll(getEpsilonTransitions(state));
        }
        return newEpsilonTransitions;
    }

}
