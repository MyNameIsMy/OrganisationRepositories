package projects.suchushin.org.pokupontest;

import java.util.List;

import projects.suchushin.org.pokupontest.requestclasses.ItemList;
import projects.suchushin.org.pokupontest.requestclasses.Organization;
import projects.suchushin.org.pokupontest.requestclasses.Repository;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GitHubService {

    @GET()
    Call<ItemList> itemList(@Url String url);

    @GET()
    Call<Organization> getOrganization(@Url String url);

    @GET()
    Call<List<Repository>> getRepositories(@Url String url);
}
