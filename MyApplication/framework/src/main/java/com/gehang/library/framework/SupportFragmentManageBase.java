package com.gehang.library.framework;

import com.gehang.library.framework.fragment.AbsBaseSupportFragment;

import java.util.List;

public interface SupportFragmentManageBase<FRAGMENT extends AbsBaseSupportFragment> {


	//////////////for Common/////////////////////////
	public void addOnBackPressedListener(OnBackPressedListener listener);
	public void removeOnBackPressedListener(OnBackPressedListener listener);
	public void showRootFragment();
	public void replaceNewFragment(FRAGMENT fragment);
	public void showNewFragment(FRAGMENT fragment);
	public void showUniqueFragment(FRAGMENT fragment); //显示唯一一个Fragment
	public void clearFragmentExcept(FRAGMENT fragment);

	/**
	 *
	 */
	public void clearFragment();
	public boolean isFragmentExist(String strFragment);
	public boolean onFragmentPop();
	public void removeFragment(FRAGMENT fragment);
	public void removeFragment(String fragmentName);
	public FRAGMENT  findFragment(String strFragment);
	public FRAGMENT getVisibleFragment();
	public List<FRAGMENT> getFragmentList();
	public boolean isBackKeyConsumed();


}
