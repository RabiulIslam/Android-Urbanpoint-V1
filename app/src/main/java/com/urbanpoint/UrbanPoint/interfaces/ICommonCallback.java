package com.urbanpoint.UrbanPoint.interfaces;


import android.os.Bundle;

import com.urbanpoint.UrbanPoint.async.CommonAsyncTaskResult;


public interface ICommonCallback {
	public void onTaskDone(Bundle result, CommonAsyncTaskResult taskResult);
}
