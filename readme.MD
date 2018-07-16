# EBookStore
课程作业。
## 技术内容
### 后端
* Spring/Spring MVC
* MyBatis
* Kotlin
* MySQL
### 前端
* Vue.js
* Bootstrap 4
## 需要准备的配置文件
* main/resources/db.properties 数据库连接配置
    * host 数据库HOST
    * port 端口(3306)
    * database 数据库名
    * username 用户名
    * password 密码
* main/resources/salt.properties 加密用字符串
    * salt 任意的随机字符串即可
* main/resources/file.properties 配置图片存储目录
    * base 可被访问的一个路径地址即可，不要以任何字符结尾，如`/home/user/img`