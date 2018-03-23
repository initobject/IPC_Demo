package com.czy.aidl_client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.czy.aidl_server.IOnOperationCompletedListener;
import com.czy.aidl_server.IOperationManager;
import com.czy.aidl_server.Parameter;

/**
 * 作者：叶应是叶
 * 时间：2018/3/18 17:51
 * 描述：https://github.com/leavesC
 * 客户端
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText et_param1;

    private EditText et_param2;

    private EditText et_result;

    private IOperationManager iOperationManager;

    private IOnOperationCompletedListener completedListener = new IOnOperationCompletedListener.Stub() {
        @Override
        public void onOperationCompleted(final Parameter result) throws RemoteException {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    et_result.setText("运算结果： " + result.getParam());
                }
            });
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iOperationManager = IOperationManager.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iOperationManager = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        bindService();
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setClassName("com.czy.aidl_server", "com.czy.aidl_server.AIDLService");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void initView() {
        et_param1 = findViewById(R.id.et_param1);
        et_param2 = findViewById(R.id.et_param2);
        et_result = findViewById(R.id.et_result);
        Button btn_registerListener = findViewById(R.id.btn_registerListener);
        Button btn_unregisterListener = findViewById(R.id.btn_unregisterListener);
        Button btn_operation = findViewById(R.id.btn_operation);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_registerListener: {
                        if (iOperationManager != null) {
                            try {
                                iOperationManager.registerListener(completedListener);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }
                    case R.id.btn_unregisterListener: {
                        if (iOperationManager != null) {
                            try {
                                iOperationManager.unregisterListener(completedListener);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }
                    case R.id.btn_operation: {
                        if (TextUtils.isEmpty(et_param1.getText()) || TextUtils.isEmpty(et_param2.getText())) {
                            return;
                        }
                        final int param1 = Integer.valueOf(et_param1.getText().toString());
                        final int param2 = Integer.valueOf(et_param2.getText().toString());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Parameter parameter1 = new Parameter(param1);
                                Parameter parameter2 = new Parameter(param2);
                                if (iOperationManager != null) {
                                    try {
                                        iOperationManager.operation(parameter1, parameter2);
                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).start();
                        break;
                    }
                }
            }
        };
        btn_registerListener.setOnClickListener(clickListener);
        btn_unregisterListener.setOnClickListener(clickListener);
        btn_operation.setOnClickListener(clickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceConnection != null) {
            unbindService(serviceConnection);
        }
    }

}
