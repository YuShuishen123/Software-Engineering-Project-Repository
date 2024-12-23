import { formatDate } from '@/utils/data'

/**
 * 格式化推荐数据
 * @param {Object} data 原始数据
 * @returns {Object} 格式化后的数据
 */
export function formatRecommendData(data) {
  if (!data) return null
  return {
    ...data,
    createTime: formatDate(data.createTime),
    similarity: data.similarity ? (data.similarity * 100).toFixed(1) + '%' : '0%'
  }
}

/**
 * 格式化特征标签
 * @param {Object} features 特征数据
 * @returns {Array} 格式化后的标签数组
 */
export function formatFeatureTags(features) {
  if (!features) return []
  return Object.entries(features)
    .map(([key, value]) => ({
      name: key,
      weight: value,
      size: Math.max(12, Math.floor(value * 24)) // 12-24px
    }))
    .sort((a, b) => b.weight - a.weight)
}
