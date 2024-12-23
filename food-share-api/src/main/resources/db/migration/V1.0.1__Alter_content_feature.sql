-- 修改 feature_vector 字段类型为 TEXT
ALTER TABLE content_feature MODIFY COLUMN feature_vector TEXT NOT NULL COMMENT '特征向量JSON';

-- 添加索引
CREATE INDEX idx_gourmet_create_time ON content_feature (gourmet_id, create_time DESC);
