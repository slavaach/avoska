package ru.e2k.chechina.zakupka.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ru.e2k.chechina.zakupka.R;
import ru.e2k.chechina.zakupka.Tipe.Home_selected;

import java.util.List;

//адаптор для отражения списка домов в меню
public class Home_selected_adapter  extends ArrayAdapter<Home_selected> {

    private LayoutInflater inflater;
    private int layout;
    private List<Home_selected> home_selected;

    public Home_selected_adapter(Context context, int resource, List<Home_selected> home_selected) {
        super(context, resource, home_selected);
        this.home_selected = home_selected;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView nameView = (TextView) view.findViewById(R.id.multi_choice_list_home_idView);

        Home_selected tt = home_selected.get(position);

        nameView.setText(tt.getName());
        if(tt.isSelect()) {
            nameView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            view.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.primary_light));
        }
                else {
                    nameView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            view.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.White));
                };

        return view;
    }


}

