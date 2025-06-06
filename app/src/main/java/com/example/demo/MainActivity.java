package com.example.demo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.model.AuthenticationModel;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // Test Save Link và mo link truyện
//    Button openLinkButton;
//    EditText linkInput;
//    Button saveButton;
//    TextView savedLinks;
//    SharedPreferences sharedPreferences;
//    static final String PREF_NAME = "SavedLinks";
//    static final String LINK_KEY = "link_list";

    // Test tính năng Authentication
//    private TextView textViewUserInfo;
//    private Button buttonLogout;
    private AuthenticationModel authModel; // Khai báo đối tượng Model



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LIFECYCLE", "onCreate được gọi");
        setContentView(R.layout.activity_main); // Tao Layout cho App

        // Authentication
        authModel = new AuthenticationModel(); // Khởi tạo Model

//        textViewUserInfo = findViewById(R.id.textViewUserInfo);
//        buttonLogout = findViewById(R.id.buttonLogout);
//
//        // Hiển thị thông tin người dùng nếu đã đăng nhập
//        FirebaseUser currentUser = authModel.getCurrentUser();
//        if (currentUser != null) {
//            String userInfo = "Đăng nhập với Email: " + currentUser.getEmail();
//            textViewUserInfo.setText(userInfo);
//        } else {
//            // Nếu không có người dùng đăng nhập, có thể chuyển hướng lại màn hình đăng nhập
//            // Trường hợp này có thể xảy ra nếu người dùng thoát ứng dụng hoặc trạng thái bị mất
//            navigateToAuthScreen();
//        }
//
//        buttonLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                authModel.signOut(); // Gọi phương thức đăng xuất trong Model
//                Toast.makeText(MainActivity.this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
//                navigateToAuthScreen();
//            }
//        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = authModel.getCurrentUser();
        if(currentUser != null){
            // Người dùng đã đăng nhập, chuyển đến AccountActivity
            navigateToAccountScreen();
        } else {
            // Người dùng chưa đăng nhập, chuyển đến AuthActivity
            // Nếu không có người dùng đăng nhập, có thể chuyển hướng lại màn hình đăng nhập
            // Trường hợp này có thể xảy ra nếu người dùng thoát ứng dụng hoặc trạng thái bị mất
            navigateToAuthScreen();
        }
    }

    private void navigateToAuthScreen() {
        Intent intent = new Intent(MainActivity.this, AuthActivity.class);
        startActivity(intent);
        finish(); // Đóng MainActivity để người dùng không quay lại được bằng nút Back
    }

    private void navigateToAccountScreen() {
        Intent intent = new Intent(MainActivity.this, AccountActivity.class); // Thay thế bằng AccountActivity
        startActivity(intent);
        finish(); // Đóng MainActivity để người dùng không quay lại được bằng nút Back
    }

}
