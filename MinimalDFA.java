import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MinimalDFA {
    public static final String EPSILON = "ε";
    private Set<CompoundState> states;
    private Set<String> alphabet;
    private Map<OrderedPair<StateGroup, String>, StateGroup> transition; // Structure (current state +
    // alphabet-element)->(state that will be reached)
    private State initialState;
    private Set<CompoundState> acceptStates;
    private DFA dfa;
    private int numStates;

    public MinimalDFA(DFA dfa) {
        this.dfa = dfa;
        this.alphabet = dfa.getAlphabet();
        this.numStates = 0;

        ArrayList<StateGroup> stateGroups = makeStateGroups();
        boolean changed = true;
        while(changed){
            changed=false;
            for (int i = 0; i < stateGroups.size() && !changed; i++) { 
                Map<OrderedPair<CompoundState, String>, StateGroup> tt = generateTransitionTable(stateGroups.get(0),
                        stateGroups);
                if (!isTransitionTableMinimal(tt)) {
                    makeSubGroups(stateGroups.get(i), stateGroups,tt);
                    changed=true;
                }
            }
        }

        

        System.out.println("a");

    }

    private ArrayList<StateGroup> makeStateGroups() {
        ArrayList<StateGroup> stateGroups = new ArrayList<>();

        StateGroup finalStates = new StateGroup(dfa.getAcceptStates());

        Set<CompoundState> nonFinalStatesFromDfa = new HashSet<>();
        for (CompoundState compoundState : dfa.getStates()) {
            if (!compoundState.isFinal()) {
                nonFinalStatesFromDfa.add(compoundState);
            }
        }

        StateGroup nonFinalStates = new StateGroup(nonFinalStatesFromDfa);

        stateGroups.add(nonFinalStates);
        stateGroups.add(finalStates);

        return stateGroups;
    }

    public Map<OrderedPair<CompoundState, String>, StateGroup> generateTransitionTable(StateGroup g,
            ArrayList<StateGroup> allGroups) {
        Map<OrderedPair<CompoundState, String>, StateGroup> transitionTable = new HashMap<>();
        for (CompoundState compoundState : g.getStatesInGroup()) {
            for (String symbol : alphabet) {
                CompoundState goingToState = dfa.getTransition()
                        .get(new OrderedPair<CompoundState, String>(compoundState, symbol));
                transitionTable.put(new OrderedPair<CompoundState, String>(compoundState, symbol),
                        getGroupOfState(goingToState, allGroups));
            }
        }
        System.out.println("a");
        return transitionTable;
    }

    public StateGroup getGroupOfState(CompoundState cs, ArrayList<StateGroup> allGroups) {
        for (StateGroup stateGroup : allGroups) {
            if (stateGroup.contains(cs)) {
                return stateGroup;
            }
        }
        System.out.println("Could not find group of compound state");
        return null;
    }

    private boolean isTransitionTableMinimal(Map<OrderedPair<CompoundState, String>, StateGroup> transitionTable) {
        for (String symbol : alphabet) {
            StateGroup seen = null;
            for (OrderedPair<CompoundState, String> op : transitionTable.keySet()) {
                if (seen == null) {
                    seen = transitionTable.get(op);
                } else {
                    if (seen != transitionTable.get(op)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void makeSubGroups(StateGroup stateGroup,ArrayList<StateGroup> allGroups,Map<OrderedPair<CompoundState, String>, StateGroup> transitionTable){
        allGroups.remove(stateGroup);
        Map<Set<OrderedPair<StateGroup,String>>,Set<CompoundState>> subGroups=new HashMap<>();
        for (CompoundState cs : stateGroup.getStatesInGroup()) {
            Set<OrderedPair<StateGroup,String>> currentSet=new HashSet<>();
            for (String symbol : alphabet) {
                StateGroup sg=transitionTable.get(new OrderedPair<CompoundState, String>(cs,symbol));
                currentSet.add(new OrderedPair<StateGroup,String>(sg, symbol));
            }
            Set<CompoundState> possibleSet=subGroups.get(currentSet);
            if(possibleSet!=null){
                possibleSet.add(cs);
            }else{
                Set<CompoundState> statesOfSubgroup=new HashSet<>();
                statesOfSubgroup.add(cs);
                subGroups.put(currentSet,statesOfSubgroup);
            }
        }
        for (Set<CompoundState> newGroupSet : subGroups.values()) {
            allGroups.add(new StateGroup(newGroupSet));
        }
        System.out.println("sdkfjh");

    }


}
