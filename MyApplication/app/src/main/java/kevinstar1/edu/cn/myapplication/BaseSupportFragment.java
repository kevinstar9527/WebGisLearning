package kevinstar1.edu.cn.myapplication;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.gehang.library.framework.SupportFragmentManageBase;
import com.gehang.library.framework.fragment.AbsBaseSupportFragment;


public abstract class BaseSupportFragment extends AbsBaseSupportFragment<SupportFragmentManageBase<BaseSupportFragment>,BaseSupportFragment> {
    private static final String TAG = "BaseSupportFragment";


    protected BaseSupportFragment mFragment = null;

    protected boolean mIsReleasing = false;

    protected boolean isFragmentValid() {
        return (isViewValid() && isAdded());

    }

    protected boolean isViewValid() {
        return (!mIsReleasing && !isViewDestroyed());

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragment = this;
    }



    private View mRootview = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, String.format("%s onCreateView", getFragmentName()));


//		mMpdp = Mpdp.getMpdp(getActivity());
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mIsReleasing = false;
        mRootview = view;

        return view;
    }

    public void onResume() {
        super.onResume();

    }

    public void onPause() {
        super.onPause();

    }


    private void unbindDrawables(View view)
    {
        if (view.getBackground() != null)
        {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView))
        {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
            {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        release();
        if(mRootview!=null){
            unbindDrawables(mRootview);
            mRootview = null;
        }

        mFragment = null;
    }


    void release() {
//        Log.i(TAG, "release");
        mIsReleasing = true;

    }

}

