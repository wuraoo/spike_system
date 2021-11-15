/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : localhost:3306
 Source Schema         : spike_system

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 15/11/2021 22:56:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for spike_system_goods
-- ----------------------------
DROP TABLE IF EXISTS `spike_system_goods`;
CREATE TABLE `spike_system_goods`  (
  `id` bigint NOT NULL COMMENT '商品id',
  `goods_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `goods_title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品标题',
  `goods_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片',
  `goods_detail` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品描述',
  `goods_price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '商品价格',
  `goods_stock` int NULL DEFAULT 0 COMMENT '商品库存，-1表示没有限制',
  `is_deleted` int NULL DEFAULT 0 COMMENT '逻辑删除',
  `gmt_create` datetime NULL DEFAULT NULL COMMENT '记录创建时间',
  `gmt_modified` datetime NULL DEFAULT NULL COMMENT '记录最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for spike_system_order
-- ----------------------------
DROP TABLE IF EXISTS `spike_system_order`;
CREATE TABLE `spike_system_order`  (
  `id` bigint NOT NULL COMMENT '订单id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `goods_id` bigint NOT NULL COMMENT '商品id',
  `user_addr` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户收货地址',
  `goods_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品名称',
  `goods_count` int NULL DEFAULT 0 COMMENT '商品数量',
  `goods_price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '商品价格',
  `goods_state` tinyint NULL DEFAULT 0 COMMENT '订单状态：0.未支付 1.已支付 2.已发货 3.已收货 4.退款 5.已完成',
  `is_deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0未删除 1已删除',
  `gmt_create` datetime NULL DEFAULT NULL COMMENT '记录创建时间',
  `gmt_modified` datetime NULL DEFAULT NULL COMMENT '记录最后修改时间',
  `gmt_pay` datetime NULL DEFAULT NULL COMMENT '订单支付时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for spike_system_skgoods
-- ----------------------------
DROP TABLE IF EXISTS `spike_system_skgoods`;
CREATE TABLE `spike_system_skgoods`  (
  `id` bigint NOT NULL COMMENT '秒杀商品id',
  `goods_id` bigint NOT NULL COMMENT '商品id，关联商品表原始信息',
  `spike_price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '秒杀价格',
  `spike_stock` int NULL DEFAULT 0 COMMENT '秒杀库存，-1表示没有限制',
  `start_date` datetime NULL DEFAULT NULL COMMENT '秒杀开始时间',
  `end_date` datetime NULL DEFAULT NULL COMMENT '秒杀结束时间',
  `is_deleted` int NULL DEFAULT 0 COMMENT '逻辑删除',
  `gmt_create` datetime NULL DEFAULT NULL COMMENT '记录创建时间',
  `gmt_modified` datetime NULL DEFAULT NULL COMMENT '记录最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for spike_system_skorder
-- ----------------------------
DROP TABLE IF EXISTS `spike_system_skorder`;
CREATE TABLE `spike_system_skorder`  (
  `id` bigint NOT NULL COMMENT '秒杀订单id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `goods_id` bigint NOT NULL COMMENT '商品id',
  `order_id` bigint NOT NULL COMMENT '订单id',
  `is_deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0未删除 1已删除',
  `gmt_create` datetime NULL DEFAULT NULL COMMENT '记录创建时间',
  `gmt_modified` datetime NULL DEFAULT NULL COMMENT '记录最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for spike_system_user
-- ----------------------------
DROP TABLE IF EXISTS `spike_system_user`;
CREATE TABLE `spike_system_user`  (
  `id` bigint NOT NULL COMMENT '用户id，使用MP的雪花算法',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci NOT NULL COMMENT '昵称',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci NOT NULL COMMENT 'MD5( pwd + slat )',
  `slat` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci NULL DEFAULT NULL COMMENT '盐值',
  `head` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci NULL DEFAULT NULL COMMENT '头像',
  `logincount` int NULL DEFAULT 0 COMMENT '登录次数',
  `is_deleted` int NULL DEFAULT 0 COMMENT '逻辑删除',
  `gmt_create` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
