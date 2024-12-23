<template>
  <el-card class="recommend-card" shadow="hover" @click.native="$emit('click', recommend)">
    <div class="card-header">
      <h4 class="title">{{ recommend.title }}</h4>
      <el-tag size="small" type="success" class="similarity">
        相似度: {{ formatSimilarity(recommend.similarity) }}
      </el-tag>
    </div>
    <div class="card-content">
      <el-tag size="mini" type="info" class="category">{{ recommend.categoryName }}</el-tag>
      <p class="description">{{ truncateContent(recommend.content) }}</p>
    </div>
  </el-card>
</template>

<script>
export default {
  name: 'RecommendCard',
  
  props: {
    recommend: {
      type: Object,
      required: true
    }
  },
  
  methods: {
    formatSimilarity(similarity) {
      return (similarity * 100).toFixed(1) + '%'
    },
    
    truncateContent(content, length = 100) {
      if (content === null || content === undefined) return '';
      return content.length > length ? content.slice(0, length) + '...' : content;
    }
  }
}
</script>

<style lang="scss" scoped>
.recommend-card {
  margin-bottom: 15px;
  cursor: pointer;
  transition: transform 0.3s;

  &:hover {
    transform: translateY(-5px);
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;

    .title {
      margin: 0;
      font-size: 16px;
      color: #303133;
    }

    .similarity {
      font-size: 12px;
    }
  }

  .card-content {
    .category {
      margin-bottom: 8px;
    }

    .description {
      margin: 0;
      font-size: 14px;
      color: #606266;
      line-height: 1.5;
    }
  }
}
</style>
