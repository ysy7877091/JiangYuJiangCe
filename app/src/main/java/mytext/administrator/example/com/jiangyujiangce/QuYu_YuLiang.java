package mytext.administrator.example.com.jiangyujiangce;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import javabeen.cn.JiangYuShuJu;

import static android.os.Build.ID;

/**
 * Created by 王聿鹏 on 2017/5/10.
 * <p>
 * 描述 ：
 */

public class QuYu_YuLiang extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_quYu_name;
    private ImageView iv_fanhui_icon1;
    private String startTime;
    private String endTime;
    private HorizontalBarChart barChart;
    private MyProgressDialog progressDialog=null;
    private List<JiangYuShuJu> list =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.quanqu_yuliang);
        init();


    }
    private void init() {
        barChart=(HorizontalBarChart)findViewById(R.id.YLchart);
        iv_fanhui_icon1 = (ImageView) findViewById(R.id.iv_fanhui_icon1);
        iv_fanhui_icon1.setOnClickListener(this);
        tv_quYu_name = (TextView) findViewById(R.id.tv_quYu_Name);
        String quYu_Name = getIntent().getStringExtra("Name");
        tv_quYu_name.setText(quYu_Name);
        if(quYu_Name.equals("全区")){
            progressDialog = new MyProgressDialog(QuYu_YuLiang.this,false,"正在加载中...");
            new Thread(Get_CheckAllRainFallHistory_List).start();

        }

        startTime = getIntent().getStringExtra("StartTime");
        endTime = getIntent().getStringExtra("EndTime");


    }
    private void YLRequest(){

        progressDialog = new MyProgressDialog(QuYu_YuLiang.this,false,"正在加载中...");
        new Thread(Get_CheckAllRainFallHistory_List).start();

    }
    Runnable Get_CheckAllRainFallHistory_List = new Runnable() {
        @Override
        public void run() {

            try {
                // 命名空间
                String nameSpace = "http://tempuri.org/";
                // 调用的方法名称
                String methodName = "Get_CheckAllRainFallHistory_List";
                // EndPoint
                String endPoint = "http://beidoujieshou.sytxmap.com:5963/GPSService.asmx";
                // SOAP Action
                String soapAction = "http://tempuri.org/Get_CheckAllRainFallHistory_List";
                // 指定WebService的命名空间和调用的方法名
                SoapObject rpc = new SoapObject(nameSpace, methodName);

                rpc.addProperty("startTime",startTime);
                Log.e("warn",startTime);
                rpc.addProperty("endTime",endTime);
                Log.e("warn",endTime);
                // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(rpc);

                HttpTransportSE ht = new HttpTransportSE(endPoint,20000);
                ht.debug = true;
                Log.e("warn", "4444");
                try {
                    // 调用WebService
                    ht.call(soapAction, envelope);
                } catch (Exception e) {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    handlerGet_CheckAllRainFallHistory_List.sendMessage(msg);
                }

                SoapObject object;
                // 开始调用远程方法
                object = (SoapObject) envelope.getResponse();
                // 得到服务器传回的数据 返回的数据时集合 每一个count是一个及集合的对象
                int count1 = object.getPropertyCount();
                Log.e("warn",String.valueOf(count1));
                if (count1 > 0) {
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < count1; i++) {
                        Log.e("warn", "11111");
                       //SoapObject soapProvince = (SoapObject) object.getProperty(i);
                        SoapObject soapProvince = (SoapObject) envelope.bodyIn;
//                        sb.append(soapProvince.getProperty("TIME").toString() + ",");
//                        Log.e("warn",soapProvince.getProperty("TIME").toString());
//                        sb.append(soapProvince.getProperty("YMD").toString() + ",");
//                        Log.e("warn",soapProvince.getProperty("YMD").toString());
//                        sb.append(soapProvince.getProperty("HH").toString() + ",");
//                        Log.e("warn",soapProvince.getProperty("HH").toString());
                        sb.append(soapProvince.getProperty("ValueX").toString() + ",");
                        Log.e("warn",soapProvince.getProperty("ValueX").toString());
                        if (i == count1 - 1) {
                            sb.append(soapProvince.getProperty("DevName").toString());
                        } else {
                            sb.append(soapProvince.getProperty("DevName").toString() + "|");
                        }
                    }
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = sb.toString();
                    handlerGet_CheckAllRainFallHistory_List.sendMessage(msg);
                }
            } catch (Exception e) {
                Message msg = Message.obtain();
                msg.what = 0;
                handlerGet_CheckAllRainFallHistory_List.sendMessage(msg);
            }
        }
    };
    Handler handlerGet_CheckAllRainFallHistory_List = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int i = msg.what;
            if(i==0){
                progressDialog.dismiss();
                Toast.makeText(QuYu_YuLiang.this,"网络或服务器异常",Toast.LENGTH_SHORT).show();
            }else if(i==1){

                String str =(String)msg.obj;
                Log.e("warn",str);


            }


        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_fanhui_icon1:finish();break;




        }

    }
}
