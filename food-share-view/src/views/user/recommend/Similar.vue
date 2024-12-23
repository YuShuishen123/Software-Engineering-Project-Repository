<template>
  <div class="similar-page">
    <div class="similar-page__header">
      <h2>相似美食推荐</h2>
      <feature-cloud 
        v-if="features && showFeatures"
        :features="features"
        class="similar-page__features"
      />
    </div>
    
    <similar-list
      :gourmet-id="gourmetId"
      :size="5"
      @item-click="handleItemClick"
      class="similar-page__list"
    />
  </div>
</template>

<script>
import SimilarList from '@/components/content/SimilarList.vue'
import FeatureCloud from '@/components/content/FeatureCloud.vue'
import { getContentFeatures } from '@/api/content'

export default {
  name: 'Similar',
  components: {
    SimilarList,
    FeatureCloud
  },
  data() {
    return {
      features: null,
      showFeatures: false  // 暂时隐藏特征云，等待后端API实现
    }
  },
  computed: {
    gourmetId() {
      return parseInt(this.$route.params.id)
    }
  },
  created() {
    // 暂时注释掉特征获取，等待后端API实现
    // this.fetchFeatures()
  },
  methods: {
    async fetchFeatures() {
      if (!this.gourmetId) return
      
      try {
        const { data } = await getContentFeatures(this.gourmetId)
        this.features = data
        this.showFeatures = true
      } catch (error) {
        console.error('Failed to fetch features:', error)
        this.showFeatures = false
      }
    },
    handleItemClick(item) {
      this.$router.push(`/user/gourmet/detail/${item.id}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.similar-page {
  padding: 20px;

  &__header {
    margin-bottom: 20px;

    h2 {
      margin: 0 0 15px;
      font-size: 24px;
      color: #303133;
    }
  }

  &__features {
    margin-bottom: 20px;
  }

  &__list {
    background: #f5f7fa;
    border-radius: 4px;
    padding: 20px;
  }
}
</style>
