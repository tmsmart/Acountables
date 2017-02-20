package group.g203.countables.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import group.g203.countables.path.main.view.MainActivity;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
