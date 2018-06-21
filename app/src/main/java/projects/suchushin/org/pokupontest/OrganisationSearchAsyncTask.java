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
    private Context context;
    private Button moreResults;
    private ProgressBar progressBar;
    private int page;

    OrganisationSearchAsyncTask(Context context, RecyclerView recyclerView, CharSequence charSequence, int page, Button moreResults, ProgressBar progressBar){
        this.context = context;
        this.recyclerView = recyclerView;
        this.charSequence = charSequence;
        this.moreResults = moreResults;
        this.progressBar = progressBar;
        this.page = page;
    }

    @Override
    protected List<Organization> doInBackground(Void... voids) {
        publishProgress();

        GitHubService service = GitHubServiceHolder.getService();

        List<Organization> organizations = new ArrayList<>();

        try {
            Response<ItemList> response = service.itemList("https://api.github.com/search/users?q=" + charSequence.toString() + "+type:org+in:fullname&per_page=10&page=" + Integer.toString(page)).execute();

            List<Item> items = response.body().getItems();

            if (items != null){
                for (Item item : items){
                    Response<Organization> r = service.getOrganization("https://api.github.com/users/" + item.getLogin() + "?client_id=" + ClientData.CLIENT_ID + "&client_secret=" + ClientData.CLIENT_SECRET).execute();
                    Organization o = r.body();
                    organizations.add(o);
                }
            }
        } catch (IOException e) {
            Toast.makeText(context, "Download error", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return organizations;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        moreResults.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(List<Organization> organizations) {
        if (recyclerView.getLayoutManager() == null){
            LinearLayoutManager llm = new LinearLayoutManager(context);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);
        }

        if (page == 1) {
            RecyclerView.Adapter adapter = new OrganisationListAdapter(context, organizations);
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
