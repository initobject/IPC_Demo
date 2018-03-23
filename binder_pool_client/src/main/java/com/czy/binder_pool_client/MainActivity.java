package com.czy.binder_pool_client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.czy.binder_pool_server.IBinderPool;
import com.czy.binder_pool_server.IOperation;

/**
 * 作者：叶应是叶
 * 时间：2018/3/23 22:32
 * 描述：https://github.com/leavesC
 */
public class MainActivity extends AppCompatActivity {

    private IOperation operation;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                IBinderPool binderPool = IBinderPool.Stub.asInterface(service);
                //本客户端的唯一标识是 100
                //获取真实的 Binder 对象
                operation = IOperation.Stub.asInterface(binderPool.queryBinder(100));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            operation = null;
            bindService();
        }
    };

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService();
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operation != null) {
                    try {
                        Log.e(TAG, "4+2 加法：" + operation.add(4, 2));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (operation != null) {
            unbindService(serviceConnection);
        }
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setClassName("com.czy.binder_pool_server", "com.czy.binder_pool_server.BinderPoolService");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

}
