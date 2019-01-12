package ru.e2k.chechina.zakupka.Tipe;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import ru.e2k.chechina.zakupka.Context_zakup;
import ru.e2k.chechina.zakupka.R;
import ru.e2k.chechina.zakupka.dao.TableUpdateListener;
import ru.e2k.chechina.zakupka.dao.Table_tipe;

import java.util.ArrayList;
import java.util.List;

// описание постоянных товаров
public class Tovar implements Table_tipe {


    private String name; // название
    private int id; // ID
    private int orders;
    //private DBHelper dbH;
    private int orders_tipe = 0;
    private String name_tipe = "";

    private int id_tipe;
    //выбран
    @Override
    public boolean check(){return  true;};
    //выбран
    @Override
    public void setCheck(boolean bzakup){};

    //выводит строку для отражения в поле по R.id
    @Override
    public String getTextView(int i){
        switch (i) {
            case R.id.list_tovar_nameView:
                return this.name;
            case R.id.list_tovar_ordersView :
                return Integer.toString(this.orders);
            case R.id.list_tovar_idView :
                return Integer.toString(this.id);
            case  R.id.list_tovar_nameTipeView:
                return this.name_tipe;
            default:
                return "";

        }
    }
    //выводит список полей в Gride отражения
    @Override
    public int[] R_id_view(){
        int[] r = new int[]{R.id.list_tovar_nameView , R.id.list_tovar_ordersView
                , R.id.list_tovar_idView , R.id.list_tovar_nameTipeView};
        return r;
    };



    @Override
    public Tovar get(){ return this;}
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
            case 3:
                return  this.name_tipe;
            default:
                return "";
        }
    }

    @Override
    public boolean isSimpel(){ return  false;};
    //@Override
    public int getCounts(){return 0;};


    public Tovar(String name, int orders, int id_tipe, int id , Context context){

        this.name=name;
        this.id=id;
        this.orders=orders;
        this.id_tipe = id_tipe;
        //this.dbH=new DBHelper(context);
    }

    public Tovar(String name, int orders, int id_tipe, int id , String name_tipe , int orders_tipe , Context context){

        this.name=name;
        this.id=id;
        this.orders=orders;
        this.id_tipe = id_tipe;
        //this.dbH=new DBHelper(context);
        this.orders_tipe = orders_tipe;
        this.name_tipe = name_tipe;
    }
    //@Override

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

    //@Override
    public int getId() {
        return this.id;
    }


    public int getId_tipe() {
        return id_tipe;
    }

    public void setId_tipe(int id_tipe) {
        this.id_tipe = id_tipe;
    }

    public int getOrders_tipe()
    {
        if(this.orders_tipe ==0){
        Cursor userCursor =  Context_zakup.getDBHelper().getWritableDatabase().rawQuery("select orders from tipe_Tovar where _id = " + Integer.toString(this.id_tipe) , null);

        userCursor.moveToFirst();
            while( userCursor.isAfterLast() ==false) { this.orders_tipe = userCursor.getInt(0);};
        userCursor.close();}
        return  this.orders_tipe;
    }

    @Override
    public void setBzakupOnli(boolean bzakup) {}

    //@Override
     public String getName_tipe()
    {
        if(this.name_tipe==""){
        Cursor userCursor =  Context_zakup.getDBHelper().getWritableDatabase().rawQuery("select name from tipe_Tovar where _id = " + Integer.toString(this.id_tipe) , null);

        userCursor.moveToFirst();
            while( userCursor.isAfterLast() ==false) {   this.name_tipe = userCursor.getString(0);};
        userCursor.close();}
        return  this.name_tipe;
    }


}
