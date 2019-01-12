package ru.e2k.chechina.zakupka.Tipe;

import ru.e2k.chechina.zakupka.Context_zakup;

//для отражение домов взято Tipe_tovar. Здесь только для работы основного меню
public class Home_selected {


    private String name; // название
    private int id; // ID
    private int orders;
    boolean select;

    public Home_selected(String name, int orders, int id , boolean select){

        this.name=name;
        this.id=id;
        this.orders=orders;
        this.select=select;
    }
    //@Override
    public Home_selected get(){ return this;}

    //@Override
    public String getText(int i){
        switch (i) {
            case 0:
                return Integer.toString( this.id);
            case 1:
                return this.name;
            case 2:
                return Integer.toString( this.orders);
            default:
                return "";
        }
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBzakupOnli(boolean bzakup) {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
        if (this.isSelect()) {

            Context_zakup.getDBHelper().getReadableDatabase().execSQL("Update Home set  selected = 1 " +
                    "where _id = " + Integer.toString(this.getId()));

        }
        else Context_zakup.getDBHelper().getReadableDatabase().execSQL("Update Home set  selected = 0 " +
                "where _id = " + Integer.toString(this.getId()));

        Context_zakup.UpdateHome();

    }

}
