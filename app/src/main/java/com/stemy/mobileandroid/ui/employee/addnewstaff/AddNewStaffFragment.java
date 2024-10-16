package com.stemy.mobileandroid.ui.employee.addnewstaff;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.stemy.mobileandroid.data.model.AccountUser;
import com.stemy.mobileandroid.databinding.FragmentAddNewStaffBinding;
import com.stemy.mobileandroid.type.Role;
import com.stemy.mobileandroid.type.UserStatus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class AddNewStaffFragment extends Fragment {

    private FragmentAddNewStaffBinding binding;

    private AddNewStaffViewModel addNewStaffViewModel;

    @Inject
    public AddNewStaffViewModelFactory addNewStaffViewModelFactory;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    RadioButton selectedRadioRole ;
    RadioButton selectedRadioStatus ;
    private File avatarFile = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddNewStaffBinding.inflate(inflater, container, false);
        addNewStaffViewModel = new ViewModelProvider(this, addNewStaffViewModelFactory)
                .get(AddNewStaffViewModel.class);

        final TextInputEditText userEmail = (TextInputEditText) binding.editTextMail;
        final TextInputEditText userFullName = (TextInputEditText) binding.editTextName;
        final TextInputEditText userAddress = (TextInputEditText) binding.editTextAddress;
        final TextInputEditText userPhone = (TextInputEditText) binding.editTextPhone;
        final TextInputEditText password = (TextInputEditText) binding.editTextPassword;

        final TextInputEditText userRole = (TextInputEditText) binding.editTextMail;
        final TextInputEditText userStatus = (TextInputEditText) binding.editTextMail;

        final Button buttonSaveRegister = binding.buttonSaveRegister;
        final Button buttonCancel = binding.buttonCancel;
        final ProgressBar loadingProgressBar = binding.loadingRegister;

        final RadioGroup statusRadioGroup = binding.statusGroup;
        final RadioGroup roleRadioGroup = binding.roleGroup;

        int selectedRoleId = roleRadioGroup.getCheckedRadioButtonId();

        int selectedStatusId = statusRadioGroup.getCheckedRadioButtonId();

//        RadioButton selectedRadioRole = getActivity().findViewById(selectedRoleId);
//        RadioButton selectedRadioStatus = getActivity().findViewById(selectedStatusId);


        addNewStaffViewModel.getAddNewStaffFormState().observe(getViewLifecycleOwner(), new Observer<AddNewStaffFormState>() {
            @Override
            public void onChanged(AddNewStaffFormState addNewStaffFormState) {
                if(addNewStaffFormState == null){
                    return;
                }
                buttonSaveRegister.setEnabled(addNewStaffFormState.isDataValid());
                if(addNewStaffFormState.getEmailError() != null){
                    userEmail.setError(getString(addNewStaffFormState.getEmailError()));
                }
                if(addNewStaffFormState.getFullnameError() != null){
                    userFullName.setError(getString(addNewStaffFormState.getFullnameError()));
                }
                if(addNewStaffFormState.getPasswordError() != null){
                    password.setError(getString(addNewStaffFormState.getPasswordError()));
                }
                if(addNewStaffFormState.getAccountRoleError() != null){
                   // TODO
                }
                if(addNewStaffFormState.getAccountStatusError() != null){
                    // TODO
                }

            }
        });

        addNewStaffViewModel.getAddNewStaffResult().observe(getViewLifecycleOwner(), new Observer<AddNewStaffResult>() {
            @Override
            public void onChanged(AddNewStaffResult addNewStaffResult) {
                if(addNewStaffResult == null){
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if(addNewStaffResult.getError() != null){
                    showLoginFailed(addNewStaffResult.getError());
                }
                if(addNewStaffResult.getSuccess() != null){
                    updateUiWithUser(addNewStaffResult.getSuccess());
                }
            }
        });


        buttonSaveRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                String email = userEmail.getText().toString();
                String fullname = userFullName.getText().toString();
                String passwordUser = password.getText().toString();
                String phone = userPhone.getText().toString();
                String address = userAddress.getText().toString();

                String seletectedRadioRoleText = null;
                if(selectedRoleId != -1){
                    selectedRadioRole = getActivity().findViewById(selectedRoleId);
                    seletectedRadioRoleText = selectedRadioRole.getText().toString();
                }
                String seletectedRadioStatusText = null;
                if(selectedStatusId != -1){
                    selectedRadioStatus = getActivity().findViewById(selectedStatusId);
                    seletectedRadioStatusText = selectedRadioStatus.getText().toString();
                }

                Role accountRole = Role.safeValueOf(seletectedRadioRoleText.toUpperCase());
                UserStatus accountStatus = UserStatus.safeValueOf(seletectedRadioStatusText.toUpperCase());
                addNewStaffViewModel.register(
                        AccountUser.builder()
                                .userMail(email)
                                .fullName(fullname)
                                .password(passwordUser)
                                .phone(phone)
                                .address(address)
                                .role(accountRole)
                                .status(accountStatus)
                                .avatarFile(avatarFile)
                                .build());
            }
        });

        FloatingActionButton editImageButton = binding.editImageButton;
        editImageButton.setOnClickListener(v -> {
            // Launch intent to pick image from gallery
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent); // Use the new launcher
        });
        return binding.getRoot();
    }



    private void updateUiWithUser(AddNewStaffSuccessInUserView model) {
        Toast.makeText(getActivity(), "REGISTER SUCCESS USER NAME " + model.getRegisteredStaff().getFullName() + " Email: " + model.getRegisteredStaff().getUserMail(), Toast.LENGTH_LONG).show();
        binding.loadingRegister.setVisibility(View.INVISIBLE);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getActivity(), "REGISTER FAILD USER NAME " , Toast.LENGTH_LONG).show();
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