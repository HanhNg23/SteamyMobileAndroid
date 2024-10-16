package com.stemy.mobileandroid.ui.employee;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import java.util.Map;

public class EmployeeFragment extends Fragment {

    private EmployeeViewModel mViewModel;
    private FragmentEmployeeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEmployeeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnAddNewEmployee.setOnClickListener(this::AddNewOnclickListender);
        binding.btnViewEmployeeList.setOnClickListener(this::ViewEmployeeListOnclickListender);
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

    public void ViewEmployeeListOnclickListender(View view){
        NavController navController = NavHostFragment.findNavController(EmployeeFragment.this);
        navController.navigate(R.id.EmployeesListFragment);
    }



}