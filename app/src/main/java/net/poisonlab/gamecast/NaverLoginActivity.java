package net.poisonlab.gamecast;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginDefine;
import com.nhn.android.naverlogin.OAuthLoginHandler;

public class NaverLoginActivity extends AppCompatActivity {

    private static final String TAG = "NaverLoginActivity";

    private static OAuthLogin mOAuthLoginInstance;
    private static Context mContext;

    private static String OAUTH_CLIENT_ID = "4piafujUf5Zbl5vUwSzC";
    private static String OAUTH_CLIENT_SECRET = "QWj9HHmKUp";
    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";

    String nickname;
    String userPK;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OAuthLoginDefine.DEVELOPER_VERSION = true;
        mContext = this;

        mOAuthLoginInstance = OAuthLogin.getInstance();
        mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);

        OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
            @Override
            public void run(boolean success) {
                if (success) {
                    String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
                    String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
                    long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
                    String tokenType = mOAuthLoginInstance.getTokenType(mContext);

                    new RequestApiTask().execute();

                } else {
                    String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
                    String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
                    Toast.makeText(mContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
                }
            };
        };

        mOAuthLoginInstance.startOauthLoginActivity(NaverLoginActivity.this, mOAuthLoginHandler);

    }


    private class RequestApiTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(Void... params) {
            String url = "https://openapi.naver.com/v1/nid/me";
            String at = mOAuthLoginInstance.getAccessToken(mContext);
            return mOAuthLoginInstance.requestApi(mContext, at, url);
        }
        protected void onPostExecute(String content) {
            try {
                JSONObject obj = new JSONObject(content);

                userPK = obj.getJSONObject("response").getString("email");
                nickname = obj.getJSONObject("response").getString("nickname");

                intent = new Intent();
                intent.putExtra("logged", true);
                intent.putExtra("nickname", nickname);
                intent.putExtra("userPK",userPK);
                setResult(RESULT_OK, intent);

                finish();

            }
            catch(JSONException e) {
                e.printStackTrace();
            }


        }
    }
}