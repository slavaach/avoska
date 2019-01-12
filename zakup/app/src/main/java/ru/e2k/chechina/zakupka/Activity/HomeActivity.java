
package ru.e2k.chechina.zakupka.Activity;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.Toolbar;
        import android.view.MenuItem;
        import android.content.Intent;
        import android.view.View;
        import android.widget.*;
        import ru.e2k.chechina.zakupka.*;
        import ru.e2k.chechina.zakupka.Tipe.Tipe_tovar;
        import ru.e2k.chechina.zakupka.adapter.Zakup_adapter_simpel;
        import ru.e2k.chechina.zakupka.dao.TableUpdateListener;
        import ru.e2k.chechina.zakupka.dao.Table_tipe;
        import ru.e2k.chechina.zakupka.dao.ToastZK;
        import ru.e2k.chechina.zakupka.update.HomeUpdate;


        import java.util.*;

// форма домов
public class HomeActivity extends AppCompatActivity {
    private Toolbar my_toolbar;
    private List<Table_tipe> tipe_tovars ;
    private ListView tipe_tovarList;
    Zakup_adapter_simpel userAdapter;
    TextView count_text_tip_tovar;
    String[] ordersView = { "по коду" , "по №","по названию"};
    private String orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipe_tovar);

        // верхний тулбар
        my_toolbar= findViewById(R.id.toolbar);
        my_toolbar.setLogo(R.drawable.ic_launcher);
        my_toolbar.setTitle("");
        ((TextView)my_toolbar.findViewById(R.id.toolbar_subtitles)).setText("Дома");
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.orders = " ";

        // инициация списка типов товаров
        count_text_tip_tovar = (TextView)findViewById(R.id.activity_tipe_tovar_Count_text_tip_tovar);
        tipe_tovars = new ArrayList<Table_tipe>();
        setOrders(" ");


        tipe_tovarList = (ListView) findViewById(R.id.activity_tipe_tovar_tip_tovar_list);


        //поле со списком для сортировки
        Spinner spinner = (Spinner) findViewById(R.id.activity_tipe_tovar_orders_spin);
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
                        setOrders(" Order by _id ");
                        break;
                    case 1:
                        setOrders(" Order by orders ");
                        break;
                    case 2:
                        setOrders(" Order by name ");
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
        tipe_tovars = HomeUpdate.Select(getOrders());

        userAdapter = new Zakup_adapter_simpel(this, R.layout.list_tip_tovar,
                tipe_tovars);
        tipe_tovarList.setAdapter(userAdapter);

        count_text_tip_tovar.setText("? из " + Integer.toString( tipe_tovars.size()));

    }

    //Добавление  дома
    public void click_addButton_tip_tovar(View view) {

        HomeUpdate.setTableInsertListener(new TableUpdateListener() {
                                                    @Override
                                                    public void onItemsUpdate() {
                                                        Refresh();
                                                    }
                                                }
        );
        HomeUpdate.Insert(this );

    }
    //удаление дома
    public void click_deleteButton_tip_tovar(View view) {
        if(tipe_tovarList.getCheckedItemPosition() != -1) {

            Table_tipe tr = tipe_tovars.get(tipe_tovarList.getCheckedItemPosition());
            HomeUpdate.setTableDeleteListener(new TableUpdateListener() {
                                                        @Override
                                                        public void onItemsUpdate() {
                                                            Refresh();
                                                        }
                                                    }
            );
            HomeUpdate.Delete(this , (Tipe_tovar) tr);
        }
        else {
            ToastZK toast = new ToastZK(getApplicationContext(), "Выберите, что удалять.",Toast.LENGTH_LONG , getResources ().getColor(R.color.primary) );
            toast.show();};
    }

    //обновление дома
    public void click_updateButton_tip_tovar(View view) {
        if(tipe_tovarList.getCheckedItemPosition() != -1) {
            Table_tipe tr = tipe_tovars.get(tipe_tovarList.getCheckedItemPosition());

            HomeUpdate.setTableUpdateListener(new TableUpdateListener() {
                @Override
                public void onItemsUpdate() {
                    Refresh();
                }
            }
            );
            HomeUpdate.upDate(this , (Tipe_tovar) tr);
        } else {
            ToastZK toast = new ToastZK(getApplicationContext(), "Выберите, что обновлять.",Toast.LENGTH_LONG , getResources ().getColor(R.color.primary) );
            toast.show();};
    }


    @Override
    //возврат в основное меню
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

    //вызов сортировки
    public void click_ordersButton_tip_tovar(View view) {

        ((Spinner) findViewById(R.id.activity_tipe_tovar_orders_spin)).performClick ();
    }


    public String getOrders (){return this.orders;}

    public void setOrders (String s){
        this.orders = s;
    }
}
