package ru.e2k.chechina.zakupka;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.content.Intent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.*;
import org.w3c.dom.Document;
import ru.e2k.chechina.zakupka.Activity.HomeActivity;
import ru.e2k.chechina.zakupka.Activity.Tipe_tovarActivity;
import ru.e2k.chechina.zakupka.Activity.TovarActivity;

import android.view.LayoutInflater;
import android.view.View;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.io.File;
import java.io.FileOutputStream;

//основное окно
public class MainActivity extends AppCompatActivity implements View.OnTouchListener   {
    private Toolbar my_toolbar;
    private boolean setZak;//закупка или заказ
    private Menu menu;
    private ObjectAnimator  animScrollToTop;
    private ViewFlipper flipper = null;//флиппер с фрагментами закука/заказ
    private float fromPosition;
    MenuItem mState;
    FirstFragment firstfragment;//заказ
    SecondFragment secondfragment;//закупка


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context_zakup.setConext(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.setZak = false; //установили. что делаем заказ

        // меню
        my_toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(my_toolbar);
        my_toolbar.setLogo(R.drawable.ic_launcher);
        my_toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(),R.color.primary)));
        mState = my_toolbar.getMenu().findItem(R.id.action_zakup);

        //прокрутка названий домой в меню
        animScrollToTop = ObjectAnimator.ofInt(my_toolbar.findViewById(R.id.ScrollView01), "scrollX",
                new int[] { 470 });
        animScrollToTop// w  w w  .j  a  v  a 2  s . c  o  m
                .setInterpolator(new AccelerateInterpolator());
        animScrollToTop.setDuration(10000);
        animScrollToTop.setRepeatCount(ObjectAnimator.INFINITE);
        animScrollToTop.start();

        //ставлю, что б работала смена фрагментов по экрану
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        mainLayout.setOnTouchListener(this);


//фрагменты
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layouts[] = new int[]{ R.layout.flipper_first, R.layout.flipper_second};
        for (int layout : layouts)
            flipper.addView(inflater.inflate(layout, null));
        firstfragment = (FirstFragment) getSupportFragmentManager().findFragmentById(R.id.flipper_first_listFirstFragment);
        secondfragment = (SecondFragment) getSupportFragmentManager().findFragmentById(R.id.flipper_second_listSecondFragment);
        ((MultiSpinner)my_toolbar.findViewById(R.id.multispinner)).setMultiSpinnerListener(new MultiSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected() {
                firstfragment.Refresh();
                secondfragment.Refresh();
            }
        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
       return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        this.menu = menu;
        //устанавливаю тулюар, на заказ
        ((MultiSpinner)my_toolbar.findViewById(R.id.multispinner)).setBsingleLine(MultiSpinner.SINGLELINE);
        mState = my_toolbar.getMenu().findItem(R.id.action_zakup);

         return true;
    }

    @Override
    //описание меню
    public boolean onOptionsItemSelected(MenuItem item) {
         int id = item.getItemId();


        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                //тут выход из приложения
                return true;
            case R.id.action_zakup:
                //тут смена заказ на закупку
                if (setZak) {
                    //заказ
                    flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.go_prev_in));
                    flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.go_prev_out));
                   ((MultiSpinner)my_toolbar.findViewById(R.id.multispinner)).setBsingleLine(MultiSpinner.SINGLELINE);
                    firstfragment.Refresh();
                    setZak = false;
                    item.setIcon(getResources().getDrawable(R.mipmap.shoping_set));
                    flipper.showPrevious();
                }
                else
                {
                   //закупка
                    flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.go_next_in));
                    flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.go_next_out));
                    secondfragment.Refresh();
                    ((MultiSpinner)my_toolbar.findViewById(R.id.multispinner)).setBsingleLine(MultiSpinner.MULTILINE);
                    ((MultiSpinner)my_toolbar.findViewById(R.id.multispinner)).setSelected(true);
                    animScrollToTop.start();
                     setZak = true;
                    item.setIcon(getResources().getDrawable(R.mipmap.shoping_ok));
                    flipper.showNext();
                }
               return true;

            case R.id.tipe_tovar:
                //тип товаров
                Intent intent_tipe_tovar = new Intent(this, Tipe_tovarActivity.class);
                startActivity(intent_tipe_tovar);
                this.finish();
                return true;
            case R.id.tovar:
                //всегда надо
                Intent intent_tovar = new Intent(this, TovarActivity.class);
                startActivity(intent_tovar);
                this.finish();
                return true;
            case R.id.home_sl:
                //дома
                Intent intent_home = new Intent(this, HomeActivity.class);
                startActivity(intent_home);
                this.finish();
                return true;
            case R.id.delete_history:
                //удаляем историю


                for (File fil: (new File(Context_zakup.patch())).listFiles()) fil.delete();

                Observable.just(new File(Context_zakup.patch()))
                        .flatMap(new Func1<File, Observable<File>>() {
                                     @Override
                                     public Observable<File> call(File dir) {
                                       return  Observable.from(dir.listFiles()) ;
                                     }
                                 })
                        .filter(new Func1<File, Boolean>() {
                                    @Override
                                    public Boolean call(File file) {

                                        return file.isFile();
                                    }
                                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(new Action1<File>() {
                                      @Override
                                      public void call(File file) {
                                          file.delete();
                                      }
                                  });




                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    //поворот налево
    public  boolean onTouchleft(){
        flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.go_next_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.go_next_out));
        if(!setZak) {
            flipper.showNext();
            ((MultiSpinner)my_toolbar.findViewById(R.id.multispinner)).setBsingleLine(MultiSpinner.MULTILINE);
            animScrollToTop.start();
            secondfragment.Refresh();
            setZak = true;
            mState.setIcon(getResources().getDrawable(R.mipmap.shoping_ok));}
        else {flipper.showPrevious();
            ((MultiSpinner)my_toolbar.findViewById(R.id.multispinner)).setBsingleLine(MultiSpinner.SINGLELINE);
            setZak = false;
            firstfragment.Refresh();
            mState.setIcon(getResources().getDrawable(R.mipmap.shoping_set));}
        return true;
    }

    //поворот направо
    public  boolean onTouchreate(){
        flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.go_prev_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.go_prev_out));
        if(!setZak) {
            flipper.showNext();
            ((MultiSpinner)my_toolbar.findViewById(R.id.multispinner)).setBsingleLine(MultiSpinner.MULTILINE);
            animScrollToTop.start();
            setZak = true;
            mState.setIcon(getResources().getDrawable(R.mipmap.shoping_ok));}
        else {flipper.showPrevious();
            ((MultiSpinner)my_toolbar.findViewById(R.id.multispinner)).setBsingleLine(MultiSpinner.SINGLELINE);
            setZak = false;
            mState.setIcon(getResources().getDrawable(R.mipmap.shoping_set));}

        return true;
    }


    //отслеживание в целом по форме возможно можно убрать)
    public boolean onTouch(View view, MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                fromPosition = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float toPosition = event.getX();
                if (fromPosition - 100 > toPosition)
                {
                    onTouchleft();
                }
                else if (fromPosition + 100 < toPosition)
                {
                    onTouchreate();

                }
            default:
                break;
        }
        return true;
    }


}
