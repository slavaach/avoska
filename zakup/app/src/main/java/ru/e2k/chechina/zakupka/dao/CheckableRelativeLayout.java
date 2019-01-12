package ru.e2k.chechina.zakupka.dao;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import ru.e2k.chechina.zakupka.R;

//Layout, которые отражает выбранную строку. Если в tag стоит normal - просто выделяет жирным, если еще и smoll, то
// увеличивает. Кроме того меняет цвет строки
public class CheckableRelativeLayout extends RelativeLayout implements Checkable {
    private boolean isChecked = false;
    private Context c;
    private AttributeSet att;

    public CheckableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.c = context;
        this.att = attrs;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
        changeColor(isChecked);
    }

    public void toggle() {
        this.isChecked = !this.isChecked;
        changeColor(this.isChecked);
    }

    private void changeColor(boolean isChecked){
        if (isChecked) {
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primary));
            for (int i = 0; i < this.getChildCount() ; i++) {
                if (this.getChildAt(i).getTag() != null && ((String)this.getChildAt(i).getTag()).contains("normal") )
                    ((TextView) this.getChildAt(i)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                if (this.getChildAt(i).getTag() != null && ((String)this.getChildAt(i).getTag()).contains("smoll") )
                     ((TextView) this.getChildAt(i)).setTextSize(18);

            }


        } else {
            setBackgroundColor(ContextCompat.getColor(getContext(),R.color.none));
            for (int i = 0; i < this.getChildCount() ; i++) {
                if (this.getChildAt(i).getTag() != null && ((String)this.getChildAt(i).getTag()).contains("normal") )
                    ((TextView) this.getChildAt(i)).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                if (this.getChildAt(i).getTag() != null && ((String)this.getChildAt(i).getTag()).contains("smoll") )
                    ((TextView) this.getChildAt(i)).setTextSize(11);


            }

        }
    }


}
