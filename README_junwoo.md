# Fake Detector Android App

## 개발 환경

| 항목 | 내용 |
|------|------|
| OS | Ubuntu 22.04 (SSH 서버) |
| 서버 | DASH 서버 (연구실 SSH 서버) |
| 에디터 | VSCode (Remote SSH 연결) |
| Flutter | 3.41.3 (stable) |
| Dart | 3.11.1 |
| Java | OpenJDK 17 (conda-forge) |
| Android SDK | 34 / 36 |
| Build Tools | 28.0.3 / 34.0.0 / 35.0.0 |

---

## 사전 준비

### 1. Flutter SDK 설치
- `git clone` 으로 홈 디렉토리에 설치
- `~/.bashrc` 에 PATH 추가

### 2. Android SDK 설치
- Google 공식 `cmdline-tools` ZIP 다운로드 후 압축 해제
- `~/Android/sdk/cmdline-tools/latest/` 경로에 배치
- `~/.bashrc` 에 `ANDROID_HOME` 및 PATH 추가

### 3. Java 설치
- `sudo` 사용 불가 환경이므로 `conda` 로 설치
- `conda install -c conda-forge openjdk=17`

### 4. Android 플랫폼 설치
- `sdkmanager` 로 `platform-tools`, `platforms;android-34/36`, `build-tools` 설치
- `flutter doctor --android-licenses` 로 라이선스 전체 동의

### 5. 환경 확인
- `flutter doctor` 로 Android toolchain  확인
- Chrome / Linux toolchain 오류는 Android APK 빌드와 무관하므로 무시

---

## 프로젝트 구조

```
fake_detector/
├── lib/
│   └── main.dart                  # 앱 핵심 코드 (WebView + 파일 업로드)
├── android/
│   └── app/
│       └── src/main/
│           ├── AndroidManifest.xml         # 앱 권한 및 설정
│           ├── kotlin/.../MainActivity.kt  # 파일 선택창 네이티브 코드
│           └── res/xml/
│               └── network_security_config.xml  # SSL 인증서 설정
├── pubspec.yaml                   # 패키지 의존성 관리
└── build/
    └── app/outputs/flutter-apk/
        └── app-release.apk        # 빌드 결과물
```

---

## 주요 패키지

| 패키지 | 용도 |
|--------|------|
| `webview_flutter` | 앱 안에서 웹사이트 표시 |
| `webview_flutter_android` | Android 전용 WebView 설정 (파일 업로드 등) |

---

## APK 빌드 명령어

```bash
cd ~/projects/fake_detector
flutter build apk --release
```

결과물 경로: `build/app/outputs/flutter-apk/app-release.apk`
