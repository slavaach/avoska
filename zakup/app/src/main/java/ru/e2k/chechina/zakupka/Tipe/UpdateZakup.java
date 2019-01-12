package ru.e2k.chechina.zakupka.Tipe;

import android.widget.CheckBox;
import ru.e2k.chechina.zakupka.Context_zakup;
import ru.e2k.chechina.zakupka.R;
import ru.e2k.chechina.zakupka.dao.Table_tipe;


//описание закупки (нижний узел дерева с домом)
public class UpdateZakup implements Table_tipe {

    private String home; // название
    private int id; // ID
    private int counts;
    private int counts_z;
    private boolean bzakup;
    private UpdateZakupListener listener;
    private CheckBox checkBox;

    @Override
    public boolean check(){return  this.bzakup;};

    //выводит строку для отражения в поле по R.id
    @Override
    public String getTextView(int i){
        switch (i) {
            case R.id.list_update_zakup_layout_home:
                return this.home;
            case R.id.list_update_zakup_layout_counts_zView:
                return getTextCounts();

            default:
                return "";

        }
    }

    //выводит список полей в Gride отражения
    @Override
    public int[] R_id_view(){
        int[] r = new int[]{R.id.list_update_zakup_layout_counts_zView, R.id.list_update_zakup_layout_home};
        return r;
    };

    public ZakupSecond getZakupSecond() {
        return zakupSecond;
    }



    private  ZakupSecond zakupSecond;

    public  UpdateZakup(String home , int id , int counts , int counts_z , ZakupSecond zakupSecond){
        this.home =home;
        this.id =id;
        this.counts = counts;
        this.counts_z = counts_z;
        if (counts <= counts_z) this.bzakup=true;else this.bzakup=false;
        this.zakupSecond = zakupSecond;
    };


    @Override
    public UpdateZakup get(){ return this;}


    public boolean isBzakup() {
        return  this.bzakup;
    }

@Override
    public void setCheck(boolean bzakup) {

        updateBzakup(bzakup);
        this.zakupSecond.refreshBzakup();

    }
    @Override
    public void setBzakupOnli(boolean bzakup) {

        this.bzakup = bzakup;
        if(this.bzakup)this.counts_z =this.counts; else this.counts_z = 0;
       // if(this.checkBox != null) {this.checkBox.setText(getTextCounts());this.checkBox.setChecked(bzakup);}

    }


//обновление по галке
    private void updateBzakup(boolean bzakup){
        this.bzakup = bzakup;
        if(this.bzakup)this.counts_z =this.counts; else this.counts_z = 0;

        String sTable = "";
        if(this.zakupSecond.isSimpel()) sTable = " zakup_sing "; else sTable = " zakup ";
        if(!this.bzakup) Context_zakup.getDBHelper().getReadableDatabase().execSQL("Update  "+ sTable +"  " +
                "set counts_z = 0  where _id =  " + Integer.toString(this.id));
        else Context_zakup.getDBHelper().getReadableDatabase().execSQL("Update  "+ sTable +"  " +
                "set counts_z = "+ Integer.toString(this.counts) +"  where _id =  " + Integer.toString(this.id));
    }

    public String getTextCounts(){
        return Integer.toString(this.counts_z) + "(" + Integer.toString(this.counts)+")";

    }

    @Override
    public String getText(int i){
        switch (i) {
            case 0:
                return Integer.toString( this.id);
            case 1:
                return this.home;
            case 2:
                return Integer.toString( this.counts);
            case 3:
                return Integer.toString( this.counts_z);
            default:
                return "";
        }
    }

    @Override
    public boolean isSimpel() {
        return false;
    }



    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


     public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public int getCounts_z() {
        return counts_z;
    }

    public void setCounts_z(int counts_z) {
        this.counts_z = counts_z;
    }

    public UpdateZakupListener getListener() {
        return listener;
    }

    public void setListener(UpdateZakupListener listener) {
        this.listener = listener;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public interface UpdateZakupListener {
        public void onItemsSelected(boolean check);
    }

}
