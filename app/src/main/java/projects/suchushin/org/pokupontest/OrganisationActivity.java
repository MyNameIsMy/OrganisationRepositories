package projects.suchushin.org.pokupontest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.jakewharton.rxbinding2.widget.RxTextView;
import java.util.concurrent.TimeUnit;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class OrganisationActivity extends Activity {
    private static int PAGINATION_HELPER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organisation);

        EditText editText = findViewById(R.id.edit_query);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        Button button = findViewById(R.id.more_results);
        ProgressBar progressBar = findViewById(R.id.progress_bar);

        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        button.setOnClickListener(v -> {
            PAGINATION_HELPER++;

            new OrganisationSearchAsyncTask(this, recyclerView, editText.getText(), PAGINATION_HELPER, button, progressBar).execute();
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GitHubService service = retrofit.create(GitHubService.class);

        GitHubServiceHolder.setService(service);

        RxTextView.textChanges(editText)
                .debounce(1, TimeUnit.SECONDS)
                .subscribe(charSequence -> {
                    if (charSequence.length() > 2){
                        PAGINATION_HELPER = 1;

                        new OrganisationSearchAsyncTask(this, recyclerView, charSequence, PAGINATION_HELPER, button, progressBar).execute();
                    }
                });
    }



}