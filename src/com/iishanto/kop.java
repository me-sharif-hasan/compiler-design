package com.iishanto;


import com.iishanto.Lexer.iiLexer;

class Kop {

    public static void main(String[] args) throws Exception {
        NonTerminal E=new NonTerminal("E");
        NonTerminal E_=new NonTerminal("E'");
        NonTerminal T=new NonTerminal("T");
        NonTerminal T_=new NonTerminal("T'");
        NonTerminal F=new NonTerminal("F");
        NonTerminal NUM=new NonTerminal("NUM");
        NonTerminal NUM_=new NonTerminal("NUM_");
        NonTerminal ASSIGNMENT=new NonTerminal("ASSIGNMENT");
        NonTerminal UNV_EXP=new NonTerminal("UNV_EXP");
        NonTerminal REL_OP=new NonTerminal("REL_OP");
        NonTerminal STATEMENT=new NonTerminal("STATEMENT");
        NonTerminal STATEMENTS=new NonTerminal("STATEMENTS").markAsStart();
        NonTerminal CONTROL=new NonTerminal("CONTROL");
        NonTerminal BODY=new NonTerminal("BODY");
        NonTerminal SCOPE=new NonTerminal("SCOPE");
        NonTerminal ASOFC=new NonTerminal("ASOFC");
        NonTerminal ASOFC_=new NonTerminal("ASOFC_");
        NonTerminal ARGS=new NonTerminal("ARGS");
        NonTerminal ARGS_=new NonTerminal("ARGS_");
        NonTerminal ARGS_P=new NonTerminal("ARGS_P");

        NonTerminal INC_DEC=new NonTerminal("INC_DEC");

        Terminal _open_paren=new Terminal("(");
        Terminal _open_curly=new Terminal("{");
        Terminal _close_paren=new Terminal(")");
        Terminal _close_curly=new Terminal("}");
        Terminal _eps=new Terminal("<eps>").markAsEpsilon();
        Terminal _const=new Terminal("<const>");
        Terminal _id=new Terminal("<id>");
        Terminal _semicolon=new Terminal(";");
        Terminal _comma=new Terminal(",");
        Terminal _if=new Terminal("if");
        Terminal _while=new Terminal("while");
        Terminal _plus=new Terminal("+");
        Terminal _unary_plus=new Terminal("++");
        Terminal _unary_minus=new Terminal("--");
        Terminal _minus=new Terminal("-");
        Terminal _mul=new Terminal("*");
        Terminal _mod=new Terminal("%");
        Terminal _div=new Terminal("/");
        Terminal _and=new Terminal("&&");
        Terminal _or=new Terminal("||");
        Terminal _eq=new Terminal("=");
        Terminal _eqeq=new Terminal("==");
        Terminal _peq=new Terminal("+=");
        Terminal _seq=new Terminal("-=");
        Terminal _meq=new Terminal("*=");
        Terminal _deq=new Terminal("/=");
        Terminal _leq=new Terminal("<=");
        Terminal _geq=new Terminal("=>");
        Terminal _neq=new Terminal("!=");
        Terminal _less=new Terminal("<");
        Terminal _greater=new Terminal(">");
        Terminal _print=new Terminal("print");
        Terminal _fun=new Terminal("fun");
        Terminal _ret=new Terminal("return");
        Terminal _str=new Terminal("<string>");

        /*
         **This is the grammar I am planning to implement**
         ---------------------------------------------------
         E  -> T E'
         E' -> + T E' | -TE' | ||TE' | epsilon
         T  -> F T'
         T' -> * F T' | /FT' | %FT' | &&FT' |epsilon
         F  -> (E) | NUM
         NUM -> id NUM'|const
         NUM' -> (ARGS)|epsilon|INC_DEC
         ARGS=ARGS_P|epsilon
         ARGS_P=E ARGS'
         ARGS'=, ARGS_P |epsilon

         INC_DEC=++|--
         UNV_EXP->E REL_OP
         REL_OP->==UNV_EXP|=>UNV_EXP|<=UNV_EXP|epsilon

         ASSIGNMENT -> <id> ASOFC | INC_DEC <id>
         ASOFC -> = ASOFC_ | (ARGS) | INC_DEC
         ASOFC_ -> +=UNV_EXP | -=UNV_EXP | *=UNV_EXP | /=UNV_EXP | =UNV_EXP

         STATEMENT -> ASSIGNMENT ; |CONTROL |print(E) ;| ; |SCOPE|return E;
         (START) STATEMENTS -> STATEMENT STATEMENTS | epsilon
         CONTROL -> while BODY | if BODY | fun <id>(ARGS) SCOPE
         BODY -> (E) STATEMENT
         SCOPE -> {STATEMENTS}
         */

        System.out.println("Parsing started ...");

        E.goes(T).and(E_);
        E_.goes(_plus).and(T).and(E_).or(_minus).and(T).and(E_).or(_or).and(T).and(E_).or(_eps);
        T.goes(F).and(T_);
        T_.goes(_mul).and(F).and(T_).or(_div).and(F).and(T_).or(_mod).and(F).and(T_).or(_and).and(F).and(T_).or(_eps);
        F.goes(_open_paren).and(E).and(_close_paren).or(NUM);
        NUM.goes(_id).and(NUM_).or(_const).or(_str);
        NUM_.goes(_open_paren).and(ARGS).and(_close_paren).or(INC_DEC).or(_eps);
        ARGS.goes(ARGS_P).or(_eps);
        ARGS_P.goes(E).and(ARGS_);
        ARGS_.goes(_comma).and(ARGS_P).or(_eps);

        INC_DEC.goes(_unary_plus).or(_unary_minus);
        ASSIGNMENT.goes(_id).and(ASOFC).or(INC_DEC).and(_id);
        ASOFC.goes(ASOFC_).or(_open_paren).and(ARGS).and(_close_paren).or(INC_DEC);

        ASOFC_.goes(_peq).and(UNV_EXP).or(_seq).and(UNV_EXP).or(_meq).and(UNV_EXP).or(_deq).and(UNV_EXP).or(_eq).and(UNV_EXP);

        UNV_EXP.goes(E).and(REL_OP);
        REL_OP.goes(_eqeq).and(UNV_EXP).or(_leq).and(UNV_EXP).or(_geq).and(UNV_EXP).or(_neq).and(UNV_EXP).or(_eps).or(_less).and(UNV_EXP).or(_greater).and(UNV_EXP);

        STATEMENT.goes(ASSIGNMENT).and(_semicolon).or(CONTROL).or(_print).and(_open_paren).and(E).and(_close_paren).and(_semicolon).or(_semicolon).or(SCOPE).or(_ret).and(E).and(_semicolon);
        STATEMENTS.goes(STATEMENT).and(STATEMENTS).or(_eps);
        CONTROL.goes(_while).and(BODY).or(_if).and(BODY).or(_fun).and(_id).and(_open_paren).and(ARGS).and(_close_paren).and(SCOPE);
        BODY.goes(_open_paren).and(UNV_EXP).and(_close_paren).and(STATEMENT);
        SCOPE.goes(_open_curly).and(STATEMENTS).and(_close_curly);

        iiLexer il=new iiLexer();
        CFG cfg=new CFG(il,Symbol.getTerminalList(),Symbol.getNonTerminalList());
        cfg.buildParseTable();
        cfg.parseLL1();

        System.out.println("Parsing success!");
    }
}