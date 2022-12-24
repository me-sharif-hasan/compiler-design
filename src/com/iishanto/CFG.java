package com.iishanto;

import com.iishanto.Lexer.Lexer;

import java.util.*;

public class CFG {
    Vector<Terminal> terminals;
    Vector<NonTerminal> nonTerminals;
    private final Map<NonTerminal, Map<Terminal,Integer>> parseTable=new HashMap<NonTerminal, Map<Terminal, Integer>>();
    private final Map<String,Terminal> index=new HashMap<String, Terminal>();

    Lexer lexer;
    public CFG(Lexer _lexer, Vector<Terminal> _terminals,Vector<NonTerminal> _nonTerminals){
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
        while (!(nextToken= lexer.getNextToken()).equals("$")){
            Terminal next=index.get(nextToken);
            if(next==null) throw new Exception("Error x0: Unexpected token out of the world!");
            if(stk.peek().getSymbolType()== Symbol.NON_TERMINAL){
                NonTerminal nt= (NonTerminal) stk.peek();
                if(parseTable.containsKey(nt)&&parseTable.get(nt).containsKey(next)){
                    int idx=parseTable.get(nt).get(next);
                    Vector <Symbol> s=nt.getProductions().get(idx);
//                    System.out.println("Info: "+nt.getSymbolName()+" , "+idx);
//                    for(Symbol k:s){
//                        if(k.getSymbolType()==Symbol.TERMINAL){
//                            System.out.println(((Terminal)k).getTerminalValue());
//                        }else{
//                            System.out.println(((NonTerminal)k).getSymbolName());
//                        }
//                    }
                    stk.pop();
                    //Collections.reverse(s);
                    //stk.addAll(s);
                    for(int i=s.size()-1;i>=0;i--){
                        stk.add(s.get(i));
                    }
                    lexer.back();
                    //System.out.println();
                }else{
                    throw new Exception("Error x1: Unexpected token "+next.getTerminalValue()+" encountered!");
                }
            }else{
                Terminal t= (Terminal) stk.peek();
                //System.out.println(t.getTerminalValue()+"\n");
                if(t.isEpsilon()) {
                    stk.pop();
                    lexer.back();
                    continue;
                }
                if(t.equals(next)){
                    stk.pop();
                }else{
                    throw new Exception("Error x2: Unexpected token "+next.getTerminalValue()+" encountered!");
                }
            }
        }
        return true;
    }
}
