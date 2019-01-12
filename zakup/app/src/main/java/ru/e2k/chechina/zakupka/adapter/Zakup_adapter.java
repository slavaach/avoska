package ru.e2k.chechina.zakupka.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import ru.e2k.chechina.zakupka.Context_zakup;
import ru.e2k.chechina.zakupka.R;
import ru.e2k.chechina.zakupka.Tipe.UpdateZakup;
import ru.e2k.chechina.zakupka.Tipe.ZakupSecond;
import ru.e2k.chechina.zakupka.dao.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


//адаптер для закупки работает по R_id_view. Отражает в указанные в нем поля и добавляет уровень в дерево
public class Zakup_adapter extends ArrayAdapter<Table_tipe> {

    private LayoutInflater inflater;
    private int layout;
    private List<Table_tipe> tovars;
     private List<Set> saveState;

    public Zakup_adapter(Context context, int resource, List<Table_tipe> tovars , int bClouse ) {
        super(context, resource, tovars);
        this.tovars = tovars;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.saveState= new ArrayList<>();

        for (Table_tipe t:tovars) {
            this.saveState.add(new Set(t.getId() , true));

        }


    }


    public View getView(int position, View convertView, ViewGroup parent) {
         final View view=inflater.inflate(this.layout, parent, false);
        final Zakup_adapter_simpel userAdapter = new   Zakup_adapter_simpel(this.getContext(), R.layout.list_update_zakup_layout,
                ((ZakupSecond)tovars.get(position)).getUpdateZakups());

       final Table_tipe tt = tovars.get(position);

       final Set set  = this.saveState.get(position);
      final int pos =position;
       //для трилиста

        final TreeListRelativeLayout trrl = (TreeListRelativeLayout)view.findViewById(R.id.list_zakup_treelist_RelativeLayout1);
        trrl.setListener(new TreeListRelativeLayout.TreeListSetSelectedListener() {
            @Override
            public void onItemsSelected(boolean close) {
                set.close = close;
            }
        });
         final ListView lv = (ListView)view.findViewById(R.id.list_zakup_treelist_ListCh);


        lv.setAdapter(userAdapter);




            ((ImageButton) view.findViewById(R.id.list_zakup_treelist_idView)).setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOpen( trrl , v  );
                    set.close = !set.close;
                   }
            }));


        for(final int ttab:tt.R_id_view()){
            if(view.findViewById(ttab)instanceof TextView) ((TextView) view.findViewById(ttab)).setText(tt.getTextView(ttab));
            if(view.findViewById(ttab) instanceof CheckBox){
                final CheckBox cView_sm =(CheckBox)view.findViewById(ttab);
                cView_sm.setChecked(tt.check());
                cView_sm.setText(tt.getTextView(ttab));
                ((ZakupSecond)tt).setListener(new ZakupSecond.ZakupSekondListener() {
                    @Override
                    public void onItemsSelected(boolean check) {
                        cView_sm.setChecked(check);
                    }         });


                cView_sm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            tt.setCheck(((CheckBox)v).isChecked());
                            cView_sm.setText(tt.getTextView(ttab));
                            userAdapter.notifyDataSetChanged();
                        }  catch(Exception ex) { System.out.println(ex.getMessage());}

                    }
                });
            }
        };
        view.setBackgroundColor(getContext().getResources().getColor( R.color.none));

        return view;

    }

   public void setCheckPosition(int position){
      // ((ListView)this.vw.get(position).findViewById(R.id.list_zakup_treelist_ListCh)).setItemChecked(SetInt.geti(),true);

   }


private  void setOpen(TreeListRelativeLayout trrl ,View v  ) {
    trrl.click_openButton_tip_tovar(v);

}




}
