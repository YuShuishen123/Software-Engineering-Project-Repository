<template>
    <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
        <el-row style="padding: 10px;margin-left: 5px;">
            <el-row style="display: flex;justify-content: left;gap: 6px;">
                <el-select style="width: 100px;" @change="fetchFreshData" size="small"
                    v-model="gourmetQueryDto.categoryId" placeholder="菜系">
                    <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                </el-select>
                <el-select style="width: 100px;" @change="fetchFreshData" size="small" v-model="gourmetQueryDto.isAudit"
                    placeholder="审核状态">
                    <el-option v-for="item in auditStatus" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                </el-select>
                <el-select style="width: 100px;" @change="fetchFreshData" size="small"
                    v-model="gourmetQueryDto.isPublish" placeholder="公开状态">
                    <el-option v-for="item in publishStatus" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                </el-select>
                <el-date-picker style="width: 216px;" @change="fetchFreshData" size="small" v-model="searchTime"
                    type="daterange" range-separator="至" start-placeholder="发布开始" end-placeholder="发布结束">
                </el-date-picker>
                <el-input size="small" style="width: 186px;" v-model="gourmetQueryDto.title" placeholder="做法标题"
                    clearable @clear="handleFilterClear">
                    <el-button slot="append" @click="handleFilter" icon="el-icon-search"></el-button>
                </el-input>
            </el-row>
        </el-row>
        <el-row style="margin: 0 22px;border-top: 1px solid rgb(245,245,245);">
            <el-table :stripe="true" :data="tableData" style="width: 100%">
                <el-table-column prop="cover" label="封面" width="90">
                    <template slot-scope="scope">
                        <img :src="scope.row.cover" style="width: 60px;height: 35px;border-radius: 5px;" />
                    </template>
                </el-table-column>
                <el-table-column prop="title" label="帖子标题"></el-table-column>
                <el-table-column prop="userId" :sortable="true" label="用户ID" width="98"></el-table-column>
                <el-table-column prop="userName" label="发布者" width="118"></el-table-column>
                <el-table-column prop="categoryId" :sortable="true" label="菜系ID" width="98"></el-table-column>
                <el-table-column prop="categoryName" label="菜系" width="98"></el-table-column>
                <el-table-column prop="createTime" :sortable="true" label="发布时间" width="168"></el-table-column>
                <el-table-column prop="isAudit" label="审核状态" width="98">
                    <template slot-scope="scope">
                        <i v-if="!scope.row.isAudit" style="margin-right: 5px;color: rgb(199,199,199);"
                            class="el-icon-warning"></i>
                        <i v-else style="margin-right: 5px;color: rgb(253, 199, 50);" class="el-icon-success"></i>
                        <el-tooltip v-if="!scope.row.isAudit" class="item" effect="dark" content="未审核美食做法，不公开访问"
                            placement="bottom-end">
                            <span style="text-decoration: underline;text-decoration-style: dashed;">未审核</span>
                        </el-tooltip>
                        <span v-else>已审核</span>
                    </template>
                </el-table-column>
                <el-table-column prop="isPublish" label="公开状态" width="98">
                    <template slot-scope="scope">
                        <i v-if="!scope.row.isPublish" style="margin-right: 5px;color: rgb(199,199,199);"
                            class="el-icon-warning"></i>
                        <i v-else style="margin-right: 5px;color: rgb(253, 199, 50);" class="el-icon-success"></i>
                        <el-tooltip v-if="!scope.row.isPublish" class="item" effect="dark" content="私有内容，他人不能访问"
                            placement="bottom-end">
                            <span style="text-decoration: underline;text-decoration-style: dashed;">私有</span>
                        </el-tooltip>
                        <span v-else>公开</span>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="110">
                    <template slot-scope="scope">
                        <span class="text-button" v-if="!scope.row.isAudit" @click="handleEdit(scope.row)">审核</span>
                        <span class="text-button" @click="handleDelete(scope.row)">删除</span>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination style="margin:10px 0;float: right;" @size-change="handleSizeChange"
                @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="[20, 50]"
                :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
                :total="totalItems"></el-pagination>
        </el-row>
        <!-- 操作面板 -->
        <el-dialog :show-close="false" :visible.sync="dialogGourmetOperaion" width="18%">
            <div style="padding:50px 20px;">
                确定审核【{{ data.title }}】这篇帖子？
            </div>
            <span slot="footer" class="dialog-footer" style="margin-top: 10px;">
                <span class="channel-button" @click="cannel()">
                    取消操作
                </span>
                <span class="edit-button" @click="auditOpetaion()">
                    确定审核
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
            dialogGourmetOperaion: false, // 开关
            isOperation: false, // 默认新增
            tableData: [],
            delectedRows: [],
            searchTime: [],
            gourmetQueryDto: {}, // 搜索条件
            categories: [], // 美食做法的数据集
            auditStatus: [{ value: null, label: '全部' }, { value: 0, label: '未审核' }, { value: 1, label: '已审核' }],
            publishStatus: [{ value: null, label: '全部' }, { value: 0, label: '私有' }, { value: 1, label: '公开' }],
        };
    },
    created() {
        this.fetchFreshData();
        this.fetchFreshCategories();
    },
    methods: {
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
        cannel() {
            this.data = {};
            this.dialogGourmetOperaion = false;
            this.isOperation = false;
        },
        // 批量删除数据
        async batchDelete() {
            if (!this.delectedRows.length) {
                this.$message(`未选中任何数据`);
                return;
            }
            const confirmed = await this.$swalConfirm({
                title: '删除美食做法数据',
                text: `删除后不可恢复，是否继续？`,
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.delectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/gourmet/batchDelete`, ids);
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
                    this.$message.error("美食做法信息删除异常：", error);
                    console.error(`美食做法信息删除异常：`, error);
                }
            }
        },
        // 审核
        async auditOpetaion() {
            try {
                const response = await this.$axios.put(`/gourmet/audit/${this.data.id}`);
                if (response.data.code === 200) {
                    this.$notify({
                        duration: 1000,
                        title: '审核',
                        message: response.data.msg,
                        type: 'success'
                    });
                    this.cannel();
                    this.fetchFreshData();
                }
            } catch (error) {
                console.error('信息新增出错:', error);
                this.$message.error('提交失败，请稍后再试！');
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
                    ...this.gourmetQueryDto
                };
                const response = await this.$axios.post('/gourmet/query', params);
                const { data } = response;
                this.tableData = data.data;
                this.totalItems = data.total;
            } catch (error) {
                this.$message.error("查询美食做法信息异常:", error);
                console.error('查询美食做法信息异常:', error);
            }
        },
        add() {
            this.dialogGourmetOperaion = true;
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
        // 美食做法修改按钮点击事件 
        handleEdit(row) {
            this.dialogGourmetOperaion = true;
            this.isOperation = true;
            this.data = { ...row }
        },
        // 美食做法修改按钮删除事件 
        handleDelete(row) {
            this.delectedRows.push(row);
            this.batchDelete();
        }
    },
};
</script>
<style scoped lang="scss"></style>