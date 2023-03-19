import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MinimalDFA {
    public static final String EPSILON = "ε";
    private Set<StateGroup> states;
    private Set<String> alphabet;
    private Map<OrderedPair<StateGroup, String>, StateGroup> transition; // Structure (current state +
    // alphabet-element)->(state that will be reached)
    private StateGroup initialState;
    private Set<StateGroup> acceptStates;
    private DFA dfa;
    private int numStates;

    public MinimalDFA(DFA dfa) {
        this.dfa = dfa;
        this.alphabet = dfa.getAlphabet();
        this.transition = new HashMap<>();
        this.acceptStates = new HashSet<>();
        this.numStates = 0;

        states = new HashSet<>();

        ArrayList<StateGroup> stateGroups = makeStateGroups();
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i < stateGroups.size() && !changed; i++) {
                Map<CompoundState, Map<String, StateGroup>> tt = generateTransitionTable(stateGroups.get(i),
                        stateGroups);
                if (!isTransitionTableMinimal(tt)) {
                    makeSubGroups(stateGroups.get(i), stateGroups, tt);
                    changed = true;
                }
            }
        }

        for (StateGroup stateGroup : stateGroups) {
            if(stateGroup.getStatesInGroup().size()!=0){
                states.add(stateGroup);
            }
            
        }


        for (StateGroup stateGroup : states) {
            addTransitions(stateGroup);
        }

        addAcceptStates();
        indicateInitialState();

        renameStates();

    }

    private void indicateInitialState() {
        for (StateGroup stateGroup : states) {
            if (stateGroup.contains(dfa.getInitialState())) {
                initialState = stateGroup;
                stateGroup.setInitial(true);
                return;
            }
        }
    }

    private void renameStates() {
        int i = 0;
        for (StateGroup stateGroup : states) {
            stateGroup.setName("q" + i);
            i++;
        }
    }

    public Set<StateGroup> getStates() {
        return states;
    }

    public Map<OrderedPair<StateGroup, String>, StateGroup> getStateTransitions(StateGroup sg) {
        Map<OrderedPair<StateGroup, String>, StateGroup> stateTransitions = new HashMap<>();
        for (OrderedPair<StateGroup, String> op : transition.keySet()) {
            if (op.getObj1().equals(sg)) {
                stateTransitions.put(op, transition.get(op));
            }
        }
        return stateTransitions;
    }

    private void addAcceptStates() {
        for (StateGroup stateGroup : states) {
            if (stateGroup.isFinal()) {
                acceptStates.add(stateGroup);
            }
        }
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

    public Map<CompoundState, Map<String, StateGroup>> generateTransitionTable(StateGroup g,
            ArrayList<StateGroup> allGroups) {
        Map<CompoundState, Map<String, StateGroup>> transitionTable = new HashMap<>();
        for (CompoundState compoundState : g.getStatesInGroup()) {
            Map<String, StateGroup> allCompoundStateTransitions = new HashMap<>();
            for (String symbol : alphabet) {
                CompoundState goingToState = dfa.getTransition()
                        .get(new OrderedPair<CompoundState, String>(compoundState, symbol));
                allCompoundStateTransitions.put(symbol, getGroupOfState(goingToState, allGroups));
            }
            transitionTable.put(compoundState, allCompoundStateTransitions);
        }
        return transitionTable;
    }

    public void addTransitions(StateGroup groupFrom) {
        if(groupFrom.getStatesInGroup().size()==0){
            //states.remove(groupFrom);
            return;
        }
        CompoundState cs = (CompoundState) groupFrom.getStatesInGroup().toArray()[0];
        for (String symbol : alphabet) {
            CompoundState cs2 = dfa.getTransition().get(new OrderedPair<CompoundState, String>(cs, symbol));
            if (cs2 != null) {
                transition.put(new OrderedPair<StateGroup, String>(groupFrom, symbol), getGroupOfState(cs2));
            }
        }
    }

    public StateGroup getGroupOfState(CompoundState cs, ArrayList<StateGroup> allGroups) {
        for (StateGroup stateGroup : allGroups) {
            if (stateGroup.contains(cs)) {
                return stateGroup;
            }
        }
        // System.out.println("Could not find group of compound state");
        return null;
    }

    public StateGroup getGroupOfState(CompoundState cs) {
        for (StateGroup stateGroup : states) {
            if (stateGroup.contains(cs)) {
                return stateGroup;
            }
        }
        // System.out.println("Could not find group of compound state");
        return null;
    }

    private boolean isTransitionTableMinimal(Map<CompoundState, Map<String, StateGroup>> transitionTable) {
        if (transitionTable.size() == 1) {
            return true;
        }
        Map<String, StateGroup> seen = null;
        for (Map<String, StateGroup> sg : transitionTable.values()) {
            if (seen == null) {
                seen = sg;
            } else {
                if (!sg.equals(seen)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void makeSubGroups(StateGroup stateGroup, ArrayList<StateGroup> allGroups,Map<CompoundState, Map<String, StateGroup>> transitionTable) {
        allGroups.remove(stateGroup);

        Map<Map<String,StateGroup>,Set<CompoundState>> subGroups=new HashMap<>();

        for (CompoundState compoundState : transitionTable.keySet()) {
            Map<String,StateGroup> csTransitions=transitionTable.get(compoundState);
            Set<CompoundState> subSet= subGroups.get(csTransitions);
            if(subSet==null){
                subSet=new HashSet<>();
                subSet.add(compoundState);
                subGroups.put(csTransitions, subSet);
            }else{
                subSet.add(compoundState);
            }
        }


        for (Set<CompoundState> newGroupSet : subGroups.values()) {
            allGroups.add(new StateGroup(newGroupSet));
        }

    }

    public String toXML() {
        String out=("<dfa>\n<initial_state>\n    <" + initialState.getName() + "/>\n</initial_state>\n");
         out += ("<states>\n");
        for (StateGroup stateGroup : states) {
            out += "    <" + stateGroup.getName() + "/>\n";
        }
        out += ("</states>\n");
        Map<StateGroup, String> transitionOutput = new HashMap<>();
        out += ("<transitions>\n");

        for (OrderedPair<StateGroup, String> op : transition.keySet()) {
            if (transitionOutput.get(op.getObj1()) == null) {
                transitionOutput.put(op.getObj1(),
                        "    <" + op.getObj1().getName() + ">\n        <" + transition.get(op).getName() + ">" + op.getObj2()
                                + "</" + transition.get(op).getName() + ">\n");
            } else {
                String str = transitionOutput.get(op.getObj1());
                str += ("        <" + transition.get(op).getName() + ">" + op.getObj2() + "</"
                        + transition.get(op).getName() + ">\n");
                transitionOutput.put(op.getObj1(), str);
            }
        }
        for (StateGroup stateGroup : transitionOutput.keySet()) {
            out += transitionOutput.get(stateGroup) + "    </" + stateGroup.getName() + ">\n";
        }
        out += "</transitions>\n";
        out += ("<accept_states>\n");
        for (StateGroup stateGroup : acceptStates) {
            out += "    <" + stateGroup.getName() + "/>\n";
        }
        out += ("</accept_states>\n</dfa>");
        return out;
    }

}
