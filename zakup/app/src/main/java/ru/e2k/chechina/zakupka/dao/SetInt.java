package ru.e2k.chechina.zakupka.dao;

public class SetInt {
    private static int i;
    private static SetInt z;
    private SetInt(){}

    public static int geti(){
        if (z== null){ z = new SetInt();
        return -1;}
    else return i;
    }
    public static void seti(int i){
        if (z== null) z = new SetInt();
        z.i =i;
    };
}
