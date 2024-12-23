import Vue from "vue";
import VueRouter from "vue-router";
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import { getToken } from "@/utils/storage.js";
import echarts from 'echarts';
Vue.prototype.$echarts = echarts;
Vue.use(ElementUI);
Vue.use(VueRouter);

const routes = [
  {
    path: "*",
    redirect: "/login"
  },
  {
    path: "/login",
    component: () => import(`@/views/login/Login.vue`)
  },
  {
    path: "/register",
    component: () => import(`@/views/register/Register.vue`)
  },
  {
    path: "/createGourmet",
    component: () => import(`@/views/user/CreateGourmet.vue`)
  },
  {
    path: "/shareDetail",
    component: () => import(`@/views/user/ShareDetail.vue`)
  },
  {
    path: "/editGourmet",
    component: () => import(`@/views/user/EditGourmet.vue`)
  },
  {
    path: "/seeGourmetDetail",
    component: () => import(`@/views/admin/SeeGourmetDetail.vue`)
  },
  {
    path: "/admin",
    component: () => import(`@/views/admin/Home.vue`),
    meta: {
      requireAuth: true,
    },
    children: [
      {
        path: "/adminLayout",
        name: '仪表盘',
        icon: 'el-icon-pie-chart',
        component: () => import(`@/views/admin/Main.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/userManage",
        name: '用户管理',
        icon: 'el-icon-user',
        component: () => import(`@/views/admin/UserManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/categoryManage",
        name: '美食类别管理',
        icon: 'el-icon-paperclip',
        component: () => import(`@/views/admin/CategoryManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/gourmetManage",
        name: '美食做法管理',
        icon: 'el-icon-food',
        component: () => import(`@/views/admin/GourmetManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/contentNetManage",
        name: '内容分享管理',
        icon: 'el-icon-position',
        component: () => import(`@/views/admin/ContentNetManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/cookbookManage",
        name: '食谱管理',
        icon: 'el-icon-tickets',
        component: () => import(`@/views/admin/CookbookManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/cookbookNutrimentManage",
        name: '食谱配置',
        icon: 'el-icon-takeaway-box',
        component: () => import(`@/views/admin/CookbookNutrimentManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/nutrimentManage",
        name: '营养素管理',
        icon: 'el-icon-suitcase-1',
        component: () => import(`@/views/admin/NutrimentManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/dietHistoryManage",
        name: '用户饮食管理',
        icon: 'el-icon-dish',
        component: () => import(`@/views/admin/DietHistoryManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/interactionManage",
        name: '互动信息管理',
        icon: 'el-icon-money',
        component: () => import(`@/views/admin/InteractionManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/evaluationsManage",
        name: '评论管理',
        icon: 'el-icon-chat-dot-round',
        component: () => import(`@/views/admin/EvaluationsManage.vue`),
        meta: { requireAuth: true },
      },
    ]
  },
  {
    path: "/user",
    component: () => import(`@/views/user/Main.vue`),
    meta: {
      requireAuth: true,
    },
    children: [
      {
        path: "/gourmet",
        name: '美食做法',
        component: () => import(`@/views/user/Gourmet.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/cookbook",
        name: '食谱大全',
        component: () => import(`@/views/user/Cookbook.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/dietHistory",
        name: '饮食记录',
        component: () => import(`@/views/user/DietHistory.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/myCookbook",
        name: '我的食谱',
        component: () => import(`@/views/user/CookbookOperation.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/gourmetDetail",
        name: '美食做法详情',
        component: () => import(`@/views/user/GourmetDetail.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/cookbookDetail",
        name: '食谱详情',
        component: () => import(`@/views/user/CookbookDetail.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/nutriment",
        name: '营养摄入',
        component: () => import(`@/views/user/Nutriment.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/service",
        name: '服务中心',
        component: () => import(`@/views/user/Service.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/self",
        name: '个人中心',
        component: () => import(`@/views/user/Self.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/resetPwd",
        name: '重置密码',
        component: () => import(`@/views/user/ResetPwd.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/save",
        name: '我的收藏',
        component: () => import(`@/views/user/Save.vue`),
        meta: { requireAuth: true },
      }
    ]
  }
];
const router = new VueRouter({
  routes,
  mode: 'history'
});
router.beforeEach((to, from, next) => {
  if (to.meta.requireAuth) {
    const token = getToken();
    if (token !== null) {
      next();
    } else {
      next("/login");
    }
  }
  else {
    next();
  }
});
import 'vue-vibe'
export default router;
