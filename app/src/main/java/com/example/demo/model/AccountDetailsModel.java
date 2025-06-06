package com.example.demo.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class AccountDetailsModel {

    private static final String PREFS_NAME = "user_account_prefs"; // Tên file SharedPreferences
    // Các KEY này sẽ được tiền tố bằng User ID
    private static final String SUFFIX_USER_NAME = "_user_name";
    private static final String SUFFIX_AVATAR_FILE_PATH = "_avatar_file_path";
    private String currentUserId; // Trường để lưu trữ User ID hiện tại

    private SharedPreferences sharedPreferences;
    private Context context; // Giữ lại Context để sử dụng cho openFileOutput/openFileInput

    // Constructor để nhận Context, cần thiết để truy cập SharedPreferences
    public AccountDetailsModel(Context context, String userId) {
        // Đảm bảo context được gán và không null
        // Sử dụng context trực tiếp hoặc context.getApplicationContext() là OK
        // Vấn đề có thể là bạn đã khai báo nhưng chưa gán
        this.context = context; // Sử dụng context được truyền vào trực tiếp
        // this.context = context.getApplicationContext(); // Hoặc cách này cũng được, nhưng hãy đảm bảo context ban đầu không null

        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.currentUserId = userId; // Gán User ID khi khởi tạo Model
        if (this.currentUserId == null || this.currentUserId.isEmpty()) {
            // Trường hợp người dùng chưa đăng nhập, sử dụng một ID mặc định hoặc ID khách
            this.currentUserId = "guest_user";
            Log.w("AccountModelDebug", "AccountDetailsModel: userId is null/empty, falling back to " + this.currentUserId);
        } else {
            Log.d("AccountModelDebug", "AccountDetailsModel: Initialized with userId: " + this.currentUserId);
        }
    }

    // Phương thức tạo khóa SharedPreferences động
    private String getUserSpecificKey(String suffix) {
        return currentUserId + suffix;
    }

    // Phương thức để lưu tên người dùng
    public void saveUserName(String userName) {
        sharedPreferences.edit().putString(SUFFIX_USER_NAME, userName).apply();
    }

    // Phương thức để đọc tên người dùng
    public String getUserName() {
        // Trả về "Người dùng" nếu chưa có tên nào được lưu
        return sharedPreferences.getString(SUFFIX_USER_NAME, "Người dùng");
    }

    // Phương thức để lưu URI ảnh đại diện
    // URI có thể là null nếu người dùng xóa ảnh hoặc chưa chọn ảnh nào
//    public void saveAvatarUri(Uri avatarUri) {
//        if (avatarUri != null) {
//            sharedPreferences.edit().putString(KEY_AVATAR_URI, avatarUri.toString()).apply();
//        } else {
//            // Nếu avatarUri là null, xóa đường dẫn ảnh cũ (nếu có)
//            sharedPreferences.edit().remove(KEY_AVATAR_URI).apply();
//        }
//    }

    /**
     * Lưu Bitmap ảnh đại diện vào bộ nhớ nội bộ của ứng dụng.
     * @param bitmap Bitmap của ảnh cần lưu.
     */
    public void saveAvatarBitmap(Bitmap bitmap) {
        // THÊM DÒNG KIỂM TRA NULL Ở ĐÂY để chắc chắn context không null trước khi dùng
        if (context == null) {
            Log.e("AccountModel", "Context is null in saveAvatarBitmap. Cannot save image.");
            return; // Thoát nếu context là null
        }

        File file = new File(context.getFilesDir(), "user_avatar.png"); // Lấy thư mục file nội bộ và tên file
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos); // Nén ảnh thành PNG
            fos.close();
            // Lưu đường dẫn tuyệt đối của file vào SharedPreferences
            sharedPreferences.edit().putString(SUFFIX_AVATAR_FILE_PATH, file.getAbsolutePath()).apply();
            Log.d("AccountModel", "Saved avatar to internal storage: " + file.getAbsolutePath());
        } catch (Exception e) {
            Log.e("AccountModel", "Error saving avatar bitmap: " + e.getMessage(), e);
        }
    }

    /**
     * Tải Bitmap ảnh đại diện từ bộ nhớ nội bộ của ứng dụng.
     * @return Bitmap của ảnh hoặc null nếu không tìm thấy/lỗi.
     */
    public Bitmap getAvatarBitmap() {
        // THÊM DÒNG KIỂM TRA NULL Ở ĐÂY
        if (context == null) {
            Log.e("AccountModel", "Context is null in getAvatarBitmap. Cannot load image.");
            return null;
        }

        String filePath = sharedPreferences.getString(SUFFIX_AVATAR_FILE_PATH, null);
        if (filePath != null) {
            File file = new File(filePath);
            if (file.exists()) {
                try {
                    FileInputStream fis = new FileInputStream(file);
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    fis.close();
                    Log.d("AccountModel", "Loaded avatar bitmap from internal storage: " + filePath);
                    return bitmap;
                } catch (Exception e) {
                    Log.e("AccountModel", "Error loading avatar bitmap: " + e.getMessage(), e);
                    // Nếu có lỗi, xóa đường dẫn đã lưu để tránh lỗi lặp lại
                    sharedPreferences.edit().remove(SUFFIX_AVATAR_FILE_PATH).apply();
                    return null;
                }
            } else {
                Log.d("AccountModel", "Avatar file not found at path: " + filePath);
                // Nếu file không tồn tại, xóa đường dẫn đã lưu
                sharedPreferences.edit().remove(SUFFIX_AVATAR_FILE_PATH).apply();
                return null;
            }
        }
        Log.d("AccountModel", "No avatar file path found in SharedPreferences.");
        return null;
    }

    // Phương thức để xóa dữ liệu người dùng khi họ đăng xuất (tùy chọn)
    public void clearUserData() {
        sharedPreferences.edit()
                .remove(getUserSpecificKey(SUFFIX_USER_NAME))
                .remove(getUserSpecificKey(SUFFIX_AVATAR_FILE_PATH))
                .apply();
        // Cũng có thể xóa file ảnh vật lý nếu bạn muốn tiết kiệm dung lượng
        String fileName = currentUserId + "_avatar.png";
        File file = new File(context.getFilesDir(), fileName);
        if (file.exists()) {
            file.delete();
            Log.d("AccountModel", "Deleted avatar file for " + currentUserId);
        }
        Log.d("AccountModel", "Cleared user data for " + currentUserId);
    }

//    // Phương thức để đọc URI ảnh đại diện
//    public Uri getAvatarUri() {
//        String uriString = sharedPreferences.getString(KEY_AVATAR_URI, null);
//        if (uriString != null) {
//            return Uri.parse(uriString);
//        }
//        return null; // Trả về null nếu chưa có ảnh nào được lưu
//    }

    // Có thể thêm các phương thức khác ở đây
    // public void saveGender(String gender) { ... }
    // public String getGender() { ... }
}
