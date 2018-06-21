package projects.suchushin.org.pokupontest;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import projects.suchushin.org.pokupontest.requestclasses.Repository;
import retrofit2.Response;

public class RepositorySearchAsyncTask extends AsyncTask<Void, Void, List<Repository>>{
    private Context context;
    private RecyclerView recyclerView;
    private TextView textView;
    private String login;
    private String name;

    RepositorySearchAsyncTask(Context context, RecyclerView recyclerView, TextView textView, String login, String name){
        this.context = context;
        this.recyclerView = recyclerView;
        this.textView = textView;
        this.login = login;
        this.name = name;
    }

    @Override
    protected List<Repository> doInBackground(Void... voids) {
        GitHubService service = GitHubServiceHolder.getService();
        try {
            List<Repository> repositories = new ArrayList<>();

            int page = 1;

            while (true){
                String request = "https://api.github.com/users/" + login + "/repos?page=" + Integer.toString(page) + "&per_page=100&client_id=" + ClientData.CLIENT_ID + "&client_secret=" + ClientData.CLIENT_SECRET;

                Response<List<Repository>> response = service.getRepositories(request).execute();

                if (response.body() == null || response.body().size() == 0)
                    break;

                repositories.addAll(response.body());

                page++;
            }

            return repositories;
        } catch (IOException e) {
            Toast.makeText(context, "Download error", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Repository> repositories) {
        if (repositories != null && repositories.size() != 0) {
            String text = name + " Repositories (" + repositories.size() + ")";
            textView.setText(text);

            LinearLayoutManager llm = new LinearLayoutManager(context);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);

            RecyclerView.Adapter adapter = new RepositoryListAdapter(repositories);
            recyclerView.setAdapter(adapter);
        } else {
            RepositoryActivity activity = (RepositoryActivity) context;
            Toast.makeText(activity, "There is not one repository", Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }

}
