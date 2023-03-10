package com.iishanto;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class NonTerminal extends Symbol {
    private final String symbolName;
    private boolean start = false;
    private final Vector<Vector<Symbol>> productions = new Vector<Vector<Symbol>>();

    private final Vector<Usage> usages = new Vector<Usage>();

    public NonTerminal(String name) {
        super(NON_TERMINAL);
        symbolName = name;
        addNonTerminal(this);
    }

    public NonTerminal markAsStart() {
        start = true;
        return this;
    }

    public boolean isStart() {
        return start;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public Vector<Vector<Symbol>> getProductions() {
        return productions;
    }

    public NonTerminal and(Symbol next) {
        productions.lastElement().add(next);
        if (next.getSymbolType() == NON_TERMINAL) {
            NonTerminal tmp = (NonTerminal) next;
            tmp.addUsages(this, productions.size() - 1, productions.lastElement().size() - 1);
        }
        return this;
    }

    public NonTerminal or(Symbol next) {
        productions.add(new Vector<Symbol>());
        return and(next);
    }

    public NonTerminal goes(Symbol next) {
        return or(next);
    }

    private final Set<Terminal> firstSet = new HashSet<Terminal>();

    public Set<Terminal> first() {
        if (firstSet.size() != 0) return firstSet;
        for (int i = 0; i < productions.size(); i++) {
            firstSet.addAll(nextFirst(i));
        }
        return firstSet;
    }

    public Set<Terminal> nextFirst(int i) {
        Set<Terminal> tmp = new HashSet<Terminal>();
        if (i < productions.size()) {
            Symbol s = productions.get(i).get(0);
            if (s.getSymbolType() == TERMINAL) {
                tmp.add((Terminal) s);
            } else {
                tmp.addAll(((NonTerminal) s).first());
            }
        }
        return tmp;
    }

    private final Set<NonTerminal> followSession = new HashSet<NonTerminal>();
    private Set<Terminal> followSet;

    /**
     * Generate follow of a non-terminal.
     *
     * @return Set<Terminal>
     */
    public Set<Terminal> follow() {
        if (followSet != null && followSet.size() != 0) return followSet;
        followSession.clear();
        followSet = follow(this);
        return followSet;
    }


    private Set<Terminal> follow(NonTerminal ref) {
        Set<Terminal> tmp = new HashSet<Terminal>();
        if (isStart()) tmp.add(Symbol.getDollar());
        for (Usage usage : usages) {
            NonTerminal lhs = usage.lhs;
            int row = usage.row;
            int col = usage.col;
            Set<Terminal> tmp2 = new HashSet<Terminal>();
            for (int i = col + 1; i < lhs.productions.get(row).size(); i++) {
                if (lhs.productions.get(row).get(i).getSymbolType() == TERMINAL) {
                    if (!((Terminal) lhs.productions.get(row).get(i)).isEpsilon()) {
                        tmp2.add((Terminal) lhs.productions.get(row).get(i));
                        break;
                    }
                } else {
                    Set<Terminal> holder = ((NonTerminal) lhs.productions.get(row).get(i)).first();
                    if (holder.size() > 0) {
                        for (Terminal t : holder) {
                            if (!t.isEpsilon()) {
                                tmp2.add(t);
                            } else {
                                if (!ref.followSession.contains(lhs) && !lhs.equals(this)) {
                                    ref.followSession.add(lhs);
                                    tmp2.addAll(lhs.follow(ref));
                                }
                            }
                        }
                    }
                    if (tmp2.size() > 0) break;
                }
            }
            if (!ref.followSession.contains(lhs) && tmp2.size() == 0 && !this.equals(lhs)) {
                ref.followSession.add(lhs);
                tmp2 = lhs.follow(ref);
            }
            tmp.addAll(tmp2);
        }
        return tmp;
    }

    /**
     * Track the usage of a non-terminal for follow finding purpose.
     *
     * @param lhs Left hand side of reference non-terminal
     * @param row Position of production in lhs
     * @param col Position in the row 'th production of lhs
     */
    protected void addUsages(NonTerminal lhs, int row, int col) {
        usages.add(new Usage(lhs, row, col));
    }

    public int getNumberOfProduction() {
        return productions.size();
    }

    /* For holding usage*/
    static class Usage {
        protected NonTerminal lhs;
        protected int row;
        protected int col;

        protected Usage(NonTerminal lhs, int row, int col) {
            this.lhs = lhs;
            this.row = row;
            this.col = col;
        }
    }
}
