package com.iishanto;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

abstract public class Symbol {
    final static boolean NON_TERMINAL = false;
    final static boolean TERMINAL = true;
    private static Terminal dollar;
    private final boolean symbolType;

    private static Vector<Terminal> terminalRecord = new Vector<Terminal>();
    private static Vector<NonTerminal> nonTerminalRecord = new Vector<NonTerminal>();

    public Symbol(boolean symbolType) {
        this.symbolType = symbolType;
    }

    public boolean getSymbolType() {
        return symbolType;
    }

    public static Terminal getDollar() {
        if (dollar == null) dollar = new Terminal("$");
        return dollar;
    }

    protected void addTerminal(Terminal t) {
        terminalRecord.add(t);
    }

    protected void addNonTerminal(NonTerminal nt) {
        nonTerminalRecord.add(nt);
    }

    public static Vector<Terminal> getTerminalList() {
        return terminalRecord;
    }

    public static Vector<NonTerminal> getNonTerminalList() {
        return nonTerminalRecord;
    }
}
