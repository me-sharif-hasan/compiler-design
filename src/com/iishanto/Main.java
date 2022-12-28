package com.iishanto;



import com.iishanto.Lexer.iiLexer;

public class Main {

    public static void main(String[] args) throws Exception {
        NonTerminal E=new NonTerminal("E");
        NonTerminal E_=new NonTerminal("E'");
        NonTerminal T=new NonTerminal("T");
        NonTerminal T_=new NonTerminal("T'");
        NonTerminal F=new NonTerminal("F");
        NonTerminal NUM=new NonTerminal("NUM");
        NonTerminal ASSIGNMENT=new NonTerminal("ASSIGNMENT");
        NonTerminal STATEMENT=new NonTerminal("STATEMENT");
        NonTerminal STATEMENTS=new NonTerminal("STATEMENTS").markAsStart();
        NonTerminal CONTROL=new NonTerminal("CONTROL");
        NonTerminal BODY=new NonTerminal("BODY");

        Terminal _open_paren=new Terminal("(");
        Terminal _open_curly=new Terminal("{");
        Terminal _close_paren=new Terminal(")");
        Terminal _close_curly=new Terminal("}");
        Terminal _eps=new Terminal("<eps>").markAsEpsilon();
        Terminal _const=new Terminal("<const>");
        Terminal _id=new Terminal("<id>");
        Terminal _semicolon=new Terminal(";");
        Terminal _if=new Terminal("if");
        Terminal _while=new Terminal("while");
        Terminal _plus=new Terminal("+");
        Terminal _minus=new Terminal("-");
        Terminal _mul=new Terminal("*");
        Terminal _div=new Terminal("/");
        Terminal _and=new Terminal("&&");
        Terminal _or=new Terminal("||");
        Terminal _eq=new Terminal("=");
        Terminal _print=new Terminal("print");

        /*
        **This is the grammar I am planning to implement**
         E  -> T E'
         E' -> + T E' | -TE' | ||TE' | epsilon
         T  -> F T'
         T' -> * F T' | /FT' | &&FT' |epsilon
         F  -> (E) | NUM
         NUM -> id|const
         ASSIGNMENT=<id> = E
         STATEMENT = ASSIGNMENT|CONTROL|print(E)
         STATEMENTS=STATEMENT;STATEMENTS | epsilon
         CONTROL = while BODY | if BODY
         BODY=(E){STATEMENTS}
         */

        E.goes(T).and(E_);
        E_.goes(_plus).and(T).and(E_).or(_minus).and(T).and(E_).or(_or).and(T).and(E_).or(_eps);
        T.goes(F).and(T_);
        T_.goes(_mul).and(F).and(T_).or(_div).and(F).and(T_).or(_and).and(F).and(T_).or(_eps);
        F.goes(_open_paren).and(E).and(_close_paren).or(NUM);
        NUM.goes(_id).or(_const);
        ASSIGNMENT.goes(_id).and(_eq).and(E);
        STATEMENT.goes(ASSIGNMENT).or(CONTROL).or(_print).and(_open_paren).and(E).and(_close_paren);
        STATEMENTS.goes(STATEMENT).and(_semicolon).and(STATEMENTS).or(_eps);
        CONTROL.goes(_while).and(BODY).or(_if).and(BODY);
        BODY.goes(_open_paren).and(E).and(_close_paren).and(_open_curly).and(STATEMENTS).and(_close_curly);
        for(NonTerminal t:Symbol.getNonTerminalList()){
            System.out.print(t.getSymbolName()+"\t\t");
            for(Terminal x:t.first()) System.out.print(x.getTerminalValue()+" ");
            System.out.println();
        }

        CFG cfg=new CFG(new iiLexer(),Symbol.getTerminalList(),Symbol.getNonTerminalList());
        cfg.buildParseTable();
        cfg.parseLL1();

        System.out.println("Parsing successful");
    }
}