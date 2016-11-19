package qbai22.com.draganddraw;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DragAndDrawActivity extends SingleFragmentActivity {


    @Override
    Fragment createFragment() {
        return DragAndDrawFragment.newInstance();
    }
}
