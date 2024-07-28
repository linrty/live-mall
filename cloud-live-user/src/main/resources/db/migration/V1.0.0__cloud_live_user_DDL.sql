DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
      user_id bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
      nick_name varchar(35)  DEFAULT NULL COMMENT '昵称',
      avatar varchar(255)  DEFAULT NULL COMMENT '头像',
      true_name varchar(20)  DEFAULT NULL COMMENT '真实姓名',
      sex tinyint(1) DEFAULT NULL COMMENT '性别 0男，1女',
      born_date datetime DEFAULT NULL COMMENT '出生时间',
      work_city int(9) DEFAULT NULL COMMENT '工作地',
      born_city int(9) DEFAULT NULL COMMENT '出生地',
      create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (user_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;
