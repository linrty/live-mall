DROP TABLE IF EXISTS t_red_packet_config;
CREATE TABLE t_red_packet_config (
   id int unsigned NOT NULL AUTO_INCREMENT,
   anchor_id bigint NOT NULL DEFAULT '0' COMMENT '主播id',
   start_time datetime DEFAULT NULL COMMENT '红包雨活动开始时间',
   total_get int NOT NULL DEFAULT '0' COMMENT '一共领取数量',
   total_get_price int NOT NULL DEFAULT '0' COMMENT '一共领取金额',
   max_get_price int NOT NULL DEFAULT '0' COMMENT '最大领取金额',
   status tinyint NOT NULL DEFAULT '1' COMMENT '(1 待准备，2已准备，3已发送)',
   total_price int NOT NULL DEFAULT '0' COMMENT '红包雨总金额数',
   total_count int unsigned NOT NULL DEFAULT '0' COMMENT '红包雨总红包数',
   config_code varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '唯一code',
   remark varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '备注',
   create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
   update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='直播间红包雨配置';

