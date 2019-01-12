package ru.e2k.chechina.zakupka.update;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.e2k.chechina.zakupka.Context_zakup;
import ru.e2k.chechina.zakupka.R;
import ru.e2k.chechina.zakupka.Tipe.UpdateZakup;
import ru.e2k.chechina.zakupka.Tipe.ZakupSecond;
import ru.e2k.chechina.zakupka.dao.TableUpdate;
import ru.e2k.chechina.zakupka.dao.Table_tipe;
import ru.e2k.chechina.zakupka.dao.ToastZK;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//класс взаимодействия с базой при закупке
public class ZakupSecondUpdate extends TableUpdate {

    private static ZakupSecondUpdate z;
    private ZakupSecondUpdate(){}
    private static Cursor userCursor;

  //изменяем колличество
    public static void upDate(Context cont , final UpdateZakup t)
    {
        LayoutInflater li = LayoutInflater.from(cont);
        View promptsView = li.inflate(R.layout.promt_counts, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder.setView(promptsView);
        //поля и текст
        final TextView update_tipe_tovar_Titul = ((TextView) promptsView.findViewById(R.id.promt_counts_name));
        update_tipe_tovar_Titul.setText("Изменяем количество");
        final EditText update_tipe_tovar_orders = (EditText) promptsView.findViewById(R.id.promt_counts_input_rov);


        builder.setCancelable(true); //наличие кнопки отмена
        builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() { // Кнопка ОК надо,ато не появится то,что внутри будет переопределенно
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });//кнопка отмена
        final AlertDialog dialog_update = builder.create();
        dialog_update.show();

        dialog_update.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);//показываю клавиатуру
        dialog_update.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Кнопка Ок вот это работает. Что бы не закрывало окно если ошибка
                if (update_tipe_tovar_orders.getText().length() != 0) {
                    String sTabl;
                     if(t.getZakupSecond().isSimpel()) sTabl = "zakup_sing"; else sTabl = "zakup";


                    Context_zakup.getDBHelper().getReadableDatabase().execSQL("Update  " + sTabl+
                            " set counts_z = " + update_tipe_tovar_orders.getText().toString() +
                            " where _id =  " + Integer.toString(t.getId()));
                    if (listenerUpdate != null) {
                        listenerUpdate.onItemsUpdate();
                    }
                     dialog_update.dismiss(); // Отпускает диалоговое окно
                } else {
                    update_tipe_tovar_Titul.setText("Введите количество");//не отпускает диалоговое окно
                }
            }
        });

    }
//сохраняем историю и изменяем кол-во закупаемого. Стоит в интерфейсе вместо кнопки insert
    public static void Insert(final Context cont , final Resources res){

            Observable.just( docHistory(cont ,  res)) //формируем документ для истории
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())//перенести в отдельный поток (что б не зависло, когда сохраняет
                    .flatMap(new Func1<Document, Observable<String>>() {//на 15 версии androida нет lamda-выражение
                        @Override
                        public Observable<String> call(Document s) {
                           FileOutputStream f= null;
                            try {
                                f = printHistory(cont, res ,  s);}//формирую файл
                            catch(Exception ex){
                                ToastZK toast = new ToastZK(cont, ex.getMessage(), Toast.LENGTH_SHORT, res.getColor(R.color.primary) );
                                toast.show();

                            }
                            finally {
                                try {
                                    if (f != null) f.close();//закрываю файл
                                } catch (Exception ex) {
                                    ToastZK toast = new ToastZK(cont, ex.getMessage(), Toast.LENGTH_SHORT, res.getColor(R.color.primary));
                                    toast.show();
                                }
                            }
                            return Observable.just("историю сохранили");//возвращию что все хорошо
                        }
                    })
                    .subscribe(new Subscriber<String>() {//возвращаю в основной поток
                @Override
                public void onCompleted() {
               }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(String s) {
                    ToastZK toast = new ToastZK(cont, s, Toast.LENGTH_SHORT, res.getColor(R.color.primary));
                    toast.show();//показываю, что все хорошо
                }
            });



        Context_zakup.getDBHelper().getReadableDatabase().execSQL("Update zakup set counts = (counts - counts_z) , counts_z = 0" +
                " where counts >= counts_z and id_home in (select _id from Home where selected = 1)");
        Context_zakup.getDBHelper().getReadableDatabase().execSQL("Update zakup set counts = 0 , counts_z = 0" +
                " where counts < counts_z and id_home in (select _id from Home where selected = 1)");
        Context_zakup.getDBHelper().getReadableDatabase().execSQL("delete from  zakup_sing where counts <= counts_z " +
                " and id_home in (select _id from Home where selected = 1)");
        Context_zakup.getDBHelper().getReadableDatabase().execSQL("Update  zakup_sing set counts = (counts - counts_z) , counts_z = 0" +
                " where counts > counts_z  and id_home in (select _id from Home where selected = 1)");
        if (listenerInsert != null) {
            listenerInsert.onItemsUpdate();
        }
    }

    //формирую xml для сохранения
private static Document docHistory(Context cont , Resources res )  {
        try{
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    Document doc = factory.newDocumentBuilder().newDocument();

    Element root = doc.createElement("root");
    root.setAttribute("xmlns", "http://www.javacore.ru/schemas/");
    doc.appendChild(root);

    SimpleDateFormat sdf = new SimpleDateFormat("yy_MM_dd_HH_mm_ss");
    String file_name = "zakup" + sdf.format(new Date()) + ".xml";

    Element item1 = doc.createElement("date");
    item1.setAttribute("new", sdf.format(new Date()));

    item1.appendChild(Zakup(doc));//кладу посточнные товары
    item1.appendChild(Zakup_sing(doc));//кладу временные товары
    item1.appendChild(Tovar(doc));//сохраняю справочник товаров
    item1.appendChild(Home(doc));//сохраняю справочник домов
    item1.appendChild(Tipe_tovar(doc));//сохрчняю справочник типов товаров

    root.appendChild(item1);
            return  doc;
        }
    catch (Exception ex){
        ToastZK toast = new ToastZK(cont, ex.getMessage(), Toast.LENGTH_SHORT, res.getColor(R.color.primary) );
        toast.show();
return  null;
    }

};
//печаю история в файл
    private static FileOutputStream printHistory (Context cont , Resources res , Document doc) throws Exception{
        FileOutputStream fos = null;

   SimpleDateFormat sdf = new SimpleDateFormat("yy_MM_dd_HH_mm_ss");
    String file_name = "zakup" + sdf.format(new Date()) + ".xml";


    File dir = new File(Context_zakup.patch());
    if (!dir.exists()) dir.mkdirs();
    ;

    fos = new FileOutputStream(new File(Context_zakup.patch(), file_name));
    TelephonyManager tMgr = (TelephonyManager) cont.getSystemService(Context.TELEPHONY_SERVICE);
    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.transform(new DOMSource(doc), new StreamResult(fos));



return fos;

        }
    //кладу посточнные товары
    public static Element Zakup(Document doc){
        Element item2 = doc.createElement("zakup");

        userCursor =  Context_zakup.getDBHelper().getWritableDatabase().rawQuery(" select _id , id_home  , " +
                        " id_tovar  , counts  , counts_z  from zakup " +
                        "where id_home in (select _id from Home where selected = 1) "
                , null);

        userCursor.moveToFirst();
        while( userCursor.isAfterLast() ==false) {
            Element item10 = doc.createElement("zakup");
            item10.setAttribute("id", Integer.toString(userCursor.getInt(userCursor.getColumnIndex("_id"))));
            item10.setAttribute("id_home", Integer.toString(userCursor.getInt(userCursor.getColumnIndex("id_home"))));
            item10.setAttribute("id_tovar", Integer.toString(userCursor.getInt(userCursor.getColumnIndex("id_tovar"))));
            item10.setAttribute("counts", Integer.toString(userCursor.getInt(userCursor.getColumnIndex("counts"))));
            item10.setAttribute("counts_z", Integer.toString(userCursor.getInt(userCursor.getColumnIndex("counts_z"))));

            item2.appendChild(item10);
            userCursor.moveToNext();
        };

        userCursor.close();
        return item2;
    }

    //кладу временные товары
    public static Element Zakup_sing(Document doc){
        Element item3 = doc.createElement("zakup_sing");

        userCursor =  Context_zakup.getDBHelper().getWritableDatabase().rawQuery(" select _id , id_home  , " +
                        " id_tipe_tovar  , name  , counts  , counts_z  from zakup_sing " +
                        "where id_home in (select _id from Home where selected = 1) "
                , null);

        userCursor.moveToFirst();
        while( userCursor.isAfterLast() ==false) {
            Element item10 = doc.createElement("zakup");
            item10.setAttribute("id", Integer.toString(userCursor.getInt(0)));
            item10.setAttribute("id_home", Integer.toString(userCursor.getInt(1)));
            item10.setAttribute("id_tipe_tovar", Integer.toString(userCursor.getInt(2)));
            item10.setAttribute("name", userCursor.getString(3));
            item10.setAttribute("counts", Integer.toString(userCursor.getInt(4)));
            item10.setAttribute("counts_z", Integer.toString(userCursor.getInt(5)));

            item3.appendChild(item10);
            userCursor.moveToNext();
        };

        userCursor.close();
return item3;
    }

    //сохраняю справочник товаров
    public static Element Tovar(Document doc){

        Element item4 = doc.createElement("Tovar");

        userCursor =  Context_zakup.getDBHelper().getWritableDatabase().rawQuery(" select _id , name  , " +
                        " id_tipe  from Tovar  "
                , null);

        userCursor.moveToFirst();
        while( userCursor.isAfterLast() ==false) {
            Element item10 = doc.createElement("zakup");
            item10.setAttribute("id", Integer.toString(userCursor.getInt(0)));
            item10.setAttribute("name", userCursor.getString(1));
            item10.setAttribute("id_tipe", Integer.toString(userCursor.getInt(2)));

            item4.appendChild(item10);
            userCursor.moveToNext();
        };

        userCursor.close();
        return item4;
    }

    //сохраняю справочник домов
    public static Element Home(Document doc){

        Element item5 = doc.createElement("Home");

        userCursor =  Context_zakup.getDBHelper().getWritableDatabase().rawQuery(" select _id , name  , " +
                        " orders  from Home  "
                , null);

        userCursor.moveToFirst();
        while( userCursor.isAfterLast() ==false) {
            Element item10 = doc.createElement("zakup");
            item10.setAttribute("id", Integer.toString(userCursor.getInt(0)));
            item10.setAttribute("name", userCursor.getString(1));
            item10.setAttribute("orders", Integer.toString(userCursor.getInt(2)));

            item5.appendChild(item10);
            userCursor.moveToNext();
        };

        userCursor.close();
return item5;
    }

    //сохрчняю справочник типов товаров
    public static Element Tipe_tovar(Document doc){

        Element item6 = doc.createElement("Tipe_Tovar");

        userCursor =  Context_zakup.getDBHelper().getWritableDatabase().rawQuery(" select _id , name  , " +
                        " orders  from Tipe_Tovar  "
                , null);

        userCursor.moveToFirst();
        while( userCursor.isAfterLast() ==false) {
            Element item10 = doc.createElement("zakup");
            item10.setAttribute("id", Integer.toString(userCursor.getInt(0)));
            item10.setAttribute("name", userCursor.getString(1));
            item10.setAttribute("orders", Integer.toString(userCursor.getInt(2)));

            item6.appendChild(item10);
            userCursor.moveToNext();
        };

        userCursor.close();
return item6;
    }

//считываю из базы что закупать
    public static List<Table_tipe> Select(Context cont ,String sorders){
        List tovars = new ArrayList<Table_tipe>();
        //получаем данные из бд в виде курсора
        String Sql = "select * from  (select zakup_sing._id , zakup_sing.name , zakup_sing.orders ," +
                " Tipe_Tovar.name as tname , ifnull(Tipe_Tovar.orders , 0) as torders , sum(zakup_sing.counts) as counts" +
                ", zakup_sing.id_tipe_tovar , 0 as Simpel , sum(zakup_sing.counts_z) as counts_z from zakup_sing " +
                " left join   Tipe_Tovar on  Tipe_Tovar._id = zakup_sing.id_tipe_tovar " +
                "where  zakup_sing.id_home in (select _id from Home where selected = 1) " +
                " group by zakup_sing._id , zakup_sing.name , zakup_sing.orders , zakup_sing.id_tipe_tovar " +
                " , Tipe_Tovar.name , Tipe_Tovar.orders";
        Sql = Sql + " union all select Tovar._id , Tovar.name , Tovar.orders ," +
                " Tipe_Tovar.name as tname, ifnull(Tipe_Tovar.orders , 0)  as torders , sum(counts) , Tovar.id_tipe " +
                ", 1 as  Simpel , sum(zakup.counts_z)  " +
                "from Zakup , Tovar left join  " +
                " Tipe_Tovar on  Tipe_Tovar._id = Tovar.id_tipe " +
                "where zakup.id_tovar= Tovar._id and zakup.id_home in (select _id from Home where selected = 1) " +
                "group by Tovar._id , Tovar.name , Tovar.orders ,  Tovar.id_tipe" +
                " ,  Tipe_Tovar.name , Tipe_Tovar.orders) a where a.counts > 0";

        userCursor =  Context_zakup.getDBHelper().getWritableDatabase().rawQuery(Sql
                + sorders, null);

        userCursor.moveToFirst();
        while( userCursor.isAfterLast() ==false) {
            boolean bSimpel = false;
            if (userCursor.getInt(7) == 0 ) bSimpel = true;
            tovars.add(new ZakupSecond(userCursor.getString(1), userCursor.getInt(2)
                    ,1 ,userCursor.getInt(0) ,userCursor.getInt(5 )
                    , userCursor.getString(3)
                    , userCursor.getInt(4) ,bSimpel ,userCursor.getInt(8 ) ,cont ));
            userCursor.moveToNext();
        };

        userCursor.close();
        return tovars;
    }

}
