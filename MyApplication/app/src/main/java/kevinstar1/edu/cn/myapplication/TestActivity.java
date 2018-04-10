package kevinstar1.edu.cn.myapplication;

import com.gehang.library.framework.SupportFragmentManageBaseActivity;

/**
 * Created by Administrator on 2017/6/2.
 */

public class TestActivity extends SupportFragmentManageBaseActivity<BaseSupportFragment> {
    @Override
    public int getContainerResId() {
        return 0;
    }

    @Override
    public void beforeShowFirstFragment() {

    }
}
