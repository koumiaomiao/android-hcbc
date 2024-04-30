### Prerequisites

- JDK 17+
- Android Studio Iguana | 2023.2.1+
- Android SDK: Go to Setting -> Language&Frameworks -> SDK, select latest sdk version, download and apply it
- Gradle: brew install gradle
- Clone Android Project: `git clone git@github.com:koumiaomiao/android-hcbc.git`

### Run Android project

- Enter root directory: android-hcbc

- Sync project by clicking Gradle refresh button

- Run project

  ``````bash
  ./gradlew assembleDebug
  ``````

- Install APK

  ``````bash
  adb install app/build/outputs/apk/debug/app-debug.apk

### Architecture

- MVVM Architecture: https://developer.android.com/topic/architecture?hl=zh-cn

- Network: OKHttp + Retrofit

- DI: Hilt

### CI/CD

Pipeline is multi-branch style, execute task by jenkins file built-in app project, follow these steps to set up jenkis

- Login AWS, Enter Elastic Container Service

- Create AWS Cluster, Define jenkins task using dockerhub latest jenkins image version: jenkins. Notice not jenkins:2.60.3

- Run jenkins server and get it's public ip address

- Chrome access jenkins public ip and config jenkins environment, init password can be found in jenkins task log

### Connect to server

- Update server address

  ``````kotlin
  object HttpClients {
  
      private const val HTTP_REQUEST_TIMEOUT = 30L
      private const val BFF_BASE_URL = "http://your-server-host:8088" // server address
  
      private val okHttpClient = OkHttpClient.Builder()
          .connectTimeout(HTTP_REQUEST_TIMEOUT, TimeUnit.SECONDS)
          .readTimeout(HTTP_REQUEST_TIMEOUT, TimeUnit.SECONDS)
          .build()
  
      val retrofit: Retrofit = Retrofit.Builder()
          .client(okHttpClient)
          .baseUrl(BFF_BASE_URL)
          .addConverterFactory(GsonConverterFactory.create())
          .build()
  }
  ``````

- install and run project according to `Run android project` section

### Notice

APP server is deployed to aws, but aws data will be erased periodically. So it's normal for app to report an error. If this happens, you can use the following steps to connect the local server:

- Open Terminal and run `ifconfig` command to get the local address

- Replace server address with your local address according to `Connect to server`

- Start local server, see the `Start local server` section of [README](https://github.com/koumiaomiao/hcbc-service) to see detail

- Rebuild project, install and run apk
