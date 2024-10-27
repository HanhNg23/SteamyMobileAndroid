package com.stemy.mobileandroid.ui.employee.updatestaff;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.stemy.mobileandroid.R;
import com.stemy.mobileandroid.data.model.AccountUser;
import com.stemy.mobileandroid.databinding.FragmentUpdateStaffBinding;
import com.stemy.mobileandroid.type.Role;
import com.stemy.mobileandroid.type.UserStatus;
import com.stemy.mobileandroid.ui.employeeslist.EmployeeDetailViewModel;
import com.stemy.mobileandroid.ui.employeeslist.EmployeeViewInDetailsFragmentArgs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UpdateStaffFragment extends Fragment {
    private FragmentUpdateStaffBinding binding;

    private UpdateStaffViewModel updateStaffViewModel;
    private EmployeeDetailViewModel employeeDetailViewModel;

    @Inject
    public UpdateStaffViewModelFactory updateStaffViewModelFactory;

    private File avatarFile = null;
    RadioButton selectedRadioRole ;
    RadioButton selectedRadioStatus ;
    private AccountUser currentAccountUser;
    private String selectedRadioRoleText = null;
    private String selectedRadioStatusText = null;
    NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateStaffBinding.inflate(inflater, container, false);
        updateStaffViewModel = new ViewModelProvider(this, updateStaffViewModelFactory)
                .get(UpdateStaffViewModel.class);

        employeeDetailViewModel = new ViewModelProvider(requireActivity()).get(EmployeeDetailViewModel.class);

        navController =  NavHostFragment.findNavController(this);
        final TextView userId = binding.userId;
        final TextView userAvatarUrl = binding.avatarUrl;
        final TextInputEditText userEmail = (TextInputEditText) binding.editTextMail;
        final TextInputEditText userFullname = (TextInputEditText) binding.editTextName;
        final TextInputEditText userAddress = (TextInputEditText) binding.editTextAddress;
        final TextInputEditText userPhone = (TextInputEditText) binding.editTextPhone;
        ImageView avatarImageView = binding.profileImage;

        final Button buttonSaveUpdateUser = binding.buttonSaveUpdate;
        final Button buttonCancel = binding.buttonCancel;
        final ProgressBar loadingProgressBar = binding.loadingRegister;

        final Map<String, RadioButton> statusRadioButtons = new HashMap<>();
        statusRadioButtons.put("active", binding.statusActive);
        statusRadioButtons.put("ban", binding.statusBan);

        final Map<String, RadioButton> roleRadioButtons = new HashMap<>();
        roleRadioButtons.put("manager", binding.radioManager);
        roleRadioButtons.put("admin", binding.radioAdmin);
        roleRadioButtons.put("staff", binding.radioStaff);

        final RadioGroup statusRadioGroup = binding.statusGroup;
        final RadioGroup roleRadioGroup = binding.roleGroup;


        currentAccountUser = UpdateStaffFragmentArgs.fromBundle(getArguments()).getAccountUserToUpdate();

        if(currentAccountUser != null){
            Glide.with(this)
                    .load(currentAccountUser.getAvatar()) // Assuming getAvatar() returns a URL or file path
                    .placeholder(R.drawable.ic_person) // Placeholder image
                    .error(R.drawable.ic_person) // Error image
                    .into(avatarImageView);
            userId.setText(String.valueOf(currentAccountUser.getUserId()));
            userFullname.setText(currentAccountUser.getFullName());
            userEmail.setText(currentAccountUser.getUserMail());
            userPhone.setText(currentAccountUser.getPhone());
            userAddress.setText(currentAccountUser.getAddress());
            userAvatarUrl.setText(currentAccountUser.getAvatar() == null ? null : currentAccountUser.getAvatar());
            String currentUserRole = currentAccountUser.getRole().rawValue.toLowerCase();
            roleRadioButtons.get(currentUserRole).setChecked(true);
            String currentUserStatus = currentAccountUser.getStatus().rawValue.toLowerCase();
            statusRadioButtons.get(currentUserStatus).setChecked(true);

        }

        buttonSaveUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                String email = userEmail.getText().toString();
                String fullname = userFullname.getText().toString();
                String address = userAddress.getText().toString();
                String phone = userPhone.getText().toString();
                String avatarUrl = userAvatarUrl.getText().toString();
                int userIdToUpdate = Integer.parseInt(userId.getText().toString());

                int selectedRoleId = roleRadioGroup.getCheckedRadioButtonId();
                int selectedStatusId = statusRadioGroup.getCheckedRadioButtonId();
                String roleText = null;
                if(selectedRoleId != -1){
                    selectedRadioRole = getActivity().findViewById(selectedRoleId);
                    roleText = selectedRadioRole.getText().toString();
                }
                String statusText = null;
                if(selectedStatusId != -1){
                    selectedRadioStatus = getActivity().findViewById(selectedStatusId);
                    statusText = selectedRadioStatus.getText().toString();
                }
                Log.d("UpdateStaffFragment ", "when save " + roleText);
                Log.d("UpdateStaffFragment ", "when save " + statusText);

                Role accountRole = Role.safeValueOf(roleText.toUpperCase());
                UserStatus accountStatus = UserStatus.safeValueOf(statusText.toUpperCase());
               AccountUser user = AccountUser.builder()
                       .userId(userIdToUpdate)
                       .userMail(email)
                       .fullName(fullname)
                       .phone(phone)
                       .address(address)
                       .role(accountRole)
                       .status(accountStatus)
                       .avatar(avatarUrl)
                       .avatarFile(avatarFile)
                       .build();
                updateStaffViewModel.updateStaff(user);
                Log.d("IN UPDATE FRAGMENT", user.toString());
            }

        });



        updateStaffViewModel.getUpdateStaffFormState().observe(getViewLifecycleOwner(), new Observer<UpdateStaffFormState>() {
            @Override
            public void onChanged(UpdateStaffFormState updateStaffFormState) {
                if(updateStaffFormState == null){
                    return;
                }
                buttonSaveUpdateUser.setEnabled(updateStaffFormState.isDataValid());
                if(updateStaffFormState.getEmailError() != null){
                    userEmail.setError(updateStaffFormState.getEmailError());
                }
                if(updateStaffFormState.getFullnameError() != null){
                    userFullname.setError(updateStaffFormState.getFullnameError());
                }
                if(updateStaffFormState.getPhoneError() != null){
                    userPhone.setError(updateStaffFormState.getPhoneError());
                }
                if(updateStaffFormState.getAccountRoleError() != null){
                    Toast.makeText(getActivity(), "Please select a role", Toast.LENGTH_SHORT).show();
                    // Alternatively, highlight the RadioGroup to indicate the error:
                    roleRadioGroup.requestFocus();
                }
                if(updateStaffFormState.getAccountStatusError() != null){
                    Toast.makeText(getActivity(), "Please select a status", Toast.LENGTH_SHORT).show();
                    // Alternatively, you can highlight the RadioGroup to indicate the error:
                    statusRadioGroup.requestFocus();
                }
            }
        });

        updateStaffViewModel.getUpdateStaffResult().observe(getViewLifecycleOwner(), new Observer<UpdateStaffResult>() {
            @Override
            public void onChanged(UpdateStaffResult updateStaffResult) {
                if(updateStaffResult == null){
                   return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if(updateStaffResult.getErrorMessage() != null && !updateStaffResult.getErrorMessage().isEmpty()){
                    showUpdateUserFailed(updateStaffResult.getErrorMessage());
                }
                if(updateStaffResult.getSuccess() != null){
                    updateUiWithUser(updateStaffResult.getSuccess());
                }

            }
        });



        TextWatcher afterTextChangedListener = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                AccountUser accountUserForCheckTexchangeOnly = AccountUser.builder()
                        .userMail(userEmail.getText().toString())
                        .fullName(userFullname.getText().toString())
                        .phone(userPhone.getText().toString())
                        .role(Role.safeValueOf(selectedRadioRoleText != null ? selectedRadioRoleText.toUpperCase() : ""))
                        .status(UserStatus.safeValueOf(selectedRadioStatusText != null ? selectedRadioStatusText.toUpperCase() : ""))
                        .build();
                updateStaffViewModel.updateUserDataChange(accountUserForCheckTexchangeOnly);
            }
        };

        userEmail.addTextChangedListener(afterTextChangedListener);
        userFullname.addTextChangedListener(afterTextChangedListener);
        userPhone.addTextChangedListener(afterTextChangedListener);

// RadioGroup listeners for role and status
        roleRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // Get the selected role text
            if (checkedId != -1) {
                RadioButton selectedRoleButton = getActivity().findViewById(checkedId);
                this.selectedRadioRoleText = selectedRoleButton.getText().toString();
                // Trigger validation after role change
                afterTextChangedListener.afterTextChanged(null);
            }
            Log.d("UpdateStaffFragment", this.selectedRadioRoleText);
        });

        statusRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // Get the selected status text
            if (checkedId != -1) {
                RadioButton selectedStatusButton = getActivity().findViewById(checkedId);
                this.selectedRadioStatusText = selectedStatusButton.getText().toString();
                // Trigger validation after status change
                afterTextChangedListener.afterTextChanged(null);
            }
            Log.d("UpdateStaffFragment", this.selectedRadioStatusText);
        });

            FloatingActionButton editImageButton = binding.editImageButton;
        editImageButton.setOnClickListener(v -> {
            // Launch intent to pick image from gallery
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent); // Use the new launcher
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use the fragment's requireActivity() method
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return binding.getRoot();
    }

    private void updateUiWithUser(AccountUser user){
        Toast.makeText(getActivity(), "Update success user id" + user.getUserId(), Toast.LENGTH_LONG).show();
        binding.loadingRegister.setVisibility(View.INVISIBLE);
        employeeDetailViewModel.setCurrentUser(user);

        // Optionally navigate back
        requireActivity().getSupportFragmentManager().popBackStack();

    }

    private void showUpdateUserFailed(String errorString){
        Toast.makeText(getActivity(), "Update failed user, error message: " + errorString, Toast.LENGTH_LONG).show();
        binding.loadingRegister.setVisibility(View.INVISIBLE);
    }


    private ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        try {
                            // Set image to ShapeableImageView
                            ImageView profileImage = binding.profileImage;
                            profileImage.setImageURI(imageUri);

                            // Convert the URI to a File object
                            File fileImage = getFileFromUri(imageUri);
                            // Save the File object for later processing (e.g., upload to the server)
                            this.avatarFile = fileImage;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private File getFileFromUri(Uri uri) throws IOException {
        // Create a temporary file from the image URI
        InputStream inputStream = requireActivity().getContentResolver().openInputStream(uri);
        File tempFile = new File(requireActivity().getCacheDir(), "profile_image.jpg");
        FileOutputStream outputStream = new FileOutputStream(tempFile);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.close();
        inputStream.close();

        return tempFile;
    }




}
