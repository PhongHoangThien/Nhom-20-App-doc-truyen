package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.demo.model.AuthenticationModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText etCurrentPassword;
    private EditText etNewPassword;
    private EditText etConfirmNewPassword;
    private Button btnSave;

    private ProgressBar progressBar; // Khai báo ProgressBar

    private AuthenticationModel authModel; // Khai báo AuthModel

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        authModel = new AuthenticationModel(); // Khởi tạo AuthModel

        btnBack = findViewById(R.id.btnBack);
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword);
        btnSave = findViewById(R.id.btnSave);

        progressBar = findViewById(R.id.progressBar); // Ánh xạ ProgressBar (bạn cần thêm ProgressBar vào layout)


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Quay lại màn hình trước đó
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logic xử lý đổi mật khẩu sẽ được thêm vào ở Giai đoạn 7
                attemptChangePassword();
            }
        });
    }

    private void attemptChangePassword() {
        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmNewPassword = etConfirmNewPassword.getText().toString().trim();

        if (currentPassword.isEmpty()) {
            etCurrentPassword.setError("Mật khẩu hiện tại không được để trống.");
            etCurrentPassword.requestFocus();
            return;
        }
        if (newPassword.isEmpty()) {
            etNewPassword.setError("Mật khẩu mới không được để trống.");
            etNewPassword.requestFocus();
            return;
        }
        if (confirmNewPassword.isEmpty()) {
            etConfirmNewPassword.setError("Xác nhận mật khẩu mới không được để trống.");
            etConfirmNewPassword.requestFocus();
            return;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            etConfirmNewPassword.setError("Mật khẩu xác nhận không khớp.");
            etConfirmNewPassword.requestFocus();
            return;
        }
        if (newPassword.length() < 6) { // Firebase yêu cầu mật khẩu tối thiểu 6 ký tự
            etNewPassword.setError("Mật khẩu mới phải có ít nhất 6 ký tự.");
            etNewPassword.requestFocus();
            return;
        }

        // Lấy email hiện tại của người dùng đang đăng nhập từ Firebase Auth
        // Chúng ta cần email để tạo AuthCredential cho reauthenticate
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null || currentUser.getEmail() == null) {
            Toast.makeText(this, "Không thể lấy thông tin email hiện tại. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            return;
        }
        String email = currentUser.getEmail();

        // Hiển thị ProgressBar (nếu có)
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        btnSave.setEnabled(false); // Vô hiệu hóa nút để tránh click nhiều lần

        authModel.reauthenticateAndUpdatePassword(email, currentPassword, newPassword, new AuthenticationModel.AuthCallback() {
            @Override
            public void onSuccess() {
                // Ẩn ProgressBar và bật lại nút
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                btnSave.setEnabled(true);

                Toast.makeText(ChangePasswordActivity.this, "Mật khẩu đã được đổi thành công.", Toast.LENGTH_LONG).show();
                finish(); // Đóng Activity sau khi đổi thành công
            }

            @Override
            public void onFailure(String errorMessage) {
                // Ẩn ProgressBar và bật lại nút
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                btnSave.setEnabled(true);

                Toast.makeText(ChangePasswordActivity.this, "Lỗi đổi mật khẩu: " + errorMessage, Toast.LENGTH_LONG).show();
                // Gợi ý cho người dùng nếu lỗi là do xác thực lại
                if (errorMessage != null && errorMessage.contains("Xác thực lại thất bại")) {
                    Toast.makeText(ChangePasswordActivity.this, "Mật khẩu hiện tại không đúng hoặc cần đăng nhập lại.", Toast.LENGTH_LONG).show();
                } else if (errorMessage != null && errorMessage.contains("less than 6 characters")) {
                    Toast.makeText(ChangePasswordActivity.this, "Mật khẩu mới phải có ít nhất 6 ký tự.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}