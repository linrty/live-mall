DROP TABLE IF EXISTS t_gift_record;
CREATE TABLE t_gift_record (
     id int unsigned NOT NULL AUTO_INCREMENT,
     user_id bigint DEFAULT NULL COMMENT '发送人',
     object_id bigint DEFAULT NULL COMMENT '收礼人',
     gift_id int DEFAULT NULL COMMENT '礼物id',
     price int DEFAULT NULL COMMENT '送礼金额',
     price_unit tinyint DEFAULT NULL COMMENT '送礼金额的单位',
     source tinyint DEFAULT NULL COMMENT '礼物来源',
     send_time datetime DEFAULT NULL COMMENT '发送时间',
     update_time datetime DEFAULT NULL COMMENT '更新时间',
     json json DEFAULT NULL,
     PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='送礼记录表';
