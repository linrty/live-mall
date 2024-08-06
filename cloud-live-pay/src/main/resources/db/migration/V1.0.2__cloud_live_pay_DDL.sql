DROP TABLE IF EXISTS t_pay_product;
CREATE TABLE t_pay_product (
     id int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
     name varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '产品名称',
     price int DEFAULT '0' COMMENT '产品价格（单位分）',
     extra varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '扩展字段',
     type tinyint DEFAULT '0' COMMENT '类型（0旗鱼直播间产品）',
     valid_status tinyint DEFAULT '0' COMMENT '状态（0无效，1有效）',
     create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='付费产品表';