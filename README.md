# [Deprecated] AndroidVolleySample
Android Studio × Volley
現在はGradleで提供されるようになったので、このプロセス及びサンプルは必要なくなった…

https://developer.android.com/training/volley/index.html

レガシーな記録として残しておく。

# Volleyインストール

## Android Studioでプロジェクト作成
なんでも良い。
## Gitセットアップ 
```
$ vim .gitignore
+ *.iml
- /.idea/*
+ .idea/

:wq
$ git init
 ```
## Volley追加
```
$ mkdir libs && cd libs
$ git submodule add https://android.googlesource.com/platform/frameworks/volley
$ cd ..
$ vim .gitmodules
+ ignore = dirty

:wq
```

## Gradle設定
app/build.gradle
```
dependencies {
    compile project(':volley')
}
```
settings.gradle
```
include ':app', ':volley'
project(':volley').projectDir = new File('libs/volley')
```

# Volleyを使う
https://developer.android.com/training/volley/index.html
