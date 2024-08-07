DROP TABLE IF EXISTS t_anchor_shop_info;
CREATE TABLE t_anchor_shop_info (
      id int unsigned NOT NULL AUTO_INCREMENT,
      anchor_id int unsigned NOT NULL DEFAULT '0' COMMENT '主播id',
      sku_id int unsigned NOT NULL DEFAULT '0' COMMENT '商品sku id',
      status tinyint unsigned NOT NULL DEFAULT '1' COMMENT '有效（0无效，1有效）',
      create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='带货主播权限配置表';


