dietHitoryList<template>
    <div style="background-color: rgb(255,255,255);padding: 20px;">
        <div class="record" @click="recordGourmet">
            <span style="font-size: 24px;font-weight: bold;">
                <i class="el-icon-food" style="margin-right: 5px;"></i>添加菜单
            </span>
        </div>
        <h3>我的菜单</h3>
        <div>
            <div v-if="dietHitoryList.length === 0">
                <el-empty description="快补充菜单吧"></el-empty>
            </div>
            <div v-else class="item-diet" style="padding-block: 10px;" v-for="(dietHistory, index) in dietHitoryList">
                <div style="font-size: 30px;font-weight: bold;">{{ getCookbookName(dietHistory) }}</div>
                <div style="margin-block: 4px;font-size: 14px;">
                    <span>
                        {{ dietHistory.detail }} - {{ dietHistory.value }}g
                    </span>
                    <span style="float: right;" @click="removeCookbook(dietHistory)">
                        <i class="el-icon-circle-close"></i>
                    </span>
                </div>
            </div>
            <div style="margin-block: 30px;">
                <el-button type="primary" @click="record">立即记录</el-button>
            </div>
        </div>
        <el-dialog :show-close="false" :visible.sync="dialogGourmetOperaion" width="20%">
            <div style="padding:16px 20px;">
                <p>选择菜单</p>
                <el-select style="width: 100%;" size="small" v-model="dieHistory.cookbookId" placeholder="选择菜单">
                    <el-option v-for="item in cookbooks" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                </el-select>
                <p>备注</p>
                <el-input type="textarea" :rows="3" placeholder="请输入内容" v-model="dieHistory.detail">
                </el-input>
                <p>食用量（g为单位）</p>
                <input class="dialog-input" v-model="dieHistory.value" placeholder="输入" />
            </div>
            <span slot="footer" class="dialog-footer" style="margin-top: 10px;">
                <span class="channel-button" @click="cannel()">
                    取消操作
                </span>
                <span class="edit-button" @click="addOperation()">
                    确认菜单
                </span>
            </span>
        </el-dialog>
    </div>
</template>
<script>
export default {
    name: "DietHistory",
    data() {
        return {
            dieHistory: {},
            dialogGourmetOperaion: false,
            cookbooks: [],
            dietHitoryList: [],
        }
    },
    created() {
        this.fetchCookbookList();
    },
    methods: {
        record() {
            this.$axios.post(`/dietHistory/save`, this.dietHitoryList).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.$notify({
                        duration: 1000,
                        title: '记录饮食',
                        message: '成功',
                        type: 'success'
                    });
                    this.dietHitoryList = [];
                }
            }).catch(error => {
                console.log("新增饮食信息异常：", error);
            })
        },
        removeCookbook(dieHistory) {
            this.dietHitoryList = this.dietHitoryList.filter(entity => entity.cookbookId !== dieHistory.cookbookId);
        },
        getCookbookName(dieHistory) {
            return this.cookbooks.filter(entity => entity.id === dieHistory.cookbookId)[0].name;
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
        addOperation() {
            this.dietHitoryList.push(this.dieHistory);
            this.cannel();
        },
        cannel() {
            this.dieHistory = {};
            this.dialogGourmetOperaion = false;
        },
        recordGourmet() {
            this.dialogGourmetOperaion = true;
        },
    }
};
</script>
<style scoped lang="scss">
.item-diet:hover {
    background-color: rgb(248, 248, 248);
}

.item-diet {
    padding: 12px;
    border-radius: 5px;
    cursor: pointer;
}

.record {
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: rgb(246, 246, 246);
    min-height: 200px;
    cursor: pointer;
}

.record:hover {
    background-color: rgb(242, 242, 242);
}
</style>
