package mytext.administrator.example.com.jiangyujiangce;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ant.liao.GifView;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISLayerInfo;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Envelope;

/**
 * Created by Administrator on 2017/5/10.
 */

public class JiangYuMap extends AppCompatActivity {
    private MapView mMapView;
    private GifView gifLoadGis;
    private int mLoadIcoChiCun = 140;
    private String mCurrentBengZhanDaiMa = "";
    private boolean mIsIdentfy = false;
    private RelativeLayout rlLoadView;////gifview的父布局
    private ArcGISDynamicMapServiceLayer layer;
    private ArcGISLayerInfo layerInforZJ = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiangyumap_layout);
        CommonMethod.setStatuColor(JiangYuMap.this,R.color.white);
        init();
    }
    private void init(){
        Button map_Back = (Button)findViewById(R.id.map_Back);
        map_Back.setOnClickListener(new JiangYuMapListener());
        Button map_History = (Button)findViewById(R.id.map_History);
        map_History.setOnClickListener(new JiangYuMapListener());
        mMapView = (MapView)findViewById(R.id.mapView);
        ImageView map_select = (ImageView) findViewById(R.id.map_select);
        map_select.setOnClickListener(new JiangYuMapListener());
        ImageView map_TongJi = (ImageView) findViewById(R.id.map_TongJi);
        map_TongJi.setOnClickListener(new JiangYuMapListener());
        gifLoadGis =(GifView)findViewById(R.id.gifLoadGis);
        getMap();
    }
    private void getMap(){
        Envelope el = new Envelope(41511137.09 - 10000,4617857.567 - 10000,41511137.09 + 20000,4617857.567 + 10000);//这里有4个坐标点，看似是一个矩形的4个顶点。
        mMapView.setExtent(el);

        layer = new ArcGISDynamicMapServiceLayer(Path.get_MapUrl());
        layer.refresh();//刷新地图
        //layer.getLayers()[1].setVisible(false);
        //添加地图

        mMapView.addLayer(layer);

        //mMapView.zoomout();
        //mMapView.setOnSingleTapListener(mOnSingleTapListener);//单击地图上的泵站
        mMapView.setOnStatusChangedListener(new mMapViewChangListener());

        //Button btn_clean = (Button) findViewById(R.id.btn_clean);//清空雨量图层
        rlLoadView = (RelativeLayout) findViewById(R.id.layoutLoadGISView);//gifview的父布局
        gifLoadGis = (GifView) findViewById(R.id.gifLoadGis); ////加载的动画
        WindowManager wm =getWindowManager();
        //gifview控件刚开始加载的背景
        gifLoadGis.setGifImage(R.drawable.load);//加载的动画
        //getLayoutParams()方法 和 setLayoutParams()方法 重新设置控件布局
        //设置gifview的margin值
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) gifLoadGis.getLayoutParams();
        lp.setMargins((wm.getDefaultDisplay().getWidth() - mLoadIcoChiCun) / 2, (wm.getDefaultDisplay().getHeight() - mLoadIcoChiCun) / 2 - 200, (wm.getDefaultDisplay().getWidth() - mLoadIcoChiCun) / 2, (wm.getDefaultDisplay().getHeight() - mLoadIcoChiCun) / 2);
        gifLoadGis.setLayoutParams(lp);
        rlLoadView.setVisibility(View.VISIBLE);
        gifLoadGis.showAnimation();//加载的动画


    }

    private  class  JiangYuMapListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.map_Back:TuiChu();break;
                case R.id.map_History:
                                        Intent intent = new Intent(JiangYuMap.this,LiShi_ChaXun.class);
                                        startActivity(intent);
                                        break;
                case R.id.map_select:break;
                case R.id.map_TongJi:
                                        Intent intent1 = new Intent(JiangYuMap.this,QuanQuJiangYu24Hour.class);
                                        startActivity(intent1);
                                        break;
            }
        }
    }

    private class mMapViewChangListener implements OnStatusChangedListener {//OnStatusChangedListener接口用于监听MapView或Layer（图层）状态变化的监听器

        @Override
        public void onStatusChanged(Object o, STATUS status) {
            if (status == STATUS.INITIALIZED) {
            } else if (status == STATUS.LAYER_LOADED) {
                if (layer != null) {
                    if (layer.getLayers() != null) {

                        layerInforZJ = layer.getLayers()[1];
                        Log.e("warn","图层总长度"+layer.getLayers().length);
                        for (int i = 0; i < layer.getLayers().length; i++) {
                            ArcGISLayerInfo layerInfor = layer.getLayers()[i];
                            if (layerInfor.getName().equals(Path.get_BengZhanZhuJi())) {//泵站名称隐藏
                                layerInfor.setVisible(false);
                            }
                            Log.e("GISActivity地图服务加载", "图层名称：" + layerInfor.getName() + "");
                        }
                    }
                }
                rlLoadView.setVisibility(View.INVISIBLE);
                //gifLoadGis.showCover();
                //Toast.makeText(GISActivity.this, "地图加载成功", Toast.LENGTH_SHORT).show();
            } else if (status == STATUS.INITIALIZATION_FAILED) {
                Toast.makeText(getApplicationContext(), "地图加载失败", Toast.LENGTH_SHORT).show();
                rlLoadView.setVisibility(View.INVISIBLE);
                gifLoadGis.showCover();
            } else if (status == STATUS.LAYER_LOADING_FAILED) {
                Toast.makeText(getApplicationContext(), "图层加载失败", Toast.LENGTH_SHORT).show();
                rlLoadView.setVisibility(View.INVISIBLE);
                gifLoadGis.showCover();
            }
        }
    }
    private void TuiChu(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否退出应用");
        //builder.setTitle("是否退出应用");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            TuiChu();
        }
        return super.onKeyDown(keyCode, event);
    }
}
