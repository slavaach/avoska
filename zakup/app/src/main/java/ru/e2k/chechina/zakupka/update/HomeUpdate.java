package ru.e2k.chechina.zakupka.update;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import ru.e2k.chechina.zakupka.Context_zakup;
import ru.e2k.chechina.zakupka.R;
import ru.e2k.chechina.zakupka.Tipe.Tipe_tovar;
import ru.e2k.chechina.zakupka.dao.TableUpdate;
import ru.e2k.chechina.zakupka.dao.Table_tipe;

import java.util.ArrayList;
import java.util.List;
//взаимодействие с базой по справочнику домов
public class HomeUpdate extends TableUpdate {

    private static HomeUpdate z;

    private HomeUpdate(){};


//обновляем дома
    public static void upDate(Context cont , final Tipe_tovar t)
    { if (z==null) z  = new HomeUpdate();
        LayoutInflater li = LayoutInflater.from(cont);
        View promptsView = li.inflate(R.layout.promt_add_tipe_tovar, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder.setView(promptsView);
//поля формы
        ((TextView)promptsView.findViewById(R.id.promt_add_tipe_tovar_name)).setText("Изменяем дом");
        final EditText update_tipe_tovar_name = (EditText) promptsView.findViewById(R.id.promt_add_tipe_tovar_input_text);
        final EditText update_tipe_tovar_orders = (EditText) promptsView.findViewById(R.id.promt_add_tipe_tovar_input_rov);
        final TextView update_tipe_tovar_Titul = (TextView) promptsView.findViewById(R.id.promt_add_tipe_tovar_name);


         update_tipe_tovar_name.setText(t.getName());
        update_tipe_tovar_orders.setText(Integer.toString(t.getOrders()));

        builder.setCancelable(true);

        builder.setCancelable(true);
        builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() { // Кнопка ОК. то, что внутри не работет. Необходимо, чтоб отразилась кнопка ок
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });//кнопка отмены
        final AlertDialog dialog_update = builder.create();
        dialog_update.show();
        dialog_update.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        dialog_update.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//кнопка ок. Вот это работет. Затем. что б при ошибка не закрывало форму
                if (update_tipe_tovar_orders.getText().length() != 0) {
                     String name= "";
                    if (update_tipe_tovar_name.getText().toString().length() > 10 )
                        name =update_tipe_tovar_name.getText().toString().substring(0 , 10);
                    else name =update_tipe_tovar_name.getText().toString();
                    Context_zakup.getDBHelper().getReadableDatabase().execSQL("Update  Home  " +
                            "set name = '" + name + "' ,  orders =" +
                            update_tipe_tovar_orders.getText().toString() + "  " +
                            "where _id =  " + Integer.toString(t.getId()));
                    if (listenerUpdate != null) {
                        listenerUpdate.onItemsUpdate();
                    }
                    dialog_update.dismiss(); // Отпускает диалоговое окно
                } else
                    update_tipe_tovar_Titul.setText("Введите порядковый номер");
            }
        });

    }
//удаляем дома,причем вмести с необходимыми закупками
    public static void Delete(Context cont , final Tipe_tovar t)
    {
        if (z==null) z  = new HomeUpdate();

        AlertDialog.Builder builder = new AlertDialog.Builder(cont);

        builder.setTitle("Важное сообщение!")
                .setMessage("Вы удаляете дом")
                .setCancelable(true);

        builder.setPositiveButton("удалить", new DialogInterface.OnClickListener() { // Кнопка ОК
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //ставлю произвольный выбор. если стоя этот
                Context_zakup.getDBHelper().getReadableDatabase().execSQL("delete from  Home where _id =  " + Integer.toString(t.getId()));
                Context_zakup.getDBHelper().getReadableDatabase().execSQL("delete from  zakup where id_home =  " + Integer.toString(t.getId()));
                if(Context_zakup.getSingleHome().getId() == t.getId())
                    Context_zakup.getDBHelper().getReadableDatabase().execSQL("Update Home_qw set  selected = " +
                            "(select  _id from  home order by _id LIMIT 1)");

                if (listenerDelete != null) {
                    listenerDelete.onItemsUpdate();
                }
                dialog.dismiss(); // Отпускает диалоговое окно
            }
        });
        builder.setNegativeButton("не надо!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

//добавляем дома
    public static void Insert(Context cont )
    {
        if (z==null) z  = new HomeUpdate();

        //LayoutInflater li = LayoutInflater.from(this);
        View promptsView = LayoutInflater.from(cont)
                .inflate(R.layout.promt_add_tipe_tovar, null);
        final AlertDialog.Builder builder_add = new AlertDialog.Builder(cont);
        builder_add.setView(promptsView);
        ((TextView)promptsView.findViewById(R.id.promt_add_tipe_tovar_name)).setText("Добавляем дом");
//поля формы
        final EditText add_tipe_tovar_name = (EditText)promptsView.findViewById(R.id.promt_add_tipe_tovar_input_text);
        final EditText add_tipe_tovar_orders = (EditText)promptsView.findViewById(R.id.promt_add_tipe_tovar_input_rov);
        final TextView add_tipe_tovar_Titul = (TextView)promptsView.findViewById(R.id.promt_add_tipe_tovar_name);

        builder_add.setCancelable(true);
        builder_add.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() { // Кнопка ОК. то, что внутри не работет. Необходимо, чтоб отразилась кнопка ок
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder_add.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });//кнопка ок. Вот это работет. Затем. что б при ошибка не закрывало форму
        final AlertDialog dialog_add = builder_add.create();
        dialog_add.show();
        dialog_add.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        dialog_add.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if( add_tipe_tovar_orders.getText().length()!=0) {
                    String name= "";
                    if (add_tipe_tovar_name.getText().toString().length() > 10 )
                        name =add_tipe_tovar_name.getText().toString().substring(0 , 10);
                    else name =add_tipe_tovar_name.getText().toString();
                    Context_zakup.getDBHelper().getReadableDatabase().execSQL("Insert into Home ( name , orders , selected) values (  '"
                            + name  + "'  , " + add_tipe_tovar_orders.getText().toString() + " , 0 )");
                    Context_zakup.getDBHelper().getReadableDatabase().execSQL("Insert into zakup ( id_home , id_tovar , counts ) " +
                            "select Home._id , Tovar._id , 0 from Home , Tovar where " +
                            " not Home._id in (select id_home from zakup ) ");
                    if (listenerInsert != null) {
                        listenerInsert.onItemsUpdate();
                    }
                    dialog_add.dismiss(); // Отпускает диалоговое окно}}
                }
                else{
                    add_tipe_tovar_Titul.setText("Введите порядковый номер");//не закрываем форму
                };
            }
        });



    }

//считываем из базы дома
    public static List<Table_tipe> Select(String orders )
    {
        List<Table_tipe> tipe_tovars = new ArrayList<Table_tipe>();
        //получаем данные из бд в виде курсора
        Cursor userCursor =  Context_zakup.getDBHelper().getWritableDatabase().rawQuery("select _id , name , orders from Home"
                + orders, null);

        userCursor.moveToFirst();
        while( userCursor.isAfterLast() ==false) {
            tipe_tovars.add(new Tipe_tovar(userCursor.getString(1), userCursor.getInt(2) ,userCursor.getInt(0) ));
            userCursor.moveToNext();
        };

        userCursor.close();
     return tipe_tovars;
    }

}
