package just.trust.me;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

      TextView textView=new TextView(this);
      textView.setText("Xposed RPC Demo");
        setContentView(textView);
    }

}
