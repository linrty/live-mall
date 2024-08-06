DROP TABLE IF EXISTS t_pay_order;
CREATE TABLE t_pay_order (
     id int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
     order_id varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单id',
     product_id int DEFAULT NULL COMMENT '产品id',
     user_id bigint DEFAULT NULL COMMENT '用户id',
     source tinyint DEFAULT NULL COMMENT '来源（0微信支付，1支付宝支付）',
     status tinyint DEFAULT NULL COMMENT '订单状态（0未支付，1已支付，2已退款，3已关闭）',
     pay_time datetime DEFAULT NULL COMMENT '支付时间',
     create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='充值订单表';