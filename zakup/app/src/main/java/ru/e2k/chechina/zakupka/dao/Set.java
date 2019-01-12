package ru.e2k.chechina.zakupka.dao;

//класс для запоминания раскрыт ли пункт в Treelist закупки
public class Set {
    public int i;
    public boolean close;

    public Set(int i , boolean close){
        this.i = i;
        this.close = close;
    }
}