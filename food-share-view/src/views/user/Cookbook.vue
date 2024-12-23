<template>
    <div class="cookbook-container">
        <div style="margin: 5px 0;">
            <span style="font-size: 24px;color: rgb(51,51,51);font-weight: bold;">食谱大全</span>
            <el-input size="small" style="width: 366px;float: right;" v-model="cookbookQueryDto.title" placeholder="食谱"
                clearable @clear="handleFilterClear">
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
        <div v-if="cookbookList.length === 0">
            <el-empty description="食谱暂时未找到"></el-empty>
        </div>
        <div v-else>
            <el-row>
                <el-col class="cookbook-item" style="margin-bottom: 10px;" :span="3"
                    v-for="(cookbook, index) in cookbookList" :key="index">
                    <div style="padding: 10px; box-sizing: border-box;">
                        <div>
                            <img :src="cookbook.cover" style="border-radius: 5px;width: 100%;height: 126px;" alt="">
                        </div>
                        <div @click="readDetail(cookbook)" style="cursor: pointer;;font-size: 16px;font-weight: 900;">{{
                            cookbook.title
                            }}</div>
                        <div style="font-size: 12px;margin-block: 4px;">{{ cookbook.createTime }}</div>
                    </div>
                </el-col>
            </el-row>
            <el-pagination style="margin: 10px 0;" @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="currentPage" :page-sizes="[10, 20]" :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper" :total="totalItems">
            </el-pagination>
        </div>

    </div>
</template>
<script>
export default {
    name: "Gourmet",
    data() {
        return {
            cookbookQueryDto: {},
            cookbookList: [],
            categories: [],
            filterText: '',
            currentPage: 1,
            pageSize: 10,
            totalItems: 0,
            categoryClick: { id: null, name: '全部' }
        }
    },
    created() {
        this.fetchCategoryData();
        this.fetchGourmetData();
    },
    methods: {
        readDetail(cookbook) {
            sessionStorage.setItem('cookbookInfo', JSON.stringify(cookbook));
            this.$router.push('/cookbookDetail');
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
            this.cookbookQueryDto.categoryId = category.id;
            this.fetchGourmetData();
        },
        // 查询食谱类别
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
        // 查询既公开又审核的食谱做法
        fetchGourmetData() {
            const queryDto = {
                current: this.currentPage,
                size: this.pageSize,
                key: this.filterText,
                ...this.cookbookQueryDto
            };
            this.$axios.post('/cookbook/queryPublish', queryDto).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.cookbookList = res.data.data;
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
.cookbook-item:hover {
    background-color: rgb(248, 248, 248);
}

.cookbook-container {
    background-color: rgb(255, 255, 255);
    padding: 20px;
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
            height: 80px;
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
