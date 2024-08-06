DROP TABLE IF EXISTS t_currency_account;
CREATE TABLE t_currency_account (
   user_id bigint unsigned NOT NULL COMMENT '用户id',
   current_balance int DEFAULT NULL COMMENT '当前余额',
   total_charged int DEFAULT NULL COMMENT '累计充值',
   status tinyint DEFAULT '1' COMMENT '账户状态(0无效1有效 2冻结）',
   create_time datetime DEFAULT CURRENT_TIMESTAMP,
   update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='账户余额表';

