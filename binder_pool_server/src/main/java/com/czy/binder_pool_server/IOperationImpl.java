package com.czy.binder_pool_server;

import android.os.RemoteException;
import android.util.Log;

/**
 * 作者：叶应是叶
 * 时间：2018/3/23 21:17
 * 描述：https://github.com/leavesC
 */
public class IOperationImpl extends IOperation.Stub {

    @Override
    public int add(int parameter1, int parameter2) throws RemoteException {
        Log.e("IOperationImpl", "add 方法被调用");
        return parameter1 + parameter2;
    }

}
