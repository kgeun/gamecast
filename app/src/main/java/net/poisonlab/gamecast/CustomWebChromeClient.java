package net.poisonlab.gamecast;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

/**
 * Created by KKLee on 16. 7. 6..
 */
public class CustomWebChromeClient extends WebChromeClient {
    private View mCustomView;
    private Activity mActivity;
    private int mOriginalOrientation;
    private FullscreenHolder mFullscreenContainer;
    private WebChromeClient.CustomViewCallback mCustomViewCollback;
    private ProgressBar progressBar;

    public CustomWebChromeClient(Activity activity) {
        this.mActivity = activity;
        View view = LayoutInflater.from(activity).inflate(R.layout.activity_gamecast_ctntview,null);
        progressBar = (ProgressBar) view.findViewById(R.id.webProgressBar);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        result.confirm();
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {

        if (mCustomView != null) {
            callback.onCustomViewHidden();
            return;
        }

        mOriginalOrientation = mActivity.getRequestedOrientation();
        FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();

        mFullscreenContainer = new FullscreenHolder(mActivity);
        mFullscreenContainer.addView(view, ViewGroup.LayoutParams.MATCH_PARENT);
        decor.addView(mFullscreenContainer, ViewGroup.LayoutParams.MATCH_PARENT);
        mCustomView = view;
        mCustomViewCollback = callback;
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);


    }

    @Override
    public void onHideCustomView() {
        super.onHideCustomView();
        if (mCustomView == null) {
            return;
        }

        FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
        decor.removeView(mFullscreenContainer);
        mFullscreenContainer = null;
        mCustomView = null;
        mCustomViewCollback.onCustomViewHidden();

        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR   );
    }

    public void setRequestOrientation(int orientation){
        mActivity.setRequestedOrientation(orientation);
    }

    private static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if(progressBar != null)
            progressBar.setProgress(newProgress);
        super.onProgressChanged(view, newProgress);
    }
}
