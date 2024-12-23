import request from '@/utils/request'

// 基于内容的推荐API封装
export const recommendApi = {
  // 获取相似美食推荐
  getSimilarRecommends(gourmetId, size = 5) {
    return request({
      url: '/content/recommend/similar',
      method: 'get',
      params: {
        gourmetId,
        size
      }
    })
  },
  
  // 获取内容特征（用于调试和展示）
  getContentFeatures(gourmetId) {
    return request({
      url: '/recommend/features',
      method: 'get',
      params: {
        gourmetId
      }
    })
  }
}
