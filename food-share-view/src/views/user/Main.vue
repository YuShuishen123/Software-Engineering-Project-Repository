<template>
    <div>
        <div class="top">
            <div class="top-left">
                <Logo sysName="橙子美食网" />
            </div>
            <div class="top-right">
                <ul>
                    <li @click="route('/gourmet')">
                        寻味
                    </li>
                    <li @click="route('/cookbook')">
                        寻技
                    </li>
                    <li @click="route('/Save')">
                        我的收藏夹
                    </li>
                    <li>
                        <el-dropdown type="success" size="mini" :hide-on-click="false">
                            <span class="el-dropdown-link">
                                我的饮食<i class="el-icon-arrow-down el-icon--right"></i>
                            </span>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item @click.native="route('/dietHistory')">记录饮食</el-dropdown-item>
                                <el-dropdown-item @click.native="route('/nutriment')">我的饮食</el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </li>
                    <li>
                        <el-dropdown type="success" size="mini" :hide-on-click="false">
                            <span class="el-dropdown-link">
                                内容创作<i class="el-icon-arrow-down el-icon--right"></i>
                            </span>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item @click.native="route('/createGourmet')">发布美食做法</el-dropdown-item>
                                <el-dropdown-item @click.native="route('/myCookbook')">我的食谱</el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </li>
                </ul>
                <el-button type="primary" style="margin-right: 15px;" size="mini" @click="route('/service')"
                    round>内容中心</el-button>
                <el-dropdown type="success" size="mini" class="user-dropdown">
                    <span class="el-dropdown-link" style="display: flex; align-items: center;cursor: pointer;">
                        <el-avatar :size="30" :src="userInfo.userAvatar" style="margin-top: 0;"></el-avatar>
                        <span class="user-name" style="margin-left: 5px;font-size: 14px;">{{ userInfo.userName }}</span>
                        <i class="el-icon-arrow-down el-icon--right" style="margin-left: 5px;"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item @click.native="route('/self')">个人资料</el-dropdown-item>
                        <el-dropdown-item @click.native="route('/resetPwd')">修改密码</el-dropdown-item>
                        <el-dropdown-item @click.native="loginOut">退出登录</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>
        </div>
        <div class="router-view">
            <div class="item">
                <router-view></router-view>
            </div>
        </div>
    </div>
</template>
<script>
import Logo from "@/components/Logo"
export default {
    components: { Logo },
    name: "User",
    data() {
        return {
            key: '',
            defaultPath: '/gourmet',
            userInfo: {}
        }
    },
    created() {
        this.auth();
        // 默认加载景点页
        this.route(this.defaultPath);
    },
    methods: {
        // 路由跳转
        route(path) {
            if (this.$route.path !== path) {
                this.$router.push(path);
            };
        },
        // 退出登录
        async loginOut() {
            const confirmed = await this.$swalConfirm({
                title: '退出登录',
                text: `退出后需要重新登录哦？`,
                icon: 'warning',
            });
            if (confirmed) {
                sessionStorage.setItem('token', null);
                this.$router.push('/');
            }
        },
        // Token 检验
        async auth() {
            const { data } = await this.$axios.get('/user/auth');
            if (data.code !== 200) { // Token校验异常
                this.$router.push('/');
            } else {
                this.userInfo = data.data;
                // 存储用户信息
                sessionStorage.setItem('userInfo', JSON.stringify(this.userInfo));
            }
        },
    }
};
</script>
<style scoped lang="scss">
.top {
    height: 65px;
    position: sticky;
    top: 0;
    background-color: rgb(255,255,255);
    z-index: 2000;
    line-height: 65px;
    padding: 4px 100px;
    box-sizing: border-box;
    display: flex;
    justify-content: space-between;
    border-bottom: 1px solid rgb(231, 231, 231);

    .top-right {
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 6px;

        ul {
            list-style: none;

            li {
                cursor: pointer;
                float: left;
                margin-right: 6px;
                padding: 5px 10px;

                min-width: 50px;
                max-width: 100px;
                color: rgb(111, 53, 71);
                font-size: 14px;
                box-sizing: border-box;
                font-weight: 500;
            }

            li:hover {
                color: rgb(25, 70, 160);
            }
        }
    }

    .top-left {
        display: flex;
        justify-content: center;
        align-items: center;
    }
}

.router-view {
    padding: 4px 30px;
    box-sizing: border-box;
    //background-color: rgb(248,248,248);
    min-height: calc(100vh - 66px);

    .item {
        width: 93%;
        margin: 0 auto;
    }
}
</style>
