package projects.suchushin.org.pokupontest;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import projects.suchushin.org.pokupontest.requestclasses.Organization;

public class OrganisationListAdapter extends RecyclerView.Adapter<OrganisationListAdapter.OrganisationViewHolder>{
    private List<Organization> organizations;
    private Context context;

    OrganisationListAdapter(Context context, List<Organization> organizations) {
        this.organizations = organizations;
        this.context = context;
    }

    @NonNull
    @Override
    public OrganisationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.organisation_item, parent, false);
        return new OrganisationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrganisationViewHolder holder, int position) {
        holder.bindOrganisation(organizations.get(position));
    }

    @Override
    public int getItemCount() {
        return organizations.size();
    }

    class OrganisationViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView name;
        TextView location;
        TextView blog;

        OrganisationViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.organisation_icon);
            name = itemView.findViewById(R.id.organisation_name);
            location = itemView.findViewById(R.id.organisation_location);
            blog = itemView.findViewById(R.id.organisation_blog);
        }

        void bindOrganisation(Organization organization) {
            RequestOptions options = new RequestOptions().centerInside();
            Glide.with(context).load(organization.getAvatarUrl()).apply(options).into(icon);

            if (organization.getName() != null && !organization.getName().equals("")) {
                name.setText(organization.getName());
            } if (organization.getLocation() != null && !organization.getLocation().equals("")){
                location.setText(organization.getLocation());
            } if (organization.getBlog() != null && !organization.getBlog().equals("")) {
                SpannableString content = new SpannableString(organization.getBlog());
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                blog.setText(content);
            }

            itemView.setOnClickListener(v -> {
                OrganisationActivity organisationActivity = (OrganisationActivity) context;
                Intent intent = new Intent(organisationActivity, RepositoryActivity.class);
                intent.putExtra("LOGIN", organization.getLogin());
                intent.putExtra("NAME", organization.getName());
                organisationActivity.startActivityForResult(intent, 2);
            });
        }
    }

    public void addFreshResults(List<Organization> results){
        organizations.addAll(results);
    }
}
