package com.gehang.library.framework.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gehang.library.basis.Log;
import com.gehang.library.framework.SupportFragmentManageBase;


public abstract class AbsBaseSupportFragment<MANAGER extends SupportFragmentManageBase,FRAGMENT extends AbsBaseSupportFragment> extends Fragment {
	private static final String TAG = "AbsBaseSupportFragment";

	public FragmentManager mFragmentManager;

	public MANAGER mSupportFragmentManage;


	public boolean mIsPaused;

	public boolean isPaused() {
		return mIsPaused;
	}

//	public void setIsPaused(boolean mIsPaused) {
//		this.mIsPaused = mIsPaused;
//	}

	public boolean mIsViewDestroyed;
	public boolean isViewDestroyed(){
		return mIsViewDestroyed;
	}

	private boolean mIsShowed = true;

	private Handler mHandler = new Handler(Looper.getMainLooper());

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG,String.format("%s onCreateView",getFragmentName()));

		View view = inflater.inflate(getFragmentLayoutResId(), container, false);


		mFragmentManager = getFragmentManager();
		mIsPaused = true;
		mIsViewDestroyed = false;

		initViews(view);

		return view;
	}

	public void initViews(View root)
	{

	}

    public void onAttach(Activity activity) {
        super.onAttach(activity);
		
		try {
			mSupportFragmentManage = (MANAGER)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement SupportFragmentManage");
		}
		mIsViewDestroyed = true;
		Log.d(TAG,String.format("%s onAttach",getFragmentName()));
	}

    public void onDetach() {
        super.onDetach();
		Log.d(TAG,String.format("%s onDetach",getFragmentName()));
    }

	public void onResumeOrVisible(){

	}

    public void onResume() {
		super.onResume();
		mIsPaused = false;
		Log.d(TAG, String.format("%s onResume", getFragmentName()));
		if( ! mIsUnderViewPager)
			onResumeOrVisible();
    }
	public void onPause() {
		super.onPause();
		Log.d(TAG, String.format("%s onPause", getFragmentName()));
		mIsPaused = true;
	}
	public void onDestroy(){
		super.onDestroy();
		Log.d(TAG,String.format("%s onDestroy",getFragmentName()));
	}
	public void onDestroyView(){
		super.onDestroyView();
		mIsViewDestroyed = true;
	}
    public void onHiddenChanged(boolean hidden) {
		if(hidden)
		{
			//Log.d(TAG,String.format("%s hide",getFragmentName()));
			onPause();
		}
		else
		{
			//Log.d(TAG,String.format("%s show",getFragmentName()));
			onResume();
		}
    }

	public void showNewFragment(FRAGMENT fragment)
	{
		mSupportFragmentManage.showNewFragment(fragment);
	}
	public void showUniqueFragment(FRAGMENT fragment)
	{
		mSupportFragmentManage.showUniqueFragment(fragment);
	}
	public void replaceNewFragment(FRAGMENT fragment)
	{
		mSupportFragmentManage.replaceNewFragment(fragment);
	}
	abstract public String getFragmentName();
	abstract public int getFragmentLayoutResId();

	public Fragment findChildFragmentById(int id)
    {
        return getChildFragmentManager().findFragmentById(id);
    }
    public Fragment findFragmentById(int id)
	{
		Fragment fragment = mFragmentManager.findFragmentById(id);
		if(fragment == null)
			fragment = findChildFragmentById(id);
        return fragment;
	}
	public void setShowed(boolean showed){
		mIsShowed = showed;
	}
	public boolean isShowed(){
		return mIsShowed;
	}
	/** Fragment is visible */
	protected boolean isVisible;
	public boolean isVisible2(){
		return isVisible;
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		
		if(getUserVisibleHint()) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
			onInvisible();
		}
	}
	/**
	 * visible,only works under ViewPager,and maybe before onResume(for the first time)
	 * it is nice to use mHandler.post(),that will be always inited already
	 */
	protected void onVisible() {
		Log.d(TAG, getFragmentName() + " onVisible ");
		if( mIsUnderViewPager){
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					onResumeOrVisible();
				}
			});
		}
	}
	
	
	/**
	 * invisible
	 */
	protected void onInvisible() {
		Log.d(TAG,getFragmentName()+ " onInvisible ");
	}




	boolean mIsShowEnterAnimation = true;
	public void setShowEnterAnimation(boolean show){
		mIsShowEnterAnimation = show;
	}
	public boolean isShowEnterAnimation(){
		return mIsShowEnterAnimation;
	}

	boolean mIsUnderViewPager = false;

	public boolean isUnderViewPager() {
		return mIsUnderViewPager;
	}

	public void setIsUnderViewPager(boolean mIsUnderViewPager) {
		this.mIsUnderViewPager = mIsUnderViewPager;
	}
}

