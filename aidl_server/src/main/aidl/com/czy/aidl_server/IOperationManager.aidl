package com.czy.aidl_server;

import com.czy.aidl_server.Parameter;
import com.czy.aidl_server.IOnOperationCompletedListener;

interface IOperationManager {

   void operation(in Parameter parameter1 , in Parameter parameter2);

   void registerListener(in IOnOperationCompletedListener listener);

   void unregisterListener(in IOnOperationCompletedListener listener);

}
