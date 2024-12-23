<template>
    <el-tabs type="border-card">
        <el-tab-pane label="我的食谱">
            <MyCookbook />
        </el-tab-pane>
        <el-tab-pane label="我的营养素">
            <MyNutrimentManage />
        </el-tab-pane>
        <el-tab-pane label="食谱配置">
            <MyCookbookNutrimentManage/>
        </el-tab-pane>
    </el-tabs>
</template>
<script>
import MyCookbook from "@/views/user/MyCookbook"
import MyNutrimentManage from "@/views/user/MyNutrimentManage"
import MyCookbookNutrimentManage from "@/views/user/MyCookbookNutrimentManage"
export default {
    components: { MyCookbook, MyNutrimentManage, MyCookbookNutrimentManage },
    name: "CookbookOperation",
    data() {
        return {

        }
    },
    created() {

    },
    methods: {

    }
};
</script>
<style scoped lang="scss"></style>
