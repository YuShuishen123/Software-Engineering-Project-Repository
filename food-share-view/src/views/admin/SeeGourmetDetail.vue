<template>
    <el-row style="width: 1000px;margin: 0 auto;background-color: rgb(255,255,255);border-radius: 2px;">
        <el-col :span="16" style="padding: 10px 20px;border-right: 1px solid rgb(245,245,245);">
            <div style="position: sticky;top: 0px;z-index: 100;background-color: rgb(255,255,255);padding-block: 10px;">
                <div class="title">{{ gourmet.title }}</div>
                <div class="info">
                    <img style="width: 20px;height: 20px;border-radius: 10px;" :src="gourmet.userAvatar" alt="">
                    <span>{{ gourmet.userName }}</span>
                    <span>
                        {{ gourmet.createTime }}
                    </span>
                    <span>
                        点赞({{ gourmet.upvoteCount }})
                    </span>
                    <span>
                        浏览({{ gourmet.viewCount }})
                    </span>
                    <span>
                        收藏({{ gourmet.saveCount }})
                    </span>
                </div>
            </div>
            <div style="border-top: 1px solid rgb(245,245,245);">
                <div v-html="gourmet.content"></div>
            </div>
            <div style="margin: 40px 0;display: flex;justify-content: center;align-items: center;">
                <span>
                    <span class="upvote-operation">
                        <el-tooltip class="item" effect="dark" :content="upvoteStatus ? '取消点赞' : '点赞这篇帖子'"
                            placement="bottom">
                            <span @click="opeationUpvote">
                                <i class="el-icon-discount"></i>
                                <span>
                                    {{ gourmet.upvoteCount }}人喜欢
                                </span>
                            </span>
                        </el-tooltip>
                    </span>
                    <span class="upvote-operation">
                        <el-tooltip class="item" effect="dark" :content="saveStatus ? '取消收藏' : '收藏这篇帖子'"
                            placement="bottom">
                            <span @click="opeationSave">
                                <i class="el-icon-star-off"></i>
                                <span>
                                    {{ gourmet.saveCount }}人收藏
                                </span>
                            </span>
                        </el-tooltip>
                    </span>
                </span>
            </div>
            <div style="border-top: 1px solid rgb(245,245,245);">
                <Evaluations :contentId="gourmetId" contentType="GOURMET" />
            </div>
        </el-col>
        <el-col :span="8" style="padding: 10px 20px;position: sticky;top: 0px;">
            <div style="margin-block: 20px;">
                <h4 style="margin-top: 10px;">评分信息</h4>
                <!-- 显示评分区域是有条件：你没有评过分才能去评分（显示） -->
                <div v-if="!ratingStatus" style="margin-block: 30px;">
                    <div style="margin-block: 4px;font-size: 12px;color: rgb(4, 81, 165);">轻触评个分数吧</div>
                    <el-rate @change="ratingEvent" v-model="ratingScore" show-text>
                    </el-rate>
                </div>
                <div
                    style="border-radius: 5px;padding: 20px;display: flex;justify-content: center;align-items: center;background-color: rgb(250,250,250);">
                    <div>
                        <div style="text-align: center;margin-block: 5px;font-weight: bold;">总评分</div>
                        <el-rate v-model="gourmet.rating" disabled show-score text-color="#ff9900"
                            score-template="{value}">
                        </el-rate>
                    </div>
                </div>
                <div>
                    <div v-if="ratingVos.length === 0">
                        <el-empty description="暂无评论哦！"></el-empty>
                    </div>
                    <div v-else style="margin-block: 10px;" v-for="(ratingVO, m) in ratingVos" :key="m">
                        <div style="display: flex;justify-content: left;align-items: center;gap: 8px;">
                            <img style="width: 20px;height: 20px;border-radius: 10px;" :src="ratingVO.userAvatar" alt="" srcset="">
                            <span style="font-size: 12px;">{{ ratingVO.userName }}</span>
                        </div>
                        <div style="margin-block: 4px;">
                            <el-rate v-model="ratingVO.score" disabled show-score text-color="#ff9900"
                                score-template="{value}">
                            </el-rate>
                        </div>
                        <div style="font-size: 12px;color: rgb(51,51,51);">
                            {{ ratingVO.createTime }}
                        </div>
                    </div>
                </div>
            </div>
        </el-col>
    </el-row>
</template>
<script>
import Evaluations from "@/components/Evaluations"
export default {
    components: { Evaluations },
    name: "GourmetDetail",
    data() {
        return {
            gourmetId: null,
            gourmet: {},
            ratingScore: 0, // 评分
            recommendGourmet: [],
            ratingVos: [], // 用户评分数据
            upvoteStatus: false, // 默认未点赞
            saveStatus: false, // 默认未收藏
            ratingStatus: false, // 评分状态
        }
    },
    created() {
        this.loadGourmetId();
        this.fetchTopGourmet();
    },
    methods: {
        // 查询用户的评分数据
        fetchRatingData(contentId) {
            const interactionQueryDto = {
                contentId,
                current: 1,
                size: 3
            }
            this.$axios.post(`/interaction/queryUserRating`, interactionQueryDto).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.ratingVos = data.data;
                }
            }).catch(error => {
                console.log("查询评分信息异常：", error);
            })
        },
        // 用户评分时触发
        ratingEvent() {
            this.$axios.post(`/interaction/ratingOperation/${this.gourmetId}/${this.ratingScore}`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.gourmet = data.data[0];
                    this.dealRating(this.gourmet);
                    this.$notify({
                        duration: 1000, // 毫秒
                        title: '评分',
                        message: '评分成功',
                        type: 'success'
                    });
                    this.ratingStatus = true;
                    this.fetchRatingData(this.gourmetId);
                } else {
                    this.$message(data.msg);
                }
            }).catch(error => {
                console.log("评分操作异常：", error);
            })
        },
        // 查询点赞状态
        fetchUpvoteOperation(contentId) {
            this.$axios.get(`/interaction/upvoteStatus/${contentId}`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    console.log("点赞的状态=>", data.data);
                    this.upvoteStatus = data.data > 0;
                }
            }).catch(error => {
                console.log("通过点赞状态异常：", error);
            })
        },
        // 查询评分状态
        fetchRatingOperation(contentId) {
            this.$axios.get(`/interaction/ratingStatus/${contentId}`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    console.log("评分的状态=>", data.data);
                    this.ratingStatus = data.data > 0;
                }
            }).catch(error => {
                console.log("通过评分状态异常：", error);
            })
        },
        // 查询收藏状态
        fetchSaveOperation(contentId) {
            this.$axios.get(`/interaction/saveStatus/${contentId}`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    console.log("收藏的状态=>", data.data);
                    this.saveStatus = data.data > 0;
                }
            }).catch(error => {
                console.log("通过收藏状态异常：", error);
            })
        },
        // 收藏操作
        opeationSave() {
            this.$axios.post(`/interaction/saveOperation/${this.gourmetId}`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    console.log(data.data > 0 ? '收藏成功' : '取消收藏成功');
                    if (data.data > 0) {
                        this.gourmet.saveCount += 1;
                    } else {
                        this.gourmet.saveCount -= 1;
                    }
                    this.saveStatus = data.data > 0;
                }
            }).catch(error => {
                console.log("收藏操作异常：", error);
            })
        },
        // 点赞操作
        opeationUpvote() {
            this.$axios.post(`/interaction/upvoteOperation/${this.gourmetId}`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    console.log(data.data > 0 ? '点赞成功' : '取消点赞成功');
                    if (data.data > 0) {
                        this.gourmet.upvoteCount += 1;
                    } else {
                        this.gourmet.upvoteCount -= 1;
                    }
                    this.upvoteStatus = data.data > 0;
                }
            }).catch(error => {
                console.log("点赞操作异常：", error);
            })
        },
        readGourmet(gourmet) {
            this.fetchGourmetById(gourmet.id);
        },
        loadGourmetId() {
            this.gourmetId = Number(sessionStorage.getItem('gourmetId'));
            // 加载美食做法数据
            this.fetchGourmetById(this.gourmetId);
            // 处理浏览操作
            this.viewOperation(this.gourmetId);
            // 加载点赞状态
            this.fetchUpvoteOperation(this.gourmetId);
            // 加载收藏状态
            this.fetchSaveOperation(this.gourmetId);
            // 加载评分状态
            this.fetchRatingOperation(this.gourmetId);
            // 加载用户的评分信息
            this.fetchRatingData(this.gourmetId);
        },
        dealRating(gourmet) {
            this.gourmet.rating = gourmet.rating === null ? 0 : gourmet.rating;
        },
        // 通过ID查找对应的美食做法
        fetchGourmetById(gourmetId) {
            this.$axios.get(`/gourmet/${gourmetId}`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.gourmet = data.data[0];
                    this.dealRating(this.gourmet);
                }
            }).catch(error => {
                console.log("通过ID查询美食做法异常：", error);
            })
        },
        // 浏览操作
        viewOperation(contentId) {
            this.$axios.post(`/interaction/viewOperation/${contentId}`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    console.log("浏览执行...");
                }
            }).catch(error => {
                console.log("浏览操作异常：", error);
            })
        },
        // 获取通过算法查回来，现在只是占位处理
        fetchTopGourmet() {
            this.$axios.post(`/gourmet/queryList`, { size: 3, current: 1 }).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.recommendGourmet = data.data;
                }
            }).catch(error => {
                console.log("查询推荐美食做法异常：", error);
            })
        },
    }
};
</script>
<style scoped lang="scss">
.upvote-operation {
    font-weight: 800;
    font-size: 16px;
    cursor: pointer;
    display: inline-block;
    padding: 6px 10px;
    border-radius: 5px;
}

.upvote-operation:hover {
    background-color: rgb(245, 245, 245);
}

.title {
    font-size: 28px;
    font-weight: 800;
    margin-block: 6px;
}

.info {
    font-size: 12px;
    padding: 4px 6px;
    color: rgb(90, 89, 89);
    display: flex;
    justify-content: left;
    align-items: center;
    margin-left: 2px;
    gap: 10px;
    margin-block: 4px;
}


.item-gourmet {
    display: flex;
    justify-content: left;
    gap: 10px;
    padding: 10px;
    border-radius: 5px;
    cursor: pointer;
    border-bottom: 1px solid rgb(245, 245, 245);

    .left {
        img {
            width: 100px;
            border-radius: 5px;
        }
    }

    .right {
        .info {
            padding: 2px;
            display: flex;
            justify-content: left;
            align-items: center;
            gap: 5px;
            font-size: 12px;
            color: rgb(90, 89, 89);
        }

        .title {
            font-size: 14px;
            font-weight: 600;
            overflow: hidden;
            width: 140px;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        .title:hover {
            text-decoration: underline;
            text-decoration-style: dashed;
        }

        .detail {
            font-size: 12px;
            padding: 2px 6px;
            color: rgb(90, 89, 89);
            display: flex;
            justify-content: left;
            gap: 10px;
        }
    }
}
</style>
