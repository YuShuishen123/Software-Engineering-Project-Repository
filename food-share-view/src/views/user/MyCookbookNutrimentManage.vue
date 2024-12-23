<template>
    <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
        <el-row style="padding: 10px;margin-left: 5px;">
            <el-row style="display: flex;justify-content: left;gap: 6px;">
                <span class="edit-button" @click="add()">
                    新增食谱配置
                </span>
            </el-row>
        </el-row>
        <el-row style="margin: 0 22px;border-top: 1px solid rgb(245,245,245);">
            <el-table :stripe="true" :data="tableData">
                <el-table-column prop="cookbookId" :sortable="true" width="120" label="食谱ID"></el-table-column>
                <el-table-column prop="nutrimentId" :sortable="true" width="120" label="营养素ID"></el-table-column>
                <el-table-column prop="cookbookName" label="食谱"></el-table-column>
                <el-table-column prop="nutrimentName" width="120" label="营养项"></el-table-column>
                <el-table-column prop="nutrimentUnit" width="130" label="营养素单位"></el-table-column>
                <el-table-column prop="g100Value" width="170" label="每100g中含量"></el-table-column>
                <el-table-column label="操作" width="110">
                    <template slot-scope="scope">
                        <span class="text-button" @click="handleEdit(scope.row)">编辑</span>
                        <span class="text-button" @click="handleDelete(scope.row)">删除</span>
                    </template>
                </el-table-column>
            </el-table>
        </el-row>
        <!-- 操作面板 -->
        <el-dialog :show-close="false" :visible.sync="dialogCategoryOperaion" width="18%">
            <div style="padding:16px 20px;">
                <p>{{ isOperation ? '修改营养素信息' : '配置营养素' }}</p>
                <el-row>
                    <span class="dialog-hover">选食谱</span>
                    <el-select style="width: 160px;" @change="fetchFreshData" size="small" v-model="data.cookbookId"
                        placeholder="食谱">
                        <el-option v-for="item in cookbookItems" :key="item.id" :label="item.name" :value="item.id">
                        </el-option>
                    </el-select>
                </el-row>
                <el-row>
                    <span class="dialog-hover">营养素</span>
                    <el-select style="width: 160px;" @change="fetchFreshData" size="small" v-model="data.nutrimentId"
                        placeholder="营养素">
                        <el-option v-for="item in nutrimentItems" :key="item.id" :label="item.name" :value="item.id">
                        </el-option>
                    </el-select>
                </el-row>
                <el-row>
                    <span class="dialog-hover">每100g中值含量</span>
                    <input class="dialog-input" v-model="data.g100Value" placeholder="输入" />
                </el-row>
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
    </el-row>
</template>

<script>
export default {
    data() {
        return {
            data: {},
            filterText: '',
            currentPage: 1,
            pageSize: 20,
            totalItems: 0,
            dialogCategoryOperaion: false, // 开关
            isOperation: false, // 默认新增
            tableData: [],
            delectedRows: [],
            cookbookNutrimentQueryDto: {}, // 搜索条件
            cookbookItems: [],
            nutrimentItems: []
        };
    },
    created() {
        this.fetchCookBookListItem();
        this.fetchNutrimentListItem();
        this.fetchFreshData();
    },
    methods: {
        // 食谱下拉选择器数据
        async fetchCookBookListItem() {
            try {
                const response = await this.$axios.get(`/cookbook/querySelectedItemsUser`);
                if (response.data.code === 200) {
                    this.cookbookItems = response.data.data;
                    
                }
            } catch (error) {
                console.log(error);
            }
        },
        // 营养素下拉选择器数据
        async fetchNutrimentListItem() {
            try {
                const response = await this.$axios.get(`/nutriment/querySelectedItemsUser`);
                if (response.data.code === 200) {
                    this.nutrimentItems = response.data.data;
                }
            } catch (error) {
                console.log(error);
            }
        },
        cannel() {
            this.data = {};
            this.dialogCategoryOperaion = false;
            this.isOperation = false;
        },
        // 批量删除数据
        async batchDelete() {
            if (!this.delectedRows.length) {
                this.$message(`未选中任何数据`);
                return;
            }
            const confirmed = await this.$swalConfirm({
                title: '删除食谱营养素关联数据',
                text: `删除后不可恢复，是否继续？`,
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.delectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/cookbookNutriment/batchDelete`, ids);
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
                    this.$message.error("食谱营养素关联信息删除异常：", error);
                    console.error(`食谱营养素关联信息删除异常：`, error);
                }
            }
        },
        // 修改信息
        async updateOperation() {
            try {
                const response = await this.$axios.put('/cookbookNutriment/update', this.data);
                if (response.data.code === 200) {
                    this.$notify({
                        duration: 1000,
                        title: '信息修改',
                        message: '修改成功',
                        type: 'success'
                    });
                    this.cannel();
                    this.fetchFreshData();
                }
            } catch (error) {
                console.error('修改出错:', error);
            }
        },
        // 信息新增
        async addOperation() {
            try {
                const response = await this.$axios.post('/cookbookNutriment/save', this.data);
                if (response.data.code === 200) {
                    this.$notify({
                        duration: 1000,
                        title: '配置新增',
                        message: '新增成功',
                        type: 'success'
                    });
                    this.cannel();
                    this.fetchFreshData();
                } else {
                    this.$notify({
                        duration: 1000,
                        title: '配置新增',
                        message: response.data.msg,
                        type: 'error'
                    });
                }
            } catch (error) {
                console.error('信息新增出错:', error);
                this.$message.error('提交失败，请稍后再试！');
            }
        },
        // 信息查询
        async fetchFreshData() {
            try {
                const response = await this.$axios.post('/cookbookNutriment/queryUser');
                const { data } = response;
                this.tableData = data.data;
            } catch (error) {
                this.$message.error("查询食谱营养素关联信息异常:", error);
                console.error('查询食谱营养素关联信息异常:', error);
            }
        },
        add() {
            this.dialogCategoryOperaion = true;
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
        // 食谱营养素关联修改按钮点击事件 
        handleEdit(row) {
            this.dialogCategoryOperaion = true;
            this.isOperation = true;
            this.data = { ...row }
        },
        // 食谱营养素关联修改按钮删除事件 
        handleDelete(row) {
            this.delectedRows.push(row);
            this.batchDelete();
        }
    },
};
</script>
<style scoped lang="scss"></style>