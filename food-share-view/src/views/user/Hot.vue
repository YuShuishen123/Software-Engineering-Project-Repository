<template>
    <div>
        <div class="item-type">
            <span :style="{
                backgroundColor: item.id === typeItem.id ? 'rgb(91, 142, 248)' : '',
                color: item.id === typeItem.id ? 'rgb(245,245,245)' : ''
            }" @click="typeSelected(typeItem)" v-for="(typeItem, index) in interactionTypes" :key="index">{{
                typeItem.name
            }}</span>
        </div>
        <LineChart @on-selected="onSelected" :tag="tag" height="500px" :values="values" :date="dates" />
    </div>
</template>
<script>
import LineChart from "@/components/LineChart"
export default {
    components: { LineChart },
    name: "Hot",
    data() {
        return {
            interactionQueryDto: {
                day: 365, // 默认查一年
            },
            values: [],
            dates: [],
            interactionTypes: [],
            item: {},
            tag: '流量状况'
        }
    },
    created() {
        this.fetchInteractionTypes();
    },
    methods: {
        changeTag(type) {
            if (type.id === 1) {
                this.tag = '点赞量';
            } else if (type.id === 2) {
                this.tag = '收藏人数';
            } else if (type.id === 3) {
                this.tag = '阅读量';
            } else if (type.id === 4) {
                this.tag = '评分人数';
            }
        },
        typeSelected(typeItem) {
            this.changeTag(typeItem);
            this.item = typeItem;
            this.interactionQueryDto.type = typeItem.id;
            this.fetchHotData();
        },
        fetchInteractionTypes() {
            this.$axios.get('/interaction/types').then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.interactionTypes = data.data;
                    this.typeSelected(this.interactionTypes[0]);
                }
            }).catch(error => {
                console.log("查询互动类型异常", error);
            });
        },
        onSelected(day) {
            this.interactionQueryDto.day = day;
            this.fetchHotData();
        },
        fetchHotData() {
            this.$axios.post('/interaction/daysQuery', this.interactionQueryDto).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    const datas = data.data;
                    this.values = datas.map(entity => entity.count);
                    this.dates = datas.map(entity => entity.name);
                }
            }).catch(error => {
                console.log("查询流量状况数据异常", error);

            });
        }
    }
};
</script>
<style scoped lang="scss">
.item-type {
    display: flex;
    justify-content: left;
    align-items: center;
    background-color: rgb(246, 246, 246);
    gap: 16px;
    padding: 18px 10px;

    span {
        cursor: pointer;
        font-size: 12px;
        display: inline-block;
        padding: 4px 20px;
        border-radius: 15px;
        color: rgb(51, 51, 51);
    }
}
</style>
