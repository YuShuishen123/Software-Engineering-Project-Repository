<template>
  <div class="content-recommend">
    <div class="recommend-header">
      <h3 class="title">相似美食推荐</h3>
    </div>
    
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-state">
      <el-skeleton :rows="3" animated />
    </div>
    
    <!-- 推荐列表 -->
    <div v-else class="recommend-list">
      <div v-if="recommendList && recommendList.length">
        <div v-for="item in recommendList" 
             :key="item.id" 
             class="gourmet-item"
             @click="readGourmet(item)">
          <div class="left">
            <el-image 
              :src="item.cover" 
              fit="cover">
              <div slot="error" class="image-slot">
                <i class="el-icon-picture-outline"></i>
              </div>
            </el-image>
          </div>
          <div class="right">
            <div class="info">
              <div class="user">
                <el-avatar 
                  :size="20" 
                  :src="item.userAvatar">
                  <i class="el-icon-user-solid"></i>
                </el-avatar>
                <span>{{ item.userName }}</span>
              </div>
              <div class="title">{{ item.title }}</div>
              <div class="stats">
                <span class="view-count">
                  <i class="el-icon-view"></i>{{ item.viewCount }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <el-empty 
        v-else
        description="暂无相似推荐"
        :image-size="200"
      />
    </div>
  </div>
</template>

<script>
import { recommendApi } from '@/api/recommend'
import { timeAgo } from "@/utils/data"

export default {
  name: 'ContentRecommend',
  
  props: {
    gourmetId: {
      type: Number,
      required: true
    },
    size: {
      type: Number,
      default: 5
    }
  },
  
  data() {
    return {
      loading: false,
      recommendList: [],
      error: null
    }
  },
  
  watch: {
    gourmetId: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.loadRecommends()
        }
      }
    }
  },
  
  methods: {
    timeOut(time) {
      return timeAgo(time)
    },

    async loadRecommends() {
      this.loading = true
      try {
        const response = await recommendApi.getSimilarRecommends(this.gourmetId, this.size)
      
        if (response && response.data && response.data.code === 200) {
          // 确保数据格式正确
          this.recommendList = response.data.data.map(item => ({
            ...item,
            id: item.id || null,
            title: item.title || '',
            categoryName: item.categoryName || '',
            cover: item.cover || '',
            userAvatar: item.userAvatar || '',
            userName: item.userName || '',
            createTime: item.createTime || '',
            viewCount: item.viewCount || 0
          }))
        }
      } catch (error) {
        console.error('Failed to load recommendations:', error)
        this.$message.error('加载推荐失败')
      } finally {
        this.loading = false
      }
    },
    
    readGourmet(gourmet) {
      console.log("点击推荐美食：", gourmet);
      
      try {
        const gourmetId = gourmet.id;
        if (!gourmetId) {
          console.error('Missing gourmet id:', gourmet);
          this.$message.error('美食信息不完整');
          return;
        }

        // 更新 sessionStorage 中的 gourmetId
        sessionStorage.setItem('gourmetId', gourmetId);
        
        // 触发父组件的事件处理
        this.$emit('recommend-click', gourmet);
      } catch (error) {
        console.error('Error handling gourmet click:', error);
        this.$message.error('操作失败');
      }
    }
  }
}
</script>

<style scoped lang="scss">
.content-recommend {
  padding: 10px 30px;
  box-sizing: border-box;
}

.recommend-header {
  margin-bottom: 20px;
}

.recommend-header .title {
  margin: 0;
  font-size: 18px;
  color: #303133;
}

.recommend-list {
  width: 100%;
  margin-top: 20px;
}

.gourmet-item {
  display: flex;
  padding: 12px;
  margin-bottom: 16px;
  border-radius: 8px;
  background-color: #fff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.1);
  }

  .left {
    flex: 0 0 120px;
    margin-right: 16px;

    .el-image {
      width: 120px;
      height: 90px;
      border-radius: 6px;
      overflow: hidden;
    }

    .image-slot {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 100%;
      height: 100%;
      background: #f5f7fa;
      color: #909399;
      font-size: 20px;
    }
  }

  .right {
    flex: 1;
    min-width: 0; // 防止内容溢出
    display: flex;
    flex-direction: column;
  }

  .info {
    font-size: 14px;
    color: #606266;
  }

  .info .user {
    display: flex;
    align-items: center;
    margin-bottom: 8px;
  }

  .info .user img {
    width: 24px;
    height: 24px;
    border-radius: 50%;
    margin-right: 8px;
  }

  .info .title {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 8px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    color: #303133;
  }

  .info .stats {
    font-size: 13px;
    color: #909399;
    display: flex;
    align-items: center;
  }

  .info .stats .el-rate {
    display: inline-flex;
    margin-right: 16px;
  }

  .info .stats .view-count,
  .info .stats .like-count {
    margin-right: 16px;
    display: inline-flex;
    align-items: center;

    i {
      margin-right: 4px;
    }
  }
}
</style>