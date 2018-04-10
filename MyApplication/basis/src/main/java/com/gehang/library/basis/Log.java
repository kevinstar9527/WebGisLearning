package com.gehang.library.basis;

/**
 * Created by Administrator on 2016-01-11.
 */
public class Log {
	/**
	 * Priority constant for the println method; use Log.v.
	 */
	public static final int VERBOSE = 2;

	/**
	 * Priority constant for the println method; use Log.d.
	 */
	public static final int DEBUG = 3;

	/**
	 * Priority constant for the println method; use Log.i.
	 */
	public static final int INFO = 4;

	/**
	 * Priority constant for the println method; use Log.w.
	 */
	public static final int WARN = 5;

	/**
	 * Priority constant for the println method; use Log.e.
	 */
	public static final int ERROR = 6;

	/**
	 * Priority constant for the println method.
	 */
	public static final int ASSERT = 7;

	protected static int mLogLevel = VERBOSE;
	protected static boolean mEnable = true;

	private Log() {
	}
	public void setLogLevel(int level){
		mLogLevel = level;
	}
	public void setEnable(boolean enable){
		mEnable = enable;
	}
	public static int v(String tag, String msg) {
		if(mEnable && mLogLevel <= VERBOSE)
			return android.util.Log.v(tag,msg);
		else
			return 0;
	}

	public static int v(String tag, String msg, Throwable tr) {
		if(mEnable && mLogLevel <= VERBOSE)
			return android.util.Log.v(tag,msg,tr);
		else
			return 0;
	}

	public static int d(String tag, String msg) {
		// some phone cannot show DEBUG level,only show INFO level,so use INFO level instead
		if(mEnable && mLogLevel <= DEBUG)
			return android.util.Log.i(tag, msg);
		else
			return 0;
	}

	public static int d(String tag, String msg, Throwable tr) {
		// some phone cannot show DEBUG level,only show INFO level,so use INFO level instead
		if(mEnable && mLogLevel <= DEBUG)
			return android.util.Log.i(tag,msg,tr);
		else
			return 0;
	}

	public static int i(String tag, String msg) {
		if(mEnable && mLogLevel <= INFO)
			return android.util.Log.i(tag,msg);
		else
			return 0;
	}

	public static int i(String tag, String msg, Throwable tr) {
		if(mEnable && mLogLevel <= INFO)
			return android.util.Log.i(tag,msg,tr);
		else
			return 0;
	}
	public static boolean isLoggable(String tag, int level){
		return android.util.Log.isLoggable(tag,level);
	}
	public static int w(String tag, String msg) {
		if(mEnable && mLogLevel <= WARN)
			return android.util.Log.w(tag, msg);
		else
			return 0;
	}

	public static int w(String tag, String msg, Throwable tr) {
		if(mEnable && mLogLevel <= WARN)
			return android.util.Log.w(tag,msg,tr);
		else
			return 0;
	}

	public static int w(String tag, Throwable tr) {
		if(mEnable && mLogLevel <= WARN)
			return android.util.Log.w(tag,tr);
		else
			return 0;
	}

	public static int e(String tag, String msg) {
		if(mEnable && mLogLevel <= ERROR)
			return android.util.Log.e(tag, msg);
		else
			return 0;
	}

	public static int e(String tag, String msg, Throwable tr) {
		if(mEnable && mLogLevel <= ERROR)
			return android.util.Log.e(tag,msg,tr);
		else
			return 0;
	}

	public static String getStackTraceString(Throwable tr) {
		return android.util.Log.getStackTraceString(tr);
	}

	public static int println(int priority, String tag, String msg) {
		return android.util.Log.println( priority, tag, msg);
	}


}
