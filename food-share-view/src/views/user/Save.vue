<template>
    <div style="background-color: rgb(255,255,255);padding: 20px;">
        <div v-if="collectionList.length === 0">
            <el-empty description="暂无收藏"></el-empty>
        </div>
        <div v-else>
            <h3>我的收藏({{ collectionList.length }})</h3>
            <div style="display: flex;justify-content: left;" class="item-gourmet"
                v-for="(gourmet, index) in collectionList" :key="index">
                <div class="left">
                    <img :src="gourmet.cover">
                </div>
                <div class="right">
                    <div class="info">
                        <img style="width: 20px;height: 20px;border-radius: 10px;" :src="gourmet.userAvatar" alt="">
                        <span>{{ gourmet.userName }}</span>
                    </div>
                    <div class="title" @click="readGourmet(gourmet)">
                        {{ gourmet.title }}
                    </div>
                    <div class="detail">
                        {{ gourmet.detail }}
                    </div>
                    <div class="detail">
                        <span>
                            {{ gourmet.createTime }}
                        </span>
                        <span>
                            <i class="el-icon-discount" style="margin-right: 4px;"></i>({{ gourmet.upvoteCount }})
                        </span>
                        <span>
                            <i class="el-icon-view" style="margin-right: 4px;"></i>({{ gourmet.viewCount }})
                        </span>
                        <span>
                            <i class="el-icon-star-off" style="margin-right: 4px;"></i>({{ gourmet.saveCount }})
                        </span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
export default {
    name: "Save",
    data() {
        return {
            collectionList: [],
        }
    },
    created() {
        this.fetchCollectionData();
    },
    methods: {
        readGourmet(gourmet) {
            sessionStorage.setItem('gourmetId', gourmet.id);
            this.$router.push('/gourmetDetail');
        },
        handleFilterClear() {
            this.filterText = '';
            this.fetchCollectionData();
        },
        // 查询我自己收藏的内容
        fetchCollectionData() {
            this.$axios.post('/interaction/queryCollectionList', {}).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.collectionList = res.data.data;
                }
            }).catch(error => {
                console.log(error);
            });
        },
    }
};
</script>
<style scoped lang="scss">
.item-category {
    display: inline-block;
    padding: 4px 12px;
    border-radius: 5px;
    margin-right: 2px;
    font-size: 16px;
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
