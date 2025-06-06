package com.example.demo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.demo.model.AccountDetailsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountDetailsActivity extends AppCompatActivity {

    private ImageView btnBack;
    private ImageView imgAvatar;
    private ImageView imgCameraIcon; // Biểu tượng máy ảnh nằm trên avatar
    private EditText etUserName; // Đã đổi từ TextView thành EditText
    private Button btnSave;

    // ActivityResultLauncher để chọn ảnh từ thư viện
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private AccountDetailsModel accountDetailsModel; // Khai báo đối tượng Model

    private String currentUserId; // Để lưu trữ User ID hiện tại


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        // Lấy thông tin người dùng hiện tại từ Firebase Auth
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUserId = user.getUid();
            Log.d("AccountDetailsDebug", "onCreate: User is logged in. UID: " + currentUserId);
        } else {
            // Trường hợp người dùng chưa đăng nhập (khách).
            // Sử dụng một ID mặc định để lưu trữ dữ liệu "khách" nếu cần,
            // hoặc không cho phép chỉnh sửa thông tin nếu chưa đăng nhập.
            currentUserId = "guest_session"; // ID tạm thời cho người dùng khách
            Log.d("AccountDetailsDebug", "onCreate: No Firebase user logged in. Using ID: " + currentUserId);
        }

        // Khởi tạo Model
        accountDetailsModel = new AccountDetailsModel(this, currentUserId); // 'this' là Context của Activity

        btnBack = findViewById(R.id.btnBack);
        imgAvatar = findViewById(R.id.imgAvatar);
        imgCameraIcon = findViewById(R.id.imgCamera); // Đảm bảo ID này đúng trong XML
        etUserName = findViewById(R.id.etUserName); // ID mới cho EditText
        btnSave = findViewById(R.id.btnSave);

        // Khởi tạo ActivityResultLauncher
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            try {
                                // Đọc Bitmap từ Uri
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                                imgAvatar.setImageBitmap(bitmap); // Hiển thị ảnh ngay lập tức
                                accountDetailsModel.saveAvatarBitmap(bitmap); // LƯU BITMAP VÀO MODEL
                                Log.d("AccountDetails", "Ảnh đã chọn và lưu Bitmap thành công.");
                            } catch (Exception e) {
                                Log.e("AccountDetails", "Lỗi khi xử lý bitmap từ Uri: " + e.getMessage(), e);
                                Toast.makeText(AccountDetailsActivity.this, "Lỗi khi đọc ảnh.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.w("AccountDetails", "selectedImageUri là null sau khi chọn ảnh.");
                            Toast.makeText(AccountDetailsActivity.this, "Không chọn ảnh nào", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("AccountDetails", "Không nhận được kết quả chọn ảnh hợp lệ.");
                        Toast.makeText(AccountDetailsActivity.this, "Không chọn ảnh nào", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Đặt lắng nghe sự kiện click cho cả avatar và icon camera
        View.OnClickListener selectImageListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImageLauncher.launch(galleryIntent);
            }
        };

        imgAvatar.setOnClickListener(selectImageListener);
        imgCameraIcon.setOnClickListener(selectImageListener);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng Activity hiện tại và quay lại Activity trước
            }
        });

        // Tải thông tin người dùng và ảnh khi Activity được tạo
        loadAccountDetails();

        // Xử lý sự kiện click cho nút "Lưu"
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUserName = etUserName.getText().toString().trim();

                if (newUserName.isEmpty()) {
                    Toast.makeText(AccountDetailsActivity.this, "Tên người dùng không được để trống.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lưu tên người dùng thông qua Model
                accountDetailsModel.saveUserName(newUserName);

                Toast.makeText(AccountDetailsActivity.this, "Thông tin đã được lưu!", Toast.LENGTH_SHORT).show();
                // Cập nhật tên người dùng trong AccountActivity (nếu muốn)
                // if (user != null) {
                //    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                //            .setDisplayName(newUserName)
                //            .build();
                //    user.updateProfile(profileUpdates)
                //            .addOnCompleteListener(task -> {
                //                if (task.isSuccessful()) {
                //                    Log.d("AccountDetails", "Firebase user profile updated.");
                //                }
                //            });
                // };
            }
        });


    }

    // Phương thức để tải thông tin từ Model và hiển thị lên View
    private void loadAccountDetails() {
        // Nếu user ID là null (trường hợp khách), không cố gắng tải dữ liệu cụ thể của user
        // Hoặc bạn có thể hiển thị thông báo lỗi hoặc yêu cầu đăng nhập.
        if (currentUserId == null || currentUserId.equals("guest_session")) {
            etUserName.setText("Khách"); // Hiển thị tên mặc định cho khách
            imgAvatar.setImageResource(R.drawable.ic_person); // Ảnh mặc định cho khách
            Log.d("AccountDetails", "Hiển thị thông tin mặc định cho người dùng khách.");
            return;
        }

        String userName = accountDetailsModel.getUserName();
        etUserName.setText(userName);
        Log.d("AccountDetails", "Tên người dùng đã tải: " + userName);

        Bitmap avatarBitmap = accountDetailsModel.getAvatarBitmap(); // TẢI BITMAP TỪ MODEL
        if (avatarBitmap  != null) {
            imgAvatar.setImageBitmap(avatarBitmap); // Hiển thị ảnh bằng Bitmap
            Log.d("AccountDetails", "Ảnh Bitmap đã tải và hiển thị.");
        } else {
            // Nếu không có ảnh nào được lưu, hiển thị icon mặc định
            imgAvatar.setImageResource(R.drawable.ic_person); // Đảm bảo bạn có ic_person.xml hoặc .png
            Log.d("AccountDetails", "Không có ảnh Bitmap được lưu, hiển thị mặc định.");
        }
    }
}
