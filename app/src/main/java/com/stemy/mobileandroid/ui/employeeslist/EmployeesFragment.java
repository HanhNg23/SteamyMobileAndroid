package com.stemy.mobileandroid.ui.employeeslist;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.common.base.Strings;
import com.stemy.mobileandroid.R;
import com.stemy.mobileandroid.data.model.AccountUser;
import com.stemy.mobileandroid.databinding.FragmentEmployeesListBinding;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EmployeesFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private EmployeesViewModel employeesViewModel;
    private FragmentEmployeesListBinding binding;
    private SearchView employeesSearchView;
    private String query = "";
    private GetAllEmpployeesSuccessInUserView globalAccountUsersAllInUserView;
    private NavController navController ;
    private String currenViewRole;
    @Inject
    EmployeesViewModelFactory employeesViewModelFactory;

    public static EmployeesFragment newInstance(int columnCount) {
        EmployeesFragment fragment = new EmployeesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEmployeesListBinding.inflate(inflater, container, false);
        navController = NavHostFragment.findNavController(this);
        this.employeesSearchView = binding.searchView;
        this.employeesSearchView.clearFocus();

        // Initialize ViewModel
        employeesViewModel = new ViewModelProvider(this, employeesViewModelFactory).get(EmployeesViewModel.class);

        currenViewRole = EmployeesFragmentArgs.fromBundle(getArguments()).getRoleView();
        employeesViewModel.setCurrentRoleView(currenViewRole);

        setupRecyclerView();
        observeViewModelForShowallEmployees();

        // Trigger the request to get all employees
        employeesViewModel.getAllEmployeesUser();

        employeesSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                binding.employeesListRecycleView.setAdapter(new EmployeesRecyclerViewAdapter(globalAccountUsersAllInUserView.getFilteredEmployees(newText), navController));
                return true;
            }
        });



        return binding.getRoot();
    }

    private void setupRecyclerView() {
        Context context = binding.getRoot().getContext();
        RecyclerView recyclerView = binding.employeesListRecycleView;

        // Set layout manager based on the column count
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
    }

    protected void observeViewModelForShowallEmployees() {
        employeesViewModel.getAllEmployeesResult().observe(getViewLifecycleOwner(), new Observer<GetAllEmployeesResult>() {
            @Override
            public void onChanged(GetAllEmployeesResult getAllEmployeesResult) {
                if (getAllEmployeesResult == null) {
                    return;
                }

                if (getAllEmployeesResult.getError() != null) {
                    showGetAllUserFailed(getAllEmployeesResult.getError());
                }

                if (getAllEmployeesResult.getSuccess() != null) {
                    updateUiWithAllEmployees(getAllEmployeesResult.getSuccess());
                }
            }
        });
    }



    private void updateUiWithAllEmployees(GetAllEmpployeesSuccessInUserView model) {
        Toast.makeText(getActivity(), "GET ALL EMPLOYEE USER SUCCESS", Toast.LENGTH_LONG).show();
        globalAccountUsersAllInUserView = model;
        List<AccountUser> accountUsers = globalAccountUsersAllInUserView.getEmployeeUsers().parallelStream().filter(u -> u.getRole().rawValue.equalsIgnoreCase(employeesViewModel.getCurrentRoleView())).collect(Collectors.toList());

        // Update adapter data
        EmployeesRecyclerViewAdapter adapter = (EmployeesRecyclerViewAdapter) binding.employeesListRecycleView.getAdapter();
        if (adapter != null) {
            adapter.updateData(accountUsers);
        } else {
            binding.employeesListRecycleView.setAdapter(new EmployeesRecyclerViewAdapter(accountUsers, navController));
        }
    }

    private void showGetAllUserFailed(@StringRes Integer errorString) {
        Toast.makeText(getActivity(), "GET ALL EMPLOYEE USER FAILED", Toast.LENGTH_LONG).show();
    }


}
