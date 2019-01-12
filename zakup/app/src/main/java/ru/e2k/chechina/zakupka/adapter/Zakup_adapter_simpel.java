package ru.e2k.chechina.zakupka.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ru.e2k.chechina.zakupka.dao.Table_tipe;
import java.util.List;


//основной адаптер, работает по R_id_view. Отражает в указанные в нем поля
public class Zakup_adapter_simpel extends ArrayAdapter<Table_tipe> {

    private LayoutInflater inflater;
    private int layout;
    private List<Table_tipe> tovars;

    public Zakup_adapter_simpel(Context context, int resource, List<Table_tipe> tovars) {
        super(context, resource, tovars);
        this.tovars = tovars;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        final Table_tipe tt = tovars.get(position);
        final int pos =position;



        for(final int ttab:tt.R_id_view()){
            if(view.findViewById(ttab)instanceof TextView) ((TextView) view.findViewById(ttab)).setText(tt.getTextView(ttab));
            if(view.findViewById(ttab) instanceof CheckBox){
                final CheckBox cView_sm =(CheckBox)view.findViewById(ttab);
                cView_sm.setChecked(tt.check());
                cView_sm.setText(tt.getTextView(ttab));
                cView_sm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                           tt.setCheck(((CheckBox)v).isChecked());
                           cView_sm.setText(tt.getTextView(ttab));
                        }  catch(Exception ex) { System.out.println(ex.getMessage());}

                    }
                });
            }
        };


        return view;
    }

   }


