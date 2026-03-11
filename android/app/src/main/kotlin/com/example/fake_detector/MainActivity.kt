package com.example.fake_detector

import android.app.Activity
import android.content.Intent  // 다른 앱/화면을 실행할 때 사용
import android.net.Uri          // 파일 경로(URI) 처리
import io.flutter.embedding.android.FlutterActivity  // Flutter 기본 Activity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel  // Flutter ↔ Android 통신

class MainActivity : FlutterActivity() {
    // Flutter의 MethodChannel 이름과 반드시 동일해야 통신 가능
    private val CHANNEL = "file_picker_channel"
    // 파일 선택창 요청 코드 (아무 숫자나 가능, 결과 받을때 구분용)
    private val FILE_PICKER_REQUEST = 1001
    // Flutter로 결과를 돌려줄 때 사용하는 객체 (비동기 처리용)
    private var pendingResult: MethodChannel.Result? = null

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        // Flutter와 통신 채널 열기
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
            .setMethodCallHandler { call, result ->
                // Flutter에서 'pickFile' 신호가 왔을 때
                if (call.method == "pickFile") {
                    pendingResult = result  // 결과를 나중에 돌려주기 위해 저장

                    // Android 파일 선택창 열기 (내 파일/갤러리/카메라)
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "*/*"  // 모든 파일 타입 허용
                    // 파일 선택창 실행
                    startActivityForResult(intent, FILE_PICKER_REQUEST)
                }
            }
    }

    // 파일 선택창에서 파일 선택 완료 후 호출되는 함수
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 우리가 열었던 파일 선택창의 결과인지 확인
        if (requestCode == FILE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                // 파일 선택됨 → 파일 경로(URI)를 Flutter로 반환
                val uri: Uri? = data.data
                pendingResult?.success(listOf(uri.toString()))
            } else {
                // 파일 선택 취소됨 → 빈 리스트 반환
                pendingResult?.success(listOf<String>())
            }
            pendingResult = null  // 사용 완료 후 초기화
        }
    }
}