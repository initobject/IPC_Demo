package com.czy.binder_pool_server;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * 作者：叶应是叶
 * 时间：2018/3/23 21:55
 * 描述：https://github.com/leavesC
 */
public class BinderPoolService extends Service {

    private class BinderPoolImpl extends IBinderPool.Stub {

        @Override
        public IBinder queryBinder(int binderId) {
            switch (binderId) {
                case 100: {
                    return new IOperationImpl();
                }
                case 200: {
                    return new IComputeImpl();
                }
            }
            return null;
        }

    }

    private Binder binderPool;

    public BinderPoolService() {
        binderPool = new BinderPoolImpl();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binderPool;
    }

}
