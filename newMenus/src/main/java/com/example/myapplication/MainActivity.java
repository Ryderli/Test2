package com.example.myapplication;

import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
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


import java.io.EOFException;
import java.io.IOException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    HttpClient client;
    ArrayList<BasicNameValuePair> param;
    String MunuURL="http://apis.juhe.cn/cook/query.php?";
    HttpParams params;
    ArrayList<BasicNameValuePair> paramList;
    EditText editText;
    ListView lv;
    ArrayList<MenuInfo> menuList;
    ArrayList<StepInfo> stepList;
    MunuAdapter adapter;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    String result=msg.obj.toString();
                    addMunu(result);
                    adapter=new MunuAdapter(getApplicationContext(),menuList);
                    lv.setAdapter(adapter);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv=(ListView) findViewById(R.id.lv);
        editText=(EditText) findViewById(R.id.edtext);
        client=new DefaultHttpClient();
        menuList=new ArrayList<MenuInfo>();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,Caipu_step.class);
                Bundle bundle=new Bundle();
                Bundle bundle2=new Bundle();
                bundle2.putString("name",menuList.get(position).getName());
                bundle.putSerializable("stepInfo",menuList.get(position).getStepInfo());
                intent.putExtras(bundle2);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
    public void showClick(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ParamCreate();
                String requestURL=MunuURL;
                String result=null;
                for(int i=0;i<param.size();i++){
                    requestURL=requestURL+param.get(i).getName()+"="+param.get(i).getValue()+"&";
                }
                HttpGet request=new HttpGet(requestURL);
                try{
                    HttpResponse httpResponse=client.execute(request);
                    if(httpResponse.getStatusLine().getStatusCode()==200){
                        result= EntityUtils.toString(httpResponse.getEntity());
                    }else if(httpResponse.getStatusLine().getStatusCode()==404){
                        result="连接失败！页面未找到";
                    }else{
                        result="连接失败";
                    }
                    Message m=new Message();
                    m.what=1;
                    m.obj=result;
                    handler.sendMessage(m);
                }catch (ClientProtocolException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            }).start();

    }
    public void ParamCreate(){
        param=new ArrayList<BasicNameValuePair>();
        BasicNameValuePair paramValue1=new BasicNameValuePair("key","749647ca1537446f4de901ec4f516cbb");
        BasicNameValuePair paramValue2=new BasicNameValuePair("dtype","json");
        BasicNameValuePair paramValue3=new BasicNameValuePair("menu",editText.getText().toString());
        param.add(paramValue1);
        param.add(paramValue2);
        param.add(paramValue3);
    }
    private void addMunu(String result){
        try{
            JSONObject jsonMinfo=new JSONObject(result);
            JSONObject resultJson=jsonMinfo.getJSONObject("result");
            JSONArray MunuArray=resultJson.getJSONArray("data");
            menuList=new ArrayList<MenuInfo>();

            for(int i=0;i<MunuArray.length();i++){
                JSONObject tree=MunuArray.getJSONObject(i);
                String name=tree.getString("title");
                String imtro=tree.getString("imtro");
                String ingredients=tree.getString("ingredients");
                String burden=tree.getString("burden");
                JSONArray album=tree.getJSONArray("albums");
                String s=album.getString(0);
                JSONArray MenuArray2=tree.getJSONArray("steps");
                stepList=new ArrayList<StepInfo>();
                for(int j=0;j<MenuArray2.length();j++){
                    JSONObject tree2=MenuArray2.getJSONObject(j);
                    String step=tree2.getString("step");
                    String ph=tree2.getString("img");
                    StepInfo stepInfo=new StepInfo(step,ph);
                    stepList.add(stepInfo);
                }
                MenuInfo menuInfo=new MenuInfo(s,name,imtro,ingredients,burden,stepList);
                menuList.add(menuInfo);

            }

        }catch (JSONException e){
            e.printStackTrace();
        }

    }
    public class MunuAdapter extends BaseAdapter{
        private Context context;
        private LayoutInflater layoutInflater;
        private ArrayList<MenuInfo> menuList=null;
        public MunuAdapter(Context context,ArrayList<MenuInfo> menuList) {
            this.context=context;
            layoutInflater=LayoutInflater.from(context);
            this.menuList=menuList;
        }

        @Override
        public int getCount() {
            return menuList.size();
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
                view=layoutInflater.inflate(R.layout.caipu_list,null);
                holder=new ViewHolder();
                holder.image=(ImageView) view.findViewById(R.id.image_photo);
                holder.name=(TextView) view.findViewById(R.id.title);
                holder.imtro=(TextView) view.findViewById(R.id.title2);
                holder.ingredients=(TextView) view.findViewById(R.id.title3);
                holder.burden=(TextView) view.findViewById(R.id.title4);
                view.setTag(holder);
            }else{
                holder=(ViewHolder) view.getTag();
            }
            new Thread()
            {
                @Override
                public void run() {
                    super.run();
                    Message msg = new Message();
                    msg.what = 2;
                    msg.obj=getHttpBitmap(menuList.get(position).getImage());
                    holder.handler.sendMessage(msg);

                }
            }.start();
            holder.name.setText(menuList.get(position).getName());
            holder.imtro.setText(menuList.get(position).getImtro());
            holder.ingredients.setText(menuList.get(position).getIngredients());
            holder.burden.setText(menuList.get(position).getBurden());
            return view;
        }
        class ViewHolder{
            ImageView image;
            TextView name;
            TextView imtro;
            TextView ingredients;
            TextView burden;

            private Handler handler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what){
                        case 2:
                            Bitmap bitmap=(Bitmap) msg.obj;
                            image.setImageBitmap(bitmap);
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
