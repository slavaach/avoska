package ru.e2k.chechina.zakupka.Tipe;

import android.content.Context;
import android.database.Cursor;
import ru.e2k.chechina.zakupka.Context_zakup;
import ru.e2k.chechina.zakupka.R;
import ru.e2k.chechina.zakupka.dao.Table_tipe;

//описание заказа
public class ZakupFirst implements Table_tipe {


    private String name; // название
    private int id; // ID
    private int orders;
    //private DBHelper dbH;
    private int orders_tipe = 0;
    private String name_tipe = "";
    private int counts;
    private boolean check;
    private int id_tipe;
    private boolean isSimpel;
    private int counts_z;
    private  boolean bzakup;
    //выводит строку для отражения в поле по R.id
    @Override
    public String getTextView(int i){
        switch (i) {
            case R.id.list_zakup_nameView:
                return this.name;
            case R.id.list_zakup_ordersView :
                return Integer.toString(this.orders);
            case R.id.list_zakup_idView:
                return Integer.toString(this.id);
            case  R.id.list_zakup_nameTipeView:
                return this.name_tipe;
            case R.id.list_zakup_check:
                return getStringConts();
            default:
                return "";

        }
    }
    @Override
    public boolean check(){return check;};

    //выводит список полей в Gride отражения
    @Override
    public int[] R_id_view(){
        int[] r = new int[]{R.id.list_zakup_nameView,R.id.list_zakup_idView
        ,R.id.list_zakup_ordersView, R.id.list_zakup_nameTipeView ,  R.id.list_zakup_check};
        return r;
    };

    @Override
    public ZakupFirst get(){ return this;}

     public  void upDate(Context cont){};
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
            case 4:
                if (check) return  "true"; else return  "false";
            case 5:
                return Integer.toString( this.counts);
            default:
                return "";
        }
    }
    public int getCounts_z() {
        return counts_z;
    }

    public void setCounts_z(int counts_z) {
        this.counts_z = counts_z;
    }

    public boolean isBzakup() {
        return bzakup;
    }

    public void setBzakup(boolean bzakup) {
        this.bzakup = bzakup;
    }




    public ZakupFirst(String name, int orders, int id_tipe, int id , int counts, String name_tipe , int orders_tipe , boolean isSimpel , Context context){

        this.name=name;
        this.id=id;
        this.orders=orders;
        this.id_tipe = id_tipe;
        //this.dbH=new DBHelper(context);
        this.orders_tipe = orders_tipe;
        this.name_tipe = name_tipe;
        this.counts = counts;
        if ( counts > 0 ) check = true; else check = false;
        this.isSimpel = isSimpel;
    }

    public ZakupFirst(String name, int orders, int id_tipe, int id , int counts, String name_tipe , int orders_tipe , boolean isSimpel , int counts_z  , Context context){

        this.name=name;
        this.id=id;
        this.orders=orders;
        this.id_tipe = id_tipe;
        //this.dbH=new DBHelper(context);
        this.orders_tipe = orders_tipe;
        this.name_tipe = name_tipe;
        this.counts = counts;
        if ( counts > 0 ) check = true; else check = false;
        this.isSimpel = isSimpel;
        this.counts_z = counts_z;
        if(counts_z > 0) bzakup = true; else bzakup = false;
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

    public void setID(int id) {
        this.id = id;
    }

    public int getId_tipe() {
        return id_tipe;
    }

    public void setId_tipe(int id_tipe) {
        this.id_tipe = id_tipe;
    }
//название типа
    public int getOrders_tipe()
    {
        if(this.orders_tipe ==0){
            Cursor userCursor =  Context_zakup.getDBHelper().getWritableDatabase().rawQuery("select orders from tipe_Tovar where _id = " + Integer.toString(this.id_tipe) , null);

            userCursor.moveToFirst();
            while( userCursor.isAfterLast() ==false) { this.orders_tipe = userCursor.getInt(0);};
            userCursor.close();}
        return  this.orders_tipe;
    }

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

    //@Override
    public int getCounts() {
        return counts;
    }

    public String getStringConts(){if (counts> 1) return Integer.toString(counts); else return "";}

    public void setCounts(int counts) {
        this.counts = counts;
        if (this.counts ==0 )this.check =false; else this.check=true;
    }

    public boolean isCheck() {
        return check;
    }

    @Override
    public void setBzakupOnli(boolean bzakup) {}
//обновление по галке в gride
    @Override
    public void setCheck(boolean check) {
        this.check = check;
        if(this.check)this.counts =1; else this.counts = 0;

        String sTable = "";
        if(this.isSimpel) sTable = " zakup_sing "; else sTable = " zakup ";
        if(!this.check) Context_zakup.getDBHelper().getReadableDatabase().execSQL("Update  "+ sTable +"  " +
                "set counts = 0  where _id =  " + Integer.toString(this.id));
        else Context_zakup.getDBHelper().getReadableDatabase().execSQL("Update  "+ sTable +"  " +
                "set counts = 1  where _id =  " + Integer.toString(this.id));


    }
    @Override
    public boolean isSimpel() {
        return isSimpel;
    }

    public void setSimpel(boolean simpel) {
        isSimpel = simpel;
    }

}
