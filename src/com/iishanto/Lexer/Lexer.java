package com.iishanto.Lexer;

public class Lexer {
    String []tokens=new String[]{"<id>","=","<id>","+","<id>",";","if","(","<id>","+","<id>","*","<id>",")","{","<id>","=","<id>","+","<id>",";","<id>","=","<id>","+","<id>",";","print","(","<id>",")",";","<id>","=","<id>","/","<const>",";","}","$"};
    //String []tokens=new String[]{"<id>","+","<id>","$"};
    int i=0;
    public String getNextToken(){
       // System.out.println("Token is: "+tokens[i]+"; "+i);
        return tokens[i++];
    }


    String []levels=new String[]{"<var>","<if>","<while>","<id>","<number>"};

    public void back(){
        i--;
    }
}
