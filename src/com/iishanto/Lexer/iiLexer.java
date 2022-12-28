package com.iishanto.Lexer;


import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class iiLexer {
    String []tokens=new String[]{
            "{","{","}","}",
            "<id>","=","<id>","+","<id>","*","<id>","(","<id>","(",")",")",";"
            ,"if","(","<id>","(","<const>",")",")",
            "{",
            "<id>","+","=","<id>","+","<id>",";",
            "<id>","++",";",
            "<id>","--",";",
            "++","<id>",";",
            "--","<id>",";",
            "<id>","=","<id>","+","<id>",";",
            "<id>","=","<id>","+","<id>",";",
            "print","(","<id>","*","<id>",")",";",
            "<id>","=","<id>","/","<const>",";",
            ";",

            "if","(","<id>","||","<id>","*","<id>",")","{","if","(","<id>","||","<id>","*","<id>",")","{","<id>","=","<id>",";","}","}",
            "fun","<id>","(",")","{","}",
            "<id>","(","<id>",")",";",
            "}",
            "$","<kopasamsu>"
    };




    public static void main(String[] args) throws Exception {
        iiLexer l=new iiLexer();
        String f;
        while (!(f=l.getNextToken()).equals("<kopasamsu>")){
            System.out.println(f);
        }
    }
    ArrayList <String>token;
    public iiLexer() throws IOException {
        token=build();
    }

    int i=0;
    public String getNextToken(){
        // System.out.println("Token is: "+tokens[i]+"; "+i);
        return token.get(i++);
    }

    public void back(){
        i--;
    }

    String temp=null;
    boolean isBack=false;
    public ArrayList <String> build() throws IOException {
        JFlexLexer jFlexLexer=new JFlexLexer(new FileReader("source.hz"));
        StringBuilder sb=new StringBuilder();
        JavaType tkn;

        ArrayList <String> arrayList=new ArrayList<String>();
        while ((tkn=jFlexLexer.yylex())!=JavaType.EOF){
            if(tkn.equals(JavaType.WHITESPACE)||tkn.equals(JavaType.COMMENT)){
                continue;
            }
            if(tkn.equals(JavaType.STRING)){
                jFlexLexer.yylex();
                jFlexLexer.yylex();
                temp = "<string>";
            }else if(tkn.equals(JavaType.IDENTIFIER)){
                if(jFlexLexer.yytext().equals("fun")){
                    temp="fun";
                }else if(jFlexLexer.yytext().equals("return")){
                    temp="return";
                }else{
                    temp="<id>";
                }
            }else if(tkn.equals(JavaType.INTEGER_LITERAL)||tkn.equals(JavaType.FLOATING_POINT_LITERAL)){
                temp="<const>";
            }
            else {
                temp = jFlexLexer.yytext();
            }
            arrayList.add(temp);
        }
        arrayList.add("$");
        arrayList.add("<kopasamsu>");
        return arrayList;
    }
}
