import request from '@/utils/request'

/**
 * 获取相似内容推荐
 * @param {Number} gourmetId 美食ID
 * @param {Number} size 推荐数量
 * @returns {Promise} 推荐结果
 */
export function getSimilarContent(gourmetId, size = 5) {
  return request({
    url: '/content/recommend/similar',
    method: 'get',
    params: { gourmetId, size }
  })
}

/**
 * 获取内容特征
 * @param {Number} gourmetId 美食ID
 * @returns {Promise} 特征数据
 */
export function getContentFeatures(gourmetId) {
  return request({
    url: '/content/recommend/features',
    method: 'get',
    params: { gourmetId }
  })
}
