package com.stemy.mobileandroid.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.stemy.mobileandroid.type.Role;
import com.stemy.mobileandroid.type.UserStatus;

import java.io.File;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountUser implements Parcelable {
    private int userId;
    private String userMail;
    private String fullName;
    private Role role;
    private UserStatus status;
    private String phone;
    private String address;
    private String password;
    private String avatar;
    private File avatarFile;
    @Builder.Default
    private boolean isAuthenticated = false;
    private String accessToken;

    // Parcelable implementation
    protected AccountUser(Parcel in) {
        userId = in.readInt();
        userMail = in.readString();
        fullName = in.readString();
        role = Role.safeValueOf(in.readString());
        status = UserStatus.safeValueOf(in.readString());
        phone = in.readString();
        address = in.readString();
        password = in.readString();
        avatar = in.readString();
        avatarFile = (File) in.readSerializable();
        isAuthenticated = in.readByte() != 0;
        accessToken = in.readString();
    }

    public static final Creator<AccountUser> CREATOR = new Creator<AccountUser>() {
        @Override
        public AccountUser createFromParcel(Parcel in) {
            return new AccountUser(in);
        }

        @Override
        public AccountUser[] newArray(int size) {
            return new AccountUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(userMail);
        dest.writeString(fullName);
        dest.writeString(role.rawValue);
        dest.writeString(status.rawValue);
        dest.writeString(phone);
        dest.writeString(address);
        dest.writeString(password);
        dest.writeString(avatar);
        dest.writeSerializable(avatarFile);
        dest.writeByte((byte) (isAuthenticated ? 1 : 0));
        dest.writeString(accessToken);
    }
}
