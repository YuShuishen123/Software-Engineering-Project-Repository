<template>
    <div style="margin: 0 auto;width: 1000px;background-color: rgb(255,255,255);border-radius: 2px;padding: 40px;">
        <div style="position: sticky;top: 0;z-index: 1000;background-color: rgb(255,255,255);">
            <div style="font-size: 40px;font-weight: bold;padding-block: 10px;">
                {{ cookbook.title }}
            </div>
            <div style="font-size: 14px;border-bottom: 1px solid rgb(246,246,246);padding-block: 6px;">
                <span style="margin-right: 15px;">所属菜系：{{ cookbook.categoryName }}</span>
                <span>收录于：{{ cookbook.createTime }}</span>
            </div>
        </div>

        <div>
            <div v-html="cookbook.content"></div>
        </div>
        <div style="border-top: 1px solid rgb(245,245,245);">
            <Evaluations :contentId="cookbook.id" contentType="COOKBOOK" />
        </div>
    </div>
</template>
<script>
import Evaluations from "@/components/Evaluations"
export default {
    components: { Evaluations },
    name: "CookbookDetail",
    data() {
        return {
            cookbook: {}
        }
    },
    created() {
        this.loadCookbookDetail();
    },
    methods: {
        loadCookbookDetail() {
            const jsonCookbook = sessionStorage.getItem('cookbookInfo');
            this.cookbook = JSON.parse(jsonCookbook);
        },
    }
};
</script>
<style scoped lang="scss"></style>
