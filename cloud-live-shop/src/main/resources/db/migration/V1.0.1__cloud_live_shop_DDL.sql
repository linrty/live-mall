DROP TABLE IF EXISTS t_sku_order_info;
CREATE TABLE t_sku_order_info (
    id int unsigned NOT NULL AUTO_INCREMENT,
    sku_id_list varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    user_id bigint unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
    room_id int unsigned NOT NULL DEFAULT '0' COMMENT '直播id',
    status int unsigned NOT NULL DEFAULT '0' COMMENT '状态',
    extra varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品订单表';


