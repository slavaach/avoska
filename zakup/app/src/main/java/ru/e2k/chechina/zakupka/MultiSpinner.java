package ru.e2k.chechina.zakupka;

import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import android.support.v7.widget.AppCompatSpinner;
import ru.e2k.chechina.zakupka.Tipe.Home_selected;
import ru.e2k.chechina.zakupka.adapter.Home_selected_adapter;

import java.util.ArrayList;
import java.util.List;

//класс для отражения списка домой в меню (множественный и одиночный выбор)
public class MultiSpinner extends AppCompatSpinner {

    private List<Home_selected> entries;//список домов
    private boolean[] selected;//выбранные дома
    private MultiSpinnerListener listener;
    Home_selected_adapter userAdapter;
    private ListView entries_List;
    private boolean bsingleLine;//выбранное состояние (мульти выбор или одиночный)
    static final public boolean MULTILINE = false;
    static final public boolean SINGLELINE = true;
    private  Home_selected singleHome; //выбранный дом


    public MultiSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiSpinner);
        Context_zakup.UpdateHome();//считываю установки
        entries = Context_zakup.getHome() ;
        singleHome = Context_zakup.getSingleHome();


        userAdapter = new Home_selected_adapter(getContext(), R.layout.multi_choice_list_home,
                entries);
        if (entries.size() > 0) {
            selected = new boolean[( entries.size())]; // инициализация
        }

        StringBuffer spinnerBuffer = new StringBuffer();//формирую строку для отражения
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).isSelect()) {
                selected[i] = true;
                spinnerBuffer.append(entries.get(i).getName());
                spinnerBuffer.append(", ");
            } else selected[i]= false;
        }

        if (spinnerBuffer.length() > 2) {
            spinnerBuffer.setLength(spinnerBuffer.length() - 2);
        }

        // отражаем полученный текст
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.multi_simpel_list_spinner_home,
                new String[] { spinnerBuffer.toString() });
        setAdapter(adapter);
        //запускаю анимацию
        setSelected(true);
        this.bsingleLine = MULTILINE;
        a.recycle();
    }

   //выбрали несколько
    private DialogInterface.OnClickListener mOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //собираем строку для отражения
            StringBuffer spinnerBuffer = new StringBuffer();
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i).isSelect()) {
                    spinnerBuffer.append(entries.get(i).getName());
                    spinnerBuffer.append(", ");
                }
            }
            if (spinnerBuffer.length() > 2) {
                spinnerBuffer.setLength(spinnerBuffer.length() - 2);
            }

            // отражаем текст
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    R.layout.multi_simpel_list_spinner_home,
                    new String[] { spinnerBuffer.toString() });
            setAdapter(adapter);


            if (listener != null) {
                listener.onItemsSelected();
            }


            setSelected(true);
            dialog.dismiss();

        }
    };

    //показываем меню выбора
    @Override
    public boolean performClick() {

        View aV = LayoutInflater.from(getContext())
            .inflate(R.layout.promt_multi_choice_home, null);
        AlertDialog.Builder aB = new android.support.v7.app.AlertDialog.Builder(getContext());
                aB.setView(aV);
        if(!isBsingleLine()) aB.setPositiveButton("Выбрать", mOnClickListener);
         else ;
        final AlertDialog aD = aB.create();
        entries_List = (ListView) aV.findViewById(R.id.promt_multi_choice_home_home_list);
        entries_List.setAdapter(userAdapter);

        entries_List.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if(!isBsingleLine()){
                    //если выбор из нескольких
                selected[position] = !selected[position];
                entries.get(position).setSelect(!entries.get(position).isSelect());
                if(selected[position]) {
                    ((TextView)entries_List.getChildAt(position - entries_List.getFirstVisiblePosition())
                            .findViewById(R.id.multi_choice_list_home_idView))
                            .setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    entries_List.getChildAt(position- entries_List.getFirstVisiblePosition())
                            .setBackgroundColor(ContextCompat.getColor(getContext(),R.color.primary_light));

                }
                else{((TextView)entries_List.getChildAt(position - entries_List.getFirstVisiblePosition())
                        .findViewById(R.id.multi_choice_list_home_idView))
                        .setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
               entries_List.getChildAt(position - entries_List.getFirstVisiblePosition())
                       .setBackgroundColor(ContextCompat.getColor(getContext(),R.color.White));};
            } else { //простой spinner
                    singleHome =  entries.get(position);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                            R.layout.multi_simpel_list_spinner_home,
                            new String[] {  entries.get(position).getName() });
                    setAdapter(adapter);
                    Context_zakup.getDBHelper().getReadableDatabase().execSQL("Update Home_qw set  selected =  " + Integer.toString(singleHome.getId()));
                    Context_zakup.UpdateHome();
                    aD.dismiss();
                    if (listener != null) {
                        listener.onItemsSelected();
                    }
            }


            }
            });
        //настройки отражения, что б было похоже на стандартный
        WindowManager.LayoutParams wmlp = aD.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP | Gravity.LEFT;//установила размер окна
        aD.show();
        wmlp.x = 150;
        wmlp.y = 10;
        int hei= entries.size()*74+133;
        if(bsingleLine) hei= entries.size()*94+5;
         if (hei > (getContext().getResources().getDisplayMetrics().heightPixels - 50))
            hei = (getContext().getResources().getDisplayMetrics().heightPixels - 50);
        aD.getWindow().setLayout(270, hei);
        aD.getWindow().setDimAmount(0.12f);

          return true;
    }

    //мульти или одиночный выбор
    public boolean isBsingleLine() {
        return bsingleLine;
    }

    public void setBsingleLine(boolean bsingleLine) {
        if(bsingleLine){ //просто линия


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    R.layout.multi_simpel_list_spinner_home,
                    new String[] {  singleHome.getName() });
            setAdapter(adapter);
        }
        else { //мультивыбор
            //собираем строку
            StringBuffer spinnerBuffer = new StringBuffer();
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i).isSelect()) {
                    selected[i] = true;
                    spinnerBuffer.append(entries.get(i).getName());
                    spinnerBuffer.append(", ");
                } else selected[i]= false;
            }
              if (spinnerBuffer.length() > 2) {
                spinnerBuffer.setLength(spinnerBuffer.length() - 2);
            }

            // отражаем строку
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    R.layout.multi_simpel_list_spinner_home,
                    new String[] { spinnerBuffer.toString() });
            setAdapter(adapter);
            setSelected(true);
             }
        this.bsingleLine = bsingleLine;
    }

    //слушатель выбора
    public void setMultiSpinnerListener(MultiSpinnerListener listener) {
        this.listener = listener;
    }

    public interface MultiSpinnerListener {
        public void onItemsSelected();
    }
}