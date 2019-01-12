package ru.e2k.chechina.zakupka.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import ru.e2k.chechina.zakupka.Context_zakup;
import ru.e2k.chechina.zakupka.MainActivity;
import ru.e2k.chechina.zakupka.R;
import ru.e2k.chechina.zakupka.Tipe.Tovar;
import ru.e2k.chechina.zakupka.adapter.Zakup_adapter_simpel;
import ru.e2k.chechina.zakupka.dao.TableUpdateListener;
import ru.e2k.chechina.zakupka.dao.Table_tipe;
import ru.e2k.chechina.zakupka.dao.ToastZK;
import ru.e2k.chechina.zakupka.update.TovarUpdate;

import java.util.ArrayList;
import java.util.List;

//форма постоянных товаров
public class TovarActivity extends AppCompatActivity {

    private Toolbar my_toolbar;
    Cursor userCursor;
    private List<Table_tipe> tovars ;
    private ListView tipe_tovarList;
    private static int lastClickId = -1;
    Zakup_adapter_simpel userAdapter;
    TextView count_text_tip_tovar;
    String[] ordersView = { "по коду" , "по №","по названию"};
    private String orders;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tovar);

        // верхний тулбар
        my_toolbar= findViewById(R.id.toolbar);
        my_toolbar.setLogo(R.drawable.ic_launcher);
        my_toolbar.setTitle("");
        ((TextView)my_toolbar.findViewById(R.id.toolbar_subtitles)).setText("Всегда надо");
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.orders = " ";

        // инициация списка типов товаров
        count_text_tip_tovar = (TextView)findViewById(R.id.activity_tovar_Count_text_tip_tovar);
        tovars = new ArrayList<Table_tipe>();
        setOrders(" ");
        tipe_tovarList = (ListView) findViewById(R.id.activity_tovar_tip_tovar_list);
        final Typeface TypefaceBold = Typeface.defaultFromStyle(Typeface.BOLD);
        final Typeface TypefaceNormal = Typeface.defaultFromStyle(Typeface.NORMAL);




        //поле со списком для сортировки
        Spinner spinner = (Spinner) findViewById(R.id.activity_tovar_orders_spin);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ordersView);
        adapter.setDropDownViewResource(R.layout.multi_choice_list_spinner_dropddown_item);
        spinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = (String)parent.getItemAtPosition(position);
                switch (position) {
                    case 0:
                        setOrders(" Order by Tovar._id ");
                        break;
                    case 1:
                        setOrders(" Order by Tovar.orders ");
                        break;
                    case 2:
                        setOrders(" Order by Tovar.name ");
                        break;
                    default:
                        setOrders(" ");
                }
                Refresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }


    //Обновляю, скачиваю данные из базы
    private void Refresh () {
        tovars = TovarUpdate.Select(this ,getOrders() );

        userAdapter = new Zakup_adapter_simpel(this, R.layout.list_tovar,
                tovars);
        tipe_tovarList.setAdapter(userAdapter);

        count_text_tip_tovar.setText("? из " + Integer.toString( tovars.size()));

    }

    //Добавление  товаров
    public void click_addButton_tip_tovar(View view) {

        TovarUpdate.setTableInsertListener(new TableUpdateListener() {
            @Override
            public void onItemsUpdate() {
                Refresh();
            }
        }
        );
        TovarUpdate.Insert(this );

    }

    //удаление товаров
    public void click_deleteButton_tip_tovar(View view) {
        if(tipe_tovarList.getCheckedItemPosition() != -1) {
              Table_tipe trCarrent =tovars.get(tipe_tovarList.getCheckedItemPosition());
            TovarUpdate.setTableDeleteListener(new TableUpdateListener() {
                @Override
                public void onItemsUpdate() {
                    Refresh();
                }
            }
            );
            TovarUpdate.Delete(this , (Tovar) trCarrent);
        }
        else {
            ToastZK toast = new ToastZK(getApplicationContext(), "Выберите, что удалять.",Toast.LENGTH_LONG , getResources ().getColor(R.color.primary) );
            toast.show();};
    }

    //изменение товаров
    public void click_updateButton_tip_tovar(View view) {
        if(tipe_tovarList.getCheckedItemPosition() != -1) {

            Table_tipe trCarrent =tovars.get(tipe_tovarList.getCheckedItemPosition());
            TovarUpdate.setTableUpdateListener(new TableUpdateListener() {
               @Override
               public void onItemsUpdate() {
                    Refresh();
              }
         }
            );
            TovarUpdate.upDate(this , (Tovar) trCarrent);
         } else {
            ToastZK toast = new ToastZK(getApplicationContext(), "Выберите, что обновлять.",Toast.LENGTH_LONG , getResources ().getColor(R.color.primary) );
            toast.show();};
    }

    //возврат в основное меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //отражение сортировки
    public void click_ordersButton_tip_tovar(View view) {

        ((Spinner) findViewById(R.id.activity_tovar_orders_spin)).performClick ();
    }


    public String getOrders (){return this.orders;}

    public void setOrders (String s){
        this.orders = s;
    }


}
