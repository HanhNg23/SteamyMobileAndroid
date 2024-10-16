package com.stemy.mobileandroid.ui.employeeslist;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stemy.mobileandroid.R;
import com.stemy.mobileandroid.data.model.AccountUser;
import com.stemy.mobileandroid.databinding.FragmentEmployeeItemBinding;
import java.util.List;
import lombok.ToString;


public class EmployeesRecyclerViewAdapter extends RecyclerView.Adapter<EmployeesRecyclerViewAdapter.ViewHolder> {

    private final List<AccountUser> systemEmployees;
    private final NavController navController;

    public EmployeesRecyclerViewAdapter(List<AccountUser> systemEmployees, NavController navController) {
        this.systemEmployees = systemEmployees;
        this.navController =navController;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentEmployeeItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.user = systemEmployees.get(position);
        holder.userId.setText( holder.user.getUserId());
        holder.userNameView.setText(holder.user.getFullName());
        holder.userRoleView.setText("Role: " +  holder.user.getRole().rawValue.toLowerCase());
        holder.userStatusView.setText("Status: " +  holder.user.getStatus().rawValue.toLowerCase());
        holder.userEmailView.setText("Email: " +  holder.user.getUserMail());
        if (holder.user != null) {
            String imageUrl =  holder.user.getAvatar();
            // Use Glide to load the image
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(holder.userAvatar);
        } else {
            // Set default image if avatar URL is null or empty
            holder.userAvatar.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_person));

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmployeesFragmentDirections.FromListToDetailsEmployee action = EmployeesFragmentDirections.fromListToDetailsEmployee(holder.user);
                navController.navigate(action);
            }
        });
    }

    @Override
    public int getItemCount() {
        return systemEmployees.size();
    }

    @ToString
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView userId;
        public final TextView userNameView;
        public final TextView userRoleView;
        public final TextView userEmailView;
        public final TextView userStatusView;
        public final ImageView userAvatar;
        public AccountUser user;

        public ViewHolder(FragmentEmployeeItemBinding binding) {
            super(binding.getRoot());
            userId = binding.userId;
            userNameView = binding.userName;
            userRoleView = binding.userRole;
            userEmailView = binding.userEmail;
            userStatusView = binding.userStatus;
            userAvatar = binding.userAvatar;
        }
    }
}