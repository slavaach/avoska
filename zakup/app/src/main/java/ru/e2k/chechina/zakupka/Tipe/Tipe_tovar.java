package ru.e2k.chechina.zakupka.Tipe;

import ru.e2k.chechina.zakupka.R;
import ru.e2k.chechina.zakupka.dao.Table_tipe;

// описание типа товаров. Так же используется для домов (потому, как поля одиннаковы)
public class Tipe_tovar implements Table_tipe {

    private String name; // название
    private int id; // ID
    private int orders;
//выбран
    @Override
    public void setCheck(boolean bzakup){};
//выбран
    @Override
    public boolean check(){return  true;};
    //выводит строку для отражения в поле по R.id
    @Override
    public String getTextView(int i){
        switch (i) {
            case R.id.list_tip_tovar_idView:
                return Integer.toString(this.id);
            case R.id.list_tip_tovar_ordersView:
                return Integer.toString(this.orders);
            case R.id.list_tip_tovar_nameView:
                return this.name;
            default:
                return "";

        }
        }

    //выводит список полей в Gride отражения
@Override
public int[] R_id_view(){
    int[] r = new int[]{R.id.list_tip_tovar_idView , R.id.list_tip_tovar_ordersView , R.id.list_tip_tovar_nameView};
     return r;
};

    public Tipe_tovar(String name, int orders, int id){

        this.name=name;
        this.id=id;
        this.orders=orders;
    }
    @Override
    public Tipe_tovar get(){ return this;}
    //строка поля по номеру столбца
    @Override
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

    @Override
    public boolean isSimpel(){ return  false;};

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrders() {
        return this.orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }
    @Override
    public void setBzakupOnli(boolean bzakup) {}

    @Override
    public int getId() {
        return this.id;
    }

    public void setID(int id) {
        this.id = id;
    }
}
