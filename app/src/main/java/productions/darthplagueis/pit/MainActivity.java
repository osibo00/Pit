package productions.darthplagueis.pit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import productions.darthplagueis.pit.view.util.PitViewContract;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final PitViewContract contract = findViewById(R.id.pitView);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contract.addPitPoint();
            }
        });
    }
}
