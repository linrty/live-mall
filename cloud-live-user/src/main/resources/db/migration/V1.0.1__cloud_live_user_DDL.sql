DROP TABLE IF EXISTS t_user_tag;
CREATE TABLE t_user_tag (
    user_id bigint NOT NULL DEFAULT -1 COMMENT '用户id',
    tag_info_01 bigint NOT NULL DEFAULT 0 COMMENT '标签记录字段',
    tag_info_02 bigint NOT NULL DEFAULT 0 COMMENT '标签记录字段',
    tag_info_03 bigint NOT NULL DEFAULT 0 COMMENT '标签记录字段',
    create_tim datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='用户标签记录';
