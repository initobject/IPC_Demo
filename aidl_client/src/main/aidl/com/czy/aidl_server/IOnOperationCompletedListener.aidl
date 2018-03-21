package com.czy.aidl_server;

import com.czy.aidl_server.Parameter;

interface IOnOperationCompletedListener {

    void onOperationCompleted(in Parameter result);

}
