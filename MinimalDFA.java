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
                Map<OrderedPair<CompoundState, String>, StateGroup> tt = generateTransitionTable(stateGroups.get(0),
                        stateGroups);
                if (!isTransitionTableMinimal(tt)) {
                    makeSubGroups(stateGroups.get(i), stateGroups, tt);
                    changed = true;
                }
            }
        }

        for (StateGroup stateGroup : stateGroups) {
            states.add(stateGroup);
        }

        for (StateGroup stateGroup : states) {
            addTransitions(stateGroup);
        }
        addAcceptStates();
        indicateInitialState();
        
        renameStates();
        System.out.println("a");

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

    private void renameStates(){
        int i=0;
        for (StateGroup stateGroup : states) {
            stateGroup.setName("q"+i);
            i++;
        }
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
        return transitionTable;
    }

    public void addTransitions(StateGroup groupFrom) {
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

    private boolean isTransitionTableMinimal(Map<OrderedPair<CompoundState, String>, StateGroup> transitionTable) {
        for (String symbol : alphabet) {
            StateGroup seen = null;
            for (OrderedPair<CompoundState, String> op : transitionTable.keySet()) {
                if (seen == null) {
                    if(transitionTable.get(op)!=null){
                        seen = transitionTable.get(op);
                    }
                    
                } else {
                    if (seen != transitionTable.get(op)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void makeSubGroups(StateGroup stateGroup, ArrayList<StateGroup> allGroups,
            Map<OrderedPair<CompoundState, String>, StateGroup> transitionTable) {
        allGroups.remove(stateGroup);
        Map<Set<OrderedPair<StateGroup, String>>, Set<CompoundState>> subGroups = new HashMap<>();
        for (CompoundState cs : stateGroup.getStatesInGroup()) {
            Set<OrderedPair<StateGroup, String>> currentSet = new HashSet<>();
            for (String symbol : alphabet) {
                StateGroup sg = transitionTable.get(new OrderedPair<CompoundState, String>(cs, symbol));
                if (sg != null) {
                    currentSet.add(new OrderedPair<StateGroup, String>(sg, symbol));
                }

            }
            Set<CompoundState> possibleSet = subGroups.get(currentSet);
            if (possibleSet != null) {
                possibleSet.add(cs);
            } else {
                Set<CompoundState> statesOfSubgroup = new HashSet<>();
                statesOfSubgroup.add(cs);
                subGroups.put(currentSet, statesOfSubgroup);
            }
        }
        for (Set<CompoundState> newGroupSet : subGroups.values()) {
            allGroups.add(new StateGroup(newGroupSet));
        }

    }

    public String toXML() {
        System.out.println("<initial state>\n\t<" + initialState.getName() + ">\n</initial state>");
        String out = ("<states>\n");
        for (StateGroup stateGroup : states) {
            out += "\t<" + stateGroup.getName() + ">\n";
        }
        out += ("</states>\n");
        Map<StateGroup,String> transitionOutput=new HashMap<>();
        out += ("<transitions>\n");

        for (OrderedPair<StateGroup,String> op : transition.keySet()) {
            if(transitionOutput.get(op.getObj1())==null){
                transitionOutput.put(op.getObj1(), "\t<"+op.getObj1().getName()+">\n\t\t<"+transition.get(op).getName()+">"+op.getObj2()+"</"+transition.get(op).getName()+">\n");
            }else{
                String str=transitionOutput.get(op.getObj1());
                str+=("\t\t<"+transition.get(op).getName()+">"+op.getObj2()+"</"+transition.get(op).getName()+">\n");
                transitionOutput.put(op.getObj1(),str);
            }
        }
        for (StateGroup stateGroup : transitionOutput.keySet()) {
            out+=transitionOutput.get(stateGroup)+"\t</"+stateGroup.getName()+">\n";
        }
        out+="</transitions>\n";
         out+= ("<accept states>\n");
        for (StateGroup stateGroup : acceptStates) {
            out += "\t<" + stateGroup.getName() + ">\n";
        }
        out += ("<accept states>\n");
        return out;
    }

}
