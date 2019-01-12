package ru.e2k.chechina.zakupka.dao;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ru.e2k.chechina.zakupka.R;
import ru.e2k.chechina.zakupka.adapter.Zakup_adapter_simpel;

//Layout для TreeList, которые разварачивает выбранную строку.
public class TreeListRelativeLayout extends RelativeLayout implements Checkable {
    private boolean isChecked = false;
    private boolean isClose = true;
    private Context c;
    private AttributeSet att;
    private TreeListSetSelectedListener listener;

    public TreeListRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.c = context;
        this.att = attrs;
     }



    public void click_openButton_tip_tovar(View v) {

        this.isClose = !this.isClose;
        changeOpen(this.isClose);

    }
private  void changeOpen(boolean b){


}

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean close) {

    }




    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
        changeColor(isChecked);
    }

    @Override
    public void toggle() {
        this.isChecked = !this.isChecked;
        changeColor(this.isChecked);
    }



    private void changeColor(boolean isChecked){
        Zakup_adapter_simpel userAdapter = (Zakup_adapter_simpel)((ListView)this.findViewById(R.id.list_zakup_treelist_ListCh)).getAdapter();

        if (isChecked) {
            this.isClose = false;
            if(this.listener != null) this.listener.onItemsSelected(false);
            ((ImageButton) this.findViewById(R.id.list_zakup_treelist_idView)).setImageResource(R.drawable.baseline_keyboard_arrow_down_black_24);
            ((ListView) this.findViewById(R.id.list_zakup_treelist_ListCh)).setVisibility(View.VISIBLE);

            int totalHeight = 0;
            for (int i = 0; i < userAdapter.getCount(); i++) {
                View mView = userAdapter.getView(i, null, (ListView) this.findViewById(R.id.list_zakup_treelist_ListCh));

                mView.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),

                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                totalHeight += mView.getMeasuredHeight();

            }
            ViewGroup.LayoutParams params = this.findViewById(R.id.list_zakup_treelist_ListCh).getLayoutParams();
            params.height = totalHeight
                    + (((ListView) this.findViewById(R.id.list_zakup_treelist_ListCh)).getDividerHeight() * (userAdapter.getCount() - 1));
            this.findViewById(R.id.list_zakup_treelist_ListCh).setLayoutParams(params);


            if(userAdapter.getCount() == 1) setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primary));

            ((TextView)this.findViewById(R.id.list_zakup_treelist_ordersView)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            ((TextView)this.findViewById(R.id.list_zakup_treelist_nameTipeView)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            ((TextView)this.findViewById(R.id.list_zakup_treelist_nameView)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));



        } else {
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.none));

            ((ImageButton) this.findViewById(R.id.list_zakup_treelist_idView)).setImageResource(R.drawable.baseline_keyboard_arrow_right_black_24);
            ((ListView) this.findViewById(R.id.list_zakup_treelist_ListCh)).setVisibility(View.INVISIBLE);
            ViewGroup.LayoutParams params = this.findViewById(R.id.list_zakup_treelist_ListCh).getLayoutParams();
            params.height = 0;
            this.findViewById(R.id.list_zakup_treelist_ListCh).setLayoutParams(params);
            this.findViewById(R.id.list_zakup_treelist_ListCh).requestLayout();


            ((TextView)this.findViewById(R.id.list_zakup_treelist_ordersView)).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            ((TextView)this.findViewById(R.id.list_zakup_treelist_nameTipeView)).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            ((TextView)this.findViewById(R.id.list_zakup_treelist_nameView)).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));


        }
    }

    public TreeListSetSelectedListener getListener() {
        return listener;
    }

    public void setListener(TreeListSetSelectedListener listener) {
        this.listener = listener;
    }

    public interface TreeListSetSelectedListener {
        public void onItemsSelected(boolean close);
    }
}
