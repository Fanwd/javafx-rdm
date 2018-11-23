# javafx-rdm
An redis client tools developed with JavaFX

#### 项目介绍
一款redis可视化客户端

#### 项目功能
1. 支持新建/删除连接;
2. 支持连接信息的自动保存、加载；
3. 支持数据类型：string、hash、list、set、zset
4. 所有数据类型增删改

#### 软件架构

1. 技术组件

javaFX、spring-boot、lettuce(redis client)、fontawesomefx(图标库)、dom4j(存储)

2. 构建工具

maven

#### 开发任务

1. 支持所有数据类型（完成）
2. 将数据模型改为观察者模式
3. 添加运行状态监控
4. 国际化

#### maven打包
1、http://www.jrsoftware.org/isdl.php安装iscc（用于生成exe安装包）
2、http://wixtoolset.org/ 安装WiX（用于生成.msi安装包）
3、打包命令mvn jfx:native