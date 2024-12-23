<template>
    <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
        <el-row style="padding: 10px;margin-left: 5px;">
            <el-row style="display: flex;justify-content: left;gap: 6px;">
                <span class="edit-button" @click="add()">
                    新增食谱
                </span>
                <el-select style="width: 100px;" @change="fetchFreshData" size="small"
                    v-model="cookbookQueryDto.categoryId" placeholder="菜系">
                    <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                </el-select>
                <el-date-picker style="width: 216px;" @change="fetchFreshData" size="small" v-model="searchTime"
                    type="daterange" range-separator="至" start-placeholder="发布开始" end-placeholder="发布结束">
                </el-date-picker>
                <el-input size="small" style="width: 166px;" v-model="cookbookQueryDto.title" placeholder="美食名"
                    clearable @clear="handleFilterClear">
                    <el-button slot="append" @click="handleFilter" icon="el-icon-search"></el-button>
                </el-input>
            </el-row>
        </el-row>
        <el-row style="margin: 0 22px;border-top: 1px solid rgb(245,245,245);">
            <el-table :stripe="true" :data="tableData" style="width: 100%">
                <el-table-column prop="userAvatar" width="68" label="头像">
                    <template slot-scope="scope">
                        <el-avatar :size="25" :src="scope.row.userAvatar" style="margin-top: 10px;"></el-avatar>
                    </template>
                </el-table-column>
                <el-table-column prop="title" label="食谱名"></el-table-column>
                <el-table-column prop="id" :sortable="true" label="内容ID" width="98"></el-table-column>
                <el-table-column prop="categoryName" label="所属类别" width="118"></el-table-column>
                <el-table-column prop="isPublish" label="是否公开" width="118">
                    <template slot-scope="scope">
                        <span>{{ scope.row.isPublish ? '公开' : '私人' }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" label="创建时间" :sortable="true" width="168"></el-table-column>
                <el-table-column label="操作" width="120">
                    <template slot-scope="scope">
                        <span class="text-button" @click="handleEdit(scope.row)">修改</span>
                        <span class="text-button" @click="handleDelete(scope.row)">删除</span>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination style="margin:10px 0;float: right;" @size-change="handleSizeChange"
                @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="[20, 50]"
                :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
                :total="totalItems"></el-pagination>
        </el-row>
        <el-dialog :show-close="false" :visible.sync="dialogCookbookOperaion" width="40%">
            <div style="padding:16px 20px;">
                <p>{{ isOperation ? '食谱修改' : '食谱新增' }}</p>
                <el-row>
                    <p>食谱</p>
                    <el-upload class="avatar-uploader"
                        action="http://localhost:21090/api/food-share-sys/v1.0/file/upload" :show-file-list="false"
                        :on-success="handleCoverSuccess">
                        <img v-if="cover" :src="cover" style="width: 150px;height: 150px;">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-row>
                <div>
                    <input class="dialog-input" v-model="data.title" placeholder="食谱名称" />
                </div>
                <div>
                    <Editor height="calc(100vh - 550px)" :receiveContent="data.content" @on-receive="onReceive" />
                </div>
                <div style="margin-block: 10px;">
                    <span class="dialog-hover">所属菜系</span>
                    <el-select style="width: 100px;" @change="fetchFreshData" size="small" v-model="data.categoryId"
                        placeholder="菜系">
                        <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id">
                        </el-option>
                    </el-select>
                </div>
            </div>
            <span slot="footer" class="dialog-footer" style="margin-top: 10px;">
                <span class="channel-button" @click="cannel()">
                    取消操作
                </span>
                <span v-if="!isOperation" class="edit-button" @click="addOperation()">
                    确定新增
                </span>
                <span v-else class="edit-button" @click="updateOperation()">
                    确定修改
                </span>
            </span>
        </el-dialog>
        <el-dialog :show-close="false" :visible.sync="dialogConfigOperation" width="30%">
            <div style="padding:16px 20px;">
                <div>
                    配置营养素信息
                </div>
                <div style="margin-top: 10px;background-color: rgb(248,248,248);padding: 10px;border-radius: 4px;">
                    <div style="margin-block: 4px;font-size: 20px;font-weight: 800;">
                        <i class="el-icon-food" style="margin-right: 4px;"></i>
                        {{ data.title }}
                    </div>
                    <div style="font-size: 12px;">
                        <span>
                            {{ data.categoryName }}
                        </span>
                        <span>
                            发布于&nbsp;{{ data.createTime }}
                        </span>
                    </div>
                </div>
                <div>
                    <el-tabs v-model="activeName" @tab-click="handleTabClick">
                        <el-tab-pane label="营养素列表" name="first">
                            <div style="max-height: 350px;overflow-y: scroll;">
                                <div style="margin-block: 10px;">
                                    <el-input size="small" style="width: 166px;" v-model="nutrimentQueryDto.name"
                                        placeholder="用户名">
                                        <el-button slot="append" @click="fetchNutrimentList"
                                            icon="el-icon-search"></el-button>
                                    </el-input>
                                </div>
                                <div v-if="nutrimentList.length === 0">
                                    <el-empty description="暂无营养素信息"></el-empty>
                                </div>
                                <div v-else class="nutriment-item" v-for="(nutriment, index) in nutrimentList"
                                    :key="index">
                                    <div>
                                        <span class="unit">
                                            {{ nutriment.unit }}
                                        </span>
                                        <span class="name">
                                            {{ nutriment.name }}
                                        </span>
                                    </div>
                                    <div class="detail">
                                        {{ nutriment.detail }}
                                    </div>
                                    <div v-if="spaceNutriment.id === nutriment.id">
                                        <input @keyup.enter="confirmNutrimentConfig" class="dialog-input"
                                            v-model="spaceNutriment.g100Value" placeholder="每100g含量" />
                                    </div>
                                    <div v-else>
                                        <span @click="configNutrimentInfo(nutriment)"
                                            style="color: rgb(29, 153, 228);font-size: 12px;">
                                            配置
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="配置信息" name="second">
                            <div v-if="cookbookNutrimentList.length === 0">
                                <el-empty description="暂无配置信息"></el-empty>
                            </div>
                            <div v-else style="max-height: 350px;overflow-y: scroll;">
                                <div class="nutriment-item" v-for="(nutriment, index) in cookbookNutrimentList"
                                    :key="index">
                                    <div>
                                        <span class="unit">
                                            {{ nutriment.nutrimentUnit }}
                                        </span>
                                        <span class="name">
                                            {{ nutriment.nutrimentName }}
                                        </span>
                                    </div>
                                    <div v-if="spaceNutriment.id === nutriment.id">
                                        <input @keyup.enter="confirmUpadteNutrimentConfig" class="dialog-input"
                                            v-model="spaceNutriment.g100Value" placeholder="每100g含量" />
                                    </div>
                                    <div v-else>
                                        <span class="unit">每100g含量
                                            <span style="font-size: 18px;font-weight: bold;">
                                                {{ nutriment.g100Value }}{{ nutriment.nutrimentUnit }}
                                            </span>
                                        </span>
                                        <span @click="updateNutrimentConfig(nutriment)"
                                            style="color: rgb(29, 153, 228);font-size: 12px;">
                                            修改
                                        </span>
                                        <span @click="delNutrimentConfig(nutriment)"
                                            style="color: rgb(229, 0, 0);font-size: 12px;">
                                            删除
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </el-tab-pane>
                    </el-tabs>
                </div>
            </div>
            <span slot="footer" class="dialog-footer" style="margin-top: 10px;">
                <span class="channel-button" @click="cannelConfig()">
                    取消操作
                </span>
            </span>
        </el-dialog>
    </el-row>
</template>

<script>
import Editor from "@/components/Editor"
export default {
    components: { Editor },
    data() {
        return {
            activeName: 'first',
            data: {},
            filterText: '',
            currentPage: 1,
            pageSize: 20,
            totalItems: 0,
            tableData: [],
            delectedRows: [],
            searchTime: [],
            cover: null,
            cookbookNutrimentList: [],
            nutrimentQueryDto: { isPublish: true }, // 只要公开的
            dialogConfigOperation: false, // 配置弹窗
            dialogCookbookOperaion: false, // 弹窗
            isOperation: false, // 默认新增
            nutrimentList: [],
            cookbookQueryDto: {}, // 搜索条件
            spaceNutriment: {},
        };
    },
    created() {
        this.fetchFreshData();
        this.fetchFreshCategories();
    },
    methods: {
        handleCoverSuccess(res, file) {
            this.$notify({
                duration: 1500,
                title: '封面上传',
                message: res.code === 200 ? '上传成功' : '上传失败',
                type: res.code === 200 ? 'success' : 'error'
            });
            // 上传成功则更新封面
            if (res.code === 200) {
                this.cover = res.data;
            }
        },
        confirmUpadteNutrimentConfig() {
            const cookbookNutriment = {
                id: this.spaceNutriment.id,
                g100Value: this.spaceNutriment.g100Value
            }
            this.$axios.post('/cookbookNutriment/save', cookbookNutriment).then(res => {
                if (res.data.code === 200) {
                    // 提示配置成功
                    this.$notify({
                        duration: 1000,
                        title: '营养素配置操作',
                        message: '修改配置成功',
                        type: 'success'
                    });
                    this.activeName = 'second';
                } else {
                    this.$notify({
                        duration: 1000,
                        title: '营养素配置操作',
                        message: res.data.msg,
                        type: 'error'
                    });
                }
            }).catch(error => {
                console.log("修改食谱营养素关联信息异常：", error);
            });
        },
        // 修改食谱关联的营养素信息
        updateNutrimentConfig(nutriment) {
            this.spaceNutriment = nutriment;
            console.log("update:", nutriment);
        },
        // 删除食谱关联的营养素信息
        delNutrimentConfig(nutriment) {
            console.log("del:", nutriment);
        },
        // 确定营养素配置
        confirmNutrimentConfig() {
            // 构造关联对象
            const cookbookNutriment = {
                cookbookId: this.data.id,
                nutrimentId: this.spaceNutriment.id,
                g100Value: this.spaceNutriment.g100Value
            }
            this.$axios.post('/cookbookNutriment/save', cookbookNutriment).then(res => {
                if (res.data.code === 200) {
                    // 提示配置成功
                    this.$notify({
                        duration: 1000,
                        title: '营养素配置操作',
                        message: '配置成功',
                        type: 'success'
                    });
                    this.activeName = 'second';
                } else {
                    this.$notify({
                        duration: 1000,
                        title: '营养素配置操作',
                        message: res.data.msg,
                        type: 'error'
                    });
                }
            }).catch(error => {
                console.log("新增食谱营养素关联信息异常：", error);
            });
        },
        configNutrimentInfo(nutriment) {
            this.spaceNutriment = nutriment;
        },
        // 标签页切换
        handleTabClick(tab, event) {
            if (tab.index === '0') { // 查的营养素信息
                this.fetchNutrimentList();
            } else if (tab.index === '1') { // 查的是配置信息
                this.fetchCookbookNutrimentData(this.data.id);
            }
        },
        // 查询食谱关联的营养素信息
        fetchCookbookNutrimentData(cookbookId) {
            const cookbookNutrimentQueryDto = { cookbookId }
            this.$axios.post('/cookbookNutriment/query', cookbookNutrimentQueryDto).then(res => {
                if (res.data.code === 200) {
                    this.cookbookNutrimentList = res.data.data;
                }
            }).catch(error => {
                console.log("查询食谱营养素关联信息异常：", error);
            });
        },
        fetchNutrimentList() {
            this.$axios.post('/nutriment/query', this.nutrimentQueryDto).then(res => {
                if (res.data.code === 200) {
                    this.nutrimentList = res.data.data;
                }
            }).catch(error => {
                console.log("查询营养素异常：", error);
            });
        },
        configNutriment(cookbook) {
            this.data = cookbook;
            this.fetchNutrimentList();
            this.dialogConfigOperation = true;
        },
        cannelConfig() {
            this.dialogConfigOperation = false;
        },
        cannel() {
            this.data = {};
            this.dialogCookbookOperaion = false;
            this.isOperation = false;
        },
        addOperation() {
            this.data.cover = this.cover;
            this.$axios.post('/cookbook/userSave', this.data).then(res => {
                if (res.data.code === 200) {
                    this.$notify({
                        duration: 1000,
                        title: '食谱操作',
                        message: '新增成功',
                        type: 'success'
                    });
                    this.cannel();
                    this.fetchFreshData();
                }
            }).catch(error => {
                console.log("新增食谱异常：", error);
            });
        },
        updateOperation() {
            this.data.cover = this.cover;
            this.$axios.put('/cookbook/update', this.data).then(res => {
                if (res.data.code === 200) {
                    this.$notify({
                        duration: 1000,
                        title: '食谱操作',
                        message: '修改成功',
                        type: 'success'
                    });
                    this.cannel();
                    this.fetchFreshData();
                }
            }).catch(error => {
                console.log("修改食谱异常：", error);
            });
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
        onReceive(content) {
            this.data.content = content;
        },
        // 打开食谱新增弹窗
        add() {
            this.dialogCookbookOperaion = true;
        },
        // 批量删除数据
        async batchDelete() {
            if (!this.delectedRows.length) {
                this.$message(`未选中任何数据`);
                return;
            }
            const confirmed = await this.$swalConfirm({
                title: '删除食谱数据',
                text: `删除后不可恢复，是否继续？`,
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.delectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/cookbook/batchDelete`, ids);
                    if (response.data.code === 200) {
                        this.$notify({
                            duration: 1000,
                            title: '信息删除',
                            message: '删除成功',
                            type: 'success'
                        });
                        this.fetchFreshData();
                        return;
                    }
                } catch (error) {
                    this.$message.error("食谱删除异常：", error);
                    console.error(`食谱删除异常：`, error);
                }
            }
        },
        // 信息查询
        async fetchFreshData() {
            try {
                this.tableData = [];
                let startTime = null;
                let endTime = null;
                if (this.searchTime != null && this.searchTime.length === 2) {
                    const [startDate, endDate] = await Promise.all(this.searchTime.map(date => date.toISOString()));
                    startTime = `${startDate.split('T')[0]}T00:00:00`;
                    endTime = `${endDate.split('T')[0]}T23:59:59`;
                }
                // 请求参数
                const params = {
                    current: this.currentPage,
                    size: this.pageSize,
                    key: this.filterText,
                    startTime: startTime,
                    endTime: endTime,
                    ...this.cookbookQueryDto
                };
                const response = await this.$axios.post('/cookbook/queryUser', params);
                const { data } = response;
                this.tableData = data.data;
                this.totalItems = data.total;
            } catch (error) {
                this.$message.error("查询食谱异常:", error);
                console.error('查询食谱异常:', error);
            }
        },
        handleFilter() {
            this.currentPage = 1;
            this.fetchFreshData();
        },
        handleFilterClear() {
            this.filterText = '';
            this.handleFilter();
        },
        handleSizeChange(val) {
            this.pageSize = val;
            this.currentPage = 1;
            this.fetchFreshData();
        },
        handleCurrentChange(val) {
            this.currentPage = val;
            this.fetchFreshData();
        },
        handleEdit(row) {
            this.cover = row.cover;
            this.data = row;
            this.dialogCookbookOperaion = true;
            this.isOperation = true;
        },
        handleDelete(row) {
            this.delectedRows.push(row);
            this.batchDelete();
        }
    },
};
</script>
<style scoped lang="scss">
.nutriment-item:hover {
    background-color: rgb(246, 246, 246);
    border-radius: 4px;
}

.nutriment-item {
    padding: 8px;
    cursor: pointer;
    margin-right: 20px;

    .unit {
        font-size: 10px;
    }

    .name {
        font-weight: 800;
        color: rgb(51, 51, 51);
    }

    .detail {
        font-size: 12px;
        margin-block: 4px;
    }
}
</style>