package net.poisonlab.gamecast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by KKLee on 16. 6. 10..
 */
public class Tab5Container extends BaseContainerFragment {
    private boolean isViewInited;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.container_framelayout, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(!isViewInited)
        {
            isViewInited = true;
            initView();
        }
    }

    private void initView()
    {
        replaceFragment(new Tab5Fr(), false);
    }
}
