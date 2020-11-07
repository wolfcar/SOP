# ISV管理后台

  前提：先安装好npm，[npm安装教程](https://blog.csdn.net/zhangwenwu2/article/details/52778521)

- 启动服务端程序，运行`WebsiteServerApplication.java`
- `cd sop-website-vue`
- 执行`npm install --registry=https://registry.npm.taobao.org`
- 执行`npm run dev`，访问`http://localhost:9529/`


- 修改端口号：打开`vue.config.js`，找到`port`属性

## 构建

开发完毕后，运行`build.sh`脚本