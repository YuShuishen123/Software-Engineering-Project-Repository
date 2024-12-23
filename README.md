# 美食分享平台

欢迎来到美食分享平台项目，这是一个旨在让美食爱好者分享和发现美食的平台！本项目使用Vue.js构建前端界面，Spring Boot作为后端服务，以及MySQL作为数据存储解决方案。

# 🍽️ Food Share Platform

[![Vue](https://img.shields.io/badge/Vue.js-3.x-4FC08D?style=flat-square&logo=vue.js)](https://vuejs.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.x-6DB33F?style=flat-square&logo=spring)](https://spring.io/projects/spring-boot)
[![Element UI](https://img.shields.io/badge/Element-UI-409EFF?style=flat-square&logo=element)](https://element.eleme.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📖 项目介绍

Food Share Platform是一个现代化的美食分享平台，致力于为美食爱好者提供一个分享美食、交流心得的综合性平台。

### ✨ 主要特性

- 🏠 用户可以分享自己的美食作品
- 📝 支持发布美食菜谱和制作步骤
- 💬 用户互动评论系统
- 📊 完整的营养成分分析
- 🔍 智能搜索推荐
- 👥 用户社交功能
- 📱 响应式设计，支持移动端访问

## 🔧 技术栈

### 前端技术

- ⚡️ Vue.js 2.x
- 🎨 Element UI
- 📦 Vuex 状态管理
- 🚦 Vue Router 路由管理
- 📡 Axios HTTP 客户端
- 🎭 SCSS 样式处理

### 后端技术

- 🚀 Spring Boot 2.x
- 🏰 Spring Security 安全框架
- 📚 MyBatis Plus ORM框架
- 🗃️ MySQL 数据库
- 🔑 Redis 缓存
- 📝 Swagger API文档

## 🚀 快速开始

### 环境要求

- Node.js 12+
- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+
- Redis 6.0+

### 前端部署

```bash
# 克隆项目
git clone https://github.com/your-username/food-share-view.git

# 进入项目目录
cd food-share-view

# 安装依赖
npm install

# 启动开发服务器
npm run serve

# 构建生产环境
npm run build
```

### 后端部署

```bash
# 克隆后端项目
git clone https://github.com/your-username/food-share-api.git

# 进入项目目录
cd food-share-api

# 编译项目
mvn clean package

# 运行项目
java -jar target/food-share-api.jar
```

## 📚 项目结构

### 前端结构

```
food-share-view/
├── public/                 # 静态资源
├── src/
│   ├── api/               # API接口
│   ├── assets/            # 资源文件
│   ├── components/        # 公共组件
│   ├── router/            # 路由配置
│   ├── store/             # Vuex状态管理
│   ├── utils/             # 工具函数
│   └── views/             # 页面组件
└── package.json           # 项目配置
```

### 后端结构

```
food-share-api/
├── src/main/java/
│   └── com/foodshare/
│       ├── config/        # 配置类
│       ├── controller/    # 控制器
│       ├── entity/        # 实体类
│       ├── mapper/        # MyBatis映射
│       ├── service/       # 服务层
│       └── utils/         # 工具类
└── pom.xml                # 项目依赖
```

## 🌟 主要功能模块

### 👤 用户模块

- 用户注册/登录
- 个人信息管理
- 用户权限控制

### 🍳 美食管理

- 美食信息发布
- 菜谱管理
- 营养成分分析

### 💬 社交互动

- 评论系统
- 点赞功能
- 用户关注

### 👨‍💼 管理后台

- 用户管理
- 内容审核
- 系统配置

## 📈 项目特点

- 🛡️ 完善的权限控制
- 🎯 组件化开发
- 📱 响应式设计
- 🔒 安全性保障
- 🚀 性能优化
- 📖 详细文档

## 🤝 贡献指南

1. Fork 本仓库
2. 创建新的分支 `git checkout -b feature/your-feature`
3. 提交更改 `git commit -am 'Add new feature'`
4. 推送到分支 `git push origin feature/your-feature`
5. 提交 Pull Request

## 📄 开源协议

本项目采用 [MIT](LICENSE) 协议开源。

## 👥 团队成员

- 👨‍💻 开发者A - 前端开发
- 👩‍💻 开发者B - 后端开发
- 👨‍🎨 开发者C - UI设计

## 📞 联系我们

- 📧 Email: <your-email@example.com>
- 💬 微信: your-wechat
- 🌐 网站: <https://your-website.com>

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者！
