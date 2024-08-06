DROP TABLE IF EXISTS t_currency_trade;
CREATE TABLE t_currency_trade (
     id bigint unsigned NOT NULL AUTO_INCREMENT,
     user_id bigint DEFAULT NULL COMMENT '用户id',
     num int DEFAULT NULL COMMENT '流水金额（单位：分）',
     type tinyint DEFAULT NULL COMMENT '流水类型',
     status tinyint DEFAULT '1' COMMENT '状态0无效1有效',
     create_time datetime DEFAULT CURRENT_TIMESTAMP,
     update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
     PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=869 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='流水记录表';