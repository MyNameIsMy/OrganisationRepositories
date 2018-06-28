package projects.suchushin.org.pokupontest;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class RepositoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);

        ImageButton button = findViewById(R.id.back_button);

        button.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        String login = intent.getExtras().getString("LOGIN");
        String name = intent.getExtras().getString("NAME");

        new RepositorySearchAsyncTask(this, login, name).execute();
    }
}
