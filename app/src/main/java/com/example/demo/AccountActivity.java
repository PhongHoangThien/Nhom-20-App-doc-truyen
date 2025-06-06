package com.example.demo; // Đảm bảo đúng tên gói của bạn

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.model.AccountDetailsModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.demo.model.AuthenticationModel; // Nếu bạn muốn dùng model để sign out

public class AccountActivity extends AppCompatActivity {

    private TextView textViewUserName;
    private TextView optionSignOut; // Nút thoát
    private AuthenticationModel authModel; // Khai báo model
    private BottomNavigationView bottomNavigationView; // Khai báo BottomNavigationView
    private TextView optionAccountDetails; //
    private AccountDetailsModel accountDetailsModel; // Khai báo đối tượng Model
    private TextView optionPersonalSettings; // Khai báo


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        textViewUserName = findViewById(R.id.textViewUserName);
        optionSignOut = findViewById(R.id.optionSignOut);
        bottomNavigationView = findViewById(R.id.bottomNavigationView); // Ánh xạ BottomNavigationView
        optionAccountDetails = findViewById(R.id.optionAccountDetails); //

        authModel = new AuthenticationModel(); // Khởi tạo model

        // **************************************************************************
        // THÊM DÒNG NÀY ĐỂ ĐẶT TRẠNG THÁI CHỌN CHO BOTTOMNAVIGATIONVIEW
        bottomNavigationView.setSelectedItemId(R.id.nav_account); //
        // **************************************************************************

        // Hiển thị tên người dùng
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userName = user.getDisplayName(); // Lấy tên hiển thị từ Google/Email
            String userEmail = user.getEmail(); // Lấy email

            if (userName != null && !userName.isEmpty()) {
                textViewUserName.setText(userName);
            } else if (userEmail != null && !userEmail.isEmpty()) {
                textViewUserName.setText(userEmail); // Nếu không có tên hiển thị, dùng email
            } else {
                textViewUserName.setText("Người dùng"); // Tên mặc định
            }
        } else {
            // Trường hợp này không nên xảy ra nếu đã đăng nhập thành công
            textViewUserName.setText("Khách");
            Toast.makeText(this, "Không tìm thấy thông tin người dùng.", Toast.LENGTH_SHORT).show();
            // Có thể chuyển hướng lại màn hình đăng nhập nếu không có người dùng
            // Intent intent = new Intent(AccountActivity.this, AuthActivity.class);
            // startActivity(intent);
            // finish();
        }

        optionPersonalSettings = findViewById(R.id.optionPersonalInfo); // Ánh xạ ID

        optionPersonalSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, PersonalSettingsActivity.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện khi nhấn nút "Thoát" (Sign Out)
        optionSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy UID trước khi đăng xuất
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userIdToClear = null;
                if (user != null) {
                    userIdToClear = user.getUid();
                }

                authModel.signOut(); // Gọi phương thức signOut từ model
                Toast.makeText(AccountActivity.this, "Đã đăng xuất.", Toast.LENGTH_SHORT).show();

                // Xóa dữ liệu của người dùng đã đăng xuất khỏi bộ nhớ cục bộ
                // Nếu có UID của người dùng đã đăng xuất, thì xóa dữ liệu của họ
                if (userIdToClear != null) {
                    AccountDetailsModel tempModel = new AccountDetailsModel(AccountActivity.this, userIdToClear);
                    tempModel.clearUserData();
                    Log.d("AccountActivity", "Cleared data for user: " + userIdToClear);
                } else {
                    Log.d("AccountActivity", "No user to clear data for (was already logged out?).");
                }

                // Chuyển hướng về màn hình đăng nhập sau khi đăng xuất
                Intent intent = new Intent(AccountActivity.this, AuthActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Xóa hết các activity trên stack
                startActivity(intent);
                finish();
            }
        });

        // **************************************************************************
        // THÊM LISTENER CHO "THÔNG TIN TÀI KHOẢN"
        optionAccountDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, AccountDetailsActivity.class);
                startActivity(intent);
            }
        });
        // **************************************************************************

        // **************************************************************************
        // THÊM LISTENER NÀY ĐỂ XỬ LÝ CHUYỂN MÀN HÌNH KHI NHẤP VÀO CÁC ITEM
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // Nếu đã ở AccountActivity và muốn về Home, hãy đảm bảo rằng
                // mình đã không tạo một MainActivity mới nếu nó đã có trong back stack.
                // Một cách tốt hơn là sử dụng singleTop hoặc clear task.
                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish(); // Kết thúc AccountActivity nếu bạn muốn quay lại MainActivity
                return true;
            } else if (itemId == R.id.nav_explore) {
                // Bạn đã ở ExploreActivity, không làm gì cả
                return true;
            } else if (itemId == R.id.nav_rank) {
                // Bạn đã ở RankActivity, không làm gì cả
                return true;
            } else if (itemId == R.id.nav_account) {
                // Bạn đã ở AccountActivity, không làm gì cả
                return true;
            }
            // Thêm các trường hợp khác cho các item khác (e.g., nav_settings)
            return false;
        });
        // **************************************************************************

    }
}