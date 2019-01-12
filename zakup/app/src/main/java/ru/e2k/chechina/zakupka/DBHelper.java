package ru.e2k.chechina.zakupka;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//создание и обноление базы
public class DBHelper extends SQLiteOpenHelper {

    private static final long serialVersionUID = 1L;

    public DBHelper(Context context)  {
        super(context, "app.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // скрипт с оздания базы
        String script = "CREATE TABLE Tipe_Tovar (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, orders integer )";

         db.execSQL(script);
        db.execSQL("insert into Tipe_Tovar (  name , orders) values ( 'Мясо, рыба' , 1)");


        script = "CREATE TABLE Tovar (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, orders integer , id_tipe integer)";
        db.execSQL(script);

        script = "CREATE TABLE Home (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, orders integer  , selected integer)";
        db.execSQL(script);
        db.execSQL("insert into Home (  name , orders , selected) values ( 'Дом' , 1 , 1)");

        db.execSQL("insert into Home (  name , orders , selected) values ( 'Осташево' , 2 , 0)");
        script = "CREATE TABLE Home_qw (_id INTEGER PRIMARY KEY AUTOINCREMENT,  selected integer)";
        db.execSQL(script);
        db.execSQL("insert into Home_qw (  selected) values ( 1)");

         db.execSQL("CREATE TABLE zakup (_id INTEGER PRIMARY KEY AUTOINCREMENT,  id_home integer ," +
                 " id_tovar integer, counts integer , counts_z integer )");
        db.execSQL("CREATE TABLE zakup_sing (_id INTEGER PRIMARY KEY AUTOINCREMENT,  id_home integer ," +
                " id_tipe_tovar integer, name text , counts integer , orders integer , counts_z integer , setting integer )");
        db.execSQL("CREATE TABLE save (_id INTEGER PRIMARY KEY AUTOINCREMENT,  date datetime )");

        db.execSQL("CREATE TABLE save_zakup (_id INTEGER PRIMARY KEY AUTOINCREMENT,  id_zakup integer ," +
                "   id_save integer ,  counts integer )");

      db.execSQL("CREATE TABLE save_zakup_sing (_id INTEGER PRIMARY KEY AUTOINCREMENT,  id_zakup_sing integer ," +
                "   id_save integer ,  counts integer )");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop table


        // Recreate
           }
}
