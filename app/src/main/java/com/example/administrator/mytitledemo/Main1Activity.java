package com.example.administrator.mytitledemo;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.R.attr.actionBarSize;

public class Main1Activity extends AppCompatActivity {

    private View view;
    private RecyclerView rv;
    private MyAdapter adapter;
    private List<String> list=new ArrayList<>();
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;

    private LinearLayoutManager manager=new LinearLayoutManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
         this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//此FLAG可使状态栏透明，且当前视图在绘制时，从屏幕顶端开始即top = 0开始绘制，这也是实现沉浸效果的基础
         this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//可不加
       }
        setContentView(R.layout.activity_main1);//在此之前添加以上FLAG

        view=findViewById(R.id.titlebar_view);
        rv= (RecyclerView) findViewById(R.id.rv);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        collapsingToolbarLayout.setTitle("标题");

        initData();
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();//titlebar为添加的顶部的（标题栏上方）占位控件
        layoutParams.height = getStatueBarHeight();
        Log.e("hight",getStatueBarHeight()+"");
        view.setLayoutParams(layoutParams);
        view.setBackgroundColor(Color.TRANSPARENT);//此处也可设置自定义的颜色，设置为透明则会直接看到底层的图片

        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);



    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            list.add("数据"+i);
        }

        adapter=new MyAdapter(list,this);


        //拿取CollapsingToolbarLayout的子View并根据版本设置其fitsSystemWindows的属性
        for (int k = 0; k < collapsingToolbarLayout.getChildCount(); k++) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                collapsingToolbarLayout.getChildAt(k).setFitsSystemWindows(true);
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                collapsingToolbarLayout.getChildAt(k).setFitsSystemWindows(false);
            } else {//不支持沉浸式状态栏的版本，要把使View向下偏移的占位控件的高度设置为0
                //设置充当TitleBar使用的控件里面的占位空间的高度为0，
            }

        }
    }

    private int getStatueBarHeight() {//拿取状态栏的高度
               int identifier = getResources().getIdentifier("status_bar_height", "dimen", "android");
               if (identifier > 0) {
                       return (int) getResources().getDimension(identifier);
                  }
               return 0;
          }




          //adapter
           class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyVieHolder>{
              private List<String> list=new ArrayList<>();
              private Context context;

              public MyAdapter(List<String> list, Context context) {
                  this.list = list;
                  this.context = context;
              }

              @Override
              public MyVieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                  View view= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
                  return new MyVieHolder(view);
              }

              @Override
              public void onBindViewHolder(MyVieHolder holder, int position) {
                  holder.textView.setText(list.get(position));
              }

              @Override
              public int getItemCount() {
                  return list.size();
              }

              class MyVieHolder extends RecyclerView.ViewHolder{
                  private TextView textView;

                  public MyVieHolder(View itemView) {
                      super(itemView);
                      textView=itemView.findViewById(R.id.tv);
                  }
              }
          }

}
