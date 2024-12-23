<template>
    <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
        <el-row style="">
            <el-row style="display: flex;justify-content: left;gap: 6px;">
                <el-date-picker style="width: 216px;" @change="fetchFreshData" size="small" v-model="searchTime"
                    type="daterange" range-separator="至" start-placeholder="分享开始" end-placeholder="分享结束">
                </el-date-picker>
                <el-input size="small" style="width: 416px;" v-model="contentNetQueryDto.accessCode" placeholder="访问码"
                    clearable @clear="handleFilterClear">
                    <el-button slot="append" @click="handleFilter" icon="el-icon-search"></el-button>
                </el-input>
            </el-row>
        </el-row>
        <el-row style="border-top: 1px solid rgb(245,245,245);">
            <el-table :stripe="true" :data="tableData">
                <el-table-column prop="gourmetTitle" label="分享内容"></el-table-column>
                <el-table-column prop="gourmetId" :sortable="true" label="内容ID" width="98"></el-table-column>
                <el-table-column prop="accessCode" label="访问码" width="118"></el-table-column>
                <el-table-column prop="accessPassword" label="访问密码" width="118"></el-table-column>
                <el-table-column prop="url" label="链接" width="158" >
                    <template slot-scope="scope">
                        <span>{{ "http://localhost:21091/shareDetail?accessCode=" + scope.row.accessCode }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="passwordAuth" label="认证状态" width="118">
                    <template slot-scope="scope">
                        <span>{{ scope.row.passwordAuth ? '密码' : '非认证' }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="validDay" :sortable="true" label="有效期" width="98"></el-table-column>
                <el-table-column prop="createTime" label="分享时间" :sortable="true" width="168"></el-table-column>
                <el-table-column label="操作" width="110">
                    <template slot-scope="scope">
                        <span class="text-button" @click="handleEdit(scope.row)">编辑</span>
                        <span class="text-button" @click="handleDelete(scope.row)">删除</span>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination style="margin:10px 0;float: right;" @size-change="handleSizeChange"
                @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="[20, 50]"
                :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
                :total="totalItems"></el-pagination>
        </el-row>
        <el-dialog :visible.sync="dialogShareOperaion" width="20%">
            <div style="padding:16px 20px;">
                <p style="text-align: center;">分享修改</p>
                <div>
                    <div>
                        <p>*有效期</p>
                        <el-radio-group size="mini" v-model="contentNet.validDay">
                            <el-radio-button label="3天"></el-radio-button>
                            <el-radio-button label="7天"></el-radio-button>
                            <el-radio-button label="30天"></el-radio-button>
                            <el-radio-button label="长期有效"></el-radio-button>
                        </el-radio-group>
                    </div>
                    <div>
                        <p>*是否启用密码认证</p>
                        <el-switch v-model="contentNet.passwordAuth" active-color="#13ce66" inactive-color="#ff4949">
                        </el-switch>
                    </div>
                    <div v-if="contentNet.passwordAuth">
                        <p>*访问密码</p>
                        <input class="dialog-input" type="password" v-model="contentNet.accessPassword"
                            placeholder="密码" />
                    </div>
                </div>
            </div>
            <span slot="footer" class="dialog-footer" style="margin-top: 10px;">
                <span class="channel-button" @click="cannel()">
                    取消操作
                </span>
                <span class="edit-button" @click="updateOperation()">
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
            tableData: [],
            delectedRows: [],
            searchTime: [],
            contentNetQueryDto: {}, // 搜索条件
            contentNet: {},
            dialogShareOperaion: false
        };
    },
    created() {
        this.fetchFreshData();
    },
    methods: {
        updateOperation() {
            const dayList = [this.contentNet.validDay];
            const validDayList = dayList.map(text => {
                const match = text.match(/\d+/g); // 使用正则表达式匹配数字
                return match ? match : []; // 如果找到数字，返回数组，否则返回空数组
            });
            if (this.contentNet.passwordAuth) {
                this.contentNet.accessPassword = this.$md5(this.contentNet.accessPassword);
            }
            const updateEntity = {
                id: this.contentNet.id,
                validDay: validDayList[0].length === 0 ? -1 : validDayList[0][0],
                passwordAuth: this.contentNet.passwordAuth,
                accessPassword: this.contentNet.accessPassword
            }
            this.$axios.put('/contentNet/update', updateEntity).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.$notify({
                        duration: 1000,
                        title: '分享操作',
                        message: '修改成功',
                        type: 'success'
                    });
                    this.cannel();
                    this.fetchFreshData();
                }
            }).catch(error => {
                console.log("修改内容分享异常", error);
            });
        },
        cannel() {
            this.dialogShareOperaion = false;
            this.contentNet();
        },
        // 批量删除数据
        async batchDelete() {
            if (!this.delectedRows.length) {
                this.$message(`未选中任何数据`);
                return;
            }
            const confirmed = await this.$swalConfirm({
                title: '删除内容分享数据',
                text: `删除后不可恢复，是否继续？`,
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.delectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/contentNet/batchDelete`, ids);
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
                    this.$message.error("内容分享信息删除异常：", error);
                    console.error(`内容分享信息删除异常：`, error);
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
                    ...this.contentNetQueryDto
                };
                const response = await this.$axios.post('/contentNet/queryUser', params);
                const { data } = response;
                this.tableData = data.data;
                this.totalItems = data.total;
            } catch (error) {
                this.$message.error("查询内容分享信息异常:", error);
                console.error('查询内容分享信息异常:', error);
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
            this.contentNet = row;
            if (row.validDay === -1) {
                this.contentNet.validDay = '长期有效';
            } else {
                this.contentNet.validDay = row.validDay + '天';
            }
            this.dialogShareOperaion = true;
        },
        // 内容分享修改按钮删除事件 
        handleDelete(row) {
            this.delectedRows.push(row);
            this.batchDelete();
        }
    },
};
</script>
<style scoped lang="scss"></style>