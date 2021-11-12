package com.android.homecloudclient.Context;

import android.app.Application;

public class ApplicationContext extends Application {

	
	int userId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
