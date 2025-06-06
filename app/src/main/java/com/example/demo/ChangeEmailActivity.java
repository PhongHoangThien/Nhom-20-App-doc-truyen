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

public class ChangeEmailActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText etCurrentPassword;
    private EditText etNewEmail;
    private Button btnSave;

    private ProgressBar progressBar; // Khai báo ProgressBar
    private AuthenticationModel authModel; // Khai báo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        authModel = new AuthenticationModel(); // Khởi tạo AuthModel

        btnBack = findViewById(R.id.btnBack);
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewEmail = findViewById(R.id.etNewEmail);
        btnSave = findViewById(R.id.btnSave);
        progressBar = findViewById(R.id.progressBar); // Ánh xạ ProgressBar (bạn cần thêm ProgressBar vào layout)

        // Ẩn ProgressBar ban đầu
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Quay lại màn hình trước đó
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logic xử lý đổi email
                attemptChangeEmail();
            }
        });
    }

    private void attemptChangeEmail() {
        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newEmail = etNewEmail.getText().toString().trim();

        if (currentPassword.isEmpty()) {
            etCurrentPassword.setError("Mật khẩu hiện tại không được để trống.");
            etCurrentPassword.requestFocus();
            return;
        }
        if (newEmail.isEmpty()) {
            etNewEmail.setError("Email mới không được để trống.");
            etNewEmail.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            etNewEmail.setError("Email mới không hợp lệ.");
            etNewEmail.requestFocus();
            return;
        }

        // Lấy email hiện tại của người dùng đang đăng nhập từ Firebase Auth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null || currentUser.getEmail() == null) {
            Toast.makeText(this, "Không thể lấy thông tin email hiện tại. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            return;
        }
        String currentEmail = currentUser.getEmail();

        // Hiển thị ProgressBar (nếu có)
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        btnSave.setEnabled(false); // Vô hiệu hóa nút để tránh click nhiều lần

        authModel.reauthenticateAndUpdateEmail(currentEmail, currentPassword, newEmail, new AuthenticationModel.AuthCallback() {
            @Override
            public void onSuccess() {
                // Ẩn ProgressBar và bật lại nút
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                btnSave.setEnabled(true);

                Toast.makeText(ChangeEmailActivity.this, "Email đã được đổi thành công. Vui lòng kiểm tra email mới để xác minh.", Toast.LENGTH_LONG).show();
                finish(); // Đóng Activity sau khi đổi thành công
            }

            @Override
            public void onFailure(String errorMessage) {
                // Ẩn ProgressBar và bật lại nút
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                btnSave.setEnabled(true);

                Toast.makeText(ChangeEmailActivity.this, "Lỗi đổi email: " + errorMessage, Toast.LENGTH_LONG).show();
                // Gợi ý cho người dùng nếu lỗi là do xác thực lại
                if (errorMessage != null && errorMessage.contains("Xác thực lại thất bại")) {
                    Toast.makeText(ChangeEmailActivity.this, "Mật khẩu hiện tại không đúng hoặc cần đăng nhập lại.", Toast.LENGTH_LONG).show();
                } else if (errorMessage != null && errorMessage.contains("already in use")) {
                    Toast.makeText(ChangeEmailActivity.this, "Email này đã được sử dụng bởi tài khoản khác.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}