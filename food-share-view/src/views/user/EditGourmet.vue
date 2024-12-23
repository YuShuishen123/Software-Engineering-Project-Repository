<template>
    <div class="container">
        <div class="top">
            <el-tooltip class="item" effect="dark" content="返回首页" placement="bottom">
                <span class="last-page" @click="goBack">
                    <i class="el-icon-back"></i>
                </span>
            </el-tooltip>
            <span class="text">修改美食帖子</span>
        </div>
        <div class="buttom">
            <div style="width: 900px;margin: 0 auto;background-color: rgb(255,255,255);">
                <div style="padding-block: 20px;border-bottom: 1px solid rgb(234, 232, 232);">
                    <input v-model="gourmet.title" style="width: 80%;font-size: 30px;" type="text" class="input-title"
                        placeholder="请输入标题">
                </div>
                <div style="border-bottom: 1px solid rgb(234, 232, 232);">
                    <Editor height="calc(100vh - 400px)" :receiveContent="gourmet.content" @on-receive="onReceive" />
                </div>
                <div style="padding-inline: 20px;">
                    <p>*从属菜系</p>
                    <el-select style="width: 250px;" size="small" v-model="gourmet.categoryId" placeholder="菜系">
                        <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id">
                        </el-option>
                    </el-select>
                </div>
                <div style="padding-inline: 20px;">
                    <p>封面</p>
                    <el-upload class="avatar-uploader"
                        action="http://localhost:21090/api/food-share-sys/v1.0/file/upload" :show-file-list="false"
                        :on-success="handleCoverSuccess">
                        <img v-if="cover" :src="cover" style="width: 180px;height: 130px;">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </div>
                <div style="padding-inline: 20px;">
                    <p>是否公开</p>
                    <el-switch v-model="gourmet.isPublish" active-color="#13ce66" inactive-color="#3c3f41">
                    </el-switch>
                </div>
                <div style="text-align: center;">
                    <el-button @click="updateGourmet" style="margin: 20px 0;" plain>确定修改</el-button>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import Editor from "@/components/Editor"
export default {
    components: { Editor },
    name: "CreateGourmet",
    data() {
        return {
            gourmet: {},
            categories: [], // 美食做法的数据集
            cover: null,
        }
    },
    created() {
        this.fetchFreshCategories();
        this.loadGourmetId();
    },
    methods: {
        loadGourmetId() {
            const gourmetId = sessionStorage.getItem('gourmetId');
            this.fetchGourmetById(Number(gourmetId));
        },
        // 通过ID查找对应的美食做法
        fetchGourmetById(gourmetId) {
            this.$axios.get(`/gourmet/${gourmetId}`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.gourmet = data.data[0];
                    this.cover = this.gourmet.cover;

                }
            }).catch(error => {
                console.log("通过ID查询美食做法异常：", error);
            })
        },
        // 修改美食
        updateGourmet() {
            this.gourmet.cover = this.cover;
            this.$axios.put('/gourmet/update', this.gourmet).then(res => {
                if (res.data.code === 200) {
                    this.$notify({
                        duration: 1000,
                        title: '美食做法修改',
                        message: '修改成功',
                        type: 'success'
                    });
                    this.goBack();
                }
            }).catch(error => {
                console.log("修改美食做法异常：", error);
            });
        },
        // 封面上传回调函数
        handleCoverSuccess(res, file) {
            this.$notify({
                duration: 1500,
                title: '封面上传',
                message: res.code === 200 ? '上传成功' : '上传失败',
                type: res.code === 200 ? 'success' : 'error'
            });
            // 上传成功则更新用户头像
            if (res.code === 200) {
                this.cover = res.data;
            }
        },
        // 查询美食做法的信息
        fetchFreshCategories() {
            this.$axios.post('/category/query', {}).then(res => {
                if (res.data.code === 200) {
                    this.categories = res.data.data;
                    const allCategory = { id: null, name: '全部' };
                    // 头插
                    this.categories.unshift(allCategory);
                }
            }).catch(error => {
                console.log("查询美食做法异常：", error);
            });
        },
        // 返回上一页
        goBack() {
            this.$router.go(-1);
        },
        onReceive(content) {
            this.gourmet.content = content;
        },
    }
};
</script>
<style scoped lang="scss">
.container {
    background-color: rgb(248, 248, 248);
    min-height: 100vh;

    .buttom {
        margin-top: 6px;
    }

    .top {
        line-height: 64px;
        padding-inline: 40px;
        background-color: rgb(255, 255, 255);

        .text {
            font-size: 18px;
            font-weight: 900;
            margin-left: 10px;
        }

        .last-page:hover {
            background-color: rgb(232, 232, 232);
        }

        .last-page {
            background-color: rgb(245, 245, 245);
            padding: 2px 4px;
            border-radius: 12px;
            border: 1px solid rgb(235, 235, 235);
            cursor: pointer;
        }
    }
}
</style>
