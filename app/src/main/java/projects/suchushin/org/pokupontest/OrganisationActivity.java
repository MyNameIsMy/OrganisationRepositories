package projects.suchushin.org.pokupontest;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class OrganisationActivity extends Activity {
    private static boolean isVersionHigher;
    static boolean isOrganisationsSearchExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organisation);

        NestedScrollView scrollView = findViewById(R.id.scroll_view);
        EditText editText = findViewById(R.id.edit_query);
        Button button = findViewById(R.id.more_results);
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        scrollView.setSmoothScrollingEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isVersionHigher = true;
            scrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (scrollY > (linearLayout.getChildAt(0).getHeight() + linearLayout.getChildAt(1).getHeight() - scrollView.getHeight()) && !isOrganisationsSearchExist){
                    new OrganisationSearchAsyncTask(this, editText.getText(), isVersionHigher, false).execute();
                    isOrganisationsSearchExist = true;
                }
            });
        } else {
            isVersionHigher = false;
            button.setOnClickListener(v -> {
                new OrganisationSearchAsyncTask(this, editText.getText(), isVersionHigher, false).execute();
            });
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GitHubService service = retrofit.create(GitHubService.class);

        GitHubServiceHolder.setService(service);

        Disposable disposable = RxTextView.textChanges(editText)
                .debounce(1, TimeUnit.SECONDS)
                .subscribe(charSequence -> {
                    if (charSequence.length() > 2){
                        new OrganisationSearchAsyncTask(this, charSequence, isVersionHigher, true).execute();

                        View view = this.getCurrentFocus();

                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    }
                });
    }
}