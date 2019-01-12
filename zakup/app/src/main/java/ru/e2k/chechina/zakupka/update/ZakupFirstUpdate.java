package ru.e2k.chechina.zakupka.update;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import ru.e2k.chechina.zakupka.Context_zakup;
import ru.e2k.chechina.zakupka.R;
import ru.e2k.chechina.zakupka.Tipe.Tipe_tovar;
import ru.e2k.chechina.zakupka.Tipe.ZakupFirst;
import ru.e2k.chechina.zakupka.dao.TableUpdate;
import ru.e2k.chechina.zakupka.dao.Table_tipe;
import ru.e2k.chechina.zakupka.dao.ToastZK;

import java.util.ArrayList;
import java.util.List;
//взаимодействие с базой при заказе
public class ZakupFirstUpdate extends TableUpdate {
    private static ZakupFirstUpdate z;
    private ZakupFirstUpdate(){}

//обновление заказа
    public static void upDate(Context cont , final ZakupFirst t)
    {
        if (z==null) z  = new ZakupFirstUpdate();

        if(t.isSimpel()) { //простой товар

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
            final Spinner update_tipe_tovar_tipe = (Spinner) promptsView.findViewById(R.id.promt_add_tovar_autocomplete);


            int iTipe = 0;
            for (int i = 0; i < Context_zakup.getTipe_tovars_st().size(); i++)
            { if (Context_zakup.getTipe_tovars_st().get(i) == t.getName_tipe())iTipe = i; }

            update_tipe_tovar_name.setText(t.getName());
            update_tipe_tovar_orders.setText(Integer.toString(t.getCounts()));
            update_tipe_tovar_tipe.setSelection(iTipe);

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
            });//кнопка отменить
            final AlertDialog dialog_update = builder.create();
            dialog_update.show();
            dialog_update.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            dialog_update.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//кнопка ок. Вот это работет. Затем. что б при ошибка не закрывало форму
                    if (update_tipe_tovar_orders.getText().length() != 0) {
                        List<Tipe_tovar> tipet = new ArrayList<Tipe_tovar>();

                        for (int i = 0; i < Context_zakup.getTipe_tovars().size(); i++) {
                            if (Context_zakup.getTipe_tovars().get(i).getName().contains(update_tipe_tovar_tipe.getSelectedItem().toString())) {
                                tipet.add(Context_zakup.getTipe_tovars().get(i));
                            }
                        }
                        int itipe = 0;
                        if (tipet.size() != 0) itipe = tipet.get(0).getId();
                        String sTable = "";
                        if (t.isSimpel()) sTable = " zakup_sing ";
                        else sTable = " list_zakup ";
                        Context_zakup.getDBHelper().getReadableDatabase().execSQL("Update  zakup_sing  " +
                                "set name = '" + update_tipe_tovar_name.getText().toString() + "' ,  counts = " +
                                update_tipe_tovar_orders.getText().toString() + " , id_tipe_tovar =  " + Integer.toString(itipe)+
                                " where _id =  " + Integer.toString(t.getId()));
                        if (listenerUpdate != null) {
                            listenerUpdate.onItemsUpdate();
                        }
                            dialog_update.dismiss(); // Отпускает диалоговое окно
                    } else
                        update_tipe_tovar_Titul.setText("Введите количество");
                }
            });
        }
        else {
// постоянный товар
            LayoutInflater li = LayoutInflater.from(cont);
            View promptsView = li.inflate(R.layout.promt_counts, null);



            AlertDialog.Builder builder = new AlertDialog.Builder(cont);
            builder.setView(promptsView);
//поля формы
            final TextView update_tipe_tovar_Titul = ((TextView) promptsView.findViewById(R.id.promt_counts_name));
            update_tipe_tovar_Titul.setText("Изменяем количество");
            final EditText update_tipe_tovar_orders = (EditText) promptsView.findViewById(R.id.promt_counts_input_rov);


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
            });
            final AlertDialog dialog_update = builder.create();
            dialog_update.show();

            dialog_update.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            dialog_update.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//кнопка ок. Вот это работет. Затем. что б при ошибка не закрывало форму
                    if (update_tipe_tovar_orders.getText().length() != 0) {

                        Context_zakup.getDBHelper().getReadableDatabase().execSQL("Update  zakup  " +
                                "set counts = " + update_tipe_tovar_orders.getText().toString() +
                                " where _id =  " + Integer.toString(t.getId()));
                        if (listenerUpdate != null) {
                            listenerUpdate.onItemsUpdate();
                        }
                        dialog_update.dismiss(); // Отпускает диалоговое окно
                    } else {
                        update_tipe_tovar_Titul.setText("Введите количество");
                    }
                }
            });

        };
    }
//удаляем из листа закупки
    public static void Delete(Context cont , final ZakupFirst t)
    {
        if (z==null) z  = new ZakupFirstUpdate();

        if (t.isSimpel()) {//если временный товар
            AlertDialog.Builder builder = new AlertDialog.Builder(cont);

            builder.setTitle("Важное сообщение!")
                    .setMessage("Вы удаляете товар")
                    .setCancelable(true);

            builder.setPositiveButton("удалить", new DialogInterface.OnClickListener() { // Кнопка ОК
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Context_zakup.getDBHelper().getReadableDatabase().execSQL("delete from  zakup_sing where _id =  " + Integer.toString(t.getId()));
                    if (listenerDelete != null) {
                        listenerDelete.onItemsUpdate();
                    }dialog.dismiss(); // Отпускает диалоговое окно
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
        } else {//постоянный товар (удалить нельзя)
            ToastZK toast = new ToastZK(cont, "Это постоянный товар.",Toast.LENGTH_LONG , cont.getResources().getColor(R.color.primary) );
            toast.show();}
    }

    //вставляем заказ. Вставляется только временный товар
    public static void Insert(Context cont )
    {
        if (z==null) z  = new ZakupFirstUpdate();
        LayoutInflater li = LayoutInflater.from(cont );
        View promptsView = li.inflate(R.layout.promt_add_tovar, null);

        Spinner autoCompleteTextView = (Spinner) promptsView.findViewById(R.id.promt_add_tovar_autocomplete);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(cont , R.layout.support_simple_spinner_dropdown_item, Context_zakup.getTipe_tovars_st());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteTextView.setAdapter(adapter);

//поля формы
        final AlertDialog.Builder builder_add = new AlertDialog.Builder(cont );
        builder_add.setView(promptsView);

        final EditText add_tipe_tovar_name = (EditText)promptsView.findViewById(R.id.promt_add_tovar_input_text);
        final EditText add_tipe_tovar_orders = (EditText)promptsView.findViewById(R.id.promt_add_tovar_input_rov);
        final TextView add_tipe_tovar_Titul = (TextView)promptsView.findViewById(R.id.promt_add_tovar_name);
        final Spinner add_tipe_tovar_tipe = (Spinner)promptsView.findViewById(R.id.promt_add_tovar_autocomplete);

        add_tipe_tovar_orders.setText("1");

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
                    Context_zakup.getDBHelper().getReadableDatabase().execSQL("Insert into zakup_sing ( id_home  , name , counts , id_tipe_tovar  ) " +
                            " select selected  , '" + add_tipe_tovar_name.getText().toString()
                            + "'  , " + add_tipe_tovar_orders.getText().toString() + " , " + Integer.toString(itipe) + " from Home_qw ");
                    if (listenerInsert != null) {
                        listenerInsert.onItemsUpdate();
                    }
                    dialog_add.dismiss(); // Отпускает диалоговое окно}}
                }
                else{
                    add_tipe_tovar_Titul.setText("Введите количество");
                };
            }
        });

    }


//считываем из базы что закупать
    public static List<Table_tipe> Select(Context cont ,String sorders){
        List tovars = new ArrayList<Table_tipe>();
        //получаем данные из бд в виде курсора

        String Sql = "select * from  (select zakup_sing._id , zakup_sing.name , zakup_sing.orders ," +
                " Tipe_Tovar.name as tname , ifnull(Tipe_Tovar.orders , 0) as torders , zakup_sing.counts " +
                ", zakup_sing.id_tipe_tovar , 0 as Simpel from zakup_sing " +
                " left join   Tipe_Tovar on  Tipe_Tovar._id = zakup_sing.id_tipe_tovar " +
                "where  zakup_sing.id_home in (select selected from Home_qw) ";
        Sql = Sql + " union all select Zakup._id , Tovar.name , Tovar.orders ," +
                " Tipe_Tovar.name as tname, ifnull(Tipe_Tovar.orders , 0)  as torders , counts , Tovar.id_tipe , 1 as  Simpel  " +
                "from Zakup , Tovar left join  " +
                " Tipe_Tovar on  Tipe_Tovar._id = Tovar.id_tipe " +
                "where zakup.id_tovar= Tovar._id and zakup.id_home in (select selected from Home_qw) )";

        Cursor userCursor =  Context_zakup.getDBHelper().getWritableDatabase().rawQuery(Sql
                + sorders, null);

        userCursor.moveToFirst();
        while( userCursor.isAfterLast() ==false) {
            boolean bSimpel = false;
            if (userCursor.getInt(7) == 0 ) bSimpel = true;
            tovars.add(new ZakupFirst(userCursor.getString(1), userCursor.getInt(2)
                    ,userCursor.getInt(6) ,userCursor.getInt(0) ,userCursor.getInt(5 )
                    , userCursor.getString(3)
                    , userCursor.getInt(4) ,bSimpel ,cont ));
            userCursor.moveToNext();
        };
        userCursor.close();
return tovars;


    }

}
