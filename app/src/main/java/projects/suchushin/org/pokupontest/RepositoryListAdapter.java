package projects.suchushin.org.pokupontest;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import projects.suchushin.org.pokupontest.requestclasses.Repository;

public class RepositoryListAdapter extends RecyclerView.Adapter<RepositoryListAdapter.RepositoryViewHolder>{
    List<Repository> repositories;

    RepositoryListAdapter(List<Repository> repositories){
        this.repositories = repositories;
    }

    @NonNull
    @Override
    public RepositoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repository_item, parent, false);
        return new RepositoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepositoryViewHolder holder, int position) {
        holder.bindRepository(repositories.get(position));
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    class RepositoryViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView description;

        public RepositoryViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.repository_name);
            description = itemView.findViewById(R.id.repository_description);
        }

        void bindRepository(Repository repository){
            name.setText(repository.getName());
            description.setText(repository.getDescription());
        }
    }
}
