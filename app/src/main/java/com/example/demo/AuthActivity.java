package com.example.demo;

import androidx.activity.result.ActivityResult; // THÊM IMPORT NÀY
import androidx.activity.result.ActivityResultCallback; // THÊM IMPORT NÀY
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts; // THÊM IMPORT NÀY
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn; // THÊM IMPORT NÀY
import com.google.android.gms.auth.api.signin.GoogleSignInAccount; // THÊM IMPORT NÀY
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException; // THÊM IMPORT NÀY
import com.google.android.gms.tasks.Task; // THÊM IMPORT NÀY
import com.google.firebase.auth.AuthCredential; // THÊM IMPORT NÀY
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider; // THÊM IMPORT NÀY
import com.example.demo.model.AuthenticationModel; // Import lớp Model của bạn

public class AuthActivity extends AppCompatActivity implements AuthenticationModel.AuthListener {

    private AuthenticationModel authModel; // Khai báo đối tượng Model

    private EditText editTextEmail, editTextPassword;
    private Button buttonRegister, buttonLogin;
    private TextView textViewForgotPassword; // Khai báo TextView mới


    // KHAI BÁO CÁC Biến dùng trong quá trình Đăng Nhập bằng Google
    private SignInButton googleSignInButton; // KHAI BÁO Biến Button
    private GoogleSignInClient mGoogleSignInClient; // KHAI BÁO Biến
    private ActivityResultLauncher<Intent> signInLauncher; // KHAI BÁO Biến (Cách xử lý onActivityResult mới)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Khởi tạo Model
        authModel = new AuthenticationModel();

        // Ánh xạ các View từ layout (View components)
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword); // Ánh xạ TextView mới thêm
        googleSignInButton = findViewById(R.id.googleSignInButton); // ÁNH XẠ BUTTON GOOGLE SIGNIN

        // --- Cấu hình Google Sign-In Options (GSO) ---
        // GSO là cấu hình cho Google Sign-In, cho biết bạn muốn lấy gì từ Google
        // DEFAULT_SIGN_IN: Yêu cầu thông tin cơ bản về profile và email
        // requestIdToken: Yêu cầu một ID Token. ĐÂY LÀ PHẦN QUAN TRỌNG NHẤT VỚI FIREBASE.
        //                 Nó cho phép Firebase xác thực người dùng Google.
        //                 Giá trị R.string.default_web_client_id được tạo tự động khi bạn thêm google-services.json
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // R.string.default_web_client_id là rất quan trọng!
                .requestEmail() // Yêu cầu email của người dùng
                .build();

        // Xây dựng GoogleSignInClient với GSO đã cấu hình
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // --- Khởi tạo ActivityResultLauncher ---
        // Đây là cách mới và an toàn hơn để xử lý kết quả từ một Activity khác (như màn hình đăng nhập Google)
        signInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) { // Nếu quá trình đăng nhập Google thành công
                            Intent data = result.getData();
                            // Lấy thông tin tài khoản Google từ kết quả Intent
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                            handleGoogleSignInResult(task); // Chuyển kết quả để xử lý
                        } else {
                            // Người dùng đã hủy đăng nhập Google hoặc có lỗi khác
                            Toast.makeText(AuthActivity.this, "Đăng nhập Google bị hủy hoặc có lỗi.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Xử lý sự kiện khi nhấn nút Đăng ký (Controller nhận input từ View)
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegisterClick();
            }
        });

        // Xử lý sự kiện khi nhấn nút Đăng nhập (Controller nhận input từ View)
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLoginClick();
            }
        });

        // Xử lý sự kiện khi nhấn "Quên mật khẩu?" (MỚI THÊM)
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleForgotPasswordClick();
            }
        });

        // --- Xử lý sự kiện khi nhấn nút Đăng nhập Google (THÊM LẮNG NGHE SỰ KIỆN NÀY) ---
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle(); // Gọi phương thức bắt đầu quá trình Google Sign-In
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Kiểm tra trạng thái đăng nhập khi Activity khởi động
        FirebaseUser currentUser = authModel.getCurrentUser();
        if(currentUser != null){
            // Nếu đã đăng nhập, chuyển hướng ngay lập tức
            navigateToMainScreen();
        }
    }

    // Phương thức xử lý logic khi nhấn nút Đăng ký
    private void handleRegisterClick() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (!validateInput(email, password)) {
            return;
        }

        // Gọi phương thức đăng ký trong Model (Controller yêu cầu Model thực hiện nghiệp vụ)
        authModel.registerUser(email, password, this); // 'this' là AuthActivity implement AuthListener
    }

    // Phương thức xử lý logic khi nhấn nút Đăng nhập
    private void handleLoginClick() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (!validateInput(email, password)) {
            return;
        }

        // Gọi phương thức đăng nhập trong Model
        authModel.loginUser(email, password, this);
    }

    // Phương thức kiểm tra dữ liệu đầu vào (một phần của Controller logic)
    private boolean validateInput(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Vui lòng nhập Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập Mật khẩu", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6) { // Firebase yêu cầu mật khẩu tối thiểu 6 ký tự
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Phương thức xử lý khi nhấn "Quên mật khẩu?" (MỚI)
    private void handleForgotPasswordClick() {
        String email = editTextEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Vui lòng nhập Email để đặt lại mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gọi phương thức gửi email đặt lại mật khẩu trong Model
        authModel.sendPasswordResetEmail(email, this);
    }

    // --- Phương thức BẮT ĐẦU quy trình Google Sign-In (THÊM PHƯƠNG THỨC NÀY) ---
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        signInLauncher.launch(signInIntent); // Khởi chạy màn hình chọn tài khoản Google
    }

    // --- Phương thức XỬ LÝ KẾT QUẢ sau khi người dùng chọn tài khoản Google (THÊM PHƯƠNG THỨC NÀY) ---
    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            // Lấy tài khoản Google đã đăng nhập thành công
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Quan trọng: Sử dụng ID Token của tài khoản Google để xác thực với Firebase
            if (account != null && account.getIdToken() != null) {
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                // Yêu cầu Model thực hiện xác thực với Firebase bằng Google Credential
                authModel.signInWithGoogleCredential(credential, this);
            } else {
                Toast.makeText(this, "Không thể lấy ID Token Google.", Toast.LENGTH_SHORT).show();
            }
        } catch (ApiException e) {
            // Xử lý lỗi nếu đăng nhập Google thất bại (ví dụ: người dùng hủy, lỗi mạng)
            Toast.makeText(this, "Đăng nhập Google thất bại: " + e.getStatusCode() + " " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // --- Triển khai Interface AuthListener (Controller nhận phản hồi từ Model) ---
    @Override
    public void onAuthSuccess(FirebaseUser user) {
        // Cập nhật View (hiển thị Toast và chuyển màn hình)
        Toast.makeText(AuthActivity.this, "Xác thực thành công!", Toast.LENGTH_SHORT).show();
        navigateToMainScreen();
    }

    @Override
    public void onAuthFailure(String errorMessage) {
        // Cập nhật View (hiển thị Toast lỗi)
        Toast.makeText(AuthActivity.this, "Xác thực thất bại: " + errorMessage, Toast.LENGTH_LONG).show();
    }

    // Phương thức chuyển sang màn hình chính
    private void navigateToMainScreen() {
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Đóng AuthActivity
    }

    //
    @Override
    public void onPasswordResetEmailSent(String email) { // MỚI
        Toast.makeText(AuthActivity.this, "Email đặt lại mật khẩu đã được gửi tới " + email + ". Vui lòng kiểm tra hộp thư đến của bạn.", Toast.LENGTH_LONG).show();
    }

    //
    @Override
    public void onPasswordResetEmailFailure(String errorMessage) { // MỚI
        Toast.makeText(AuthActivity.this, "Không thể gửi email đặt lại mật khẩu: " + errorMessage, Toast.LENGTH_LONG).show();
    }
}
