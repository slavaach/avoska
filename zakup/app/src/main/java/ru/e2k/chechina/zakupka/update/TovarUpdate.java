package ru.e2k.chechina.zakupka.update;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import ru.e2k.chechina.zakupka.Context_zakup;
import ru.e2k.chechina.zakupka.R;
import ru.e2k.chechina.zakupka.Tipe.Tipe_tovar;
import ru.e2k.chechina.zakupka.Tipe.Tovar;
import ru.e2k.chechina.zakupka.dao.TableUpdate;
import ru.e2k.chechina.zakupka.dao.Table_tipe;

import java.util.ArrayList;
import java.util.List;
//взаимодействие с базой по справочнику постоянных товаров
public class TovarUpdate extends TableUpdate {

    private static TovarUpdate z;

    private TovarUpdate(){};


    //изменяем тип товаров
    public static void upDate(Context cont , final Tovar t)
    { if (z==null) z  = new TovarUpdate();

        LayoutInflater li = LayoutInflater.from(cont);
        View promptsView = li.inflate(R.layout.promt_add_tovar, null);

        Spinner autoCompleteTextView = (Spinner) promptsView.findViewById(R.id.promt_add_tovar_autocomplete);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(cont, R.layout.support_simple_spinner_dropdown_item, Context_zakup.getTipe_tovars_st());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteTextView.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder.setView(promptsView);

        ((TextView) promptsView.findViewById(R.id.promt_add_tovar_name)).setText("Изменяем товар");
//поля формы
        final EditText update_tipe_tovar_name = (EditText) promptsView.findViewById(R.id.promt_add_tovar_input_text);
        final EditText update_tipe_tovar_orders = (EditText) promptsView.findViewById(R.id.promt_add_tovar_input_rov);
        final TextView update_tipe_tovar_Titul = (TextView) promptsView.findViewById(R.id.promt_add_tovar_name);
        final Spinner update_tipe_tovar_tipe = (Spinner)promptsView.findViewById(R.id.promt_add_tovar_autocomplete);

//справочник типов товаров
        update_tipe_tovar_name.setText(t.getName());
        update_tipe_tovar_orders.setText(Integer.toString(t.getOrders()));
        int iTipe = 0;
        for (int i = 0; i < Context_zakup.getTipe_tovars_st().size(); i++)
        { if (Context_zakup.getTipe_tovars_st().get(i) == t.getName_tipe())iTipe = i; };

        update_tipe_tovar_tipe.setSelection(iTipe);

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
                    Tovar tr = (Tovar)t.get();
                    List<Tipe_tovar> tipet = new ArrayList<Tipe_tovar>();

                    for (int i = 0; i < Context_zakup.getTipe_tovars().size(); i++) {
                        if (Context_zakup.getTipe_tovars().get(i).getName().contains(update_tipe_tovar_tipe.getSelectedItem().toString())) {
                            tipet.add(Context_zakup.getTipe_tovars().get(i));
                        }
                    }
                    int itipe = 0;
                    if ( tipet.size() !=0) itipe = tipet.get(0).getId();

                    Context_zakup.getDBHelper().getReadableDatabase().execSQL("Update  Tovar  " +
                            "set name = '" + update_tipe_tovar_name.getText().toString() + "' ,  orders = " +
                            update_tipe_tovar_orders.getText().toString() + " , id_tipe =  " + Integer.toString(itipe)+
                            " where _id =  " + Integer.toString(tr.getId()));
                    if (listenerUpdate != null) {
                        listenerUpdate.onItemsUpdate();
                    }
                    dialog_update.dismiss(); // Отпускает диалоговое окно
                }
                else
                    update_tipe_tovar_Titul.setText("Введите порядковый номер");//не отпускает диалоговое окно
            }
        });

    }
    //удаляем постоянные отвары
    public static void Delete(Context cont , final Tovar t)
    {
        if (z==null) z  = new TovarUpdate();
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);

        builder.setTitle("Важное сообщение!")
                .setMessage("Вы удаляете товар")
                .setCancelable(true);

        builder.setPositiveButton("удалить", new DialogInterface.OnClickListener() { // Кнопка ОК
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Context_zakup.getDBHelper().getReadableDatabase().execSQL("delete from  Tovar where _id =  " + Integer.toString(t.getId()));
                Context_zakup.getDBHelper().getReadableDatabase().execSQL("delete from  zakup where id_tovar =  " + Integer.toString(t.getId()));
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

//добавляем постоянные товаря
    public static void Insert(Context cont )
    {
        if (z==null) z  = new TovarUpdate();
        LayoutInflater li = LayoutInflater.from(cont);
        View promptsView = li.inflate(R.layout.promt_add_tovar, null);
        Spinner autoCompleteTextView = (Spinner) promptsView.findViewById(R.id.promt_add_tovar_autocomplete);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(cont, R.layout.support_simple_spinner_dropdown_item, Context_zakup.getTipe_tovars_st());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteTextView.setAdapter(adapter);

//поля формы
        final AlertDialog.Builder builder_add = new AlertDialog.Builder(cont);
        builder_add.setView(promptsView);

        final EditText add_tipe_tovar_name = (EditText)promptsView.findViewById(R.id.promt_add_tovar_input_text);
        final EditText add_tipe_tovar_orders = (EditText)promptsView.findViewById(R.id.promt_add_tovar_input_rov);
        final TextView add_tipe_tovar_Titul = (TextView)promptsView.findViewById(R.id.promt_add_tovar_name);
        final Spinner add_tipe_tovar_tipe = (Spinner)promptsView.findViewById(R.id.promt_add_tovar_autocomplete);

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
        });
        final AlertDialog dialog_add = builder_add.create();
        dialog_add.show();
        dialog_add.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


        dialog_add.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {//кнопка ок. Вот это работет. Затем. что б при ошибка не закрывало форму
                if( add_tipe_tovar_orders.getText().length()!=0) {
                    List<Tipe_tovar> tipet = new ArrayList<Tipe_tovar>();

                    for (int i = 0; i < Context_zakup.getTipe_tovars().size(); i++) {
                        if (Context_zakup.getTipe_tovars().get(i).getName().contains(add_tipe_tovar_tipe.getSelectedItem().toString())) {
                            tipet.add(Context_zakup.getTipe_tovars().get(i));
                        }
                    }
                    int itipe = 0;
                    if ( tipet.size() !=0) itipe = tipet.get(0).getId();
                    Context_zakup.getDBHelper().getReadableDatabase().execSQL("Insert into Tovar ( name , orders , id_tipe ) values (  '" + add_tipe_tovar_name.getText().toString()
                            + "'  , " + add_tipe_tovar_orders.getText().toString() + " , " + Integer.toString(itipe) + ")");
                    Context_zakup.getDBHelper().getReadableDatabase().execSQL("Insert into zakup ( id_home , id_tovar , counts ) " +
                            "select Home._id , Tovar._id , 0 from Home , Tovar where " +
                            " not Tovar._id in (select id_tovar from zakup ) ");
                    if (listenerInsert != null) {
                        listenerInsert.onItemsUpdate();
                    }
                    dialog_add.dismiss(); // Отпускает диалоговое окно}}
                }
                else{
                    add_tipe_tovar_Titul.setText("Введите порядковый номер");//не отпускаетдиалоговое окно
                };
            }
        });


    }

//считываем из базы постоянные товары
    public static List<Table_tipe> Select (Context cont  ,String sorders){
        List tovars = new ArrayList<Table_tipe>();
        //получаем данные из бд в виде курсора
        Cursor userCursor =  Context_zakup.getDBHelper().getWritableDatabase().rawQuery("select Tovar._id , Tovar.name , Tovar.orders ," +
                " Tipe_Tovar.name , ifnull(Tipe_Tovar.orders , 0)  from Tovar left join  " +
                " Tipe_Tovar on  Tipe_Tovar._id = Tovar.id_tipe "  + sorders, null);

        userCursor.moveToFirst();
        while( userCursor.isAfterLast() ==false) {
            tovars.add(new Tovar(userCursor.getString(1), userCursor.getInt(2)
                    ,1 ,userCursor.getInt(0) , userCursor.getString(3)
                    , userCursor.getInt(4) ,cont ));
            userCursor.moveToNext();
        };

        userCursor.close();
        return tovars;
    }

}
