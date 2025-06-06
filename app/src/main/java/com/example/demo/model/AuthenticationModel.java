package com.example.demo.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * tạo một lớp Java riêng biệt tên là AuthenticationModel
 * để chứa tất cả logic liên quan đến Firebase Authentication.
 */
public class AuthenticationModel {

    private FirebaseAuth mAuth;

    // Interface để báo cáo kết quả về Controller
    public interface AuthListener {
        void onAuthSuccess(FirebaseUser user);
        void onAuthFailure(String errorMessage);
        void onPasswordResetEmailSent(String email); // Thêm phương thức này để xử lý kết quả của việc đặt lại mật khẩu.
        void onPasswordResetEmailFailure(String errorMessage); // Thêm phương thức này để xử lý kết quả của việc đặt lại mật khẩu.
    }

    public AuthenticationModel() {
        mAuth = FirebaseAuth.getInstance();
    }

    // Callback interface để thông báo kết quả về Controller (Activity)
    public interface AuthCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    // Phương thức kiểm tra trạng thái đăng nhập hiện tại
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    // Phương thức đăng ký người dùng
    public void registerUser(String email, String password, final AuthListener listener) {
        mAuth.createUserWithEmailAndPassword(email, password) // Gọi Firebase SDK yêu cầu Tạo Tài Khoản
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            listener.onAuthSuccess(user); // Báo cáo thành công
                        } else {
                            listener.onAuthFailure(task.getException().getMessage()); // Báo cáo thất bại
                        }
                    }
                });
    }

    // Phương thức đăng nhập người dùng
    public void loginUser(String email, String password, final AuthListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            listener.onAuthSuccess(user); // Báo cáo thành công
                        } else {
                            listener.onAuthFailure(task.getException().getMessage()); // Báo cáo thất bại
                        }
                    }
                });
    }

    // Phương thức gửi email đặt lại mật khẩu (MỚI THÊM VÔ)
    public void sendPasswordResetEmail(String email, final AuthListener listener) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            listener.onPasswordResetEmailSent(email);
                        } else {
                            listener.onPasswordResetEmailFailure(task.getException().getMessage());
                        }
                    }
                });
    }

    // Phương thức đăng nhập bằng Google Credential
    public void signInWithGoogleCredential(AuthCredential credential, final AuthListener listener) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            listener.onAuthSuccess(user); // Báo cáo thành công về Controller
                        } else {
                            listener.onAuthFailure(task.getException().getMessage()); // Báo cáo thất bại
                        }
                    }
                });
    }

    // Phương thức đăng xuất
    public void signOut() {
        mAuth.signOut();
    }

    // Phương thức xóa tài khoản người dùng hiện tại
//    public void deleteUser(AuthCallback callback) {
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user == null) {
//            callback.onFailure("Không có người dùng nào đăng nhập.");
//            Log.e("AuthModel", "Delete user failed: No user logged in.");
//            return;
//        }
//
//        user.delete()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        callback.onSuccess();
//                        Log.d("AuthModel", "User account deleted successfully.");
//                    } else {
//                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Lỗi không xác định khi xóa tài khoản.";
//                        callback.onFailure(errorMessage);
//                        Log.e("AuthModel", "Failed to delete user account: " + errorMessage, task.getException());
//                    }
//                });
//    }

    // Phương thức xóa tài khoản có yêu cầu xác thực lại
    public void reauthenticateAndDeleteUser(String email, String password, AuthCallback callback) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            callback.onFailure("Không có người dùng nào đăng nhập.");
            Log.e("AuthModel", "Reauthenticate and delete user failed: No user logged in.");
            return;
        }

        // Tạo AuthCredential từ email và mật khẩu hiện tại
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        // Bước 1: Xác thực lại người dùng
        user.reauthenticate(credential)
                .addOnCompleteListener(reauthTask -> {
                    if (reauthTask.isSuccessful()) {
                        Log.d("AuthModel", "User re-authenticated successfully.");
                        // Bước 2: Nếu xác thực lại thành công, tiến hành xóa tài khoản
                        user.delete()
                                .addOnCompleteListener(deleteTask -> {
                                    if (deleteTask.isSuccessful()) {
                                        callback.onSuccess();
                                        Log.d("AuthModel", "User account deleted successfully after reauthentication.");
                                    } else {
                                        String errorMessage = deleteTask.getException() != null ? deleteTask.getException().getMessage() : "Lỗi không xác định khi xóa tài khoản sau xác thực lại.";
                                        callback.onFailure(errorMessage);
                                        Log.e("AuthModel", "Failed to delete user account after reauthentication: " + errorMessage, deleteTask.getException());
                                    }
                                });
                    } else {
                        String errorMessage = reauthTask.getException() != null ? reauthTask.getException().getMessage() : "Lỗi xác thực lại.";
                        callback.onFailure("Xác thực lại thất bại: " + errorMessage);
                        Log.e("AuthModel", "Reauthentication failed: " + errorMessage, reauthTask.getException());
                    }
                });
    }

    // Phương thức đổi email có yêu cầu xác thực lại
    public void reauthenticateAndUpdateEmail(String currentEmail, String currentPassword, String newEmail, AuthCallback callback) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            callback.onFailure("Không có người dùng nào đăng nhập.");
            Log.e("AuthModel", "Reauthenticate and update email failed: No user logged in.");
            return;
        }

        // Tạo AuthCredential từ email và mật khẩu hiện tại
        AuthCredential credential = EmailAuthProvider.getCredential(currentEmail, currentPassword);

        // Bước 1: Xác thực lại người dùng
        user.reauthenticate(credential)
                .addOnCompleteListener(reauthTask -> {
                    if (reauthTask.isSuccessful()) {
                        Log.d("AuthModel", "User re-authenticated successfully for email update.");
                        // Bước 2: Nếu xác thực lại thành công, tiến hành cập nhật email
                        user.updateEmail(newEmail)
                                .addOnCompleteListener(updateEmailTask -> {
                                    if (updateEmailTask.isSuccessful()) {
                                        Log.d("AuthModel", "User email updated successfully.");
                                        // Tùy chọn: Gửi email xác minh cho email mới (rất khuyến khích)
//                                        user.sendEmailVerification()
//                                                .addOnCompleteListener(verificationTask -> {
//                                                    if (verificationTask.isSuccessful()) {
//                                                        Log.d("AuthModel", "Verification email sent to new email address.");
//                                                    } else {
//                                                        Log.e("AuthModel", "Failed to send verification email to new address.", verificationTask.getException());
//                                                    }
//                                                });
                                        callback.onSuccess();
                                    } else {
                                        String errorMessage = updateEmailTask.getException() != null ? updateEmailTask.getException().getMessage() : "Lỗi không xác định khi đổi email.";
                                        callback.onFailure(errorMessage);
                                        Log.e("AuthModel", "Failed to update user email: " + errorMessage, updateEmailTask.getException());
                                    }
                                });
                    } else {
                        String errorMessage = reauthTask.getException() != null ? reauthTask.getException().getMessage() : "Lỗi xác thực lại.";
                        callback.onFailure("Xác thực lại thất bại: " + errorMessage);
                        Log.e("AuthModel", "Reauthentication failed for email update: " + errorMessage, reauthTask.getException());
                    }
                });
    }

    // Phương thức đổi mật khẩu có yêu cầu xác thực lại
    public void reauthenticateAndUpdatePassword(String email, String currentPassword, String newPassword, AuthCallback callback) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            callback.onFailure("Không có người dùng nào đăng nhập.");
            Log.e("AuthModel", "Reauthenticate and update password failed: No user logged in.");
            return;
        }

        // Tạo AuthCredential từ email và mật khẩu hiện tại
        AuthCredential credential = EmailAuthProvider.getCredential(email, currentPassword);

        // Bước 1: Xác thực lại người dùng
        user.reauthenticate(credential)
                .addOnCompleteListener(reauthTask -> {
                    if (reauthTask.isSuccessful()) {
                        Log.d("AuthModel", "User re-authenticated successfully for password update.");
                        // Bước 2: Nếu xác thực lại thành công, tiến hành cập nhật mật khẩu
                        user.updatePassword(newPassword)
                                .addOnCompleteListener(updatePasswordTask -> {
                                    if (updatePasswordTask.isSuccessful()) {
                                        Log.d("AuthModel", "User password updated successfully.");
                                        callback.onSuccess();
                                    } else {
                                        String errorMessage = updatePasswordTask.getException() != null ? updatePasswordTask.getException().getMessage() : "Lỗi không xác định khi đổi mật khẩu.";
                                        callback.onFailure(errorMessage);
                                        Log.e("AuthModel", "Failed to update user password: " + errorMessage, updatePasswordTask.getException());
                                    }
                                });
                    } else {
                        String errorMessage = reauthTask.getException() != null ? reauthTask.getException().getMessage() : "Lỗi xác thực lại.";
                        callback.onFailure("Xác thực lại thất bại: " + errorMessage);
                        Log.e("AuthModel", "Reauthentication failed for password update: " + errorMessage, reauthTask.getException());
                    }
                });
    }
}
