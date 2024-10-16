package com.stemy.mobileandroid.ui.employeeslist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.stemy.mobileandroid.R;
import com.stemy.mobileandroid.data.model.AccountUser;
import com.stemy.mobileandroid.databinding.FragmentUserIndetailsBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EmployeeViewInDetailsFragment extends Fragment {

    private AccountUser currentAccountUser;
    private FragmentUserIndetailsBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentUserIndetailsBinding.inflate(inflater, container, false);

        ImageView avatarImageView = binding.imageViewAvatar;
        TextView fullNameTextView = binding.textViewFullName;
        TextView emailTextView = binding.textViewEmail;
        TextView phoneTextView = binding.textViewPhone;
        TextView addressTextView = binding.textViewAddress;
        TextView roleTextView = binding.textViewRole;
        TextView statusTextView = binding.textViewStatus;

        currentAccountUser = EmployeeViewInDetailsFragmentArgs.fromBundle(getArguments()).getAccountUserToView();

        if(currentAccountUser != null){
            Glide.with(this)
                    .load(currentAccountUser.getAvatar()) // Assuming getAvatar() returns a URL or file path
                    .placeholder(R.drawable.ic_person) // Placeholder image
                    .error(R.drawable.ic_person) // Error image
                    .into(avatarImageView);

            fullNameTextView.setText(currentAccountUser.getFullName());
            emailTextView.setText(currentAccountUser.getUserMail());
            phoneTextView.setText(currentAccountUser.getPhone());
            addressTextView.setText(currentAccountUser.getAddress());
            roleTextView.setText(currentAccountUser.getRole().rawValue.toLowerCase()); // Assuming Role has a proper toString implementation
            statusTextView.setText(currentAccountUser.getStatus().rawValue.toLowerCase());
        }
        return binding.getRoot();
    }
}