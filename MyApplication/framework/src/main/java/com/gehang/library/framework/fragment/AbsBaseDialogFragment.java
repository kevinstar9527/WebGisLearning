package com.gehang.library.framework.fragment;


import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.gehang.library.basis.Log;
import com.gehang.library.framework.OnDialogDestroyListener;
import com.gehang.library.framework.R;


public abstract class AbsBaseDialogFragment extends DialogFragment {
	private static final String TAG = "AbsBaseDialogFragment";

	public FragmentManager mFragmentManager;


	protected OnDialogDestroyListener mOnDialogDestroyListener;
	public boolean mIsViewDestroyed = true;

	private boolean mIsOnTop = false;
	private Handler mHandlerWindow = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(getFragmentLayoutResId(), container, false);



		mFragmentManager = getFragmentManager();
		mIsViewDestroyed = false;

		if(isNoTitle())
			getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		if(isSetBackgroundColor())
			getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getBackgroundColor()));

		if(isSetBackgroundResource()){
			int resid = getBackgroundResourceID();
			if(resid > 0){
				getDialog().getWindow().setBackgroundDrawableResource(getBackgroundResourceID());
			}

		}



		initViews(view);

		mHandlerWindow.post(new Runnable(){
			public void run(){
				if (getDialog() == null) {
					return;
				}
 				if(isCanceledOnTouchOutside())
					getDialog().setCanceledOnTouchOutside(true);
				else
					getDialog().setCanceledOnTouchOutside(false);

	            Window dialogWindow = getDialog().getWindow();

	            // Make the dialog possible to be outside touch
	            if( isNotTouchModal())
		            dialogWindow.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
		                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
				if(isNotDimBehide())
	            	dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	
	            getView().invalidate();
			}
		});


		return view;
	}
	public void initViews(View root)
	{

	}
    public void onAttach(Activity activity) {
        super.onAttach(activity);

		mIsViewDestroyed = true;

		//Log.d(TAG,String.format("%s create",getFragmentName()));
	}
    public void onDetach() {
        super.onDetach();
		//Log.d(TAG,String.format("%s destroy",getFragmentName()));
    }
    public void onResume() {
        super.onResume();

		Log.d(TAG,String.format("%s onResume",getFragmentName()));
		WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
		layoutParams.width = getDialogWidth();
		layoutParams.height = getDialogHeight();
		getDialog().getWindow().setAttributes(layoutParams);
		
		mIsOnTop = true;
    }
	public void onPause() {
		super.onPause();
		Log.d(TAG, String.format("%s onPause", getFragmentName()));
		mIsOnTop = false;
	}
	public void onDestroyView(){
		super.onDestroyView();
		mIsViewDestroyed = true;
	}
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, String.format("%s onDestroy", getFragmentName()));
		if(mOnDialogDestroyListener != null)
			mOnDialogDestroyListener.onDestroy();
    }
	public void setOnDialogDestroyListener(OnDialogDestroyListener listener){
		mOnDialogDestroyListener = listener;
	}

	// default dehavior
	public int getDialogWidth(){
		return getResources().getDimensionPixelSize(R.dimen.default_dialog_width);
	}
	public int getDialogHeight(){
		return  WindowManager.LayoutParams.WRAP_CONTENT;
	}
	// default dehavior
	// �Ի����Ƿ���԰�back����ʧ
	public boolean isCanceledOnTouchOutside(){
		return true;
	}
	// default dehavior
	// �Ի�����ı����Ƿ���
	public boolean isNotDimBehide(){
		return true;
	}
	// default dehavior
	// �Ի�����İ�ť�Ƿ���Ե��
	public boolean isNotTouchModal(){
		return false;
	}
    public void show(FragmentManager manager) {
		try {
			super.show(manager, getFragmentName());
		}catch (Exception e){
			Log.e(TAG,e.toString());
		}
    }
	protected boolean isNoTitle(){
		return true;
	}
	protected boolean isSetBackgroundColor(){
		return true;
	}
	protected boolean isSetBackgroundResource(){
		return false;
	}
	protected int getBackgroundColor(){
		return 0;
	}

	protected int getBackgroundResourceID(){
		return -1;
	}
	abstract public String getFragmentName();
	abstract public int getFragmentLayoutResId();

	public boolean isViewDestroyed(){
		return mIsViewDestroyed;
	}

}

