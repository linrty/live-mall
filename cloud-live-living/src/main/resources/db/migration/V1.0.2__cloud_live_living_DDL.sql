DROP TABLE IF EXISTS t_gift_config;
CREATE TABLE t_gift_config (
     gift_id int unsigned NOT NULL AUTO_INCREMENT COMMENT '礼物id',
     price int unsigned DEFAULT NULL COMMENT '虚拟货币价格',
     gift_name varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '礼物名称',
     status tinyint unsigned DEFAULT NULL COMMENT '状态(0无效,1有效)',
     cover_img_url varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '礼物封面地址',
     svga_url varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'svga资源地址',
     create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (gift_id)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='礼物配置表';