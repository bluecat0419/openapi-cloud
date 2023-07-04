SET NAMES utf8mb4;

-- ----------------------------
-- 1、用户表
-- ----------------------------
CREATE TABLE `user` (
                        `id` bigint(20) NOT NULL,
                        `username` varchar(50) NOT NULL COMMENT '用户名',
                        `password` varchar(500) DEFAULT NULL COMMENT '密码',
                        `ava_url` varchar(500) DEFAULT NULL COMMENT '头像地址',
                        `real_name` varchar(20) DEFAULT NULL COMMENT '真实姓名',
                        `gender` tinyint(3) DEFAULT NULL COMMENT '性别   0：男   1：女',
                        `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
                        `mobile` varchar(50) DEFAULT NULL COMMENT '手机号',
                        `status` tinyint(2) DEFAULT '1' COMMENT '状态  0：停用   1：正常',
                        `access_key` varchar(500) DEFAULT NULL COMMENT '签名 accessKey',
                        `secret_key` varchar(500) DEFAULT NULL COMMENT '签名 secretKey',
                        `is_super_admin` tinyint(2) DEFAULT '0' COMMENT '是否超级管理员  0：否   1：是',
                        `creator` bigint(20) DEFAULT NULL COMMENT '创建者',
                        `create_date` datetime DEFAULT NULL COMMENT '创建时间',
                        `updater` bigint(20) DEFAULT NULL COMMENT '更新者',
                        `update_date` datetime DEFAULT NULL COMMENT '更新时间',
                        `last_login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `uk_username` (`username`) USING BTREE,
                        UNIQUE KEY `uk_email` (`email`) USING BTREE,
                        UNIQUE KEY `uk_mobile` (`mobile`) USING BTREE,
                        UNIQUE KEY `uk_access_key` (`access_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

INSERT INTO `api_cloud`.`user`(`id`, `username`, `password`, `ava_url`, `real_name`, `gender`, `email`, `mobile`, `status`, `access_key`, `secret_key`, `is_super_admin`, `creator`, `create_date`, `updater`, `update_date`, `last_login_date`) VALUES (1650158234709372929, 'admin', '1478ABA7EA0B7D6E1542D4850A5F60243B30509D0D4BDF9E68099F17', '', '管理员', 0, NULL, NULL, 1, '', '', 1, NULL, '2023-04-23 23:22:36', 1650158234709372929, '2023-06-09 11:52:39', '2023-04-23 23:22:36');


-- ----------------------------
-- 2、登录日志表
-- ----------------------------
CREATE TABLE `login_log` (
                             `id` bigint(20) NOT NULL,
                             `type` tinyint(2) DEFAULT NULL COMMENT '用户操作类型   0：用户登录   1：用户退出',
                             `status` tinyint(2) DEFAULT NULL COMMENT '状态  0：失败    1：成功    2：账号已锁定',
                             `login_ip` varchar(32) DEFAULT NULL COMMENT '操作ip地址',
                             `user_agent` varchar(600) DEFAULT NULL COMMENT '用户代理',
                             `username` varchar(50) DEFAULT NULL COMMENT '用户名',
                             `creator` bigint(20) DEFAULT NULL COMMENT '创建人',
                             `create_date` datetime DEFAULT NULL COMMENT '创建时间',
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';


-- ----------------------------
-- 3、接口信息表
-- ----------------------------
CREATE TABLE `interface_info` (
                                  `id` bigint(20) NOT NULL COMMENT '主键',
                                  `name` varchar(256) NOT NULL COMMENT '名称',
                                  `description` varchar(256) DEFAULT NULL COMMENT '描述',
                                  `url` varchar(512) NOT NULL COMMENT '接口地址',
                                  `icon` varchar(1000) DEFAULT NULL COMMENT '图标',
                                  `request_params` text COMMENT '请求参数',
                                  `request_body` text COMMENT '请求体',
                                  `request_header` text COMMENT '请求头',
                                  `response_header` text COMMENT '响应头',
                                  `response_type` varchar(255) DEFAULT 'JSON' COMMENT '返回类型',
                                  `response_data` text NOT NULL COMMENT '返回数据',
                                  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '接口状态（0-关闭，1-开启）',
                                  `method` varchar(256) NOT NULL COMMENT '请求类型',
                                  `code_demo` text COMMENT '代码示例',
                                  `creator` bigint(20) DEFAULT NULL COMMENT '创建者',
                                  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
                                  `updater` bigint(20) DEFAULT NULL COMMENT '更新者',
                                  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
                                  `is_deleted` tinyint(2) DEFAULT '0' COMMENT '是否删除(0-未删, 1-已删)',
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口信息';


-- ----------------------------
-- 4、用户调用接口关系表
-- ----------------------------
CREATE TABLE `user_interface_info` (
                                       `id` bigint(20) NOT NULL COMMENT '主键',
                                       `user_id` bigint(20) NOT NULL COMMENT '调用用户 id',
                                       `interface_info_id` bigint(20) NOT NULL COMMENT '接口 id',
                                       `total_num` int(11) NOT NULL DEFAULT '0' COMMENT '总调用次数',
                                       `left_num` int(11) NOT NULL DEFAULT '0' COMMENT '剩余调用次数',
                                       `status` int(11) NOT NULL DEFAULT '0' COMMENT '0-正常，1-禁用',
                                       `creator` bigint(20) DEFAULT NULL COMMENT '创建者',
                                       `create_date` datetime DEFAULT NULL COMMENT '创建时间',
                                       `updater` bigint(20) DEFAULT NULL COMMENT '更新者',
                                       `update_date` datetime DEFAULT NULL COMMENT '更新时间',
                                       `is_deleted` tinyint(2) DEFAULT '0' COMMENT '是否删除(0-未删, 1-已删)',
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户调用接口关系';


-- ----------------------------
-- 5、接口套餐表
-- ----------------------------
CREATE TABLE `interface_package` (
                                     `id` bigint(20) NOT NULL,
                                     `interface_info_id` bigint(20) NOT NULL COMMENT '接口 id',
                                     `name` varchar(50) NOT NULL COMMENT '套餐名称',
                                     `invoke_count` int(10) NOT NULL COMMENT '接口次数',
                                     `price` double(10,2) NOT NULL DEFAULT '0.00' COMMENT '价格',
                                     `discount` double(10,1) DEFAULT NULL COMMENT '折扣',
                                     `creator` bigint(20) DEFAULT NULL COMMENT '创建者',
                                     `create_date` datetime DEFAULT NULL COMMENT '创建时间',
                                     `expire_date` datetime DEFAULT NULL COMMENT '过期时间',
                                     `status` tinyint(10) DEFAULT '0' COMMENT '状态(0：可用   1：不可用)',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口套餐';


-- ----------------------------
-- 6、接口订单表
-- ----------------------------
CREATE TABLE `interface_orders` (
                                    `id` bigint(20) NOT NULL,
                                    `order_no` varchar(50) NOT NULL COMMENT '订单号(自定义)',
                                    `trade_no` varchar(50) DEFAULT NULL COMMENT '交易号(支付平台方)',
                                    `user_id` bigint(20) NOT NULL COMMENT '用户id',
                                    `interface_package_id` bigint(20) NOT NULL COMMENT '接口套餐id',
                                    `package_info` text COMMENT '创建订单时的套餐信息',
                                    `count` int(10) DEFAULT '1' COMMENT '套餐购买数量',
                                    `total_price` double(10,2) NOT NULL COMMENT '总金额',
                                    `qr_code` blob COMMENT '支付二维码',
                                    `pay_type` tinyint(10) NOT NULL COMMENT '支付方式 (0:支付宝)',
                                    `pay_info` text COMMENT '支付信息  json',
                                    `refund_info` text COMMENT '退款信息  json',
                                    `creator` bigint(20) DEFAULT NULL COMMENT '创建者',
                                    `create_date` datetime DEFAULT NULL COMMENT '创建时间',
                                    `payment_date` datetime DEFAULT NULL COMMENT '付款时间',
                                    `status` tinyint(10) DEFAULT '0' COMMENT '订单状态   (-1：订单已退款   0：订单提交  1：支付成功   2：订单过期 )',
                                    `is_deleted` tinyint(10) DEFAULT '0' COMMENT '是否删除(0-未删, 1-已删)',
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `uk_order_no` (`order_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口购买订单';


-- ----------------------------
-- 7、接口评论表
-- ----------------------------
CREATE TABLE `interface_comments` (
                                      `id` bigint(20) NOT NULL,
                                      `interface_info_id` bigint(20) NOT NULL COMMENT '接口 id',
                                      `user_id` bigint(20) NOT NULL COMMENT '用户 id',
                                      `username` varchar(255) DEFAULT NULL COMMENT '用户名(脱敏后)',
                                      `comment` text NOT NULL COMMENT '评论',
                                      `score` double(10,1) NOT NULL COMMENT '评分',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(2) DEFAULT '0' COMMENT '是否删除(0-未删, 1-已删)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口评论';


-- ----------------------------
-- 8、系统文档表
-- ----------------------------
CREATE TABLE `document` (
                            `id` bigint(20) NOT NULL COMMENT '主键',
                            `title` varchar(255) DEFAULT NULL COMMENT '文档标题',
                            `content` longtext COMMENT '文档内容',
                            `sort` int(10) DEFAULT NULL COMMENT '排序',
                            `creator` bigint(20) DEFAULT NULL COMMENT '创建者',
                            `create_date` datetime DEFAULT NULL COMMENT '创建时间',
                            `updater` bigint(20) DEFAULT NULL COMMENT '更新者',
                            `update_date` datetime DEFAULT NULL COMMENT '更新时间',
                            `is_deleted` tinyint(2) DEFAULT '0' COMMENT '是否删除(0-未删, 1-已删)',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
