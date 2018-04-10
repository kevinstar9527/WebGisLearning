package com.gehang.library.framework;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.gehang.library.basis.Log;
import com.gehang.library.framework.fragment.AbsBaseSupportFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <FRAGMENT>
 */
public abstract class SupportFragmentManageBaseActivity<FRAGMENT extends AbsBaseSupportFragment> extends FragmentActivity implements SupportFragmentManageBase<FRAGMENT> {


	/**
	 * 类名
	 */
	private static final String TAG = "SupportFgManageAct";

	private static final boolean TEST = false;

	private static boolean DEBUG = true;


	protected FragmentManager mFragmentManager;

	protected FRAGMENT mRootFragment = null;

	protected boolean mIsDestroying = false;
	protected boolean mIsDestroyed;
	protected boolean mIsPaused = true;
	protected boolean mIsStopped = true;


	protected List<FRAGMENT> mFragmentList = new ArrayList<FRAGMENT>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		mFragmentManager = getSupportFragmentManager();
		mIsDestroying = false;
		mIsDestroyed = false;

	}
	@Override
	protected void onStart() {
		super.onStart();

		mIsStopped = false;
	}
	@Override
	protected void onResume() {
		super.onResume();
		mIsPaused = false;
	}

	@Override
	protected void onPause() {
		super.onPause();
		mIsPaused = true;
	}

	@Override
	protected void onStop() {
		super.onStop();

		mIsStopped = true;

	}


	protected boolean isValidActivity(){
		return (!mIsDestroying && !mIsDestroyed);
	}

	@Override
	protected void onDestroy(){
		mIsDestroying = true;
		super.onDestroy();
		mIsDestroyed = true;
	}

	/*
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    //No call for super(). Bug on API Level > 11.
	}
	*/

	public void printFragmentList() {
		Log.d (TAG, "-----------fg---------------");
		for (FRAGMENT fg:mFragmentList)
			Log.d(TAG,"fg=" + fg.getFragmentName() + ", " + (fg.isShowed() ?"show":"hide"));
		Log.d(TAG, "----------------------------") ;
	}
	// if fragment == null,meanings clear all fragment
	// fragment is reference,not find by name
	public void clearFragmentExcept(FRAGMENT fragment)
	{
		if(fragment == null)
			Log.d(TAG,"before clearFragmentExcept null");
		else{
			Log.d(TAG,"before clearFragmentExcept" + fragment.getFragmentName());
			//fragment = findFragment(fragment.getFragmentName());
		}
		printFragmentList();
		for(FRAGMENT fg:mFragmentList)
			if(fg != fragment) {
				fg.setShowed(false);
				FragmentTransaction beginTransaction = mFragmentManager.beginTransaction();
				beginTransaction.remove(fg);
				beginTransaction.commitAllowingStateLoss();
			}
		int i = 0;
		while (i < mFragmentList.size()) {
			FRAGMENT fg = mFragmentList.get(i);
			if (fg != fragment)
				mFragmentList.remove(fg);
			else
				i++;
		}
		if (mRootFragment != fragment)
			mRootFragment = fragment;
		if (fragment != null && !fragment.isShowed()) {
			fragment.setShowed(true);
			FragmentTransaction beginTransaction = mFragmentManager.beginTransaction();
			beginTransaction.show(fragment);
			beginTransaction.commitAllowingStateLoss();
		}
		Log.d(TAG, "after clearFragmentExcept");
		printFragmentList();
	}
	@Override
	public void clearFragment() {

		clearFragmentExcept(null);
	}
	public void showFragment(FRAGMENT fragment) {
		Log.d(TAG, "before showFragment " + fragment.getFragmentName());
		printFragmentList();
		//if( ! fragment.isHidden())
		//	return;
		for (FRAGMENT fg:mFragmentList)
			if (fg != fragment && fg.isShowed()) {
				fg.setShowed(false);
				FragmentTransaction beginTransaction = mFragmentManager.beginTransaction();
				beginTransaction.hide(fg);
				setFragmentTransAnim(beginTransaction);
				beginTransaction.commitAllowingStateLoss();
		}
		if (!fragment.isShowed()) {
			fragment.setShowed(true);
			FragmentTransaction beginTransaction = mFragmentManager.beginTransaction();
			if (fragment.isShowEnterAnimation())
				setFragmentTransAnim(beginTransaction);
			beginTransaction.show(fragment);
			beginTransaction.commitAllowingStateLoss();

			// switch this fragment to top
			//mFragmentList.remove(fragment);
			//mFragmentList.add(fragment);
			FRAGMENT fragmentTemp = mFragmentList.get(mFragmentList.size() - 1);
			if (fragmentTemp != fragment) {
				int index = mFragmentList.indexOf(fragment);
				mFragmentList.set(mFragmentList.size() - 1, fragment);
				mFragmentList.set(index, fragmentTemp);
			}
		}
		Log.d(TAG, "after showFragment " + fragment.getFragmentName());
		printFragmentList();
	}

	public static void setFragmentTransAnim(final FragmentTransaction trans) {
		if (trans != null) {
			trans.setCustomAnimations(
					R.anim.push_right_in,
					R.anim.push_right_out,
					R.anim.push_left_in,
					R.anim.push_left_out);
		}

	}
	public void replaceNewFragment(FRAGMENT fragment) {
		if (fragment == null)
			return;

		clearFragmentExcept(null);

		fragment.setShowed(true);
		FragmentTransaction beginTransaction = mFragmentManager.beginTransaction();

		//setFragmentTransAnim(beginTransaction);

		beginTransaction.replace(getContainerResId(), fragment,fragment.getFragmentName());
		beginTransaction.commitAllowingStateLoss();
		boolean bTestFlag = false;
		bTestFlag = fragment.isAdded();
		mRootFragment = fragment;
		mFragmentList.add(fragment);
		Log.d(TAG, "replace mRootFragment = " + mRootFragment.getFragmentName());
		Log.d(TAG, "after replaceNewFragment");
		printFragmentList();
	}
	public FRAGMENT getVisibleFragment() {
		for (FRAGMENT fg:mFragmentList)
			if (fg.isShowed())
				return fg;
		return null;
	}

	public void showNewFragment(FRAGMENT fragment) {
		if (fragment == null)
			return;
		Log.d(TAG, "before showNewFragment " + fragment.getFragmentName());
		printFragmentList();


		FragmentTransaction beginTransaction = mFragmentManager.beginTransaction();

		//setFragmentTransAnim(beginTransaction);

		FRAGMENT fg = getVisibleFragment();
		if (fg != null) {
			fg.setShowed(false);
			beginTransaction.hide(fg);
		}
		fragment.setShowed(true);
		beginTransaction.add(getContainerResId(), fragment, fragment.getFragmentName());
		beginTransaction.commitAllowingStateLoss();

		if (mFragmentList.size() == 0)
			beforeShowFirstFragment();

		mFragmentList.add(fragment);

		Log.d(TAG, "after showNewFragment " + fragment.getFragmentName());
		printFragmentList();
	}

	public void showUniqueFragment(FRAGMENT fragment) {
		if (fragment == null || mFragmentManager == null) {
			return;
		}

		Log.d(TAG, "before showUniqueFragment " + fragment.getFragmentName());
		printFragmentList();
		if (!isFragmentExist(fragment.getFragmentName())) {

			FragmentTransaction beginTransaction = mFragmentManager.beginTransaction();

			//setFragmentTransAnim(beginTransaction);
			FRAGMENT fg = getVisibleFragment();
			if (fg != null) {
				fg.setShowed(false);
				beginTransaction.hide(fg);
			}
			fragment.setShowed(true);
			beginTransaction.add(getContainerResId(), fragment, fragment.getFragmentName());
			beginTransaction.commitAllowingStateLoss();

			if (mFragmentList.size() == 0)
				beforeShowFirstFragment();

			mFragmentList.add(fragment);
		} else {
			showFragment(findFragment(fragment.getFragmentName()));
		}
		Log.d(TAG, "after showUniqueFragment " + fragment.getFragmentName());
		printFragmentList();
	}

	public void showRootFragment() {
		if (mRootFragment == null)
			return;
		//showFragment(mRootFragment);
		clearFragmentExcept(mRootFragment);
	}
	public boolean isFragmentExist(String strFragment)
	{
		for(FRAGMENT fg:mFragmentList)
			if(fg.getFragmentName().equals(strFragment))
				return true;
		return false;
	}

	public FRAGMENT findFragment(String strFragment)
	{
		for(FRAGMENT fg:mFragmentList)
			if(fg.getFragmentName().equals(strFragment))
				return fg;
		return null;
	}
	public boolean onFragmentPop()
	{
		if(mFragmentList.size() < 1)
			return false;
		FRAGMENT fragment = mFragmentList.get(mFragmentList.size() - 1);

		fragment.setShowed(false);
		FragmentTransaction beginTransaction = mFragmentManager.beginTransaction();
		beginTransaction.remove(fragment);
		beginTransaction.commitAllowingStateLoss();

		mFragmentList.remove(fragment);

		if(mFragmentList.size() > 0)
		{
			fragment = mFragmentList.get(mFragmentList.size() - 1);
			showFragment(fragment);
		}
		return true;
	}

	public void removeFragment(FRAGMENT fragment)
	{
		removeFragment(fragment.getFragmentName());
	}

	public void removeFragment(String fragmentName)
	{
		FRAGMENT fragmentToBeRemoved = findFragment(fragmentName);
		if(fragmentToBeRemoved == null || mIsDestroying)
			return;
		boolean isShowed = fragmentToBeRemoved.isShowed();
		fragmentToBeRemoved.setShowed(false);
		FragmentTransaction beginTransaction = mFragmentManager.beginTransaction();
		beginTransaction.remove(fragmentToBeRemoved);
		beginTransaction.commitAllowingStateLoss();

		mFragmentList.remove(fragmentToBeRemoved);

		if(isShowed && mFragmentList.size() > 0)
		{
			FRAGMENT fragment = mFragmentList.get(mFragmentList.size() - 1);
			showFragment(fragment);
		}
	}

	public List<FRAGMENT> getFragmentList(){
		return mFragmentList;
	}

	public ArrayList<OnBackPressedListener> mOnBackPressedListenerList = new ArrayList<OnBackPressedListener>();
	public void addOnBackPressedListener(OnBackPressedListener listener)
	{
		mOnBackPressedListenerList.add(listener);
		Log.d(TAG, String.format("addOnBackPressedListener size=%d", mOnBackPressedListenerList.size()));
	}
	public void removeOnBackPressedListener(OnBackPressedListener listener)
	{
		mOnBackPressedListenerList.remove(listener);
		Log.d(TAG, String.format("removeOnBackPressedListener size=%d", mOnBackPressedListenerList.size()));
	}

	public boolean isBackKeyConsumed(){
		boolean isProcessed = false;

		int i;
		for(i=mOnBackPressedListenerList.size() - 1;i>=0;i--)
		{
			OnBackPressedListener listener = mOnBackPressedListenerList.get(i);
			if(listener.onBackPressed())
			{
				isProcessed = true;
				break;
			}
		}
		return isProcessed;
	}
	abstract public int getContainerResId();
	abstract public void beforeShowFirstFragment();

}
