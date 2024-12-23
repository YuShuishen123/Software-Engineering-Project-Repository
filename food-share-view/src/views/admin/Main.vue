<template>
    <div style="box-sizing: border-box;overflow-y: hidden;overflow-x: hidden;">
        <el-row>
            <el-col :span="16">
                <div>
                    <div>
                        <el-row style="padding-block: 20px;margin-inline: 30px;background-color: rgb(246,246,246);">
                            <el-col :span="4" v-for="(staticItem, index) in staticData" :key="index">
                                <div class="static-item">
                                    <el-statistic group-separator="," :precision="0" :value="staticItem.count"
                                        :title="staticItem.name"></el-statistic>
                                </div>
                            </el-col>
                        </el-row>
                    </div>
                    <div>
                        <div style="padding: 20px 60px;box-sizing: border-box;">
                            <LineChart height="280px" tag="内容分享趋势" @on-selected="netItemSelected" :values="shareValues"
                                :date="shareDates" />
                        </div>
                        <div style="padding: 20px 60px;box-sizing: border-box;">
                            <LineChart height="280px" tag="收录美食做法" @on-selected="gourmetItemSelected"
                                :values="gourmetValues" :date="gourmetDates" />
                        </div>
                    </div>
                </div>
            </el-col>
            <el-col :span="8">
                <div class="buttom">
                    <p style="font-size: 24px;">最热帖子</p>
                    <div class="gourmet-item" v-for="(gourmet, index) in gourmetList" :key="index">
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
                                <span>
                                    点赞({{ gourmet.upvoteCount}})
                                </span>
                                <span>
                                    阅读({{ gourmet.viewCount }})
                                </span>
                                <span>
                                    收藏({{ gourmet.saveCount }})
                                </span>
                                <span>{{ timeOut(gourmet.createTime) }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </el-col>
        </el-row>
    </div>
</template>
<script>
import LineChart from "@/components/LineChart"
import { timeAgo } from "@/utils/data"
export default {
    components: { LineChart },
    data() {
        return {
            staticData: [],
            gourmetList: [],
            shareValues: [],
            shareDates: [],
            gourmetValues: [],
            gourmetDates: [],
            defaultQueryDays: 365, // 由于数据太少，直接查一年来测试
        }
    },
    created() {
        this.fentchStaticCount();
        this.fentchgourmet();
        this.netItemSelected(this.defaultQueryDays);
        this.gourmetItemSelected(this.defaultQueryDays);
    },
    methods: {
        timeOut(time) {
            return timeAgo(time);
        },
        // 查询指定时间内的美食做法存量
        gourmetItemSelected(day) {
            this.$axios.get(`/gourmet/daysQuery/${day}`).then(res => {
                if (res.data.code === 200) {
                    this.gourmetValues = res.data.data.map(money => money.count);
                    this.gourmetDates = res.data.data.map(money => money.name);
                }
            });
        },
        // 查询指定时间内的内容分享量
        netItemSelected(day) {
            this.$axios.post(`/contentNet/daysQuery`, { day }).then(res => {
                if (res.data.code === 200) {
                    this.shareValues = res.data.data.map(money => money.count);
                    this.shareDates = res.data.data.map(money => money.name);
                }
            });
        },
        readGourmet(gourmet) {
            sessionStorage.setItem('gourmetId', gourmet.id);
            this.$router.push('/seeGourmetDetail');
        },
        // 查询阅读量最高的美食做法数据
        fentchgourmet() {
            const gourmetQueryDto = {
                current: 1,
                size: 5
            }
            this.$axios.post('/gourmet/queryByView', gourmetQueryDto).then(res => {
                if (res.data.code === 200) {
                    this.gourmetList = res.data.data;
                }
            });
        },
        // 查询静态数据
        fentchStaticCount() {
            this.$axios.get('/views/staticControls').then(res => {
                if (res.data.code === 200) {
                    this.staticData = res.data.data;
                }
            });
        },
    },
};
</script>
<style scoped lang="scss">
.buttom {
    padding: 0px 20px;
    box-sizing: border-box;

    .gourmet-item {
        display: flex;
        justify-content: left;
        padding: 10px 0;
        cursor: pointer;

        .left {
            padding: 5px;
            box-sizing: border-box;

            img {
                width: 138px;
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
                    font-size: 14px;
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
}
</style>