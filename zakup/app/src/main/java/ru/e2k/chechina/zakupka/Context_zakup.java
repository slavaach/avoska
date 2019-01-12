package ru.e2k.chechina.zakupka;

import android.content.Context;
import android.database.Cursor;
import ru.e2k.chechina.zakupka.Tipe.Home_selected;
import ru.e2k.chechina.zakupka.Tipe.Tipe_tovar;
import ru.e2k.chechina.zakupka.dao.Table_tipe;

import java.util.ArrayList;
import java.util.List;

//синголтон для контекста, коннекта к базе и основных справочников
public class Context_zakup {

    private static Context_zakup z;
    private static DBHelper db;
    private static Context cont;
    private static List<Tipe_tovar> tipe_tovars ;
    private static List<String> tipe_tovars_st;
    private static List<Table_tipe> tipe_tovars_tipe ;
    private static Home_selected singleHome;
    private static List<Home_selected> home;

//конструктор
    private Context_zakup(){};

    //путь к файлам
    public static String patch(){
        return  cont.getExternalFilesDir(null) + "/Авоська";
    };

//контекст
    public static void setConext(Context cont){
    if(z== null){
        z = new Context_zakup();
        z.cont = cont;
        z.db = new DBHelper(cont);


        //заполнение списка типов товаров
        UpdateTipe_tovars();
        UpdateHome();
    }
}
//коннект к базе
public static DBHelper getDBHelper () {  return z.db;}
    //контекст
public static Context getContext(){return z.cont;}
//список типов товаров
public static List<Tipe_tovar> getTipe_tovars(){return z.tipe_tovars;}
//список типов товаров в виде строки
public static List<String>  getTipe_tovars_st(){return z.tipe_tovars_st;}
//выбранный дом
    public static Home_selected getSingleHome() {
        return singleHome;
    }
//список домов
    public static List<Home_selected> getHome() {
        return home;
    }
//обновление списка товаров
    public static void UpdateTipe_tovars()
{
    //заполнение списка типов товаров
    Cursor userCursor =  Context_zakup.getDBHelper().getWritableDatabase().rawQuery("select _id , name , orders from Tipe_Tovar"  , null);
    z.tipe_tovars_st = new ArrayList<String>();
    z.tipe_tovars = new ArrayList<Tipe_tovar>();
    z.tipe_tovars_tipe = new ArrayList<>();
    int i=0;
    userCursor.moveToFirst();
    while( userCursor.isAfterLast() ==false) {
        z.tipe_tovars.add(new Tipe_tovar(userCursor.getString(1), userCursor.getInt(2) ,userCursor.getInt(0) ));
        z.tipe_tovars_tipe.add(new Tipe_tovar(userCursor.getString(1), userCursor.getInt(2) ,userCursor.getInt(0) ));
        z.tipe_tovars_st.add( userCursor.getString(1));
        userCursor.moveToNext();
        i++;
    };

    userCursor.close();
}

//обновление домов
public static void UpdateHome(){
    z.home = new ArrayList<Home_selected>();

    //получаем данные из бд в виде курсора надо бы перенести в Context_zakup
    Cursor userCursor =  Context_zakup.getDBHelper().getWritableDatabase().rawQuery("select _id , name , orders , selected from Home order by orders "  , null);
    userCursor.moveToFirst();
    while( userCursor.isAfterLast() ==false) {
        boolean b = false;
        if (userCursor.getInt(3) ==1) b = true;
        z.home.add(new Home_selected(userCursor.getString(1), userCursor.getInt(2) ,userCursor.getInt(0)  , b));
        userCursor.moveToNext();
    };

    userCursor.close();

    Cursor userCursor_qw =  Context_zakup.getDBHelper().getWritableDatabase()
            .rawQuery("select Home._id , Home.name , Home.orders , Home.selected " +
                    "from Home ,  Home_qw where Home_qw.selected = Home._id"  , null);

    userCursor_qw.moveToFirst();

    z.singleHome = new Home_selected(userCursor_qw.getString(1), userCursor_qw.getInt(2) ,userCursor_qw.getInt(0)  , false);


}

}
