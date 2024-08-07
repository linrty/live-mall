DROP TABLE IF EXISTS t_sku_info;
CREATE TABLE t_sku_info (
      id int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
      sku_id int unsigned NOT NULL DEFAULT '0' COMMENT 'sku id',
      sku_price int unsigned NOT NULL DEFAULT '0' COMMENT 'sku价格',
      sku_code varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'sku编码',
      name varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '商品名称',
      icon_url varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '缩略图',
      original_icon_url varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '原图',
      remark varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品描述',
      status tinyint unsigned NOT NULL DEFAULT '0' COMMENT '状态(0下架，1上架)',
      category_id int NOT NULL COMMENT '类目id',
      create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品sku信息表';

