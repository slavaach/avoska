package ru.e2k.chechina.zakupka.dao;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import ru.e2k.chechina.zakupka.R;
 //настройки сообщений
public class ToastZK extends Toast {


public ToastZK (Context cont){
    super(cont);
 };


    public ToastZK (Context cont, CharSequence text,  int duration ,int color ) {
        super(cont);
        LinearLayout layout=new LinearLayout(cont);
        layout.setBackgroundResource(R.color.primary_dark);
        TextView view1 = new TextView(cont);
        view1.setBackgroundResource(R.color.primary_dark);
        view1.setTextColor(color);
        view1.setText(text);
        view1.setTextSize(18);

        layout.addView(view1);
        layout.setPadding(20, 20, 20, 20);
        //Toast.makeText(getContext(), "тут не удаляется",Toast.LENGTH_LONG);
        super.setDuration(duration);
        super.setView(layout);
    }
}
