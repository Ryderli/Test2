package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Caipu_step extends Activity {
    StepAdapter adapter;
    HttpClient client;
    ArrayList<StepInfo> stepList;
    ListView lv2;
    TextView name_step;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caipu_step);
        stepList=new ArrayList<StepInfo>();
        lv2=(ListView) findViewById(R.id.lv2);
        name_step=(TextView) findViewById(R.id.cai_name);

        Bundle bundle=getIntent().getExtras();
        Bundle bundle2=getIntent().getExtras();
        String name= bundle2.getString("name");
        stepList= (ArrayList<StepInfo>) bundle.getSerializable("stepInfo");
        adapter=new Caipu_step.StepAdapter(getApplicationContext(),stepList);
        lv2.setAdapter(adapter);
        name_step.setText(name);

    }


    public class StepAdapter extends BaseAdapter{
        ArrayList<StepInfo> stepList;
        LayoutInflater inflater;
        public StepAdapter(Context context,ArrayList<StepInfo> stepList) {
            inflater=LayoutInflater.from(context);
            this.stepList=stepList;
        }

        @Override
        public int getCount() {
            return stepList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view=convertView;
            final ViewHolder holder;
            if(view==null){
                view=inflater.inflate(R.layout.step_list,null);
                holder=new ViewHolder();
                holder.step=(TextView) view.findViewById(R.id.step_title);
                holder.img=(ImageView) view.findViewById(R.id.step_img);
                view.setTag(holder);
            }else{
                holder=(ViewHolder) view.getTag();
            }

            holder.step.setText(stepList.get(position).getStep_title());
            new Thread()
            {
                @Override
                public void run() {
                    super.run();
                    Message msg = new Message();
                    msg.what = 2;
                    msg.obj=getHttpBitmap(stepList.get(position).getImageView());
                    System.out.println(stepList.get(position).getImageView());
                    holder.handler.sendMessage(msg);
                }
            }.start();
            return view;
        }
        class ViewHolder{
            TextView step;
            ImageView img;
            private Handler handler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what){
                        case 2:
                            Bitmap bitmap=(Bitmap) msg.obj;
                            img.setImageBitmap(bitmap);
                            break;
                    }
                }
            };
        }
    }
    public static Bitmap getHttpBitmap(String urlpath) {

        Bitmap bitmap = null;

        try {

            //生成一个URL对象

            URL url = new URL(urlpath);

            //打开连接

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

//			conn.setConnectTimeout(6*1000);

//			conn.setDoInput(true);

            conn.connect();

            //得到数据流

            InputStream inputstream = conn.getInputStream();


            bitmap = BitmapFactory.decodeStream(inputstream);

            //关闭输入流

            inputstream.close();

            //关闭连接

            conn.disconnect();



        } catch (Exception e) {

            e.printStackTrace();
        }

        return bitmap;

    }

}
