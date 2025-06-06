package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.demo.model.AccountDetailsModel;
import com.example.demo.model.AuthenticationModel;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseAuth; // Để lấy UID nếu cần xóa dữ liệu cục bộ
import com.google.firebase.auth.FirebaseUser; // Để lấy email của user hiện tại

public class PersonalSettingsActivity extends AppCompatActivity {

    private ImageView btnBack;
    private LinearLayout optionChangePassword; // Khai báo
    private LinearLayout optionChangeEmail; // Giữ lại cho các giai đoạn sau
    private LinearLayout optionDeleteAccount; // Giữ lại cho các giai đoạn sau

    private AuthenticationModel authModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_settings);

        // if (authModel == null) { // Chỉ khởi tạo nếu chưa có
        //     authModel = new AuthModel();
        // }

        authModel = new AuthenticationModel(); // Khởi tạo AuthModel

        btnBack = findViewById(R.id.btnBack);
        optionChangePassword = findViewById(R.id.optionChangePassword); // Ánh xạ ID
        optionChangeEmail = findViewById(R.id.optionChangeEmail); // Ánh xạ ID
        optionDeleteAccount = findViewById(R.id.optionDeleteAccount); // Ánh xạ ID

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Quay lại màn hình trước đó
            }
        });

        optionChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalSettingsActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        // Giữ các OnClickListener rỗng hoặc không có gì cho các option còn lại tạm thời
        // Chúng ta sẽ thêm logic vào các giai đoạn sau
        optionChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sẽ điều hướng đến ChangeEmailActivity
                Intent intent = new Intent(PersonalSettingsActivity.this, ChangeEmailActivity.class);
                startActivity(intent);
            }
        });

        optionDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sẽ hiển thị cảnh báo
                showDeleteAccountConfirmationDialog(); // Gọi phương thức hiển thị dialog
            }
        });
    }

    // Phương thức để hiển thị hộp thoại xác nhận xóa tài khoản
    private void showDeleteAccountConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xóa tài khoản") // Tiêu đề của hộp thoại
                .setMessage("Bạn có chắc chắn muốn xóa tài khoản này? Hành động này không thể hoàn tác.") // Nội dung cảnh báo
                .setPositiveButton("Xóa", (dialog, which) -> {
                    // Logic xử lý khi người dùng nhấn "Xóa"
                    // (Sẽ được triển khai chi tiết ở Giai đoạn 5)
//                    Toast.makeText(PersonalSettingsActivity.this, "Đang thực hiện xóa tài khoản...", Toast.LENGTH_SHORT).show();
                    showReauthenticateDialogForDelete(); // Gọi dialog xác thực lại
                })
                .setNegativeButton("Hủy", (dialog, which) -> {
                    // Logic xử lý khi người dùng nhấn "Hủy" hoặc click ra ngoài
                    dialog.dismiss(); // Đóng hộp thoại
                    Toast.makeText(PersonalSettingsActivity.this, "Đã hủy xóa tài khoản.", Toast.LENGTH_SHORT).show();
                })
                .setIcon(android.R.drawable.ic_dialog_alert) // Thêm icon cảnh báo
                .show(); // Hiển thị hộp thoại
    }

    private void showReauthenticateDialogForDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận mật khẩu");
        builder.setMessage("Vui lòng nhập mật khẩu hiện tại của bạn để xác nhận xóa tài khoản.");

        // Thiết lập EditText để nhập mật khẩu
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
            String password = input.getText().toString().trim();
            if (password.isEmpty()) {
                Toast.makeText(PersonalSettingsActivity.this, "Mật khẩu không được để trống.", Toast.LENGTH_SHORT).show();
                return;
            }
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null && user.getEmail() != null) {
                String email = user.getEmail();
                performDeleteAccount(email, password); // Gọi hàm xóa với email và mật khẩu
            } else {
                Toast.makeText(PersonalSettingsActivity.this, "Không thể lấy thông tin người dùng. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> {
            dialog.cancel();
        });

        builder.show();
    }

    private void performDeleteAccount(String email, String password) {
        Toast.makeText(PersonalSettingsActivity.this, "Đang xử lý xóa tài khoản...", Toast.LENGTH_LONG).show();

        authModel.reauthenticateAndDeleteUser(email, password, new AuthenticationModel.AuthCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(PersonalSettingsActivity.this, "Tài khoản đã được xóa thành công.", Toast.LENGTH_SHORT).show();

                // Lấy UID của người dùng ĐÃ ĐĂNG NHẬP TRƯỚC KHI BỊ XÓA để xóa dữ liệu cục bộ
                // Quan trọng: getCurrentUser() có thể trả về null ngay sau khi xóa,
                // nên hãy lấy UID trước hoặc chuyển nó từ màn hình trước đó nếu cần.
                // Tuy nhiên, vì chúng ta đang reauthenticate, user object vẫn còn valid ở đây.
                String userIdToClear = FirebaseAuth.getInstance().getUid(); // Lấy UID trực tiếp sau khi reauth thành công

                if (userIdToClear != null) {
                    AccountDetailsModel tempModel = new AccountDetailsModel(PersonalSettingsActivity.this, userIdToClear);
                    tempModel.clearUserData(); // Gọi phương thức clearUserData từ AccountDetailsModel
                    Log.d("PersonalSettings", "Cleared local data for deleted user: " + userIdToClear);
                } else {
                    Log.w("PersonalSettings", "Could not get user ID to clear local data after deletion.");
                }

                // Chuyển hướng về màn hình đăng nhập
                Intent intent = new Intent(PersonalSettingsActivity.this, AuthActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // Đóng PersonalSettingsActivity
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(PersonalSettingsActivity.this, "Lỗi xóa tài khoản: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

}