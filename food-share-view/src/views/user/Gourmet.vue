<template>
    <div style="background-color: rgb(255,255,255);padding: 20px;">
        <el-row>
            <el-col :span="18">
                <div style="margin: 40px 0;">
                    <span style="font-size: 24px;color: rgb(51,51,51);font-weight: bold;">集锦</span>
                    <el-input size="small" style="width: 266px;float: right;" v-model="gourtmetQueryDto.title"
                        placeholder="搜索美食" clearable @clear="handleFilterClear">
                        <el-button slot="append" @click="fetchGourmetData" icon="el-icon-search"></el-button>
                    </el-input>
                </div>
                <div style="margin-block: 20px;">
                    <span :style="{
                        backgroundColor: categoryClick.id === category.id ? 'rgb(38, 151, 70)' : '',
                        color: categoryClick.id === category.id ? 'rgb(245,245,245)' : ''
                    }" @click="categorySelected(category)" class="item-category"
                        v-for="(category, index) in categories" :key="index">
                        {{ category.name }}
                    </span>
                </div>
                <div v-if="gourmetList.length === 0">
                    <el-empty description="美食做法暂时未找到"></el-empty>
                </div>
                <div v-else>
                    <div style="display: flex;justify-content: left;" class="item-gourmet"
                        v-for="(gourmet, index) in gourmetList" :key="index">
                        <div class="left">
                            <img :src="gourmet.cover">
                        </div>
                        <div class="right">
                            <div class="info">
                                <img style="width: 20px;height: 20px;border-radius: 10px;" :src="gourmet.userAvatar"
                                    alt="">
                                <span>{{ gourmet.userName }}</span>
                            </div>
                            <div style="margin-left: 4px;font-size: 24px;" class="title" @click="readGourmet(gourmet)">
                                {{ gourmet.title }}
                            </div>
                            <div class="detail">
                                {{ gourmet.detail }}
                            </div>
                            <div class="detail">
                                <span>
                                    {{ gourmet.createTime }}
                                </span>
                                <span>
                                    <i class="el-icon-discount" style="margin-right: 4px;"></i>({{ gourmet.upvoteCount
                                    }})
                                </span>
                                <span>
                                    <i class="el-icon-view" style="margin-right: 4px;"></i>({{ gourmet.viewCount }})
                                </span>
                                <span>
                                    <i class="el-icon-star-off" style="margin-right: 4px;"></i>({{ gourmet.saveCount }})
                                </span>
                            </div>
                        </div>
                    </div>
                    <el-pagination style="margin: 10px 0;" @size-change="handleSizeChange"
                        @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="[10, 20]"
                        :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="totalItems">
                    </el-pagination>
                </div>
            </el-col>
            <el-col :span="6" style="padding: 10px 30px;box-sizing: border-box;position: sticky;top: 60px;">
                <p style="font-size: 24px;">猜你想看</p>
                <div class="gourmet-item" v-for="(gourmet, index) in topList" :key="index"  @click="readGourmet(gourmet)">
                        <div class="left">
                            <img :src="gourmet.cover" :alt="gourmet.title" srcset="">
                        </div>
                        <div class="right">
                            <div class="user">
                                <img :src="gourmet.userAvatar" alt="" srcset="">
                                <span>{{ gourmet.userName }}</span>
                            </div>
                            <div class="title" @click="readGourmet(gourmet)">{{ gourmet.title }}</div>
                            <div class="info">
                                <span>{{ gourmet.categoryName }}</span>
                                <span>{{ timeOut(gourmet.createTime) }}</span>
                            </div>
                        </div>
                    </div>
            </el-col>
        </el-row>
    </div>
</template>
<script>
import { timeAgo } from "@/utils/data"
export default {
    name: "Gourmet",
    data() {
        return {
            gourtmetQueryDto: {},
            gourmetList: [],
            categories: [],
            filterText: '',
            currentPage: 1,
            pageSize: 10,
            totalItems: 0,
            topList: [],
            defaultLoadItem: 5, // 默认加载的推荐条目
            categoryClick: { id: null, name: '全部' }
        }
    },
    created() {
        this.fetchCategoryData();
        this.fetchGourmetData();
        this.loadRecommend(this.defaultLoadItem);
    },
    methods: {
        timeOut(time) {
            return timeAgo(time);
        },
        loadRecommend(item) {
            this.$axios.get(`/recommend/${item}`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.topList = res.data.data;
                    console.log(this.topList);
                }
            }).catch(error => {
                console.log(error);
            });
        },
        readGourmet(gourmet) {
            sessionStorage.setItem('gourmetId', gourmet.id);
            this.$router.push('/gourmetDetail');
        },
        handleFilterClear() {
            this.filterText = '';
            this.fetchGourmetData();
        },
        // 页面大小改变
        handleSizeChange(val) {
            this.pageSize = val;
            this.currentPage = 1;
            this.fetchGourmetData();
        },
        // 当前页切换
        handleCurrentChange(val) {
            this.currentPage = val;
            this.fetchGourmetData();
        },
        categorySelected(category) {
            this.categoryClick = category;
            this.gourtmetQueryDto.categoryId = category.id;
            this.fetchGourmetData();
        },
        // 查询美食类别
        fetchCategoryData() {
            this.$axios.post('/category/query', {}).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.categories = res.data.data;
                    this.categories.unshift(this.categoryClick);
                }
            }).catch(error => {
                console.log(error);
            });
        },
        // 查询既公开又审核的美食做法
        fetchGourmetData() {
            const queryDto = {
                current: this.currentPage,
                size: this.pageSize,
                key: this.filterText,
                ...this.gourtmetQueryDto
            };
            this.$axios.post('/gourmet/queryList', queryDto).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.gourmetList = res.data.data;
                    this.totalItems = data.total;
                }
            }).catch(error => {
                console.log(error);
            });
        },
    }
};
</script>



<style scoped lang="scss">
.gourmet-item {
        display: flex;
        justify-content: left;
        padding: 10px 0;
        cursor: pointer;

        .left {
            padding: 5px;
            box-sizing: border-box;

            img {
                width: 108px;
                height: 80px;
                border-radius: 5px;
            }
        }

        .right {
            padding: 5px;
            box-sizing: border-box;

            .user {
                display: flex;
                justify-content: left;
                align-items: center;
                margin-block: 4px;

                img {
                    width: 20px;
                    height: 20px;
                    border-radius: 10px;
                }

                span {
                    margin-left: 4px;
                    font-size: 12px;
                }
            }

            .title {
                font-size: 16px;
                width: 200px;
                font-weight: 800;
                padding-bottom: 6px;
                white-space: nowrap;
                /* 防止文本换行 */
                overflow: hidden;
                /* 隐藏超出容器的文本 */
                text-overflow: ellipsis;
                /* 使用省略号表示被截断的文本 */
            }

            .title:hover {
                text-decoration: underline;
                text-decoration-style: dashed;
            }

            .detail {
                font-size: 14px;
                color: rgb(131, 130, 130);
            }

            .info {
                font-size: 12px;
                margin-top: 4px;
                display: flex;
                justify-content: left;
                gap: 10px;

                span:first-child {
                    display: inline-block;
                    padding: 1px 3px;
                    border-radius: 2px;
                    background-color: rgb(237, 243, 249);
                    color: rgb(136, 115, 233);
                }
            }
        }

    }
.item-category {
    display: inline-block;
    padding: 8px 25px;
    border-radius: 15px;
    margin-right: 2px;
    font-size: 14px;
    cursor: pointer;
}

.item-gourmet:hover {
    background-color: rgb(248, 248, 248);
}

.item-gourmet {
    display: flex;
    justify-content: left;
    gap: 10px;
    padding: 10px;
    border-radius: 5px;
    cursor: pointer;

    .left {
        img {
            width: 190px;
            height: 130px;
            border-radius: 5px;
        }
    }

    .right {
        .info {
            padding: 4px 6px;
            display: flex;
            justify-content: left;
            align-items: center;
            gap: 5px;
            font-size: 14px;
            color: rgb(90, 89, 89);
        }

        .title {
            font-size: 20px;
            font-weight: 800;
        }

        .title:hover {
            text-decoration: underline;
            text-decoration-style: dashed;
        }

        .detail {
            font-size: 12px;
            padding: 4px 6px;
            color: rgb(90, 89, 89);
            display: flex;
            justify-content: left;
            gap: 10px;
        }
    }
}
</style>
