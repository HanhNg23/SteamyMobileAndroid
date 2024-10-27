package com.stemy.mobileandroid.ui.employeeslist;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        holder.userId.setText(String.valueOf(holder.user.getUserId()));
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
        Drawable background;
        switch (holder.user.getRole().rawValue.toLowerCase()) {
            case "admin":
                background = ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.small_badge_background);
                if (background != null) {
                    background.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.adminColor), PorterDuff.Mode.SRC_IN);
                    holder.userRoleView.setBackground(background);
                }
                break;
            case "staff":
                background = ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.small_badge_background);
                if (background != null) {
                    background.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.staffColor), PorterDuff.Mode.SRC_IN);
                    holder.userRoleView.setBackground(background);
                }
                break;
            case "manager":
                background = ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.small_badge_background);
                if (background != null) {
                    background.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.managerColor), PorterDuff.Mode.SRC_IN);
                    holder.userRoleView.setBackground(background);
                }
                break;
            case "customer":
                background = ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.small_badge_background);
                if (background != null) {
                    background.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.customerColor), PorterDuff.Mode.SRC_IN);
                    holder.userRoleView.setBackground(background);
                }
                break;
            default:
                break;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.user != null) {
                    EmployeesFragmentDirections.FromListToDetailsEmployee action =
                            EmployeesFragmentDirections.fromListToDetailsEmployee(holder.user);
                    navController.navigate(action);
                } else {
                    // Thông báo lỗi nếu user là null
                    Toast.makeText(view.getContext(), "User data is not available", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return systemEmployees.size();
    }

    public void updateData(List<AccountUser> newEmployees) {
        systemEmployees.removeAll(systemEmployees);
        systemEmployees.addAll(newEmployees);
        this.notifyDataSetChanged();
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