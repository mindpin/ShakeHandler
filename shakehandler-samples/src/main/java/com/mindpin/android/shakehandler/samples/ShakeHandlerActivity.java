package com.mindpin.android.shakehandler.samples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.mindpin.android.shakehandler.ParamGetter;
import com.mindpin.android.shakehandler.ShakeHandler;
import com.mindpin.android.shakehandler.samples.model.Book;

public class ShakeHandlerActivity extends Activity {
    private static final String TAG = "ShakeHandlerActivity";
    ShakeHandler sh;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);

        sh = new ShakeHandler(this);
        sh.set_url("http://kc-alpha.4ye.me/account/sign_in.json");

        // for login start
        sh.add_http_param("user[login]", "user1");
        sh.add_http_param("user[password]", "1234");
        // for login end

        // for test start
        sh.add_http_param("userage", "25");
        Book book = new Book(1, "booooooook");
        sh.add_http_param("book_title", new ParamGetter<Book>(book) {
            @Override
            public String get_param_value(Book book) {
                return book.title;
            }
        });
        sh.set_cookie("woshicookiestring");
        // for test end

        sh.set_callback_listener(new ShakeHandler.CallbackListener() {
            public void callback(String json) {
//                Log.d(TAG, "callback json:" + json);
                Intent intent = new Intent(ShakeHandlerActivity.this, ResultActivity.class);
                intent.putExtra("result", json);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sh.start_preview();
    }

    @Override
    protected void onPause() {
        sh.stop_preview();
        super.onPause();
    }
}