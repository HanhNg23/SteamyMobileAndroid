package com.stemy.mobileandroid.ui.employee;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stemy.mobileandroid.R;
import com.stemy.mobileandroid.databinding.FragmentAddNewStaffBinding;
import com.stemy.mobileandroid.databinding.FragmentEmployeeBinding;
import com.stemy.mobileandroid.ui.employeeslist.EmployeesViewModel;
import com.stemy.mobileandroid.ui.employeeslist.EmployeesViewModelFactory;

import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EmployeeFragment extends Fragment {

    private EmployeeViewModel mViewModel;
    private FragmentEmployeeBinding binding;
    private EmployeesViewModel employeesViewModel;
    @Inject
    EmployeesViewModelFactory employeesViewModelFactory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEmployeeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        employeesViewModel = new ViewModelProvider(this, employeesViewModelFactory).get(EmployeesViewModel.class);
        binding.btnAddNewEmployee.setOnClickListener(this::AddNewOnclickListender);
        binding.btnViewAdminEmployeeList.setOnClickListener(v ->ViewEmployeeListOnclickListender(v, "admin"));
        binding.btnViewStaffEmployeeList.setOnClickListener(v -> ViewEmployeeListOnclickListender(v, "staff"));
        binding.btnViewManagerEmployeeList.setOnClickListener(v -> ViewEmployeeListOnclickListender(v, "manager"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void AddNewOnclickListender(View view){
        NavController navController = NavHostFragment.findNavController(EmployeeFragment.this);
        NavDirections action = EmployeeFragmentDirections.addNewEmployee();
        navController.navigate(action);

    }

    public void ViewEmployeeListOnclickListender(View view, String role){
        NavController navController = NavHostFragment.findNavController(EmployeeFragment.this);
       // navController.navigate(R.id.EmployeesListFragment);
        EmployeeFragmentDirections.FromHomeToEmployeeList action = EmployeeFragmentDirections.fromHomeToEmployeeList(role);
        navController.navigate(action);
    }



}