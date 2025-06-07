package com.example.demo.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 建立 Server 與 Client 在傳遞資料上的統一結構與標準(含錯誤)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
	private int status;     // 狀態 例如: 200, 400
	private String message; // 訊息 例如: 查詢成功, 新增成功, 請求錯誤
	private T data; 	    // payload 實際資料
	private LocalDateTime timestamp; // 時間戳記
    private String errorCode;       // 錯誤代碼，若有錯誤，可給出更具體的錯誤類型或代碼
	
	// 成功回應
	public static <T> ApiResponse<T> success(String message, T data) {
		return new ApiResponse<T>(200, message, data, LocalDateTime.now(), null);
	}
	
	// 失敗回應+代碼
	public static <T> ApiResponse<T> error(int status, String message, String errorCode) {
		return new ApiResponse<T>(status, message, null, LocalDateTime.now(), errorCode);
	}
	
	// 失敗回應 (不帶代碼)
    public static <T> ApiResponse<T> error(int status, String message) {
        return new ApiResponse<T>(status, message, null, LocalDateTime.now(), null);
    }
	
}