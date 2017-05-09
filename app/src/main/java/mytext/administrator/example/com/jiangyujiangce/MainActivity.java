package mytext.administrator.example.com.jiangyujiangce;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.model.LoginRunnable;
import net.model.PublicOneListInterface;

public class MainActivity extends AppCompatActivity implements PublicOneListInterface{
    private  EditText ED_Username;
    private  EditText ED_Paqssword;
    private MyProgressDialog ProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        CommonMethod.setStatuColor(MainActivity.this,R.color.white);
        init();
    }
    private void init(){
        ED_Username = (EditText)findViewById(R.id.ED_Username);
        ED_Username.setOnFocusChangeListener(new ED_FocusChangeListener());
        ED_Paqssword = (EditText)findViewById(R.id.ED_Paqssword);
        ED_Paqssword.setOnFocusChangeListener(new ED_FocusChangeListener());
        //登录
        ImageView login = (ImageView)findViewById(R.id.login);
        login.setOnClickListener(new MainActivityListener());
        //注册
        TextView zhuCe = (TextView)findViewById(R.id.zhuCe);
        zhuCe.setOnClickListener(new MainActivityListener());
    }

    @Override
    public void onGetDataSuccess(String succmessage) {
        cancelDialog();
        Log.e("warn",succmessage);
    }

    @Override
    public void onGetDataError(String errmessage) {
        cancelDialog();
        Log.e("warn",errmessage);
    }

    @Override
    public void onEmptyData(String Emptymessage) {
        cancelDialog();
    }

    private class MainActivityListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.login:
                                    if(ED_Username.getText().toString().equals("")||ED_Username.getText().toString()==null){
                                        Toast.makeText(MainActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if(ED_Paqssword.getText().toString().equals("")||ED_Paqssword.getText().toString()==null){
                                        Toast.makeText(MainActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    ProgressDialog = new MyProgressDialog(MainActivity.this,false,"登陆中...");
                                    new LoginRunnable(ED_Username.getText().toString(),ED_Paqssword.getText().toString()).getShopsData(MainActivity.this);
                                    break;
                case R.id.zhuCe:
                    Intent Register = new Intent(MainActivity.this,ZhuCe.class);
                    startActivity(Register);
                    break;
            }
        }
    }
    private class ED_FocusChangeListener implements View.OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()){
                case R.id.ED_Username:
                                            if(hasFocus){
                                                ED_Username.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext_focusbiankuang));
                                            }else{
                                                ED_Username.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext_biankuang));
                                            }
                                            break;
                case R.id.ED_Paqssword:

                                            if(hasFocus){
                                                ED_Paqssword.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext_focusbiankuang));
                                            }else{
                                                ED_Paqssword.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext_biankuang));
                                            }

                    break;
            }
        }
    }
    private void cancelDialog(){
        if(ProgressDialog!=null){
            ProgressDialog.dismiss();
            ProgressDialog=null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelDialog();
    }
}
