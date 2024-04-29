### Prerequisites

JDK 17+
Android Studio Iguana | 2023.2.1+
Install Android SDK: Go to Setting -> Language&Frameworks -> SDK, select latest sdk version, download and apply it
Install Gradle: brew install gradle
Clone Android Project: https://github.com/koumiaomiao/android-hcbc

### Run Android project

Enter root directory: android-hcbc
Sync project by clicking Gradle refresh button
Run project: ./gradlew assembleDebug [--stacktrace]
Install APK: adb install apk-path [apk relative path: /androidhcbc/app/build/outputs/apk/debug/app-debug.apk]

### Architecture

MVVM Architecture: https://developer.android.com/topic/architecture?hl=zh-cn
Network: OKHttp + Retrofit
DI: Hilt

### CI/CD

Login AWS, Enter Elastic Container Service
Create AWS Cluster, Define jenkins task using dockerhub latest jenkins image version: jenkins. Notice not jenkins:2.60.3
Run jenkins server and get it's public ip address
Chrome access jenkins public ip and config jenkins environment, init password can be found in jenkins task log
Pipeline is multi-branch style, execute task by jenkins file built-in app project