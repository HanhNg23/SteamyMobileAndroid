package com.stemy.mobileandroid.ui.employeeslist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.stemy.mobileandroid.R;
import com.stemy.mobileandroid.data.model.AccountUser;
import com.stemy.mobileandroid.databinding.FragmentUserIndetailsBinding;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EmployeeViewInDetailsFragment extends Fragment {

    private static final String TAG = "EmployeeViewInDetailsFragment";
    private AccountUser currentAccountUser;
    private FragmentUserIndetailsBinding binding;
    private NavController navController;
    @Inject
    EmployeesViewModelFactory employeesViewModelFactory;
    @Inject
    EmployeeDetailViewModelFactory employeeDetailViewModelFactory;
    private EmployeesViewModel employeesViewModel;
    private EmployeeDetailViewModel employeeDetailViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentUserIndetailsBinding.inflate(inflater, container, false);
        navController = NavHostFragment.findNavController(this);
        employeesViewModel = new ViewModelProvider(this, employeesViewModelFactory).get(EmployeesViewModel.class);
        employeeDetailViewModel = new ViewModelProvider(this, employeeDetailViewModelFactory).get(EmployeeDetailViewModel.class);

        // Get currentAccountUser from arguments
        currentAccountUser = EmployeeViewInDetailsFragmentArgs.fromBundle(getArguments()).getAccountUserToView();
        employeeDetailViewModel.setCurrentUser(currentAccountUser);

        // Observe currentUser from ViewModel
        employeeDetailViewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<AccountUser>() {
            @Override
            public void onChanged(AccountUser user) {
                if (user != null) {
                    updateUI(user);
                }
            }
        });

        binding.buttonEdit.setOnClickListener(view -> {
            EmployeeViewInDetailsFragmentDirections.FromViewDetailUserToUpdate action = EmployeeViewInDetailsFragmentDirections.fromViewDetailUserToUpdate(currentAccountUser);
            navController.navigate(action);
        });

        binding.buttonRemove.setOnClickListener(view -> {
            new AlertDialog.Builder(view.getContext())
                    .setTitle("Confirm Removal")
                    .setMessage("Are you sure you want to remove this employee?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Assuming currentUser is already set with the employee details
                        AccountUser user = currentAccountUser;
                        if (user != null) {
                            // Call removeEmployee from ViewModel
                            employeeDetailViewModel.removeEmployee(user.getUserId());

                            // Observe the removeEmployeeResult for success or failure
                            employeeDetailViewModel.removeEmployeeResult().observe(getViewLifecycleOwner(), result -> {
                                if (result.getSuccess()) {
                                    // Go back to the employee list page (home or list page)
                                    NavController navController = Navigation.findNavController(view);
                                    navController.navigate(R.id.EmployeesListFragment);
                                } else {
                                    // Show an error message if removal fails
                                    Toast.makeText(view.getContext(), "Failed to remove employee: " + result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .setNegativeButton("No", null) // Do nothing if user cancels
                    .show();
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Re-fetch data from ViewModel
        reloadData();
        AccountUser updatedUser = employeeDetailViewModel.getCurrentUser().getValue();
        if (updatedUser != null) {
            updateUI(updatedUser);
        }
    }

    private void reloadData() {
        employeesViewModel.getAllEmployeesUser();
        employeesViewModel.getAllEmployeesResult().observe(getViewLifecycleOwner(), new Observer<GetAllEmployeesResult>() {
            @Override
            public void onChanged(GetAllEmployeesResult getAllEmployeesResult) {
                if (getAllEmployeesResult == null) {
                    return;
                }

                if (getAllEmployeesResult.getError() != null) {
                    Toast.makeText(getActivity(), "Failed to load updated user data", Toast.LENGTH_LONG).show();
                    return;
                }

                if (getAllEmployeesResult.getSuccess() != null) {
                    // Update the currentAccountUser with the latest data
                    currentAccountUser = getAllEmployeesResult.getSuccess().getEmployeeUsersById(currentAccountUser.getUserId());
                    employeeDetailViewModel.setCurrentUser(currentAccountUser);
                }
            }
        });
    }

    private void updateUI(AccountUser user) {
        Glide.with(this)
                .load(user.getAvatar())
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(binding.imageViewAvatar);

        binding.textViewFullName.setText(user.getFullName());
        binding.textViewEmail.setText(user.getUserMail());
        binding.textViewPhone.setText(user.getPhone());
        binding.textViewAddress.setText(user.getAddress());
        binding.textViewRole.setText(user.getRole().rawValue.toLowerCase());
        binding.textViewStatus.setText(user.getStatus().rawValue.toLowerCase());
        binding.userId.setText(String.valueOf(user.getUserId()));
    }
}
