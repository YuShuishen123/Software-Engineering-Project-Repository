<template>
    <div style="width: 100%;min-height: 100vh;background-color: rgb(248,248,248);">
        <div v-if="authStatus" style="width: 283px;border: 1px solid rgb(241,246,246);padding: 30px;">
            <p>认证信息</p>
            <div>
                <input class="dialog-input" type="password" v-model="contentNetQueryDto.accessPassword"
                    placeholder="密码" />
            </div>
            <div style="margin-block: 20px;">
                <span class="edit-button" @click="addOperation()">
                    确定
                </span>
            </div>
        </div>
        <div v-else
            style="padding: 30px;width: 800px;margin: 0 auto;background-color: rgb(255,255,255);min-height: 100vh;">
            <div style="font-size: 30px;font-weight: bold;">
                {{ gourmetInfo[0].title }}
            </div>
            <div style="margin-block: 10px;font-size: 14px;">
                <span style="margin-right: 8px;">{{ gourmetInfo[0].categoryName }}</span>
                <span style="margin-right: 8px;">由{{ gourmetInfo[0].userName }}创作</span>
                <span style="margin-right: 8px;">{{ gourmetInfo[0].viewCount }}人观看</span>
                <span style="margin-right: 8px;">{{ gourmetInfo[0].saveCount }}人收藏</span>
                <span>{{ gourmetInfo[0].upvoteCount }}人点赞</span>
            </div>
            <div style="border-top: 1px solid rgb(245,245,245);">
                <div v-html="gourmetInfo[0].content"></div>
            </div>
        </div>
    </div>
</template>
<script>
export default {
    name: "ShareDetail",
    data() {
        return {
            accessCode: null,
            authStatus: false,
            contentNetQueryDto: {},
            gourmetInfo: []
        }
    },
    created() {
        this.accessCode = this.$route.query.accessCode;
        this.loadAuthStatus(this.accessCode);
    },
    methods: {
        addOperation() {
            this.contentNetQueryDto.accessCode = this.accessCode;
            this.contentNetQueryDto.accessPassword = this.$md5(this.contentNetQueryDto.accessPassword);
            this.$axios.post('/contentNet/findContent', this.contentNetQueryDto).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.contentNetQueryDto = {};
                    this.gourmetInfo = data.data;
                    this.authStatus = false;
                } else {
                    this.$notify({
                        duration: 2000,
                        title: '文档操作',
                        message: data.msg,
                        type: 'warning'
                    });
                }
            }).catch(error => {
                console.log("查询帖子信息异常", error);
            });
        },
        // 通过访问码查询帖子的认证状态
        loadAuthStatus(accessCode) {
            this.$axios.post('/contentNet/authStatus', { accessCode }).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.authStatus = data.data;
                    if (!this.authStatus) {
                        this.loadGourmetInfo(accessCode);
                    }
                }
            }).catch(error => {
                console.log("查询帖子认证状态异常", error);
            });
        },
        loadGourmetInfo(accessCode) {
            this.$axios.post('/contentNet/findContent', { accessCode }).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.contentNetQueryDto = {};
                    this.gourmetInfo = data.data;
                } else {
                    this.$notify({
                        duration: 2000,
                        title: '文档操作',
                        message: data.msg,
                        type: 'warning'
                    });
                }
            }).catch(error => {
                console.log("查询帖子信息异常", error);
            });
        },
    }
};
</script>
<style scoped lang="scss"></style>
