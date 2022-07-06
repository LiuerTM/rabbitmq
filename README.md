### RabbitMQ

------



#### 安装

1. **下载**

   <a href="https://github.com/rabbitmq/erlang-rpm/tags">ErLang 依赖下载地址</a>

   <a href="https://github.com/rabbitmq/rabbitmq-server/tags">RabbitMQ Server 下载地址</a>


2. **安装**

   ```shell
   [root@ming ~]# cd /usr/local/share/applications
   [root@ming applications]# rpm -ivh erlang-21.3.1-1.el7.x86_64.rpm
   [root@ming applications]# yum install -y socat
   [root@ming applications]# rpm -ivh rabbitmq-server-3.8.8-1.el7.noarch.rpm
   
   # RabbitMQ 服务状态、开启、关闭
   [root@ming ~]# systemctl status rabbitmq-server
   [root@ming ~]# systemctl start rabbitmq-server
   [root@ming ~]# systemctl stop rabbitmq-server
   
   # 安装 RabbitMQ 控制面板（浏览器访问：host:port(15672)）
   [root@ming ~]# rabbitmq-plugins enable rabbitmq_management
   # 新增用户名和密码
   [root@ming ~]# rabbitmqctl add_user username password
   # 给用户添加角色
   [root@ming ~]# rabbitmqctl set_user_tags username tags(administrator)
   # 给用户添加权限 virtual host、用户、资源、写操作、读操作
   [root@ming ~]# rabbitmqctl set_permissions -p "/" username ".*" ".*" ".*"
   ```