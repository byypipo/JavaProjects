package com.util;

public abstract class CustomThread extends Thread {

	@Override
	public void run() {
		try {
			startThread();
		} catch (Exception e) {
		}
	}

	public abstract void startThread() throws Exception;

	public void stopThread() {
		interrupt();
	}

	public boolean isStoped() {
		return Thread.currentThread().isInterrupted();
	}
}
