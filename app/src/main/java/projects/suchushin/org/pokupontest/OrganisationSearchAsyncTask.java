package projects.suchushin.org.pokupontest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import projects.suchushin.org.pokupontest.requestclasses.Item;
import projects.suchushin.org.pokupontest.requestclasses.ItemList;
import projects.suchushin.org.pokupontest.requestclasses.Organization;
import retrofit2.Response;

public class OrganisationSearchAsyncTask extends AsyncTask<Void, Void, List<Organization>>{
    private WeakReference<RecyclerView> recyclerView;
    private WeakReference<OrganisationActivity> activity;
    private WeakReference<Button> moreResults;
    private WeakReference<ProgressBar> progressBar;
    private CharSequence charSequence;
    private boolean isVersionHigher;
    private static int page = 1;

    OrganisationSearchAsyncTask(Context context, CharSequence charSequence, boolean isVersionHigher, boolean isNewTask){
        activity = new WeakReference<>((OrganisationActivity) context);
        recyclerView = new WeakReference<>(activity.get().findViewById(R.id.recycler_view));
        moreResults = new WeakReference<>(activity.get().findViewById(R.id.more_results));
        progressBar = new WeakReference<>(activity.get().findViewById(R.id.progress_bar));

        this.charSequence = charSequence;
        this.isVersionHigher = isVersionHigher;

        if (isNewTask)
            page = 1;
        else
            page++;
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
            e.printStackTrace();
        }
        return organizations;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        if (page == 1){
            recyclerView.get().setAdapter(null);
        }

        moreResults.get().setVisibility(View.INVISIBLE);
        progressBar.get().setVisibility(View.VISIBLE);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onPostExecute(List<Organization> organizations) {
        if (organizations == null || organizations.size() == 0){
            progressBar.get().setVisibility(View.INVISIBLE);
            moreResults.get().setVisibility(View.INVISIBLE);

            Toast.makeText(activity.get(), "There is not one organisation", Toast.LENGTH_LONG).show();
        } else {
            if (recyclerView.get().getLayoutManager() == null){
                LinearLayoutManager llm = new LinearLayoutManager(activity.get());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.get().setLayoutManager(llm);
            }

            if (page == 1) {
                RecyclerView.Adapter adapter = new OrganisationListAdapter(activity.get(), organizations);
                recyclerView.get().setAdapter(adapter);
            } else {
                OrganisationListAdapter adapter = (OrganisationListAdapter) recyclerView.get().getAdapter();
                adapter.addFreshResults(organizations);
                recyclerView.get().getAdapter().notifyDataSetChanged();
            }

            progressBar.get().setVisibility(View.INVISIBLE);
            if (!isVersionHigher){
                moreResults.get().setVisibility(View.VISIBLE);
            }
        }
        OrganisationActivity.isOrganisationsSearchExist = false;
    }
}
