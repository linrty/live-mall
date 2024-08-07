DROP TABLE IF EXISTS t_pay_topic;
CREATE TABLE t_pay_topic (
       id bigint unsigned NOT NULL AUTO_INCREMENT,
       topic varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'mq主题',
       status tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效',
       biz_code int NOT NULL COMMENT '业务code',
       remark varchar(200) NOT NULL COMMENT '描述',
       create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
       update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
       PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='支付主题配置表';
