package com.iishanto;

public class Terminal extends Symbol {
    private final String terminalValue;
    private boolean epsilon = false;

    public Terminal(String terminalValue) {
        super(TERMINAL);

        this.terminalValue = terminalValue;
        addTerminal(this);
    }

    public Terminal markAsEpsilon() {
        epsilon = true;
        return this;
    }

    public boolean isEpsilon() {
        return epsilon;
    }

    public String getTerminalValue() {
        return terminalValue;
    }
}
