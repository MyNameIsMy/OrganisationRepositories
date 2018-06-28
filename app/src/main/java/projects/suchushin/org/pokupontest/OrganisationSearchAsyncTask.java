package projects.suchushin.org.pokupontest;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import projects.suchushin.org.pokupontest.requestclasses.Item;
import projects.suchushin.org.pokupontest.requestclasses.ItemList;
import projects.suchushin.org.pokupontest.requestclasses.Organization;
import retrofit2.Response;

public class OrganisationSearchAsyncTask extends AsyncTask<Void, Void, List<Organization>>{
    private RecyclerView recyclerView;
    private CharSequence charSequence;
    private OrganisationActivity activity;
    private Button moreResults;
    private ProgressBar progressBar;
    private int page;

    OrganisationSearchAsyncTask(Context context, CharSequence charSequence, int page){
        this.activity = (OrganisationActivity) context;
        this.recyclerView = activity.findViewById(R.id.recycler_view);
        this.moreResults = activity.findViewById(R.id.more_results);
        this.progressBar = activity.findViewById(R.id.progress_bar);
        this.charSequence = charSequence;
        this.page = page;
    }

    @Override
    protected List<Organization> doInBackground(Void... voids) {
        publishProgress();

        GitHubService service = GitHubServiceHolder.getService();

        List<Organization> organizations = new ArrayList<>();

        String url;

        try {
            url = "https://api.github.com/search/users?q=" + charSequence.toString() + "+type:org+in:fullname&per_page=10&page=" + Integer.toString(page);

            Response<ItemList> response = service.itemList(url).execute();

            List<Item> items = response.body().getItems();

            if (items != null){
                for (Item item : items){
                    url = "https://api.github.com/users/" + item.getLogin() + "?client_id=" + ClientData.CLIENT_ID + "&client_secret=" + ClientData.CLIENT_SECRET;

                    Response<Organization> r = service.getOrganization(url).execute();

                    Organization o = r.body();

                    organizations.add(o);
                }
            }
        } catch (IOException e) {
            Toast.makeText(activity, "Download error", Toast.LENGTH_LONG).show();
        }
        return organizations;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        if (page == 1){
            recyclerView.setAdapter(null);
        }

        moreResults.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(List<Organization> organizations) {
        if (organizations == null || organizations.size() == 0){
            progressBar.setVisibility(View.INVISIBLE);
            moreResults.setVisibility(View.INVISIBLE);

            Toast.makeText(activity, "There is not one organisation", Toast.LENGTH_LONG).show();
        } else {
            if (recyclerView.getLayoutManager() == null){
                LinearLayoutManager llm = new LinearLayoutManager(activity);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(llm);
            }

            if (page == 1) {
                RecyclerView.Adapter adapter = new OrganisationListAdapter(activity, organizations);
                recyclerView.setAdapter(adapter);
            } else {
                OrganisationListAdapter adapter = (OrganisationListAdapter) recyclerView.getAdapter();
                adapter.addFreshResults(organizations);
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            progressBar.setVisibility(View.INVISIBLE);
            moreResults.setVisibility(View.VISIBLE);
        }
    }
}
