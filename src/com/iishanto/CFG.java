package com.iishanto;

import com.iishanto.Lexer.iiLexer;

import java.util.*;

public class CFG {
    Vector<Terminal> terminals;
    Vector<NonTerminal> nonTerminals;
    private final Map<NonTerminal, Map<Terminal,Integer>> parseTable=new HashMap<NonTerminal, Map<Terminal, Integer>>();
    private final Map<String,Terminal> index=new HashMap<String, Terminal>();

    iiLexer lexer;
    public CFG(iiLexer _lexer, Vector<Terminal> _terminals, Vector<NonTerminal> _nonTerminals){
        terminals=_terminals;
        nonTerminals=_nonTerminals;
        lexer=_lexer;
        buildTerminalIndex();
    }
    public void buildParseTable(){
        for(NonTerminal nt:Symbol.getNonTerminalList()){
            Set<Terminal> follow=nt.follow();
            for(int i=0;i<nt.getNumberOfProduction();i++){
                Set<Terminal> first=nt.nextFirst(i);
                for(Terminal t:first){
                    if(!parseTable.containsKey(nt)){
                        parseTable.put(nt,new HashMap<Terminal, Integer>());
                    }
                    if(!t.isEpsilon()){
                        parseTable.get(nt).put(t,i);
                    }else{
                        for(Terminal ft:follow){
                            parseTable.get(nt).put(ft,i);
                        }
                    }
                }
            }
        }
    }

    private void buildTerminalIndex(){
        for(Terminal t:terminals){
            index.put(t.getTerminalValue(),t);
        }
        index.put(Symbol.getDollar().getTerminalValue(),Symbol.getDollar());
    }
    public boolean parseLL1() throws Exception{
        Stack <Symbol> stk=new Stack<Symbol>();
        stk.push(Symbol.getDollar());
        for(NonTerminal nonTerminal:nonTerminals){
            if(nonTerminal.isStart()){
                stk.push(nonTerminal);
                break;
            }
        }
        String nextToken;
        int tokenCount=0;
        while (!(nextToken= lexer.getNextToken()).equals("<kopasamsu>")){
            Terminal next=index.get(nextToken);
            tokenCount++;
            if(next==null) throw new Exception("Unexpected token error x0 in token "+tokenCount+": "+ nextToken+" is from out of the world!");
            if(stk.peek().getSymbolType()== Symbol.NON_TERMINAL){
                NonTerminal nt= (NonTerminal) stk.peek();
                if(parseTable.containsKey(nt)&&parseTable.get(nt).containsKey(next)){
                    int idx=parseTable.get(nt).get(next);
                    Vector <Symbol> s=nt.getProductions().get(idx);
                    stk.pop();
                    for(int i=s.size()-1;i>=0;i--){
                        stk.add(s.get(i));
                    }
                    lexer.back();
                    tokenCount--;
                }else{
                    throw new Exception("Parse error x1 in token "+tokenCount+": Unexpected token "+next.getTerminalValue()+" encountered!");
                }
            }else{
                Terminal t= (Terminal) stk.peek();
                if(t.isEpsilon()) {
                    stk.pop();
                    lexer.back();
                    tokenCount--;
                    continue;
                }
                if(t.equals(next)){
                    stk.pop();
                }else{
                    throw new Exception("Parse error x2 in token "+tokenCount+": Expected token "+t.getTerminalValue()+" in the source found: "+next.getTerminalValue());
                }
            }
        }
        if(stk.size()!=0){
            throw new Exception("Parse error x3: unexpected end of the source");
        }
        return true;
    }
}
