package ru.e2k.chechina.zakupka.dao;

//что б не писать слушателей во всех Update-ах к базе
public abstract class TableUpdate {

    private static TableUpdate z;

    protected static TableUpdateListener listenerUpdate;
    protected static TableUpdateListener listenerInsert;
    protected static TableUpdateListener listenerDelete;

    protected TableUpdate(){};

    public    static  TableUpdateListener getTableUpdateListener() { return z.listenerUpdate; }

    public    static  void setTableUpdateListener(TableUpdateListener listener) {z.listenerUpdate = listener; }


    public    static  TableUpdateListener getTableInsertListener() { return z.listenerInsert; }

    public   static  void setTableInsertListener(TableUpdateListener listener) {z.listenerInsert = listener; }

    public    static  TableUpdateListener getTableDeleteListener() { return z.listenerDelete; }

    public  static  void setTableDeleteListener(TableUpdateListener listener) {z.listenerDelete = listener; }
}
