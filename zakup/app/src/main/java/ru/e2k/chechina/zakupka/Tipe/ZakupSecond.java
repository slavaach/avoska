package ru.e2k.chechina.zakupka.Tipe;

import android.content.Context;
import android.database.Cursor;
import ru.e2k.chechina.zakupka.Context_zakup;
import ru.e2k.chechina.zakupka.R;
import ru.e2k.chechina.zakupka.dao.Table_tipe;

import java.util.ArrayList;
import java.util.List;

import static ru.e2k.chechina.zakupka.Context_zakup.getDBHelper;

//описание основного грида закупки
public class ZakupSecond implements Table_tipe {


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
    private ZakupSekondListener listener;
    private List<Table_tipe> updateZakups;

    public ZakupSekondListener getListener() {
        return listener;
    }

    public void setListener(ZakupSekondListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean check(){return bzakup;};
    //выводит строку для отражения в поле по R.id
    @Override
    public String getTextView(int i){
        switch (i) {
            case R.id.list_zakup_treelist_nameView:
                return this.name;
           case  R.id.list_zakup_treelist_nameTipeView:
                return this.name_tipe;
            case R.id.list_zakup_treelist_check:
                return getStringConts();
            default:
                return "";

        }
    }

    public String getStringConts(){if (counts> 1) return Integer.toString(counts); else return "";}

    //выводит список полей в Gride отражения
    @Override
    public int[] R_id_view(){
        int[] r = new int[]{R.id.list_zakup_treelist_check,R.id.list_zakup_treelist_nameView
        ,R.id.list_zakup_treelist_nameTipeView};
        return r;
    };


    @Override
    public ZakupSecond get(){ return this;}

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
                if (bzakup) return  "true"; else return  "false";
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



     public  void upDate(Context cont){};
//обновление по галке в gridе
@Override
    public void setCheck(boolean bzakup) {
        this.bzakup = bzakup;
        if(this.check)this.counts_z =this.counts; else this.counts_z = 0;
        String sTable = "";
        if(this.isSimpel) {

            if (!this.bzakup) getDBHelper().getReadableDatabase().execSQL("Update  zakup_sing   " +
                    "set counts_z = 0  where _id =  " + Integer.toString(this.id));
            else getDBHelper().getReadableDatabase().execSQL("Update  zakup_sing " +
                    "set counts_z = counts   where _id  =  " + Integer.toString(this.id));
        }
        else {
            if (!this.bzakup) getDBHelper().getReadableDatabase().execSQL("Update  zakup   " +
                    "set counts_z = 0  where id_tovar =  " + Integer.toString(this.id)
                    +" and id_home in (select _id from Home where selected = 1)");
            else getDBHelper().getReadableDatabase().execSQL("Update  zakup  " +
                    "set counts_z = counts   where id_tovar =  " + Integer.toString(this.id)
                    +" and id_home in (select _id from Home where selected = 1)");
        };
        for(Table_tipe up:updateZakups)up.setBzakupOnli(bzakup);
    }

//обновление при постановке гаоки на нижнем уровне дерева
    public void refreshBzakup() {
        String sql;
    if(this.isSimpel) {
        sql = "select counts_z  from zakup_sing where _id = "+ Integer.toString(this.id);
           }
    else
    {
        sql = "select sum(counts_z)  from zakup where " +
                "zakup.id_home in (select _id from Home where selected = 1) and id_tovar = "+ Integer.toString(this.id);

    }
    Cursor userCo = Context_zakup.getDBHelper().getWritableDatabase().rawQuery(sql, null);
    userCo.moveToFirst();
    this.counts_z = userCo.getInt(0);
    if(counts_z == counts) bzakup = true; else bzakup = false;
    if(this.listener != null) this.listener.onItemsSelected(bzakup);

    }

    @Override
    public void setBzakupOnli(boolean bzakup) {}

    public ZakupSecond(String name, int orders, int id_tipe, int id , int counts, String name_tipe , int orders_tipe , boolean isSimpel , int counts_z  , Context context){

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
        if(counts_z >= counts) bzakup = true; else bzakup = false;
        String Sql = "";
        updateZakups = new ArrayList<>();
        if (this.isSimpel())
            Sql = "select zakup_sing._id , " +
                    " zakup_sing.counts as counts , zakup_sing.counts_z as counts_z , Home.name " +
                    "from zakup_sing  left join   Home on  zakup_sing.id_home = Home._id " +
                    "where selected = 1 and zakup_sing.counts > 0 and zakup_sing._id = " + Integer.toString(this.getId());
        else Sql = "select zakup._id , " +
                " zakup.counts as counts , zakup.counts_z as counts_z , Home.name " +
                "from zakup  left join   Home on  zakup.id_home = Home._id " +
                "where selected = 1 and zakup.counts > 0 and zakup.id_tovar = " + Integer.toString(this.getId());

        Cursor userCursor =  Context_zakup.getDBHelper().getWritableDatabase().rawQuery(Sql
                , null);

        userCursor.moveToFirst();
        while( userCursor.isAfterLast() ==false) {
            updateZakups.add(new UpdateZakup(userCursor.getString(3) ,userCursor.getInt(0)
                    , userCursor.getInt(1) , userCursor.getInt(2) , this));
            userCursor.moveToNext();
        };

        userCursor.close();
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //@LayoutPrint(idLayout = R.id.list_zakup_treelist_ordersView)
    public int getOrders() {
        return this.orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    //@LayoutPrint(idLayout = R.id.list_zakup_idView)
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

    public int getOrders_tipe()
    {
        if(this.orders_tipe ==0){
            Cursor userCursor =  getDBHelper().getWritableDatabase().rawQuery("select orders from tipe_Tovar where _id = " + Integer.toString(this.id_tipe) , null);

            userCursor.moveToFirst();
            while( userCursor.isAfterLast() ==false) { this.orders_tipe = userCursor.getInt(0);};
            userCursor.close();}
        return  this.orders_tipe;
    }

    public String getName_tipe()
    {
        if(this.name_tipe==""){
            Cursor userCursor =  getDBHelper().getWritableDatabase().rawQuery("select name from tipe_Tovar where _id = " + Integer.toString(this.id_tipe) , null);

            userCursor.moveToFirst();
            while( userCursor.isAfterLast() ==false) {   this.name_tipe = userCursor.getString(0);};
            userCursor.close();}
        return  this.name_tipe;
    }

    //@Override
    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public boolean isCheck() {
        return check;
    }


    @Override
    public boolean isSimpel() {
        return isSimpel;
    }


    public List<Table_tipe> getUpdateZakups() {
        return updateZakups;
    }

    public void setUpdateZakups(List<Table_tipe> updateZakups) {
        this.updateZakups = updateZakups;
    }

    public interface ZakupSekondListener {
        public void onItemsSelected(boolean check);
    }
}
