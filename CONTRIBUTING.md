# Contributor向けガイド

## 環境構築(インストール)

Windowsユーザ向けに書いています。
インストールするものは以下の通りです。

1. Java8
2. Gradle
3. MySQL
4. RabbitMQ
5. Eclipse/SpringToolSuite

インストール済みのものがあればスキップしてください。

### 1. Java8のインストール
chocolateyを使っている場合は、
```cmd
choco install jdk8
```
でOKです。

```cmd
java -version
```
でversion番号が出てくればOKです。

### 2. Gradleのインストール

chocolateyを使っている場合は
```cmd
choco install gradle
```
でOKです。
```cmd
gradle -v
```
でversion番号が出てくればOKです。

### 3. RabbitMQのインストール
Chocolateyを使っている場合は
```cmd
choco install rabbitmq
```
でOKです。
管理者権限でコマンドプロンプトを立ち上げて
```cmd
net start rabbitmq
```
を実行し、http://localhost:15672/ にアクセスし、ログイン画面が出てくればOKです。

### 4. MySQLのインストール
Chocolateyを使っている場合は
```cmd
choco install mysql
```
でOKです。
```cmd
mysql --version
```
でバージョンが表示されればOKです。

### 5. Eclipse / SpringToolSuiteのインストール

EclipseかSpringtoolSuiteを持っていない場合はSpringToolSuiteをインストールします。
[ここ](https://spring.io/tools)の「Windows 64bit」のボタンを押してダウンロードします。
解凍すればそのまま使えます。



## 環境構築(セットアップ)
### 1.MySQLのセットアップ
#### 1.1. すでにMySQLのユーザが設定済みである場合 
管理者権限でコマンドプロンプトを立ち上げて、
```cmd
net start mysql
mysql -u <ユーザ名> -p 
```
でmysqlのコンソールを立ち上げて、
```sql
create database jiriki_db;
create database jiriki_db_test;
grant all on jiriki_db.* to <ユーザ名>;
grant all on jiriki_db_test.* to <ユーザ名>;
```
を実行します。
終わったら、src/main/resourcesの中に格納されている
application.ymlというファイルを編集し、

```yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/jiriki_db?serverTimezone=JST
    username: <ユーザ名>
    password: <パスワード>
```
usernameとpasswordの欄に、ユーザ名とパスワードを入力しておいてください。
編集したらこのファイルはコミットしないようにしてください。

#### 1.2. MySQLのユーザの設定をしていない場合
管理者権限でコマンドプロンプトを立ち上げて、
```cmd
net start mysql
mysql -u root
```
でmysqlのコンソールを立ち上げて、
```sql
create user mysql identifed by 'mysql'
create database jiriki_db;
create database jiriki_db_test;
grant all on jiriki_db.* to mysql;
grant all on jiriki_db_test.* to mysql;
```
### 2. Eclipse/SpringToolSuiteの設定
#### 2.1 コードフォーマッターの設定
1. [google-java-formatter](https://github.com/google/google-java-format#eclipse)のEclipse用プラグインをダウンロードし、Eclipseの中のdropinsフォルダにjarファイルを置きます。
2. Eclipseを立ち上げて、Window(ウィンドウ) -> Preferences(環境設定) -> Java -> Code Style -> Formatterを開きます。
3. Formatter Implementationの欄にプルダウンがあるので、google-java-formatを選択して、Apply and Closeを押します。以上です。

#### 2.2 このリポジトリの読み込み
1. Eclipseを起動し、File -> Importを選択します。
2. Git -> Projects from Gitを選択します。
3. Clone URIを選択します。
4. URIの欄に https://github.com/gogotea55t/bbp-jiriki-backend を入力し、他の必要事項を埋めたうえで、次へ進みます。
5. フォルダのインポートが完了したら、フォルダの上で右クリックし、Configure -> Add Gradle Natureをクリックします。
6. いろいろ読み込まれたら終了です。

#### 2.3 Lombokのインストール
1. [Lombok](https://projectlombok.org/download)のjarファイルをダウンロードします。
2. ダウンロードしたjarファイルをダブルクリックで実行します。
3. Specify Locationをクリックし、Eclipseを保存している場所を指定します。
4. Eclipseを再起動します。コンパイルエラーが出ている場合は、Project -> Cleanを選択してキャッシュを消します。

### 2.4 起動確認
1. JirikiApplication.javaの上で右クリックし、Run -> Spring Boot Appを選択します。
2. 8080ポートでアプリが起動すればクリアです。

## Branchの運用

Branchは以下のルールで回しています。
* master：　最新のソースコードを管理する
* release：　本番環境で動いているコードを管理する
* #IssueNo-xxxx：　Issueを解決するためのコードを管理する

したがって、運用としては以下のような感じです。
* Issueを解決しようと思ったら、masterからbranchを切ってmasterにマージする
* 本番環境にリリースするときは、タグをつけて、masterブランチからreleaseブランチにマージ
* 開発途中に本番環境でヤバめの不具合が見つかったときはreleaseブランチから切って修正し、その後masterにもマージ
  （あんまり何度もやってないです）

## Commitに当たってのお願い

[こちらのサイト](https://qiita.com/numanomanu/items/45dd285b286a1f7280ed)と、
[この規約](https://github.com/angular/angular.js/blob/master/DEVELOPERS.md#type)に基づき、
各Commitにはプレフィックスを付けています。以下のルールです。
* feat: 機能の追加
* fix: バグの修正
* docs: ドキュメントだけの変更
* style: コードのスタイルだけの変更 (空白やセミコロンを消す……etc)
* refactor: 機能は変えないけれども今後を見越してリファクタリングしたとき
* perf: 機能は変えないけれども性能を高めた時
* test: テストコードの修正をしたとき
* chore: ライブラリに何か追加した時

このルールに沿っていただきたく考えております。重ね掛けはOKです。  
(e.g.)
* feat: 楽曲追加機能を実装
* test: feat: 楽曲追加機能のテストを実装
* test: fix: バリデーションがうまくいかない不具合のテストを修正

## 継続的インテグレーション
[Travis-ci](https://travis-ci.com/)を使っています。
* テストの実行(Travis-ci)
* テストカバレッジの測定(Coveralls)、下がると「失敗」になります
* APIキーなどヤバいファイルをコミットしてないかチェック(GitGuardian)

masterブランチはビルド成功の状態にキープしておきたいので、ご協力をお願いします。
