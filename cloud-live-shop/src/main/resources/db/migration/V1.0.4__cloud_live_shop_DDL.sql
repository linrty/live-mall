DROP TABLE IF EXISTS t_category_info;
CREATE TABLE t_category_info (
   id int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
   level int unsigned NOT NULL DEFAULT '0' COMMENT '类目级别',
   parent_id int unsigned NOT NULL DEFAULT '0' COMMENT '父类目id',
   category_name varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类目名称',
   status tinyint unsigned NOT NULL DEFAULT '0' COMMENT '状态（0无效，1有效）',
   create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
   PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='类目表';
