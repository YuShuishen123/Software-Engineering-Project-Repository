<template>
    <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
        <el-row style="padding: 10px;margin-left: 5px;">
            <el-row>
                <span style="font-size: 26px;font-weight: 900;display: inline-block;padding: 0 10px;">我的饮食</span>
                <span style="float: right;">
                    <el-date-picker style="width: 216px;margin-right: 5px;" @change="fetchFreshData" size="small" v-model="searchTime"
                        type="daterange" range-separator="至" start-placeholder="记录开始" end-placeholder="记录结束">
                    </el-date-picker>
                    <el-input size="small" style="width: 166px;" v-model="dietHistoryQueryDto.cookbookId"
                        placeholder="食谱编号" clearable @clear="handleFilterClear">
                        <el-button slot="append" @click="handleFilter" icon="el-icon-search"></el-button>
                    </el-input>
                </span>

            </el-row>
        </el-row>
        <el-row style="margin: 0 22px;border-top: 1px solid rgb(245,245,245);">
            <el-table :stripe="true" :data="tableData" style="width: 100%">
                <el-table-column prop="cookbookCover" width="80" label="封面">
                    <template slot-scope="scope">
                        <img :src="scope.row.cookbookCover" style="width: 40px;height: 40px;border-radius: 5px;" alt=""
                            srcset="">
                    </template>
                </el-table-column>
                <el-table-column prop="cookbookTitle" label="食谱"></el-table-column>
                <el-table-column prop="detail" label="备注" width="200"></el-table-column>
                <el-table-column prop="value" label="食量" width="100">
                    <template slot-scope="scope">
                        <span>{{ scope.row.value }} g</span>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" label="记录时间" width="168"></el-table-column>
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
        <div style="padding-inline: 20px;">
            <el-row>
                <span class="dialog-hover">营养素</span>
                <el-select style="width: 160px;" @change="loadNutrimentData" size="small"
                    v-model="dietQueryDto.nutrimentId" placeholder="营养素">
                    <el-option v-for="item in nutrimentItems" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                </el-select>
            </el-row>
            <LineChart tag="营养摄入情况" height="400px" @on-selected="onSelected" :values="values" :date="dates" />
        </div>
        <el-dialog :show-close="false" :visible.sync="dialogDietOperaion" width="20%">
            <div style="padding:16px 20px;">
                <p>选择菜单</p>
                <el-select style="width: 100%;" size="small" v-model="data.cookbookId" placeholder="选择菜单">
                    <el-option v-for="item in cookbooks" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                </el-select>
                <p>备注</p>
                <el-input type="textarea" :rows="3" placeholder="请输入内容" v-model="data.detail">
                </el-input>
                <p>食用量（g为单位）</p>
                <input class="dialog-input" v-model="data.value" placeholder="输入" />
            </div>
            <span slot="footer" class="dialog-footer" style="margin-top: 10px;">
                <span class="channel-button" @click="cannel()">
                    取消操作
                </span>
                <span class="edit-button" @click="updateOperation()">
                    确认修改
                </span>
            </span>
        </el-dialog>
    </el-row>
</template>

<script>
import LineChart from "@/components/LineChart"
export default {
    components: { LineChart },
    data() {
        return {
            data: {},
            filterText: '',
            currentPage: 1,
            pageSize: 20,
            totalItems: 0,
            dietQueryDto: { day: 365 },
            dialogDietOperaion: false, // 开关
            tableData: [],
            delectedRows: [],
            cookbooks: [],
            searchTime: [],
            dietHistoryQueryDto: {}, // 搜索条件
            nutrimentUseList: [], // 营养素摄入情况
            values: [],
            dates: [],
            nutrimentItems: []
        };
    },
    created() {
        this.fetchFreshData();
        this.fetchCookbookList();
        this.loadNutrimentData();
        this.fetchNutrimentListItem();
    },
    methods: {
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
        onSelected(day) {
            this.dietQueryDto.day = day;
            this.loadNutrimentData();
        },
        updateOperation() {
            this.$axios.put(`/dietHistory/update`, this.data).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.$notify({
                        duration: 1000,
                        title: '饮食操作',
                        message: '修改成功',
                        type: 'success'
                    });
                    this.cannel();
                    this.fetchFreshData();
                }
            }).catch(error => {
                console.log("查询食谱信息异常：", error);
            })
        },
        // 查询营养摄入情况
        loadNutrimentData() {
            this.$axios.post(`/dietHistory/queryDietNutrimentInfo`, this.dietQueryDto).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.nutrimentUseList = data.data;
                    this.dates = this.nutrimentUseList.map(entity => entity.time);
                    this.values = this.nutrimentUseList.map(entity => entity.value);
                    console.log(this.dates);
                    console.log(this.values);

                }
            }).catch(error => {
                console.log("查询营养素摄入信息异常：", error);
            })
        },
        fetchCookbookList() {
            this.$axios.get(`/cookbook/querySelectedItemsAll`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.cookbooks = data.data;
                }
            }).catch(error => {
                console.log("查询食谱信息异常：", error);
            })
        },
        cannel() {
            this.data = {};
            this.dialogDietOperaion = false;
            this.isOperation = false;
        },
        // 批量删除数据
        async batchDelete() {
            if (!this.delectedRows.length) {
                this.$message(`未选中任何数据`);
                return;
            }
            const confirmed = await this.$swalConfirm({
                title: '删除用户饮食数据',
                text: `删除后不可恢复，是否继续？`,
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.delectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/dietHistory/batchDelete`, ids);
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
                    this.$message.error("用户饮食信息删除异常：", error);
                    console.error(`用户饮食信息删除异常：`, error);
                }
            }
        },
        // 修改信息
        async updateOperation() {
            try {
                const response = await this.$axios.put('/dietHistory/update', this.data);
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
                const response = await this.$axios.post('/dietHistory/save', this.data);
                if (response.data.code === 200) {
                    this.$notify({
                        duration: 1000,
                        title: '信息新增',
                        message: '新增成功',
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
                    ...this.dietHistoryQueryDto
                };
                const response = await this.$axios.post('/dietHistory/query', params);
                const { data } = response;
                this.tableData = data.data;
                this.totalItems = data.total;
            } catch (error) {
                this.$message.error("查询用户饮食信息异常:", error);
                console.error('查询用户饮食信息异常:', error);
            }
        },
        add() {
            this.dialogDietOperaion = true;
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
        // 用户饮食修改按钮点击事件 
        handleEdit(row) {
            this.dialogDietOperaion = true;
            this.data = { ...row }
        },
        // 用户饮食修改按钮删除事件 
        handleDelete(row) {
            this.delectedRows.push(row);
            this.batchDelete();
        }
    },
};
</script>
<style scoped lang="scss"></style>