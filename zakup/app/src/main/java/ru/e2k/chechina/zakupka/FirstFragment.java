package ru.e2k.chechina.zakupka;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.*;
import ru.e2k.chechina.zakupka.Tipe.Tipe_tovar;
import ru.e2k.chechina.zakupka.Tipe.ZakupFirst;
import ru.e2k.chechina.zakupka.adapter.Zakup_adapter_simpel;
import ru.e2k.chechina.zakupka.dao.TableUpdateListener;
import ru.e2k.chechina.zakupka.dao.Table_tipe;
import ru.e2k.chechina.zakupka.dao.ToastZK;
import ru.e2k.chechina.zakupka.update.ZakupFirstUpdate;


import java.util.ArrayList;
import java.util.List;

//фрагмент для заказа
public class FirstFragment extends Fragment {


    private List<Table_tipe> tovars ;
    private ListView tipe_tovarList;
    private static int lastClickId = -1;
    Zakup_adapter_simpel userAdapter;
    TextView count_text_tip_tovar;
    String[] ordersView = { "по коду" , "по №","по названию", "по типу" , "по коду типа"};
    private String orders;
    private View view;
   private List<Tipe_tovar> tipe_tovars ;
    List<String> tipe_tovars_st;
    private float fromPositionX;
    private float fromPositionY;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_zakup, container, false);


        this.orders = " ";


        // инициация списка типов товаров
        count_text_tip_tovar = (TextView)view.findViewById(R.id.fragment_zakup_Count_text_tip_tovar);
        tovars = new ArrayList<Table_tipe>();
        setOrders(" ");
        tipe_tovarList = (ListView) view.findViewById(R.id.fragment_zakup_tip_tovar_list);

        //заполнение списка типов товаров
        Context_zakup.UpdateTipe_tovars();
        tipe_tovars_st = Context_zakup.getTipe_tovars_st();
        tipe_tovars = Context_zakup.getTipe_tovars();


        //подсчет на какой строке
        tipe_tovarList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                ItemClickListener(position);
            }

        });

        //установка слушателя, что б и скролинг работал и смена фрагментов
        final  MainActivity Mv = (MainActivity) this.getActivity();
        tipe_tovarList.setOnTouchListener(new  View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        fromPositionX = event.getX();
                        fromPositionY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        float toPositionX = event.getX();
                        float toPositionY = event.getY();

                        if(Math.abs(fromPositionY-toPositionY) < Math.abs(fromPositionX-toPositionX)
                        && Math.abs(fromPositionX-toPositionX) > 100)
                           if(fromPositionX  > toPositionX) Mv.onTouchleft(); else Mv.onTouchreate();
                       break;
                    default:
                        break;
                }
                return false; //что б отработало и основное
            }
     });



        //установка кнопок

        ((ImageButton) view.findViewById(R.id.fragment_zakup_ordersButton_tip_tovar))
                .setOnClickListener((new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                  click_ordersButton_tip_tovar(v);
            }
        }));
        ((ImageButton) view.findViewById(R.id.fragment_zakup_deleteButton_tip_tovar))
                .setOnClickListener((new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        click_deleteButton_tip_tovar(v);
                    }
                }));
        ((ImageButton) view.findViewById(R.id.fragment_zakup_updateButton_tip_tovar))
                .setOnClickListener((new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        click_updateButton_tip_tovar(v);
                    }
                }));
        ((ImageButton) view.findViewById(R.id.fragment_zakup_addButton_tip_tovar))
                .setOnClickListener((new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        click_addButton_tip_tovar(v);
                    }
                }));


        //поле со списком для сортировки
        Spinner spinner = (Spinner) view.findViewById(R.id.fragment_zakup_orders_spin);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, ordersView);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = (String) parent.getItemAtPosition(position);
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
                    case 3:
                        setOrders(" Order by tname ");
                        break;
                    case 4:
                        setOrders(" Order by torders ");
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
        return view;
}

    //Обновляю, скачиваю данные из базы
    public void Refresh () {
        tovars = ZakupFirstUpdate.Select(getContext()  , getOrders());


        userAdapter = new Zakup_adapter_simpel(getContext(), R.layout.list_zakup,
                tovars);
        tipe_tovarList.setAdapter(userAdapter);

        count_text_tip_tovar.setText("? из " + Integer.toString( tovars.size()));

    }

    //добавление в заказ
    public void click_addButton_tip_tovar(View view) {

        ZakupFirstUpdate.setTableInsertListener(new TableUpdateListener() {
            @Override
            public void onItemsUpdate() {
                Refresh();
            }
        }
        );
        ZakupFirstUpdate.Insert(getContext() );

    }

    //удаление из заказа
    public void click_deleteButton_tip_tovar(View view) {
        if(tipe_tovarList.getCheckedItemPosition() != -1) {
            Table_tipe trCarrent = tovars.get(tipe_tovarList.getCheckedItemPosition());
            ZakupFirstUpdate.setTableDeleteListener(new TableUpdateListener() {
                                                   @Override
                                                   public void onItemsUpdate() {
                                                       Refresh();
                                                   }
                                               }
            );
            ZakupFirstUpdate.Delete(getContext() , (ZakupFirst) trCarrent);
        }
        else {
            ToastZK toast = new ToastZK(getContext(), "Выберите, что удалять.",Toast.LENGTH_LONG , getResources ().getColor(R.color.primary) );
            toast.show();};
    }

    //изменение заказа
    public void click_updateButton_tip_tovar(View view) {
        if(tipe_tovarList.getCheckedItemPosition() != -1) {
            Table_tipe trCarrent = tovars.get(tipe_tovarList.getCheckedItemPosition());
            ZakupFirstUpdate.setTableUpdateListener(new TableUpdateListener() {
                                                   @Override
                                                   public void onItemsUpdate() {
                                                       Refresh();
                                                   }
                                               }
            );
            ZakupFirstUpdate.upDate(getContext() , (ZakupFirst) trCarrent);
        } else {
            ToastZK toast = new ToastZK(getContext(), "Выберите, что обновлять.",Toast.LENGTH_LONG , getResources ().getColor(R.color.primary) );
            toast.show();};
    }




    //сортировка
    public void click_ordersButton_tip_tovar(View view) {

        ((Spinner) this.view.findViewById(R.id.fragment_zakup_orders_spin)).performClick ();
    }


    public String getOrders (){return this.orders;}

    public void setOrders (String s){
        this.orders = s;
    }


    public void ItemClickListener(int position)
    {
        tipe_tovarList.setItemChecked(position , true);

        count_text_tip_tovar.setText(Integer.toString( position +1 ) +
                " из " + Integer.toString( tovars.size()));

    }
}

