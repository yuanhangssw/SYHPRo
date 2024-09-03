/*
 Navicat Premium Data Transfer

 Source Server         : tj
 Source Server Type    : MySQL
 Source Server Version : 50738
 Source Host           : localhost:3306
 Source Schema         : bypucha

 Target Server Type    : MySQL
 Target Server Version : 50738
 File Encoding         : 65001

 Date: 07/04/2024 18:45:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for b_audit
-- ----------------------------
DROP TABLE IF EXISTS `b_audit`;
CREATE TABLE `b_audit`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `patrol_id` int(11) NULL DEFAULT NULL COMMENT '巡查id',
  `audit_user` int(11) NULL DEFAULT NULL COMMENT '审核人',
  `audit_opinion` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '审核意见',
  `audit_status` int(11) NULL DEFAULT NULL COMMENT '审核状态（通过、未通过）',
  `current_dept` int(11) NULL DEFAULT NULL COMMENT '当前审核部门',
  `subordinate_dept` int(11) NULL DEFAULT NULL COMMENT '下级审核部门',
  `data_state` int(11) NULL DEFAULT NULL COMMENT '数据状态(1进行中2流程中止3完成)',
  `freedom1` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom2` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom3` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom4` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom5` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '审核记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_audit
-- ----------------------------
INSERT INTO `b_audit` VALUES (1, 1, NULL, '11111', 2, 101, 100, 2, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_audit` VALUES (2, 2, NULL, '22222', 1, 101, 100, 3, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_audit` VALUES (3, 3, NULL, '33333', 2, 101, 100, 2, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_audit` VALUES (4, 4, NULL, '444', 2, 101, 100, 2, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_audit` VALUES (5, 1, NULL, '111', 2, 101, 100, 2, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_audit` VALUES (6, 1, NULL, '11111', 1, 101, 100, 3, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_audit` VALUES (7, 4, NULL, NULL, NULL, 101, 100, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_audit` VALUES (8, 5, NULL, '5555', 2, 101, 100, 2, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_audit` VALUES (9, 5, NULL, NULL, NULL, 101, 100, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_audit` VALUES (10, 6, NULL, NULL, 2, 101, 100, 2, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_audit` VALUES (11, 6, NULL, NULL, 1, 101, 100, 3, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_audit` VALUES (12, 7, NULL, NULL, 2, 101, 100, 2, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_audit` VALUES (13, 8, NULL, NULL, NULL, 101, 100, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_audit` VALUES (14, 7, NULL, NULL, NULL, 101, 100, 1, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for b_dyke_prevention
-- ----------------------------
DROP TABLE IF EXISTS `b_dyke_prevention`;
CREATE TABLE `b_dyke_prevention`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `detriment_type` int(255) NULL DEFAULT NULL COMMENT '危害类型',
  `project_id` int(11) NULL DEFAULT NULL COMMENT '项目',
  `unit_id` int(11) NULL DEFAULT NULL COMMENT '单元id',
  `detriment_level` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '危害等级',
  `leaks_number` int(11) NULL DEFAULT NULL COMMENT '渗漏处数',
  `through_number` int(11) NULL DEFAULT NULL COMMENT '穿坝处数',
  `drop_socket_number` int(11) NULL DEFAULT NULL COMMENT '跌窝处数',
  `nest_digging` int(11) NULL DEFAULT NULL COMMENT '挖巢数量 （个）',
  `charge_area` double NULL DEFAULT NULL COMMENT '施药面积 （m2）',
  `grouting_quantity` double NULL DEFAULT NULL COMMENT '灌浆量 （延米）',
  `invest_capital` double NULL DEFAULT NULL COMMENT '投入资金 （万元）',
  `zoon_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '动物种类',
  `zoon_govern_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '动物治理方式',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `detriment_length` double NULL DEFAULT NULL COMMENT '危害长度总计',
  `one_detriment_length` double NULL DEFAULT NULL COMMENT 'I级危害长度',
  `two_detriment_length` double NULL DEFAULT NULL COMMENT 'II级危害长度',
  `three_detriment_length` double NULL DEFAULT NULL COMMENT 'III级危害长度',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '堤防防治情况表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_dyke_prevention
-- ----------------------------

-- ----------------------------
-- Table structure for b_fill
-- ----------------------------
DROP TABLE IF EXISTS `b_fill`;
CREATE TABLE `b_fill`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NULL DEFAULT NULL COMMENT '项目',
  `unit_id` int(11) NULL DEFAULT NULL COMMENT '单元',
  `leaks_number` double NULL DEFAULT NULL COMMENT '渗漏处数',
  `through_number` double NULL DEFAULT NULL COMMENT '穿坝处数',
  `drop_socket_number` double NULL DEFAULT NULL COMMENT '跌窝处数',
  `nest_digging` double NULL DEFAULT NULL COMMENT '挖巢数量 （个）',
  `charge_area` double NULL DEFAULT NULL COMMENT '施药面积 （m2）',
  `grouting_quantity` double NULL DEFAULT NULL COMMENT '灌浆量 （延米）',
  `invest_capital` double NULL DEFAULT NULL COMMENT '投入资金 （万元）',
  `quantity_governance` double NULL DEFAULT NULL COMMENT '治理数量',
  `fill_type` bigint(20) NULL DEFAULT NULL COMMENT '填报类型1白蚁2动物',
  `inspector` int(11) NULL DEFAULT NULL COMMENT '巡查用户',
  `create_time` datetime NULL DEFAULT NULL COMMENT '上传时间',
  `del_flag` char(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
  `freedom1` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom2` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom3` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom4` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom5` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 93 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '填报登记表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_fill
-- ----------------------------
INSERT INTO `b_fill` VALUES (1, 6, 3, 4, 5, 1, 6, 100, 4, 6.5, 8, 1, 1, '2024-03-25 17:08:42', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_fill` VALUES (2, 1, 1, 2, 3, 4, 2, 4, 2, 4.3, 5, 1, 1, '2024-03-25 17:05:49', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_fill` VALUES (3, 1, 14, 3, 4, 5, 6, 7, 8, 9, 0, 1, 1, '2024-03-07 18:49:58', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_fill` VALUES (85, 1, 13, 2, 2, 3, 2, 100, 10, 1, 6, 1, 1, '2024-03-14 10:21:51', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_fill` VALUES (86, 1, 1, 0, 1, 2, 3, 10, 4, 1, 8, 2, 1, '2024-03-15 11:10:52', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_fill` VALUES (87, 1, 13, 1, 0, 0, 0, 0, 0, 0, 0, 2, 1, '2024-03-15 11:36:21', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_fill` VALUES (88, 10, 4, 3, 0, 0, 5, -50, 0, -1000, 0, 1, 4, '2024-03-19 14:42:31', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_fill` VALUES (89, 10, 4, 3, 0, 0, 5, -50, 0, -1000, 0, 1, 4, '2024-03-19 14:42:40', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_fill` VALUES (90, 1, 1, 1, 1, 1, 1, 10, 5, 1.5, 6, 2, 1, '2024-03-25 16:44:26', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_fill` VALUES (91, 1, 1, 1, 0, 0, 0, 0, 0, 0.5, 1, 1, 1, '2024-03-25 16:46:06', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_fill` VALUES (92, 6, 3, 0, 0, 0, 1, 0, 0, 0.5, 0, 2, 1, '2024-03-25 17:09:09', NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for b_inspector
-- ----------------------------
DROP TABLE IF EXISTS `b_inspector`;
CREATE TABLE `b_inspector`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `inspector_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '巡查用户姓名',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '手机号',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '密码',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '部门用户创建者',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门创建的用户',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
  `freedom1` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom2` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom3` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom4` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom5` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '巡查员用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_inspector
-- ----------------------------
INSERT INTO `b_inspector` VALUES (1, 'a', '13521962466', 'MTIzNDU2', 1, 100, '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_inspector` VALUES (2, 'b', '18725544400', 'MTg3MjU1NDQ0MDA=', 1, 100, '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_inspector` VALUES (3, '马', '18734475590', 'MTIzNDU2', 1, 100, '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_inspector` VALUES (4, '孙黎明', '13001011630', 'MTIzNDU2', 1, 100, '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_inspector` VALUES (6, '李亮', '13691386792', 'MTIzNDU2', 1, 102, '0', NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for b_inspector_unit
-- ----------------------------
DROP TABLE IF EXISTS `b_inspector_unit`;
CREATE TABLE `b_inspector_unit`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `inspector` int(11) NULL DEFAULT NULL COMMENT '巡查用户',
  `project_id` int(11) NULL DEFAULT NULL COMMENT '项目id',
  `patrol_unit_id` int(11) NULL DEFAULT NULL COMMENT '巡查单元',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 135 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '巡查用户和巡查单元关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_inspector_unit
-- ----------------------------
INSERT INTO `b_inspector_unit` VALUES (45, 2, 1, 1);
INSERT INTO `b_inspector_unit` VALUES (46, 2, 1, 13);
INSERT INTO `b_inspector_unit` VALUES (47, 2, 1, 14);
INSERT INTO `b_inspector_unit` VALUES (48, 2, 10, 4);
INSERT INTO `b_inspector_unit` VALUES (49, 2, 10, 5);
INSERT INTO `b_inspector_unit` VALUES (50, 2, 10, 6);
INSERT INTO `b_inspector_unit` VALUES (51, 2, 10, 7);
INSERT INTO `b_inspector_unit` VALUES (52, 2, 10, 8);
INSERT INTO `b_inspector_unit` VALUES (53, 2, 10, 9);
INSERT INTO `b_inspector_unit` VALUES (54, 2, 10, 10);
INSERT INTO `b_inspector_unit` VALUES (55, 2, 10, 11);
INSERT INTO `b_inspector_unit` VALUES (56, 2, 10, 12);
INSERT INTO `b_inspector_unit` VALUES (57, 3, 1, 1);
INSERT INTO `b_inspector_unit` VALUES (58, 3, 1, 13);
INSERT INTO `b_inspector_unit` VALUES (59, 3, 1, 14);
INSERT INTO `b_inspector_unit` VALUES (60, 3, 2, 2);
INSERT INTO `b_inspector_unit` VALUES (61, 3, 10, 4);
INSERT INTO `b_inspector_unit` VALUES (62, 3, 10, 5);
INSERT INTO `b_inspector_unit` VALUES (63, 3, 10, 6);
INSERT INTO `b_inspector_unit` VALUES (64, 3, 10, 7);
INSERT INTO `b_inspector_unit` VALUES (65, 3, 10, 8);
INSERT INTO `b_inspector_unit` VALUES (66, 3, 10, 9);
INSERT INTO `b_inspector_unit` VALUES (67, 3, 10, 10);
INSERT INTO `b_inspector_unit` VALUES (68, 3, 10, 11);
INSERT INTO `b_inspector_unit` VALUES (69, 3, 10, 12);
INSERT INTO `b_inspector_unit` VALUES (109, 4, 10, 4);
INSERT INTO `b_inspector_unit` VALUES (110, 4, 10, 5);
INSERT INTO `b_inspector_unit` VALUES (111, 4, 10, 6);
INSERT INTO `b_inspector_unit` VALUES (112, 4, 10, 7);
INSERT INTO `b_inspector_unit` VALUES (113, 4, 10, 8);
INSERT INTO `b_inspector_unit` VALUES (114, 4, 10, 9);
INSERT INTO `b_inspector_unit` VALUES (115, 4, 10, 10);
INSERT INTO `b_inspector_unit` VALUES (116, 4, 10, 11);
INSERT INTO `b_inspector_unit` VALUES (117, 4, 10, 12);
INSERT INTO `b_inspector_unit` VALUES (118, 4, 11, 15);
INSERT INTO `b_inspector_unit` VALUES (119, 4, 1, 1);
INSERT INTO `b_inspector_unit` VALUES (120, 4, 1, 13);
INSERT INTO `b_inspector_unit` VALUES (121, 4, 1, 14);
INSERT INTO `b_inspector_unit` VALUES (122, 4, 6, 3);
INSERT INTO `b_inspector_unit` VALUES (123, 4, 2, 2);
INSERT INTO `b_inspector_unit` VALUES (126, 6, 12, 16);
INSERT INTO `b_inspector_unit` VALUES (127, 6, 6, 3);
INSERT INTO `b_inspector_unit` VALUES (128, 1, 6, 3);
INSERT INTO `b_inspector_unit` VALUES (129, 1, 1, 1);
INSERT INTO `b_inspector_unit` VALUES (130, 1, 1, 13);
INSERT INTO `b_inspector_unit` VALUES (131, 1, 10, 6);
INSERT INTO `b_inspector_unit` VALUES (132, 1, 10, 8);
INSERT INTO `b_inspector_unit` VALUES (133, 1, 13, 17);
INSERT INTO `b_inspector_unit` VALUES (134, 1, 13, 18);

-- ----------------------------
-- Table structure for b_patrol
-- ----------------------------
DROP TABLE IF EXISTS `b_patrol`;
CREATE TABLE `b_patrol`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `patrol_type` bigint(20) NULL DEFAULT NULL COMMENT '巡查类型',
  `patrol_unit` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '巡查单元',
  `inspector_id` int(11) NULL DEFAULT NULL COMMENT '巡查用户',
  `project_id` int(11) NULL DEFAULT NULL COMMENT '工程id',
  `pile_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '桩号',
  `section_position` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '断面位置（顶、上游、下游）',
  `axis_distance` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '轴线距离（正、负）',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '文字描述',
  `patrol_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '巡查时间',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '巡查位置',
  `lon` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '经度',
  `lat` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '纬度',
  `high` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '高程',
  `audit_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '审核状态（1已提交、2审核中、3通过、4未通过）',
  `freedom1` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom2` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom3` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom4` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom5` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '巡查表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_patrol
-- ----------------------------
INSERT INTO `b_patrol` VALUES (1, 1, '3', 1, 6, '1', NULL, NULL, '1111111', '2024-03-27 14:06:22', '', NULL, NULL, NULL, '3', '', '2024-03-27 14:15:58', '1', NULL, NULL);
INSERT INTO `b_patrol` VALUES (2, 1, '3', 1, 6, '11', NULL, NULL, '22222', '2024-03-27 14:06:42', '', NULL, NULL, NULL, '3', '', '2024-03-27 14:13:55', '2', NULL, NULL);
INSERT INTO `b_patrol` VALUES (3, 2, '3', 1, 6, NULL, '上游', NULL, '33333', '2024-03-27 14:07:08', '', NULL, NULL, NULL, '4', '', '2024-03-27 14:16:46', '9', NULL, NULL);
INSERT INTO `b_patrol` VALUES (4, 2, '3', 1, 6, NULL, NULL, NULL, '44444', '2024-03-27 14:07:31', '', NULL, NULL, NULL, '1', '', '2024-03-27 14:13:24', '10', NULL, NULL);
INSERT INTO `b_patrol` VALUES (5, 1, '3', 1, 6, NULL, '下游', NULL, '5555', '2024-03-27 14:18:02', '', NULL, NULL, NULL, '1', '', '2024-03-27 14:19:31', '5', NULL, NULL);
INSERT INTO `b_patrol` VALUES (6, 1, '18', 1, 13, NULL, '顶', NULL, '1111', '2024-03-27 14:33:32', '', NULL, NULL, NULL, '3', '', '2024-03-27 14:35:01', '1', NULL, NULL);
INSERT INTO `b_patrol` VALUES (7, 1, '18', 1, 13, NULL, '上游', NULL, NULL, '2024-03-27 14:35:10', '', NULL, NULL, NULL, '1', '', '2024-03-27 14:35:46', '1', NULL, NULL);
INSERT INTO `b_patrol` VALUES (8, 1, '17', 1, 13, NULL, NULL, NULL, NULL, '2024-03-27 14:35:26', '', NULL, NULL, NULL, '1', '', '2024-03-27 14:35:31', '1', NULL, NULL);

-- ----------------------------
-- Table structure for b_patrol_file
-- ----------------------------
DROP TABLE IF EXISTS `b_patrol_file`;
CREATE TABLE `b_patrol_file`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `patorl_id` int(11) NULL DEFAULT NULL COMMENT '巡查id',
  `file_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '文件地址',
  `del_flag` char(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '上传时间',
  `freedom1` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom2` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom3` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom4` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom5` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '巡查文件表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_patrol_file
-- ----------------------------
INSERT INTO `b_patrol_file` VALUES (1, NULL, '/profile/upload/2024/03/06/hhs-small_20240306192922A010.png', '0', '2024-03-06 19:29:25', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (2, NULL, '/profile/upload/2024/03/06/hhs-small_20240306192922A010.png', '0', '2024-03-06 19:30:09', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (3, NULL, '/profile/upload/2024/03/07/1709780130914_20240307105533A001.jpg', '0', '2024-03-07 10:55:45', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (4, NULL, '/profile/upload/2024/03/07/hhs-small_20240307105754A002.png', '0', '2024-03-07 10:58:03', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (5, NULL, '/profile/upload/2024/03/07/hhs-small_20240307110020A003.png', '0', '2024-03-07 11:01:23', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (6, NULL, '/profile/upload/2024/03/07/login-bg_20240307144858A004.png', '0', '2024-03-07 14:50:30', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (7, NULL, '/profile/upload/2024/03/07/swiper_20240307145006A005.png', '0', '2024-03-07 14:50:30', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (8, NULL, '/profile/upload/2024/03/07/hhs-small_20240307145021A006.png', '0', '2024-03-07 14:50:30', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (9, NULL, '/profile/upload/2024/03/07/baiyi-small_20240307145137A007.png', '0', '2024-03-07 14:51:53', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (10, NULL, '/profile/upload/2024/03/07/bg-img_20240307145145A008.png', '0', '2024-03-07 14:51:54', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (11, NULL, '/profile/upload/2024/03/07/hhs-logo_20240307145147A009.png', '0', '2024-03-07 14:51:54', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (12, NULL, '/profile/upload/2024/03/08/tmp_baa780b940ffcc1250f7d1881eaccd5dd885693f8b8deb56_20240308165034A001.jpg', '0', '2024-03-08 16:50:38', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (13, NULL, '/profile/upload/2024/03/08/tmp_ba264da7c91ab3b18e2dc4dbd1d3a9d8_20240308192857A002.jpg', '0', '2024-03-08 19:29:00', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (14, NULL, '/profile/upload/2024/03/11/baiyi-small_20240311133907A009.png/profile/upload/2024/03/11/unit-bg_20240311133910A010.png/profile/upload/2024/03/11/empty_20240311133913A011.png', '0', '2024-03-11 13:39:28', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (15, NULL, '/profile/upload/2024/03/11/home_20240311134559A015.png', '0', '2024-03-11 13:46:55', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (16, NULL, '/profile/upload/2024/03/11/work__20240311134604A017.png', '0', '2024-03-11 13:46:55', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (17, NULL, '/profile/upload/2024/03/11/mine__20240311134901A018.png', '0', '2024-03-11 13:49:03', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (18, 34, '/profile/upload/2024/03/19/tmp_b1f31576a2781c1449a619ed526a1e19faff8faf3cb93351_20240319144007A001.jpg', '0', '2024-03-19 14:40:24', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (19, 35, '/profile/upload/2024/03/19/tmp_b1f31576a2781c1449a619ed526a1e19faff8faf3cb93351_20240319144007A001.jpg', '0', '2024-03-19 14:40:29', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (20, 36, '/profile/upload/2024/03/19/tmp_b1f31576a2781c1449a619ed526a1e19faff8faf3cb93351_20240319144007A001.jpg', '0', '2024-03-19 14:40:31', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (21, 37, '/profile/upload/2024/03/19/tmp_cb1958a1967d3480a43eed3d772efee834ae1275d95d24ea_20240319144201A002.jpg', '0', '2024-03-19 14:42:34', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (22, 38, '/profile/upload/2024/03/19/tmp_832069f8c4ec3a0462f0a0081bc1838a205731d63d396618_20240319144615A003.jpg', '0', '2024-03-19 14:46:40', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (23, 39, '/profile/upload/2024/03/19/tmp_832069f8c4ec3a0462f0a0081bc1838a205731d63d396618_20240319144615A003.jpg', '0', '2024-03-19 14:46:58', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (24, 40, '/profile/upload/2024/03/19/tmp_832069f8c4ec3a0462f0a0081bc1838a205731d63d396618_20240319144615A003.jpg', '0', '2024-03-19 14:47:02', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (25, 48, '/profile/upload/2024/03/21/tmp_fa7738f52ab58d61ee598199ba760ff18d16702231e6be2d_20240321164537A004.jpg', '0', '2024-03-21 16:45:50', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (26, 50, '/profile/upload/2024/03/23/tmp_930ffa638e3322ba58cbfbf2b1cb4e68_20240323171940A005.jpg', '0', '2024-03-23 17:20:00', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (27, 83, '/profile/upload/2024/03/26/tmp_c8967e9a2dba876242735ee49edfb8f2361c009d7c9c32a0_20240326153127A001.jpg', '0', '2024-03-26 15:32:05', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_file` VALUES (28, 84, '/profile/upload/2024/03/26/tmp_45192df1336e16250581fa1cd50ac7c60e5efadd4e85ed0f_20240326155457A002.jpg', '0', '2024-03-26 15:55:18', NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for b_patrol_type
-- ----------------------------
DROP TABLE IF EXISTS `b_patrol_type`;
CREATE TABLE `b_patrol_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `patrol_type` bigint(20) NULL DEFAULT NULL COMMENT '巡查类型1白蚁2动物',
  `type_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '巡查类型名称',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `freedom1` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom2` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom3` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom4` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom5` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '巡查类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_patrol_type
-- ----------------------------
INSERT INTO `b_patrol_type` VALUES (1, 1, '鸡枞菌', '0', 'admin', '2024-03-05 10:00:09', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_type` VALUES (2, 1, '碳棒菌', '0', 'admin', '2024-03-05 10:00:09', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_type` VALUES (3, 1, '泥被泥线', '0', 'admin', '2024-03-05 10:00:09', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_type` VALUES (4, 1, '分飞孔', '0', 'admin', '2024-03-05 10:00:09', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_type` VALUES (5, 1, '啃食木材', '0', 'admin', '2024-03-05 10:00:09', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_type` VALUES (6, 1, '白蚁', '0', 'admin', '2024-03-05 10:00:09', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_type` VALUES (7, 1, '巢穴', '0', 'admin', '2024-03-05 10:00:09', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_type` VALUES (8, 1, '取食点', '0', 'admin', '2024-03-05 10:00:09', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_type` VALUES (9, 2, '活动迹象', '0', 'admin', '2024-03-05 10:00:09', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_type` VALUES (10, 2, '取食点', '0', 'admin', '2024-03-05 10:00:09', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_type` VALUES (11, 2, '巢穴', '0', 'admin', '2024-03-05 10:00:09', NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for b_patrol_unit
-- ----------------------------
DROP TABLE IF EXISTS `b_patrol_unit`;
CREATE TABLE `b_patrol_unit`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NULL DEFAULT NULL COMMENT '项目id',
  `unit_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '单元名称',
  `project_type` bigint(20) NULL DEFAULT NULL COMMENT '项目类型1水库2堤防',
  `dam_type` int(11) NULL DEFAULT NULL COMMENT '坝型',
  `dam_length` double NULL DEFAULT NULL COMMENT '坝长',
  `dam_higth` double NULL DEFAULT NULL COMMENT '坝高',
  `slope_protection` int(11) NULL DEFAULT NULL COMMENT '护坡方式',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `freedom1` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom2` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom3` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom4` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom5` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '巡查单元表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_patrol_unit
-- ----------------------------
INSERT INTO `b_patrol_unit` VALUES (1, 1, '11号单元', NULL, NULL, NULL, NULL, NULL, '0', NULL, '2024-03-06 15:16:43', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_unit` VALUES (2, 2, '2号单元', NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_unit` VALUES (3, 6, '3号单元', NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_unit` VALUES (4, 10, 'k2+256-k3+00', NULL, NULL, NULL, NULL, NULL, '0', 'admin', '2024-03-06 16:11:01', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_unit` VALUES (5, 10, 'k3+00-k4+00', NULL, NULL, NULL, NULL, NULL, '0', 'admin', '2024-03-06 16:11:01', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_unit` VALUES (6, 10, 'k4+00-k5+00', NULL, NULL, NULL, NULL, NULL, '0', 'admin', '2024-03-06 16:11:01', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_unit` VALUES (7, 10, 'k5+00-k6+00', NULL, NULL, NULL, NULL, NULL, '0', 'admin', '2024-03-06 16:11:01', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_unit` VALUES (8, 10, 'k6+00-k7+00', NULL, NULL, NULL, NULL, NULL, '0', 'admin', '2024-03-06 16:11:01', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_unit` VALUES (9, 10, 'k7+00-k8+00', NULL, NULL, NULL, NULL, NULL, '0', 'admin', '2024-03-06 16:11:01', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_unit` VALUES (10, 10, 'k8+00-k9+00', NULL, NULL, NULL, NULL, NULL, '0', 'admin', '2024-03-06 16:11:01', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_unit` VALUES (11, 10, 'k9+00-k10+00', NULL, NULL, NULL, NULL, NULL, '0', 'admin', '2024-03-06 16:11:02', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_unit` VALUES (12, 10, 'k10+00-k10+200', NULL, NULL, NULL, NULL, NULL, '0', 'admin', '2024-03-06 16:11:02', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_unit` VALUES (13, 1, '12号单元', NULL, NULL, NULL, NULL, NULL, '0', NULL, '2024-03-06 15:16:43', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_unit` VALUES (14, 1, '13号单元', NULL, NULL, NULL, NULL, NULL, '0', NULL, '2024-03-06 15:16:43', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_unit` VALUES (15, 11, '1-11', NULL, NULL, NULL, NULL, NULL, '0', NULL, '2024-03-19 11:47:52', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_unit` VALUES (16, 12, '206', NULL, NULL, NULL, NULL, NULL, '0', NULL, '2024-03-26 15:04:55', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_unit` VALUES (17, 13, '单元1', NULL, NULL, NULL, NULL, NULL, '0', NULL, '2024-03-27 14:32:21', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_patrol_unit` VALUES (18, 13, '单元2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2024-03-27 14:32:29', NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for b_patrol_unit_place
-- ----------------------------
DROP TABLE IF EXISTS `b_patrol_unit_place`;
CREATE TABLE `b_patrol_unit_place`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `patrol_unit` int(11) NULL DEFAULT NULL COMMENT '普查单元',
  `patrol_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '巡查表多个巡查逗号隔开',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '普查处管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_patrol_unit_place
-- ----------------------------

-- ----------------------------
-- Table structure for b_project
-- ----------------------------
DROP TABLE IF EXISTS `b_project`;
CREATE TABLE `b_project`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '项目名称',
  `project_type` bigint(20) NULL DEFAULT NULL COMMENT '项目类型1水库2堤坝',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '所属部门',
  `registration_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '登记号',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '工程所在地',
  `reservoir_grade` int(11) NULL DEFAULT NULL COMMENT '水库等级',
  `dyke_grade` int(11) NULL DEFAULT NULL COMMENT '堤防等级',
  `dyke_length` double NULL DEFAULT NULL COMMENT '堤防长度',
  `dyke_patrol_length` double NULL DEFAULT NULL COMMENT '堤防普查长度',
  `dyke_pile` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '堤防起始桩号',
  `dyke_pile_end` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '堤防终止桩号',
  `dyke_patrol_pile` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '堤防普查起始桩号',
  `dyke_patrol_pile_end` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '堤防普查终止桩号',
  `storage_capacity` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '库容',
  `census_method` int(11) NULL DEFAULT NULL COMMENT '普查方式',
  `person` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '负责人',
  `person_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '手机号',
  `del_flag` char(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `freedom1` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom2` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom3` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom4` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `freedom5` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '项目信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of b_project
-- ----------------------------
INSERT INTO `b_project` VALUES (1, '水库', 1, 100, '1', '1', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '18732445930', '0', NULL, '2024-03-05 18:40:36', NULL, '2024-03-06 09:22:09', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_project` VALUES (2, '水库2', 1, 102, '2', '2', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2', '2', '0', NULL, '2024-03-05 18:42:25', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_project` VALUES (6, '堤坝', 2, 101, '2', '2', NULL, 2, 2, 2, '2', NULL, '2', NULL, NULL, NULL, '2', '2', '0', NULL, '2024-03-05 18:51:05', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_project` VALUES (7, '水库3', 1, 101, '1', '1', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '122', '0', NULL, '2024-03-05 18:51:23', NULL, '2024-03-06 09:21:25', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_project` VALUES (8, '堤坝2', 2, 100, '1', '1', NULL, 1, 1, 1, '1', NULL, '1', NULL, NULL, NULL, '1', '1', '0', NULL, '2024-03-05 18:51:37', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_project` VALUES (9, '水库4', 1, 100, '4', '4', 4, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '4', '4', '0', NULL, '2024-03-06 08:55:44', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_project` VALUES (10, '堤防3', 2, 101, '3', '3', NULL, 3, 3, 3, '3', '3', 'k2+256', 'k10+200', NULL, NULL, '3', '13333333333', '0', NULL, '2024-03-06 16:11:01', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_project` VALUES (11, '水库项目测试-', 1, 108, '121212', '1212', 12, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '李亮', '13691145478', '0', NULL, '2024-03-19 11:47:39', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_project` VALUES (12, '斋堂水库', 1, 102, '111111', '北京', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '李亮', '13691386792', '0', NULL, '2024-03-26 15:04:33', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `b_project` VALUES (13, 'T-1', 1, 101, '123655', '广西', 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'a', '13521962466', '0', NULL, '2024-03-27 14:30:54', NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for b_reservoir_prevention
-- ----------------------------
DROP TABLE IF EXISTS `b_reservoir_prevention`;
CREATE TABLE `b_reservoir_prevention`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `detriment_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '危害类型',
  `project_id` int(11) NULL DEFAULT NULL COMMENT '项目',
  `unit_id` int(11) NULL DEFAULT NULL COMMENT '单元id',
  `detriment_level` int(11) NULL DEFAULT NULL COMMENT '危害等级',
  `leaks_number` int(11) NULL DEFAULT NULL COMMENT '渗漏处数',
  `through_number` int(11) NULL DEFAULT NULL COMMENT '穿坝处数',
  `drop_socket_number` int(11) NULL DEFAULT NULL COMMENT '跌窝处数',
  `nest_digging` int(11) NULL DEFAULT NULL COMMENT '挖巢数量 （个）',
  `charge_area` double NULL DEFAULT NULL COMMENT '施药面积 （m2）',
  `grouting_quantity` double NULL DEFAULT NULL COMMENT '灌浆量 （延米）',
  `invest_capital` double NULL DEFAULT NULL COMMENT '投入资金 （万元）',
  `zoon_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '动物种类',
  `zoon_govern_number` int(11) NULL DEFAULT NULL COMMENT '动物治理数量',
  `zoon_govern_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '动物治理方式',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '水库防治情况表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_reservoir_prevention
-- ----------------------------

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `tpl_web_type` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '前端模板类型（element-ui模版 element-plus模版）',
  `package_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '代码生成业务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table
-- ----------------------------
INSERT INTO `gen_table` VALUES (1, 'b_audit', '审核记录表', NULL, NULL, 'BAudit', 'crud', '', 'com.ruoyi.system', 'system', 'audit', '审核记录', 'ruoyi', '0', '/', NULL, 'admin', '2024-03-05 13:50:52', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (2, 'b_danger', '致险情况登记表', NULL, NULL, 'BDanger', 'crud', '', 'com.ruoyi.system', 'system', 'danger', '致险情况登记', 'ruoyi', '0', '/', NULL, 'admin', '2024-03-05 13:50:52', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (3, 'b_govern', '治理情况登记表', NULL, NULL, 'BGovern', 'crud', '', 'com.ruoyi.system', 'system', 'govern', '治理情况登记', 'ruoyi', '0', '/', NULL, 'admin', '2024-03-05 13:50:52', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (4, 'b_inspector', '巡查员用户表', NULL, NULL, 'BInspector', 'crud', '', 'com.ruoyi.system', 'system', 'inspector', '巡查员用户', 'ruoyi', '0', '/', NULL, 'admin', '2024-03-05 13:50:52', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (5, 'b_inspector_unit', '巡查用户和巡查单元关联表', NULL, NULL, 'BInspectorUnit', 'crud', '', 'com.ruoyi.system', 'system', 'unit', '巡查用户和巡查单元关联', 'ruoyi', '0', '/', NULL, 'admin', '2024-03-05 13:50:52', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (6, 'b_patrol', '巡查表', NULL, NULL, 'BPatrol', 'crud', '', 'com.ruoyi.system', 'system', 'patrol', '巡查', 'ruoyi', '0', '/', NULL, 'admin', '2024-03-05 13:50:52', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (7, 'b_patrol_file', '巡查文件表', NULL, NULL, 'BPatrolFile', 'crud', '', 'com.ruoyi.system', 'system', 'file', '巡查文件', 'ruoyi', '0', '/', NULL, 'admin', '2024-03-05 13:50:52', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (8, 'b_patrol_type', '巡查类型表', NULL, NULL, 'BPatrolType', 'crud', '', 'com.ruoyi.system', 'system', 'type', '巡查类型', 'ruoyi', '0', '/', NULL, 'admin', '2024-03-05 13:50:52', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (9, 'b_patrol_unit', '巡查单元表', NULL, NULL, 'BPatrolUnit', 'crud', '', 'com.ruoyi.system', 'system', 'unit', '巡查单元', 'ruoyi', '0', '/', NULL, 'admin', '2024-03-05 13:50:52', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (10, 'b_project', '项目信息表', NULL, NULL, 'BProject', 'crud', '', 'com.ruoyi.system', 'system', 'project', '项目信息', 'ruoyi', '0', '/', NULL, 'admin', '2024-03-05 13:50:52', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (11, 'b_fill', '填报登记表', NULL, NULL, 'BFill', 'crud', '', 'com.ruoyi.system', 'system', 'fill', '填报登记', 'ruoyi', '0', '/', NULL, 'admin', '2024-03-07 15:43:53', '', NULL, NULL);

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint(20) NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '字典类型',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 155 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '代码生成业务表字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------
INSERT INTO `gen_table_column` VALUES (1, 1, 'id', NULL, 'int(11)', 'Long', 'id', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (2, 1, 'patrol_id', '巡查id', 'int(11)', 'Long', 'patrolId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (3, 1, 'audit_user', '审核人', 'int(11)', 'Long', 'auditUser', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (4, 1, 'audit_opinion', '审核意见', 'varchar(255)', 'String', 'auditOpinion', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (5, 1, 'audit_status', '审核状态（通过、未通过）', 'int(11)', 'Long', 'auditStatus', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'radio', '', 5, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (6, 1, 'current_dept', '当前审核部门', 'int(11)', 'Long', 'currentDept', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (7, 1, 'subordinate_dept', '下级审核部门', 'int(11)', 'Long', 'subordinateDept', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (8, 1, 'data_state', '数据状态(1进行中2流程中止3完成)', 'int(11)', 'Long', 'dataState', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (9, 1, 'freedom1', NULL, 'varchar(255)', 'String', 'freedom1', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (10, 1, 'freedom2', NULL, 'varchar(255)', 'String', 'freedom2', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 10, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (11, 1, 'freedom3', NULL, 'varchar(255)', 'String', 'freedom3', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 11, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (12, 1, 'freedom4', NULL, 'varchar(255)', 'String', 'freedom4', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 12, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (13, 1, 'freedom5', NULL, 'varchar(255)', 'String', 'freedom5', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 13, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (14, 2, 'id', NULL, 'int(11)', 'Long', 'id', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (15, 2, 'project_id', '项目', 'int(11)', 'Long', 'projectId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (16, 2, 'unit_id', '单元', 'int(11)', 'Long', 'unitId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (17, 2, 'danger_type', '致险类型', 'varchar(255)', 'String', 'dangerType', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'select', '', 4, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (18, 2, 'danger_number', '致险类型数据', 'double', 'Long', 'dangerNumber', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (19, 2, 'inspector', '巡查用户', 'int(20)', 'Long', 'inspector', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (20, 2, 'create_time', '上传时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (21, 2, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(20)', 'String', 'delFlag', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 8, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (22, 2, 'freedom1', NULL, 'varchar(255)', 'String', 'freedom1', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (23, 2, 'freedom2', NULL, 'varchar(255)', 'String', 'freedom2', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 10, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (24, 2, 'freedom3', NULL, 'varchar(255)', 'String', 'freedom3', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 11, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (25, 2, 'freedom4', NULL, 'varchar(255)', 'String', 'freedom4', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 12, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (26, 2, 'freedom5', NULL, 'varchar(255)', 'String', 'freedom5', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 13, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (27, 3, 'id', NULL, 'int(11)', 'Long', 'id', '1', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (28, 3, 'project_id', '项目id', 'int(11)', 'Long', 'projectId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (29, 3, 'govern_type', '治理类型', 'varchar(255)', 'String', 'governType', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'select', '', 3, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (30, 3, 'govern_number', '治理类型数据', 'double', 'Long', 'governNumber', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (31, 3, 'inspector', '巡查用户', 'int(20)', 'Long', 'inspector', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (32, 3, 'create_time', '上传时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 6, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (33, 3, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(20)', 'String', 'delFlag', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 7, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (34, 3, 'freedom1', NULL, 'varchar(255)', 'String', 'freedom1', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (35, 3, 'freedom2', NULL, 'varchar(255)', 'String', 'freedom2', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (36, 3, 'freedom3', NULL, 'varchar(255)', 'String', 'freedom3', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 10, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (37, 3, 'freedom4', NULL, 'varchar(255)', 'String', 'freedom4', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 11, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (38, 3, 'freedom5', NULL, 'varchar(255)', 'String', 'freedom5', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 12, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (39, 4, 'id', NULL, 'int(11)', 'Long', 'id', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (40, 4, 'inspector_name', '巡查用户姓名', 'varchar(255)', 'String', 'inspectorName', '0', '0', '0', '1', '1', '1', '1', 'LIKE', 'input', '', 2, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (41, 4, 'phone', '手机号', 'varchar(255)', 'String', 'phone', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (42, 4, 'password', '密码', 'varchar(255)', 'String', 'password', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (43, 4, 'user_id', '部门用户创建者', 'int(11)', 'Long', 'userId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (44, 4, 'dept_id', '部门创建的用户', 'int(11)', 'Long', 'deptId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (45, 4, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 7, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (46, 4, 'freedom1', NULL, 'varchar(255)', 'String', 'freedom1', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (47, 4, 'freedom2', NULL, 'varchar(255)', 'String', 'freedom2', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (48, 4, 'freedom3', NULL, 'varchar(255)', 'String', 'freedom3', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 10, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (49, 4, 'freedom4', NULL, 'varchar(255)', 'String', 'freedom4', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 11, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (50, 4, 'freedom5', NULL, 'varchar(255)', 'String', 'freedom5', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 12, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (51, 5, 'id', NULL, 'int(11)', 'Long', 'id', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (52, 5, 'inspector', '巡查用户', 'int(20)', 'Long', 'inspector', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (53, 5, 'project_id', '项目id', 'int(11)', 'Long', 'projectId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (54, 5, 'patrol_unit_id', '巡查单元', 'int(20)', 'Long', 'patrolUnitId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (55, 6, 'id', NULL, 'int(11)', 'Long', 'id', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (56, 6, 'patrol_type', '巡查类型', 'bigint(20)', 'Long', 'patrolType', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'select', '', 2, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (57, 6, 'patrol_unit', '巡查单元', 'varchar(255)', 'String', 'patrolUnit', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (58, 6, 'inspector_id', '巡查用户', 'int(11)', 'Long', 'inspectorId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (59, 6, 'project_id', '工程id', 'int(11)', 'Long', 'projectId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (60, 6, 'pile_number', '桩号', 'varchar(255)', 'String', 'pileNumber', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (61, 6, 'section_position', '断面位置（顶、上游、下游）', 'varchar(255)', 'String', 'sectionPosition', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (62, 6, 'axis_distance', '轴线距离（正、负）', 'varchar(255)', 'String', 'axisDistance', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (63, 6, 'description', '文字描述', 'varchar(255)', 'String', 'description', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (64, 6, 'patrol_time', '巡查时间', 'varchar(255)', 'String', 'patrolTime', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 10, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (65, 6, 'address', '巡查位置', 'varchar(255)', 'String', 'address', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 11, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (66, 6, 'lon', '经度', 'varchar(255)', 'String', 'lon', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 12, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (67, 6, 'lat', '纬度', 'varchar(255)', 'String', 'lat', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 13, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (68, 6, 'high', '高程', 'varchar(255)', 'String', 'high', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 14, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (69, 6, 'audit_status', '审核状态（已提交、审核中、通过、未通过）', 'varchar(255)', 'String', 'auditStatus', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'radio', '', 15, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (70, 6, 'freedom1', NULL, 'varchar(255)', 'String', 'freedom1', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 16, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (71, 6, 'freedom2', NULL, 'varchar(255)', 'String', 'freedom2', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 17, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (72, 6, 'freedom3', NULL, 'varchar(255)', 'String', 'freedom3', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 18, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (73, 6, 'freedom4', NULL, 'varchar(255)', 'String', 'freedom4', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 19, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (74, 6, 'freedom5', NULL, 'varchar(255)', 'String', 'freedom5', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 20, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (75, 7, 'id', NULL, 'int(11)', 'Long', 'id', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (76, 7, 'patorl_id', '巡查id', 'int(11)', 'Long', 'patorlId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (77, 7, 'file_path', '文件地址', 'varchar(255)', 'String', 'filePath', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (78, 7, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(20)', 'String', 'delFlag', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 4, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (79, 7, 'create_time', '上传时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 5, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (80, 7, 'freedom1', NULL, 'varchar(255)', 'String', 'freedom1', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (81, 7, 'freedom2', NULL, 'varchar(255)', 'String', 'freedom2', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (82, 7, 'freedom3', NULL, 'varchar(255)', 'String', 'freedom3', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (83, 7, 'freedom4', NULL, 'varchar(255)', 'String', 'freedom4', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (84, 7, 'freedom5', NULL, 'varchar(255)', 'String', 'freedom5', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 10, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (85, 8, 'id', NULL, 'int(11)', 'Long', 'id', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (86, 8, 'patrol_type', '巡查类型1白蚁2动物', 'bigint(20)', 'Long', 'patrolType', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'select', '', 2, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (87, 8, 'type_name', '巡查类型名称', 'varchar(255)', 'String', 'typeName', '0', '0', '0', '1', '1', '1', '1', 'LIKE', 'input', '', 3, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (88, 8, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 4, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (89, 8, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 5, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (90, 8, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 6, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (91, 8, 'update_by', '修改者', 'varchar(64)', 'String', 'updateBy', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'input', '', 7, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (92, 8, 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'datetime', '', 8, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (93, 8, 'freedom1', NULL, 'varchar(255)', 'String', 'freedom1', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (94, 8, 'freedom2', NULL, 'varchar(255)', 'String', 'freedom2', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 10, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (95, 8, 'freedom3', NULL, 'varchar(255)', 'String', 'freedom3', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 11, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (96, 8, 'freedom4', NULL, 'varchar(255)', 'String', 'freedom4', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 12, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (97, 8, 'freedom5', NULL, 'varchar(255)', 'String', 'freedom5', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 13, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (98, 9, 'id', NULL, 'int(11)', 'Long', 'id', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (99, 9, 'project_id', '项目id', 'int(11)', 'Long', 'projectId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (100, 9, 'unit_name', '单元名称', 'varchar(255)', 'String', 'unitName', '0', '0', '0', '1', '1', '1', '1', 'LIKE', 'input', '', 3, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (101, 9, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 4, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (102, 9, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 5, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (103, 9, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 6, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (104, 9, 'update_by', '修改者', 'varchar(64)', 'String', 'updateBy', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'input', '', 7, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (105, 9, 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'datetime', '', 8, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (106, 9, 'freedom1', NULL, 'varchar(255)', 'String', 'freedom1', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (107, 9, 'freedom2', NULL, 'varchar(255)', 'String', 'freedom2', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 10, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (108, 9, 'freedom3', NULL, 'varchar(255)', 'String', 'freedom3', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 11, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (109, 9, 'freedom4', NULL, 'varchar(255)', 'String', 'freedom4', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 12, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (110, 9, 'freedom5', NULL, 'varchar(255)', 'String', 'freedom5', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 13, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (111, 10, 'id', NULL, 'int(11)', 'Long', 'id', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (112, 10, 'project_name', '项目名称', 'varchar(255)', 'String', 'projectName', '0', '0', '0', '1', '1', '1', '1', 'LIKE', 'input', '', 2, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (113, 10, 'project_type', '项目类型1水库2堤坝', 'bigint(20)', 'Long', 'projectType', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'select', '', 3, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (114, 10, 'dept_id', '所属部门', 'int(11)', 'Long', 'deptId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (115, 10, 'registration_number', '登记号', 'varchar(255)', 'String', 'registrationNumber', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (116, 10, 'address', '工程所在地', 'varchar(255)', 'String', 'address', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (117, 10, 'reservoir_grade', '水库等级', 'int(20)', 'Long', 'reservoirGrade', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (118, 10, 'dyke_grade', '堤防等级', 'int(20)', 'Long', 'dykeGrade', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (119, 10, 'dyke_length', '堤防长度', 'double', 'Long', 'dykeLength', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (120, 10, 'dyke_patrol_length', '堤防普查长度', 'double', 'Long', 'dykePatrolLength', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 10, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (121, 10, 'dyke_pile', '堤防起始桩号', 'varchar(255)', 'String', 'dykePile', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 11, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (122, 10, 'dyke_patrol_pile', '堤防普查起始桩号', 'varchar(255)', 'String', 'dykePatrolPile', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 12, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (123, 10, 'person', '负责人', 'varchar(255)', 'String', 'person', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 13, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (124, 10, 'person_phone', '手机号', 'varchar(255)', 'String', 'personPhone', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 14, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (125, 10, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(20)', 'String', 'delFlag', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 15, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (126, 10, 'create_by', '创建人', 'varchar(64)', 'String', 'createBy', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 16, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (127, 10, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 17, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (128, 10, 'update_by', '修改者', 'varchar(64)', 'String', 'updateBy', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'input', '', 18, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (129, 10, 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'datetime', '', 19, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (130, 10, 'freedom1', NULL, 'varchar(255)', 'String', 'freedom1', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 20, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (131, 10, 'freedom2', NULL, 'varchar(255)', 'String', 'freedom2', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 21, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (132, 10, 'freedom3', NULL, 'varchar(255)', 'String', 'freedom3', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 22, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (133, 10, 'freedom4', NULL, 'varchar(255)', 'String', 'freedom4', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 23, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (134, 10, 'freedom5', NULL, 'varchar(255)', 'String', 'freedom5', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 24, 'admin', '2024-03-05 13:50:52', '', NULL);
INSERT INTO `gen_table_column` VALUES (135, 11, 'id', NULL, 'int(11)', 'Long', 'id', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2024-03-07 15:43:53', '', NULL);
INSERT INTO `gen_table_column` VALUES (136, 11, 'project_id', '项目', 'int(11)', 'Long', 'projectId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2024-03-07 15:43:53', '', NULL);
INSERT INTO `gen_table_column` VALUES (137, 11, 'unit_id', '单元', 'int(11)', 'Long', 'unitId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2024-03-07 15:43:53', '', NULL);
INSERT INTO `gen_table_column` VALUES (138, 11, 'leaks_number', '渗漏处数', 'double', 'Long', 'leaksNumber', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2024-03-07 15:43:53', '', NULL);
INSERT INTO `gen_table_column` VALUES (139, 11, 'through_number', '穿坝处数', 'double', 'Long', 'throughNumber', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2024-03-07 15:43:53', '', NULL);
INSERT INTO `gen_table_column` VALUES (140, 11, 'drop_socket_number', '跌窝处数', 'double', 'Long', 'dropSocketNumber', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2024-03-07 15:43:53', '', NULL);
INSERT INTO `gen_table_column` VALUES (141, 11, 'nest_digging', '挖巢数量 （个）', 'double', 'Long', 'nestDigging', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2024-03-07 15:43:53', '', NULL);
INSERT INTO `gen_table_column` VALUES (142, 11, 'charge_area', '施药面积 （m2）', 'double', 'Long', 'chargeArea', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2024-03-07 15:43:53', '', NULL);
INSERT INTO `gen_table_column` VALUES (143, 11, 'grouting_quantity', '灌浆量 （延米）', 'double', 'Long', 'groutingQuantity', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2024-03-07 15:43:53', '', NULL);
INSERT INTO `gen_table_column` VALUES (144, 11, 'invest_capital', '投入资金 （万元）', 'double', 'Long', 'investCapital', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 10, 'admin', '2024-03-07 15:43:53', '', NULL);
INSERT INTO `gen_table_column` VALUES (145, 11, 'quantity_governance', '治理数量', 'double', 'Long', 'quantityGovernance', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 11, 'admin', '2024-03-07 15:43:53', '', NULL);
INSERT INTO `gen_table_column` VALUES (146, 11, 'fill_type', '填报类型1白蚁2动物', 'bigint(20)', 'Long', 'fillType', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'select', '', 12, 'admin', '2024-03-07 15:43:53', '', NULL);
INSERT INTO `gen_table_column` VALUES (147, 11, 'inspector', '巡查用户', 'int(20)', 'Long', 'inspector', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 13, 'admin', '2024-03-07 15:43:53', '', NULL);
INSERT INTO `gen_table_column` VALUES (148, 11, 'create_time', '上传时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 14, 'admin', '2024-03-07 15:43:53', '', NULL);
INSERT INTO `gen_table_column` VALUES (149, 11, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(20)', 'String', 'delFlag', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 15, 'admin', '2024-03-07 15:43:53', '', NULL);
INSERT INTO `gen_table_column` VALUES (150, 11, 'freedom1', NULL, 'varchar(255)', 'String', 'freedom1', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 16, 'admin', '2024-03-07 15:43:53', '', NULL);
INSERT INTO `gen_table_column` VALUES (151, 11, 'freedom2', NULL, 'varchar(255)', 'String', 'freedom2', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 17, 'admin', '2024-03-07 15:43:53', '', NULL);
INSERT INTO `gen_table_column` VALUES (152, 11, 'freedom3', NULL, 'varchar(255)', 'String', 'freedom3', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 18, 'admin', '2024-03-07 15:43:53', '', NULL);
INSERT INTO `gen_table_column` VALUES (153, 11, 'freedom4', NULL, 'varchar(255)', 'String', 'freedom4', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 19, 'admin', '2024-03-07 15:43:53', '', NULL);
INSERT INTO `gen_table_column` VALUES (154, 11, 'freedom5', NULL, 'varchar(255)', 'String', 'freedom5', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 20, 'admin', '2024-03-07 15:43:53', '', NULL);

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`  (
  `sched_name` varchar(120) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `blob_data` blob NULL COMMENT '存放持久化Trigger对象',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = 'Blob类型的触发器表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`  (
  `sched_name` varchar(120) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '调度名称',
  `calendar_name` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '日历名称',
  `calendar` blob NOT NULL COMMENT '存放持久化calendar对象',
  PRIMARY KEY (`sched_name`, `calendar_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = '日历信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`  (
  `sched_name` varchar(120) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `cron_expression` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT 'cron表达式',
  `time_zone_id` varchar(80) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '时区',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = 'Cron类型的触发器表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers`  (
  `sched_name` varchar(120) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '调度名称',
  `entry_id` varchar(95) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '调度器实例id',
  `trigger_name` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `instance_name` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '调度器实例名',
  `fired_time` bigint(20) NOT NULL COMMENT '触发的时间',
  `sched_time` bigint(20) NOT NULL COMMENT '定时器制定的时间',
  `priority` int(11) NOT NULL COMMENT '优先级',
  `state` varchar(16) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '状态',
  `job_name` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '任务组名',
  `is_nonconcurrent` varchar(1) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '是否并发',
  `requests_recovery` varchar(1) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '是否接受恢复执行',
  PRIMARY KEY (`sched_name`, `entry_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = '已触发的触发器表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`  (
  `sched_name` varchar(120) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '调度名称',
  `job_name` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '任务组名',
  `description` varchar(250) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '相关介绍',
  `job_class_name` varchar(250) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '执行任务类名称',
  `is_durable` varchar(1) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '是否持久化',
  `is_nonconcurrent` varchar(1) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '是否并发',
  `is_update_data` varchar(1) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '是否更新数据',
  `requests_recovery` varchar(1) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '是否接受恢复执行',
  `job_data` blob NULL COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = '任务详细信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`  (
  `sched_name` varchar(120) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '调度名称',
  `lock_name` varchar(40) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '悲观锁名称',
  PRIMARY KEY (`sched_name`, `lock_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = '存储的悲观锁信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`  (
  `sched_name` varchar(120) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '调度名称',
  `trigger_group` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  PRIMARY KEY (`sched_name`, `trigger_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = '暂停的触发器表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state`  (
  `sched_name` varchar(120) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '调度名称',
  `instance_name` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '实例名称',
  `last_checkin_time` bigint(20) NOT NULL COMMENT '上次检查时间',
  `checkin_interval` bigint(20) NOT NULL COMMENT '检查间隔时间',
  PRIMARY KEY (`sched_name`, `instance_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = '调度器状态表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`  (
  `sched_name` varchar(120) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `repeat_count` bigint(20) NOT NULL COMMENT '重复的次数统计',
  `repeat_interval` bigint(20) NOT NULL COMMENT '重复的间隔时间',
  `times_triggered` bigint(20) NOT NULL COMMENT '已经触发的次数',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = '简单触发器的信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers`  (
  `sched_name` varchar(120) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `str_prop_1` varchar(512) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT 'String类型的trigger的第一个参数',
  `str_prop_2` varchar(512) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT 'String类型的trigger的第二个参数',
  `str_prop_3` varchar(512) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT 'String类型的trigger的第三个参数',
  `int_prop_1` int(11) NULL DEFAULT NULL COMMENT 'int类型的trigger的第一个参数',
  `int_prop_2` int(11) NULL DEFAULT NULL COMMENT 'int类型的trigger的第二个参数',
  `long_prop_1` bigint(20) NULL DEFAULT NULL COMMENT 'long类型的trigger的第一个参数',
  `long_prop_2` bigint(20) NULL DEFAULT NULL COMMENT 'long类型的trigger的第二个参数',
  `dec_prop_1` decimal(13, 4) NULL DEFAULT NULL COMMENT 'decimal类型的trigger的第一个参数',
  `dec_prop_2` decimal(13, 4) NULL DEFAULT NULL COMMENT 'decimal类型的trigger的第二个参数',
  `bool_prop_1` varchar(1) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT 'Boolean类型的trigger的第一个参数',
  `bool_prop_2` varchar(1) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT 'Boolean类型的trigger的第二个参数',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = '同步机制的行锁表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`  (
  `sched_name` varchar(120) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '触发器的名字',
  `trigger_group` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '触发器所属组的名字',
  `job_name` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT 'qrtz_job_details表job_name的外键',
  `job_group` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT 'qrtz_job_details表job_group的外键',
  `description` varchar(250) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '相关介绍',
  `next_fire_time` bigint(20) NULL DEFAULT NULL COMMENT '上一次触发时间（毫秒）',
  `prev_fire_time` bigint(20) NULL DEFAULT NULL COMMENT '下一次触发时间（默认为-1表示不触发）',
  `priority` int(11) NULL DEFAULT NULL COMMENT '优先级',
  `trigger_state` varchar(16) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '触发器状态',
  `trigger_type` varchar(8) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '触发器的类型',
  `start_time` bigint(20) NOT NULL COMMENT '开始时间',
  `end_time` bigint(20) NULL DEFAULT NULL COMMENT '结束时间',
  `calendar_name` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '日程表名称',
  `misfire_instr` smallint(6) NULL DEFAULT NULL COMMENT '补偿执行的策略',
  `job_data` blob NULL COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  INDEX `sched_name`(`sched_name`, `job_name`, `job_group`) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci COMMENT = '触发器详细信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '参数配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2024-03-05 09:07:13', '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2024-03-05 09:07:13', '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2024-03-05 09:07:13', '', NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config` VALUES (4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'true', 'Y', 'admin', '2024-03-05 09:07:13', '', NULL, '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2024-03-05 09:07:13', '', NULL, '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (6, '用户登录-黑名单列表', 'sys.login.blackIPList', '', 'Y', 'admin', '2024-03-05 09:07:13', '', NULL, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int(11) NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 200 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', '水利水电科学研究院', 0, '研究院', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2024-03-05 09:07:01', 'admin', '2024-03-08 21:43:52');
INSERT INTO `sys_dept` VALUES (101, 100, '0,100', '广西省水利厅', 1, '广西省水利厅', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2024-03-05 09:07:01', 'admin', '2024-03-08 21:45:01');
INSERT INTO `sys_dept` VALUES (102, 100, '0,100', '北京市水利局', 2, '', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2024-03-05 09:07:01', 'admin', '2024-03-08 21:46:54');
INSERT INTO `sys_dept` VALUES (103, 101, '0,100,101', '南宁市水利局', 1, '南宁', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2024-03-05 09:07:01', 'admin', '2024-03-08 21:44:47');
INSERT INTO `sys_dept` VALUES (104, 101, '0,100,101', '桂林市水利局', 2, '桂林市水利局', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2024-03-05 09:07:01', 'admin', '2024-03-08 21:45:35');
INSERT INTO `sys_dept` VALUES (105, 101, '0,100,101', '北海市水利厅', 3, '', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2024-03-05 09:07:01', 'admin', '2024-03-08 21:45:59');
INSERT INTO `sys_dept` VALUES (106, 101, '0,100,101', '柳州市水利厅', 4, '', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2024-03-05 09:07:02', 'admin', '2024-03-08 21:46:13');
INSERT INTO `sys_dept` VALUES (107, 101, '0,100,101', '玉林市水利局', 5, '', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2024-03-05 09:07:02', 'admin', '2024-03-08 21:46:39');
INSERT INTO `sys_dept` VALUES (108, 102, '0,100,102', '密云水利', 1, '', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2024-03-05 09:07:02', 'admin', '2024-03-08 21:47:07');
INSERT INTO `sys_dept` VALUES (109, 102, '0,100,102', '通州水利', 2, '', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2024-03-05 09:07:02', 'admin', '2024-03-08 21:47:17');

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int(11) NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 108 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2024-03-05 09:07:11', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2024-03-05 09:07:11', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2024-03-05 09:07:11', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2024-03-05 09:07:11', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2024-03-05 09:07:11', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2024-03-05 09:07:11', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2024-03-05 09:07:11', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2024-03-05 09:07:11', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2024-03-05 09:07:11', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2024-03-05 09:07:12', '', NULL, '默认分组');
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2024-03-05 09:07:12', '', NULL, '系统分组');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2024-03-05 09:07:12', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2024-03-05 09:07:12', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2024-03-05 09:07:12', '', NULL, '通知');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2024-03-05 09:07:12', '', NULL, '公告');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2024-03-05 09:07:12', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2024-03-05 09:07:12', '', NULL, '关闭状态');
INSERT INTO `sys_dict_data` VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-03-05 09:07:12', '', NULL, '其他操作');
INSERT INTO `sys_dict_data` VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-03-05 09:07:12', '', NULL, '新增操作');
INSERT INTO `sys_dict_data` VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-03-05 09:07:12', '', NULL, '修改操作');
INSERT INTO `sys_dict_data` VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-03-05 09:07:12', '', NULL, '删除操作');
INSERT INTO `sys_dict_data` VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2024-03-05 09:07:12', '', NULL, '授权操作');
INSERT INTO `sys_dict_data` VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-03-05 09:07:12', '', NULL, '导出操作');
INSERT INTO `sys_dict_data` VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-03-05 09:07:12', '', NULL, '导入操作');
INSERT INTO `sys_dict_data` VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-03-05 09:07:12', '', NULL, '强退操作');
INSERT INTO `sys_dict_data` VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-03-05 09:07:12', '', NULL, '生成操作');
INSERT INTO `sys_dict_data` VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-03-05 09:07:12', '', NULL, '清空操作');
INSERT INTO `sys_dict_data` VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2024-03-05 09:07:12', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2024-03-05 09:07:12', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (100, 0, '渗漏处数', '1', 'danger_type', NULL, 'default', 'N', '0', 'admin', '2024-03-06 10:02:15', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (101, 1, '穿坝处数', '2', 'danger_type', NULL, 'default', 'N', '0', 'admin', '2024-03-06 10:02:24', 'admin', '2024-03-06 10:02:30', NULL);
INSERT INTO `sys_dict_data` VALUES (102, 2, '跌窝处数', '3', 'danger_type', NULL, 'default', 'N', '0', 'admin', '2024-03-06 10:02:41', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (103, 0, '挖巢数量 （个）', '1', 'govern_type', NULL, 'default', 'N', '0', 'admin', '2024-03-06 10:03:37', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (104, 1, '施药面积 （m2）', '2', 'govern_type', NULL, 'default', 'N', '0', 'admin', '2024-03-06 10:03:55', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (105, 2, '灌浆量 （延米）', '3', 'govern_type', NULL, 'default', 'N', '0', 'admin', '2024-03-06 10:04:10', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (106, 3, '投入资金 （万元）', '4', 'govern_type', NULL, 'default', 'N', '0', 'admin', '2024-03-06 10:04:37', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (107, 4, '治理数量', '5', 'govern_type', NULL, 'default', 'N', '0', 'admin', '2024-03-06 10:04:52', 'admin', '2024-03-06 10:04:58', NULL);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 102 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2024-03-05 09:07:11', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2024-03-05 09:07:11', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2024-03-05 09:07:11', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2024-03-05 09:07:11', '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2024-03-05 09:07:11', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2024-03-05 09:07:11', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2024-03-05 09:07:11', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2024-03-05 09:07:11', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2024-03-05 09:07:11', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2024-03-05 09:07:11', '', NULL, '登录状态列表');
INSERT INTO `sys_dict_type` VALUES (100, '致险类型', 'danger_type', '0', 'admin', '2024-03-06 10:01:58', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (101, '治理情况', 'govern_type', '0', 'admin', '2024-03-06 10:03:12', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '定时任务调度表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES (1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1', 'admin', '2024-03-05 09:07:14', '', NULL, '');
INSERT INTO `sys_job` VALUES (2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 'admin', '2024-03-05 09:07:14', '', NULL, '');
INSERT INTO `sys_job` VALUES (3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', 'admin', '2024-03-05 09:07:14', '', NULL, '');

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`  (
  `job_log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '日志信息',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '异常信息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '定时任务调度日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`  (
  `info_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '提示消息',
  `login_time` datetime NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `idx_sys_logininfor_s`(`status`) USING BTREE,
  INDEX `idx_sys_logininfor_lt`(`login_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 290 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '系统访问记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------
INSERT INTO `sys_logininfor` VALUES (100, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-05 13:38:46');
INSERT INTO `sys_logininfor` VALUES (101, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '1', '用户不存在/密码错误', '2024-03-05 17:32:21');
INSERT INTO `sys_logininfor` VALUES (102, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-05 17:32:30');
INSERT INTO `sys_logininfor` VALUES (103, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-05 17:54:48');
INSERT INTO `sys_logininfor` VALUES (104, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '1', '验证码错误', '2024-03-05 18:23:16');
INSERT INTO `sys_logininfor` VALUES (105, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-05 18:23:19');
INSERT INTO `sys_logininfor` VALUES (106, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-05 18:29:07');
INSERT INTO `sys_logininfor` VALUES (107, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-06 08:42:07');
INSERT INTO `sys_logininfor` VALUES (108, 'admin', '192.168.1.129', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-06 10:00:37');
INSERT INTO `sys_logininfor` VALUES (109, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-06 10:44:22');
INSERT INTO `sys_logininfor` VALUES (110, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-06 13:45:38');
INSERT INTO `sys_logininfor` VALUES (111, 'admin', '192.168.1.117', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-06 13:47:47');
INSERT INTO `sys_logininfor` VALUES (112, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-06 14:35:58');
INSERT INTO `sys_logininfor` VALUES (113, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-06 17:28:01');
INSERT INTO `sys_logininfor` VALUES (114, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-07 09:24:01');
INSERT INTO `sys_logininfor` VALUES (115, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-07 09:59:51');
INSERT INTO `sys_logininfor` VALUES (116, 'admin', '192.168.1.117', '内网IP', 'Unknown', 'Unknown', '1', '验证码已失效', '2024-03-07 10:15:15');
INSERT INTO `sys_logininfor` VALUES (117, 'admin', '192.168.1.117', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-07 10:28:21');
INSERT INTO `sys_logininfor` VALUES (118, 'admin', '192.168.1.117', '内网IP', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-07 10:29:13');
INSERT INTO `sys_logininfor` VALUES (119, 'ry', '192.168.1.117', '内网IP', 'Chrome 12', 'Windows 10', '1', '用户不存在/密码错误', '2024-03-07 10:29:21');
INSERT INTO `sys_logininfor` VALUES (120, 'admin', '192.168.1.117', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-07 10:29:33');
INSERT INTO `sys_logininfor` VALUES (121, 'admin', '192.168.1.117', '内网IP', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-07 10:30:09');
INSERT INTO `sys_logininfor` VALUES (122, 'ceshi', '192.168.1.117', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-07 10:30:16');
INSERT INTO `sys_logininfor` VALUES (123, 'ceshi', '192.168.1.117', '内网IP', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-07 10:33:53');
INSERT INTO `sys_logininfor` VALUES (124, 'ceshi', '192.168.1.117', '内网IP', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-07 10:33:53');
INSERT INTO `sys_logininfor` VALUES (125, 'ceshi', '192.168.1.117', '内网IP', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-07 10:33:53');
INSERT INTO `sys_logininfor` VALUES (126, 'ceshi', '192.168.1.117', '内网IP', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-07 10:33:53');
INSERT INTO `sys_logininfor` VALUES (127, 'admin', '192.168.1.117', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-07 10:33:59');
INSERT INTO `sys_logininfor` VALUES (128, 'admin', '192.168.1.117', '内网IP', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-07 10:34:44');
INSERT INTO `sys_logininfor` VALUES (129, 'ceshi', '192.168.1.117', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-07 10:34:55');
INSERT INTO `sys_logininfor` VALUES (130, 'ceshi', '192.168.1.117', '内网IP', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-07 10:48:32');
INSERT INTO `sys_logininfor` VALUES (131, 'admin', '192.168.1.117', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-07 10:48:36');
INSERT INTO `sys_logininfor` VALUES (132, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-07 13:40:20');
INSERT INTO `sys_logininfor` VALUES (133, 'admin', '192.168.1.117', '内网IP', 'Chrome 12', 'Windows 10', '1', '验证码已失效', '2024-03-07 14:21:27');
INSERT INTO `sys_logininfor` VALUES (134, 'admin', '192.168.1.117', '内网IP', 'Chrome 12', 'Windows 10', '1', '验证码错误', '2024-03-07 14:21:31');
INSERT INTO `sys_logininfor` VALUES (135, 'admin', '192.168.1.117', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-07 14:21:34');
INSERT INTO `sys_logininfor` VALUES (136, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-07 16:17:22');
INSERT INTO `sys_logininfor` VALUES (137, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-07 16:28:57');
INSERT INTO `sys_logininfor` VALUES (138, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-07 16:34:13');
INSERT INTO `sys_logininfor` VALUES (139, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '1', '验证码错误', '2024-03-07 16:49:34');
INSERT INTO `sys_logininfor` VALUES (140, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-07 16:49:36');
INSERT INTO `sys_logininfor` VALUES (141, 'admin', '192.168.1.117', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-07 16:50:11');
INSERT INTO `sys_logininfor` VALUES (142, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-08 08:45:31');
INSERT INTO `sys_logininfor` VALUES (143, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-08 08:49:13');
INSERT INTO `sys_logininfor` VALUES (144, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-08 09:15:47');
INSERT INTO `sys_logininfor` VALUES (145, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-08 09:19:20');
INSERT INTO `sys_logininfor` VALUES (146, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '1', '验证码错误', '2024-03-08 11:30:06');
INSERT INTO `sys_logininfor` VALUES (147, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-08 11:30:09');
INSERT INTO `sys_logininfor` VALUES (148, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-08 14:56:11');
INSERT INTO `sys_logininfor` VALUES (149, 'admin', '114.241.111.94', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-08 15:05:40');
INSERT INTO `sys_logininfor` VALUES (150, 'admin', '114.241.111.94', 'XX XX', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-08 15:06:35');
INSERT INTO `sys_logininfor` VALUES (151, 'admin', '114.241.111.94', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-08 15:06:45');
INSERT INTO `sys_logininfor` VALUES (152, 'admin', '114.241.111.94', 'XX XX', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-08 15:07:00');
INSERT INTO `sys_logininfor` VALUES (153, 'admin', '114.241.111.94', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-08 15:07:03');
INSERT INTO `sys_logininfor` VALUES (154, 'admin', '114.241.111.94', 'XX XX', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-08 15:07:12');
INSERT INTO `sys_logininfor` VALUES (155, 'admin', '114.241.111.94', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-08 15:10:29');
INSERT INTO `sys_logininfor` VALUES (156, 'admin', '114.241.111.94', 'XX XX', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-08 15:10:32');
INSERT INTO `sys_logininfor` VALUES (157, 'admin', '114.241.111.94', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-08 15:10:41');
INSERT INTO `sys_logininfor` VALUES (158, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-08 15:11:41');
INSERT INTO `sys_logininfor` VALUES (159, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-08 15:11:46');
INSERT INTO `sys_logininfor` VALUES (160, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-08 15:12:31');
INSERT INTO `sys_logininfor` VALUES (161, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-08 15:12:35');
INSERT INTO `sys_logininfor` VALUES (162, 'admin', '114.241.111.94', 'XX XX', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-08 15:14:01');
INSERT INTO `sys_logininfor` VALUES (163, 'admin', '114.241.111.94', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-08 18:44:58');
INSERT INTO `sys_logininfor` VALUES (164, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-08 18:49:21');
INSERT INTO `sys_logininfor` VALUES (165, 'admin', '60.8.100.94', 'XX XX', 'Chrome Mobile', 'Android 1.x', '0', '登录成功', '2024-03-08 19:30:40');
INSERT INTO `sys_logininfor` VALUES (166, 'admin', '120.244.206.205', 'XX XX', 'Chrome 12', 'Mac OS X', '0', '登录成功', '2024-03-08 21:43:07');
INSERT INTO `sys_logininfor` VALUES (167, 'admin', '116.176.18.145', 'XX XX', 'Chrome 11', 'Windows 10', '0', '登录成功', '2024-03-11 11:05:30');
INSERT INTO `sys_logininfor` VALUES (168, 'admin', '114.241.111.94', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-14 09:21:30');
INSERT INTO `sys_logininfor` VALUES (169, 'admin', '114.241.111.94', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-14 16:02:36');
INSERT INTO `sys_logininfor` VALUES (170, 'admin', '123.127.143.209', 'XX XX', 'Chrome 11', 'Windows 10', '0', '登录成功', '2024-03-15 09:47:04');
INSERT INTO `sys_logininfor` VALUES (171, 'admin', '123.127.143.209', 'XX XX', 'Chrome 10', 'Windows 10', '0', '登录成功', '2024-03-15 09:49:20');
INSERT INTO `sys_logininfor` VALUES (172, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-15 09:56:40');
INSERT INTO `sys_logininfor` VALUES (173, 'admin', '114.241.111.94', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-15 11:41:08');
INSERT INTO `sys_logininfor` VALUES (174, 'admin', '114.241.111.94', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-15 14:08:54');
INSERT INTO `sys_logininfor` VALUES (175, 'admin', '114.241.111.94', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-15 14:09:16');
INSERT INTO `sys_logininfor` VALUES (176, 'admin', '114.241.111.94', 'XX XX', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-15 14:09:23');
INSERT INTO `sys_logininfor` VALUES (177, 'admin', '114.241.111.94', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-15 14:09:25');
INSERT INTO `sys_logininfor` VALUES (178, 'admin', '114.241.111.94', 'XX XX', 'Chrome 12', 'Windows 10', '1', '验证码错误', '2024-03-15 14:09:49');
INSERT INTO `sys_logininfor` VALUES (179, 'admin', '114.241.111.94', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-15 14:09:51');
INSERT INTO `sys_logininfor` VALUES (180, 'admin', '114.241.111.94', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-15 15:31:26');
INSERT INTO `sys_logininfor` VALUES (181, 'admin', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '1', '验证码已失效', '2024-03-16 15:04:15');
INSERT INTO `sys_logininfor` VALUES (182, 'admin', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-16 15:04:18');
INSERT INTO `sys_logininfor` VALUES (183, 'admin', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-18 10:15:45');
INSERT INTO `sys_logininfor` VALUES (184, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '1', '用户不存在/密码错误', '2024-03-18 10:29:22');
INSERT INTO `sys_logininfor` VALUES (185, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-18 10:29:31');
INSERT INTO `sys_logininfor` VALUES (186, 'admin', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-18 15:00:29');
INSERT INTO `sys_logininfor` VALUES (187, 'admin', '123.127.143.209', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-19 11:26:07');
INSERT INTO `sys_logininfor` VALUES (188, 'admin', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-19 13:50:05');
INSERT INTO `sys_logininfor` VALUES (189, 'admin', '123.127.143.209', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-19 13:58:35');
INSERT INTO `sys_logininfor` VALUES (190, 'admin', '123.127.143.209', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-19 14:23:53');
INSERT INTO `sys_logininfor` VALUES (191, 'admin', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-19 14:36:56');
INSERT INTO `sys_logininfor` VALUES (192, 'admin', '123.127.143.209', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-19 14:54:27');
INSERT INTO `sys_logininfor` VALUES (193, 'admin', '123.127.143.209', 'XX XX', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-19 15:02:49');
INSERT INTO `sys_logininfor` VALUES (194, 'miyun001', '123.127.143.209', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-19 15:02:58');
INSERT INTO `sys_logininfor` VALUES (195, 'admin', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-19 15:25:15');
INSERT INTO `sys_logininfor` VALUES (196, '13001011630', '120.244.226.157', 'XX XX', 'Chrome 10', 'Windows 10', '1', '验证码错误', '2024-03-23 11:16:42');
INSERT INTO `sys_logininfor` VALUES (197, '13001011630', '120.244.226.157', 'XX XX', 'Chrome 10', 'Windows 10', '1', '用户不存在/密码错误', '2024-03-23 11:16:44');
INSERT INTO `sys_logininfor` VALUES (198, '13001011630', '120.244.226.157', 'XX XX', 'Chrome 10', 'Windows 10', '1', '用户不存在/密码错误', '2024-03-23 11:17:04');
INSERT INTO `sys_logininfor` VALUES (199, '13001011630', '120.244.226.157', 'XX XX', 'Chrome 10', 'Windows 10', '1', '用户不存在/密码错误', '2024-03-23 11:17:10');
INSERT INTO `sys_logininfor` VALUES (200, '13001011630', '120.244.226.157', 'XX XX', 'Chrome 11', 'Windows 10', '1', '用户不存在/密码错误', '2024-03-23 11:17:32');
INSERT INTO `sys_logininfor` VALUES (201, 'admin', '120.244.226.157', 'XX XX', 'Chrome 11', 'Windows 10', '1', '用户不存在/密码错误', '2024-03-23 11:22:04');
INSERT INTO `sys_logininfor` VALUES (202, 'admin', '120.244.226.157', 'XX XX', 'Chrome 11', 'Windows 10', '1', '用户不存在/密码错误', '2024-03-23 11:22:09');
INSERT INTO `sys_logininfor` VALUES (203, 'admin', '120.244.226.157', 'XX XX', 'Chrome 11', 'Windows 10', '0', '登录成功', '2024-03-23 11:22:44');
INSERT INTO `sys_logininfor` VALUES (204, 'admin', '120.244.226.157', 'XX XX', 'Chrome 11', 'Windows 10', '0', '登录成功', '2024-03-23 17:16:10');
INSERT INTO `sys_logininfor` VALUES (205, 'admin', '114.241.108.229', 'XX XX', 'Chrome 10', 'Windows 10', '0', '登录成功', '2024-03-25 11:37:14');
INSERT INTO `sys_logininfor` VALUES (206, 'admin', '114.241.108.229', 'XX XX', 'Chrome 10', 'Windows 10', '0', '登录成功', '2024-03-25 13:41:31');
INSERT INTO `sys_logininfor` VALUES (207, 'admin', '114.241.108.229', 'XX XX', 'Chrome 10', 'Windows 10', '0', '登录成功', '2024-03-25 14:26:13');
INSERT INTO `sys_logininfor` VALUES (208, 'admin', '114.241.108.229', 'XX XX', 'Chrome 10', 'Windows 10', '1', '验证码错误', '2024-03-25 15:03:27');
INSERT INTO `sys_logininfor` VALUES (209, 'admin', '114.241.108.229', 'XX XX', 'Chrome 10', 'Windows 10', '0', '登录成功', '2024-03-25 15:03:31');
INSERT INTO `sys_logininfor` VALUES (210, 'admin', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-25 17:04:05');
INSERT INTO `sys_logininfor` VALUES (211, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '1', '用户不存在/密码错误', '2024-03-26 09:43:11');
INSERT INTO `sys_logininfor` VALUES (212, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '1', '验证码错误', '2024-03-26 09:43:17');
INSERT INTO `sys_logininfor` VALUES (213, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '1', '验证码错误', '2024-03-26 09:43:21');
INSERT INTO `sys_logininfor` VALUES (214, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-26 09:43:26');
INSERT INTO `sys_logininfor` VALUES (215, 'admin', '123.127.143.209', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-26 14:58:07');
INSERT INTO `sys_logininfor` VALUES (216, 'admin', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-26 15:11:53');
INSERT INTO `sys_logininfor` VALUES (217, 'admin', '123.127.143.209', 'XX XX', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-26 15:36:52');
INSERT INTO `sys_logininfor` VALUES (218, '13001011630', '123.127.143.209', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-26 15:37:00');
INSERT INTO `sys_logininfor` VALUES (219, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '1', '用户不存在/密码错误', '2024-03-26 15:37:58');
INSERT INTO `sys_logininfor` VALUES (220, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '1', '验证码错误', '2024-03-26 15:38:02');
INSERT INTO `sys_logininfor` VALUES (221, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-26 15:38:06');
INSERT INTO `sys_logininfor` VALUES (222, 'admin', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-26 15:38:15');
INSERT INTO `sys_logininfor` VALUES (223, '13001011630', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-26 15:38:22');
INSERT INTO `sys_logininfor` VALUES (224, '13001011630', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-26 15:38:28');
INSERT INTO `sys_logininfor` VALUES (225, 'admin', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '1', '验证码错误', '2024-03-26 15:38:31');
INSERT INTO `sys_logininfor` VALUES (226, 'admin', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-26 15:38:34');
INSERT INTO `sys_logininfor` VALUES (227, 'admin', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-26 15:38:53');
INSERT INTO `sys_logininfor` VALUES (228, '13001011630', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-26 15:39:01');
INSERT INTO `sys_logininfor` VALUES (229, '13001011630', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-26 15:39:47');
INSERT INTO `sys_logininfor` VALUES (230, 'admin', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-26 15:39:51');
INSERT INTO `sys_logininfor` VALUES (231, 'admin', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-26 15:40:13');
INSERT INTO `sys_logininfor` VALUES (232, 'admin', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-26 15:40:23');
INSERT INTO `sys_logininfor` VALUES (233, 'admin', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-26 15:40:58');
INSERT INTO `sys_logininfor` VALUES (234, '13001011630', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-26 15:41:06');
INSERT INTO `sys_logininfor` VALUES (235, '13001011630', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-26 15:42:32');
INSERT INTO `sys_logininfor` VALUES (236, 'admin', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-26 15:42:36');
INSERT INTO `sys_logininfor` VALUES (237, 'admin', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-26 15:42:44');
INSERT INTO `sys_logininfor` VALUES (238, '13001011630', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-26 15:42:52');
INSERT INTO `sys_logininfor` VALUES (239, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-26 15:44:12');
INSERT INTO `sys_logininfor` VALUES (240, '13001011630', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-26 15:44:20');
INSERT INTO `sys_logininfor` VALUES (241, '13001011630', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-26 15:44:36');
INSERT INTO `sys_logininfor` VALUES (242, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '1', '用户不存在/密码错误', '2024-03-26 15:44:47');
INSERT INTO `sys_logininfor` VALUES (243, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '1', '用户不存在/密码错误', '2024-03-26 15:44:55');
INSERT INTO `sys_logininfor` VALUES (244, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-26 15:45:10');
INSERT INTO `sys_logininfor` VALUES (245, '13001011630', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-26 15:46:14');
INSERT INTO `sys_logininfor` VALUES (246, '13001011630', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-26 15:47:39');
INSERT INTO `sys_logininfor` VALUES (247, '13001011630', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-26 15:47:47');
INSERT INTO `sys_logininfor` VALUES (248, 'admin', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-26 15:52:05');
INSERT INTO `sys_logininfor` VALUES (249, '13001011630', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-26 15:52:14');
INSERT INTO `sys_logininfor` VALUES (250, '13001011630', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-26 15:52:17');
INSERT INTO `sys_logininfor` VALUES (251, 'admin', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-26 15:52:21');
INSERT INTO `sys_logininfor` VALUES (252, 'admin', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-26 15:52:29');
INSERT INTO `sys_logininfor` VALUES (253, '13001011630', '114.241.108.229', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-26 15:52:37');
INSERT INTO `sys_logininfor` VALUES (254, '13001011630', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-26 16:22:24');
INSERT INTO `sys_logininfor` VALUES (255, '13001011630', '127.0.0.1', '内网IP', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-26 16:22:28');
INSERT INTO `sys_logininfor` VALUES (256, 'admin', '123.127.143.209', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-26 16:22:31');
INSERT INTO `sys_logininfor` VALUES (257, 'admin', '123.127.143.209', 'XX XX', 'Chrome 12', 'Windows 10', '0', '退出成功', '2024-03-26 16:22:39');
INSERT INTO `sys_logininfor` VALUES (258, '13001011630', '123.127.143.209', 'XX XX', 'Chrome 12', 'Windows 10', '0', '登录成功', '2024-03-26 16:22:50');
INSERT INTO `sys_logininfor` VALUES (259, 'admin', '114.241.108.229', 'XX XX', 'Chrome 10', 'Windows 10', '0', '登录成功', '2024-03-26 17:20:01');
INSERT INTO `sys_logininfor` VALUES (260, 'admin', '114.241.108.229', 'XX XX', 'Chrome 10', 'Windows 10', '0', '登录成功', '2024-03-27 08:41:44');
INSERT INTO `sys_logininfor` VALUES (261, 'admin', '114.241.108.229', 'XX XX', 'Chrome 10', 'Windows 10', '0', '退出成功', '2024-03-27 08:45:02');
INSERT INTO `sys_logininfor` VALUES (262, 'ceshi', '114.241.108.229', 'XX XX', 'Chrome 10', 'Windows 10', '1', '用户不存在/密码错误', '2024-03-27 08:45:11');
INSERT INTO `sys_logininfor` VALUES (263, 'cs', '114.241.108.229', 'XX XX', 'Chrome 10', 'Windows 10', '1', '用户不存在/密码错误', '2024-03-27 08:45:19');
INSERT INTO `sys_logininfor` VALUES (264, 'admin', '114.241.108.229', 'XX XX', 'Chrome 10', 'Windows 10', '0', '登录成功', '2024-03-27 08:45:24');
INSERT INTO `sys_logininfor` VALUES (265, 'admin', '114.241.108.229', 'XX XX', 'Chrome 10', 'Windows 10', '0', '退出成功', '2024-03-27 08:45:45');
INSERT INTO `sys_logininfor` VALUES (266, 'ceshi', '114.241.108.229', 'XX XX', 'Chrome 10', 'Windows 10', '1', '用户不存在/密码错误', '2024-03-27 08:45:55');
INSERT INTO `sys_logininfor` VALUES (267, 'ceshi', '114.241.108.229', 'XX XX', 'Chrome 10', 'Windows 10', '0', '登录成功', '2024-03-27 08:46:03');
INSERT INTO `sys_logininfor` VALUES (268, 'ceshi', '114.241.108.229', 'XX XX', 'Chrome 10', 'Windows 10', '0', '退出成功', '2024-03-27 08:55:54');
INSERT INTO `sys_logininfor` VALUES (269, 'admin', '114.241.108.229', 'XX XX', 'Chrome 10', 'Windows 10', '0', '登录成功', '2024-03-27 08:55:59');
INSERT INTO `sys_logininfor` VALUES (270, 'admin', '114.241.108.229', 'XX XX', 'Chrome 10', 'Windows 10', '0', '退出成功', '2024-03-27 09:01:35');
INSERT INTO `sys_logininfor` VALUES (271, 'ceshi', '114.241.108.229', 'XX XX', 'Chrome 10', 'Windows 10', '0', '登录成功', '2024-03-27 09:01:45');
INSERT INTO `sys_logininfor` VALUES (272, 'ceshi', '114.241.108.229', 'XX XX', 'Chrome 10', 'Windows 10', '0', '退出成功', '2024-03-27 09:02:16');
INSERT INTO `sys_logininfor` VALUES (273, 'admin', '114.241.108.229', 'XX XX', 'Chrome 10', 'Windows 10', '0', '登录成功', '2024-03-27 09:02:20');
INSERT INTO `sys_logininfor` VALUES (274, 'admin', '114.241.108.229', 'XX XX', 'Chrome 10', 'Windows 10', '0', '退出成功', '2024-03-27 09:11:44');
INSERT INTO `sys_logininfor` VALUES (275, 'ceshi', '114.241.108.229', 'XX XX', 'Chrome 10', 'Windows 10', '0', '登录成功', '2024-03-27 09:11:54');
INSERT INTO `sys_logininfor` VALUES (276, 'ceshi', '192.168.1.118', '内网IP', 'Chrome 10', 'Windows 10', '0', '登录成功', '2024-03-27 10:13:36');
INSERT INTO `sys_logininfor` VALUES (277, 'ceshi', '192.168.1.118', '内网IP', 'Chrome 10', 'Windows 10', '0', '退出成功', '2024-03-27 10:14:31');
INSERT INTO `sys_logininfor` VALUES (278, 'admin', '192.168.1.118', '内网IP', 'Chrome 10', 'Windows 10', '0', '登录成功', '2024-03-27 10:14:35');
INSERT INTO `sys_logininfor` VALUES (279, 'admin', '192.168.1.118', '内网IP', 'Chrome 10', 'Windows 10', '0', '退出成功', '2024-03-27 10:20:16');
INSERT INTO `sys_logininfor` VALUES (280, 'admin', '192.168.1.118', '内网IP', 'Chrome 10', 'Windows 10', '0', '登录成功', '2024-03-27 10:20:20');
INSERT INTO `sys_logininfor` VALUES (281, 'admin', '192.168.1.118', '内网IP', 'Chrome 10', 'Windows 10', '0', '登录成功', '2024-03-27 11:56:01');
INSERT INTO `sys_logininfor` VALUES (282, 'ceshi', '192.168.1.118', '内网IP', 'Chrome 10', 'Windows 10', '0', '登录成功', '2024-03-27 13:36:03');
INSERT INTO `sys_logininfor` VALUES (283, 'admin', '192.168.1.118', '内网IP', 'Chrome 10', 'Windows 10', '0', '登录成功', '2024-03-27 14:10:35');
INSERT INTO `sys_logininfor` VALUES (284, 'admin', '192.168.1.118', '内网IP', 'Chrome 10', 'Windows 10', '0', '退出成功', '2024-03-27 14:10:51');
INSERT INTO `sys_logininfor` VALUES (285, 'ceshi', '192.168.1.118', '内网IP', 'Chrome 10', 'Windows 10', '0', '登录成功', '2024-03-27 14:11:02');
INSERT INTO `sys_logininfor` VALUES (286, 'ceshi', '192.168.1.118', '内网IP', 'Chrome 10', 'Windows 10', '0', '退出成功', '2024-03-27 14:23:48');
INSERT INTO `sys_logininfor` VALUES (287, 'admin', '192.168.1.118', '内网IP', 'Chrome 10', 'Windows 10', '0', '登录成功', '2024-03-27 14:23:52');
INSERT INTO `sys_logininfor` VALUES (288, 'admin', '192.168.1.118', '内网IP', 'Chrome 10', 'Windows 10', '0', '退出成功', '2024-03-27 14:34:05');
INSERT INTO `sys_logininfor` VALUES (289, 'ceshi', '192.168.1.118', '内网IP', 'Chrome 10', 'Windows 10', '0', '登录成功', '2024-03-27 14:34:16');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int(11) NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '路由参数',
  `is_frame` int(11) NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int(11) NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2009 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 2, 'system', NULL, '', 1, 0, 'M', '0', '0', '', 'system', 'admin', '2024-03-05 09:07:03', 'admin', '2024-03-08 15:06:19', '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 2, 'monitor', NULL, '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', '2024-03-05 09:07:03', '', NULL, '系统监控目录');
INSERT INTO `sys_menu` VALUES (3, '系统工具', 0, 3, 'tool', NULL, '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', '2024-03-05 09:07:03', '', NULL, '系统工具目录');
INSERT INTO `sys_menu` VALUES (4, '若依官网', 0, 4, 'http://ruoyi.vip', NULL, '', 0, 0, 'M', '1', '1', '', 'guide', 'admin', '2024-03-05 09:07:03', 'admin', '2024-03-06 11:07:48', '若依官网地址');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', '2024-03-05 09:07:03', '', NULL, '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', '2024-03-05 09:07:03', '', NULL, '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', '2024-03-05 09:07:04', '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '组织管理', 1, 4, 'dept', 'system/dept/index', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin', '2024-03-05 09:07:04', 'admin', '2024-03-16 15:20:47', '部门管理菜单');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post', 'admin', '2024-03-05 09:07:04', '', NULL, '岗位管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', '2024-03-05 09:07:04', '', NULL, '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit', 'admin', '2024-03-05 09:07:04', '', NULL, '参数设置菜单');
INSERT INTO `sys_menu` VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 'admin', '2024-03-05 09:07:04', '', NULL, '通知公告菜单');
INSERT INTO `sys_menu` VALUES (108, '日志管理', 1, 9, 'log', '', '', 1, 0, 'M', '0', '0', '', 'log', 'admin', '2024-03-05 09:07:04', '', NULL, '日志管理菜单');
INSERT INTO `sys_menu` VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', 'admin', '2024-03-05 09:07:04', '', NULL, '在线用户菜单');
INSERT INTO `sys_menu` VALUES (110, '定时任务', 2, 2, 'job', 'monitor/job/index', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job', 'admin', '2024-03-05 09:07:04', '', NULL, '定时任务菜单');
INSERT INTO `sys_menu` VALUES (111, '数据监控', 2, 3, 'druid', 'monitor/druid/index', '', 1, 0, 'C', '0', '0', 'monitor:druid:list', 'druid', 'admin', '2024-03-05 09:07:04', '', NULL, '数据监控菜单');
INSERT INTO `sys_menu` VALUES (112, '服务监控', 2, 4, 'server', 'monitor/server/index', '', 1, 0, 'C', '0', '0', 'monitor:server:list', 'server', 'admin', '2024-03-05 09:07:04', '', NULL, '服务监控菜单');
INSERT INTO `sys_menu` VALUES (113, '缓存监控', 2, 5, 'cache', 'monitor/cache/index', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis', 'admin', '2024-03-05 09:07:04', '', NULL, '缓存监控菜单');
INSERT INTO `sys_menu` VALUES (114, '缓存列表', 2, 6, 'cacheList', 'monitor/cache/list', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis-list', 'admin', '2024-03-05 09:07:04', '', NULL, '缓存列表菜单');
INSERT INTO `sys_menu` VALUES (115, '表单构建', 3, 1, 'build', 'tool/build/index', '', 1, 0, 'C', '0', '0', 'tool:build:list', 'build', 'admin', '2024-03-05 09:07:04', '', NULL, '表单构建菜单');
INSERT INTO `sys_menu` VALUES (116, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 'admin', '2024-03-05 09:07:04', '', NULL, '代码生成菜单');
INSERT INTO `sys_menu` VALUES (117, '系统接口', 3, 3, 'swagger', 'tool/swagger/index', '', 1, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', 'admin', '2024-03-05 09:07:04', '', NULL, '系统接口菜单');
INSERT INTO `sys_menu` VALUES (500, '操作日志', 108, 1, 'operlog', 'monitor/operlog/index', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list', 'form', 'admin', '2024-03-05 09:07:04', '', NULL, '操作日志菜单');
INSERT INTO `sys_menu` VALUES (501, '登录日志', 108, 2, 'logininfor', 'monitor/logininfor/index', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor', 'admin', '2024-03-05 09:07:04', '', NULL, '登录日志菜单');
INSERT INTO `sys_menu` VALUES (1000, '用户查询', 100, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2024-03-05 09:07:04', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1001, '用户新增', 100, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2024-03-05 09:07:04', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1002, '用户修改', 100, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2024-03-05 09:07:04', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1003, '用户删除', 100, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2024-03-05 09:07:04', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1004, '用户导出', 100, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2024-03-05 09:07:04', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1005, '用户导入', 100, 6, '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2024-03-05 09:07:04', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1006, '重置密码', 100, 7, '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2024-03-05 09:07:04', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1007, '角色查询', 101, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2024-03-05 09:07:04', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1008, '角色新增', 101, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2024-03-05 09:07:04', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1009, '角色修改', 101, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1010, '角色删除', 101, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1011, '角色导出', 101, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1012, '菜单查询', 102, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1013, '菜单新增', 102, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1014, '菜单修改', 102, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1015, '菜单删除', 102, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1016, '部门查询', 103, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1017, '部门新增', 103, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1018, '部门修改', 103, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1019, '部门删除', 103, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1020, '岗位查询', 104, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1021, '岗位新增', 104, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1022, '岗位修改', 104, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1023, '岗位删除', 104, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1024, '岗位导出', 104, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1025, '字典查询', 105, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1026, '字典新增', 105, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1027, '字典修改', 105, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1028, '字典删除', 105, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1029, '字典导出', 105, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1030, '参数查询', 106, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1031, '参数新增', 106, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1032, '参数修改', 106, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1033, '参数删除', 106, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1034, '参数导出', 106, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1035, '公告查询', 107, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1036, '公告新增', 107, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1037, '公告修改', 107, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1038, '公告删除', 107, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', '2024-03-05 09:07:05', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1039, '操作查询', 500, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 'admin', '2024-03-05 09:07:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1040, '操作删除', 500, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin', '2024-03-05 09:07:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1041, '日志导出', 500, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', 'admin', '2024-03-05 09:07:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1042, '登录查询', 501, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', 'admin', '2024-03-05 09:07:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1043, '登录删除', 501, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin', '2024-03-05 09:07:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1044, '日志导出', 501, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin', '2024-03-05 09:07:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1045, '账户解锁', 501, 4, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock', '#', 'admin', '2024-03-05 09:07:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1046, '在线查询', 109, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', '2024-03-05 09:07:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1047, '批量强退', 109, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', '2024-03-05 09:07:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1048, '单条强退', 109, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2024-03-05 09:07:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1049, '任务查询', 110, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', '2024-03-05 09:07:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1050, '任务新增', 110, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', '2024-03-05 09:07:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1051, '任务修改', 110, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', '2024-03-05 09:07:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1052, '任务删除', 110, 4, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', '2024-03-05 09:07:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1053, '状态修改', 110, 5, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', '2024-03-05 09:07:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1054, '任务导出', 110, 6, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', '2024-03-05 09:07:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1055, '生成查询', 116, 1, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2024-03-05 09:07:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1056, '生成修改', 116, 2, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2024-03-05 09:07:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1057, '生成删除', 116, 3, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2024-03-05 09:07:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1058, '导入代码', 116, 4, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2024-03-05 09:07:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1059, '预览代码', 116, 5, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2024-03-05 09:07:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1060, '生成代码', 116, 6, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2024-03-05 09:07:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2000, '项目管理', 0, 1, 'project', 'system/project/index', NULL, 1, 1, 'C', '0', '0', 'system:project:list', 'peoples', 'admin', '2024-03-05 18:00:17', 'admin', '2024-03-26 15:43:51', '');
INSERT INTO `sys_menu` VALUES (2001, '普查单元', 1, 2, 'unit', 'system/unit/index', NULL, 1, 1, 'C', '1', '0', '', '#', 'admin', '2024-03-06 14:42:58', 'admin', '2024-03-06 15:38:14', '');
INSERT INTO `sys_menu` VALUES (2002, '巡查员管理', 0, 1, 'inspector', 'system/inspector/index', NULL, 1, 1, 'C', '0', '0', '', 'peoples', 'admin', '2024-03-06 16:30:17', 'admin', '2024-03-26 15:24:07', '');
INSERT INTO `sys_menu` VALUES (2003, '巡查审批', 0, 1, 'audit', 'system/audit/index', NULL, 1, 1, 'C', '0', '0', '', 'peoples', 'admin', '2024-03-07 16:33:04', 'admin', '2024-03-26 15:24:31', '');
INSERT INTO `sys_menu` VALUES (2005, '巡查记录', 1, 2, 'patrol', 'system/patrol/index', NULL, 1, 1, 'C', '1', '0', '', '#', 'admin', '2024-03-08 10:08:32', 'admin', '2024-03-08 10:25:37', '');
INSERT INTO `sys_menu` VALUES (2006, '填报记录', 1, 2, 'fill', 'system/fill/index', NULL, 1, 1, 'C', '1', '0', '', '#', 'admin', '2024-03-08 10:24:21', 'admin', '2024-03-08 10:25:41', '');
INSERT INTO `sys_menu` VALUES (2007, 'system:unit:list', 2002, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:unit:list', '#', 'admin', '2024-03-26 15:47:03', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2008, 'system:inspector:list', 2002, 2, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:inspector:list', '#', 'admin', '2024-03-26 15:47:16', '', NULL, '');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob NULL COMMENT '公告内容',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '通知公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, '温馨提醒：2018-07-01 若依新版本发布啦', '2', 0xE696B0E78988E69CACE58685E5AEB9, '0', 'admin', '2024-03-05 09:07:15', '', NULL, '管理员');
INSERT INTO `sys_notice` VALUES (2, '维护通知：2018-07-01 若依系统凌晨维护', '1', 0xE7BBB4E68AA4E58685E5AEB9, '0', 'admin', '2024-03-05 09:07:15', '', NULL, '管理员');

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int(11) NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` int(11) NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '返回参数',
  `status` int(11) NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint(20) NULL DEFAULT 0 COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`) USING BTREE,
  INDEX `idx_sys_oper_log_bt`(`business_type`) USING BTREE,
  INDEX `idx_sys_oper_log_s`(`status`) USING BTREE,
  INDEX `idx_sys_oper_log_ot`(`oper_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 271 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '操作日志记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------
INSERT INTO `sys_oper_log` VALUES (100, '代码生成', 6, 'com.ruoyi.generator.controller.GenController.importTableSave()', 'POST', 1, 'admin', '研发部门', '/tool/gen/importTable', '127.0.0.1', '内网IP', '{\"tables\":\"b_danger,b_govern,b_inspector_unit,b_patrol_file,b_inspector,b_patrol_type,b_patrol,b_patrol_unit,b_project,b_audit\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-05 13:50:52', 494);
INSERT INTO `sys_oper_log` VALUES (101, '代码生成', 8, 'com.ruoyi.generator.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '研发部门', '/tool/gen/batchGenCode', '127.0.0.1', '内网IP', '{\"tables\":\"b_govern\"}', NULL, 0, NULL, '2024-03-05 14:09:14', 32);
INSERT INTO `sys_oper_log` VALUES (102, '代码生成', 8, 'com.ruoyi.generator.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '研发部门', '/tool/gen/batchGenCode', '127.0.0.1', '内网IP', '{\"tables\":\"b_inspector\"}', NULL, 0, NULL, '2024-03-05 14:09:18', 25);
INSERT INTO `sys_oper_log` VALUES (103, '代码生成', 8, 'com.ruoyi.generator.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '研发部门', '/tool/gen/batchGenCode', '127.0.0.1', '内网IP', '{\"tables\":\"b_inspector_unit\"}', NULL, 0, NULL, '2024-03-05 14:09:19', 17);
INSERT INTO `sys_oper_log` VALUES (104, '代码生成', 8, 'com.ruoyi.generator.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '研发部门', '/tool/gen/batchGenCode', '127.0.0.1', '内网IP', '{\"tables\":\"b_patrol\"}', NULL, 0, NULL, '2024-03-05 14:09:20', 28);
INSERT INTO `sys_oper_log` VALUES (105, '代码生成', 8, 'com.ruoyi.generator.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '研发部门', '/tool/gen/batchGenCode', '127.0.0.1', '内网IP', '{\"tables\":\"b_patrol_file\"}', NULL, 0, NULL, '2024-03-05 14:09:21', 20);
INSERT INTO `sys_oper_log` VALUES (106, '代码生成', 8, 'com.ruoyi.generator.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '研发部门', '/tool/gen/batchGenCode', '127.0.0.1', '内网IP', '{\"tables\":\"b_audit,b_danger,b_govern,b_inspector,b_inspector_unit,b_patrol,b_patrol_file,b_patrol_type,b_patrol_unit,b_project\"}', NULL, 0, NULL, '2024-03-05 14:11:37', 244);
INSERT INTO `sys_oper_log` VALUES (107, '代码生成', 8, 'com.ruoyi.generator.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '研发部门', '/tool/gen/batchGenCode', '127.0.0.1', '内网IP', '{\"tables\":\"b_audit\"}', NULL, 0, NULL, '2024-03-05 17:42:34', 85);
INSERT INTO `sys_oper_log` VALUES (108, '代码生成', 8, 'com.ruoyi.generator.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '研发部门', '/tool/gen/batchGenCode', '127.0.0.1', '内网IP', '{\"tables\":\"b_project\"}', NULL, 0, NULL, '2024-03-05 17:55:52', 97);
INSERT INTO `sys_oper_log` VALUES (109, '菜单管理', 1, 'com.ruoyi.web.controller.system.SysMenuController.add()', 'POST', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/project/index\",\"createBy\":\"admin\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"项目管理\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":0,\"path\":\"project\",\"status\":\"0\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-05 18:00:17', 44);
INSERT INTO `sys_oper_log` VALUES (110, '项目信息', 1, 'com.ruoyi.web.controller.termite.BProjectController.add()', 'POST', 1, 'admin', '研发部门', '/system/project', '127.0.0.1', '内网IP', '{\"address\":\"1\",\"createTime\":\"2024-03-05 18:40:36\",\"delFlag\":\"0\",\"deptId\":100,\"id\":1,\"params\":{},\"person\":\"1\",\"personPhone\":\"1\",\"projectName\":\"水库\",\"projectType\":1,\"registrationNumber\":\"1\",\"reservoirGrade\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-05 18:40:36', 45);
INSERT INTO `sys_oper_log` VALUES (111, '项目信息', 1, 'com.ruoyi.web.controller.termite.BProjectController.add()', 'POST', 1, 'admin', '研发部门', '/system/project', '127.0.0.1', '内网IP', '{\"address\":\"2\",\"createTime\":\"2024-03-05 18:42:24\",\"delFlag\":\"0\",\"deptId\":102,\"id\":2,\"params\":{},\"person\":\"2\",\"personPhone\":\"2\",\"projectName\":\"水库2\",\"projectType\":1,\"registrationNumber\":\"2\",\"reservoirGrade\":2}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-05 18:42:24', 36);
INSERT INTO `sys_oper_log` VALUES (112, '项目信息', 1, 'com.ruoyi.web.controller.termite.BProjectController.add()', 'POST', 1, 'admin', '研发部门', '/system/project', '127.0.0.1', '内网IP', '{\"address\":\"1\",\"createTime\":\"2024-03-05 18:45:15\",\"delFlag\":\"0\",\"deptId\":102,\"dykeGrade\":1,\"dykeLength\":1,\"dykePatrolLength\":1,\"dykePatrolPile\":\"1\",\"dykePile\":\"1\",\"id\":3,\"params\":{},\"person\":\"1\",\"personPhone\":\"1\",\"projectName\":\"堤坝\",\"projectType\":1,\"registrationNumber\":\"1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-05 18:45:15', 33);
INSERT INTO `sys_oper_log` VALUES (113, '项目信息', 3, 'com.ruoyi.web.controller.termite.BProjectController.remove()', 'DELETE', 1, 'admin', '研发部门', '/system/project/3', '127.0.0.1', '内网IP', '{}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-05 18:46:39', 43);
INSERT INTO `sys_oper_log` VALUES (114, '项目信息', 1, 'com.ruoyi.web.controller.termite.BProjectController.add()', 'POST', 1, 'admin', '研发部门', '/system/project', '127.0.0.1', '内网IP', '{\"address\":\"2\",\"createTime\":\"2024-03-05 18:46:54\",\"delFlag\":\"0\",\"deptId\":101,\"dykeGrade\":2,\"dykeLength\":2,\"dykePatrolLength\":2,\"dykePatrolPile\":\"2\",\"dykePile\":\"2\",\"id\":4,\"params\":{},\"person\":\"2\",\"personPhone\":\"2\",\"projectName\":\"堤坝\",\"projectType\":1,\"registrationNumber\":\"2\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-05 18:46:54', 45);
INSERT INTO `sys_oper_log` VALUES (115, '项目信息', 1, 'com.ruoyi.web.controller.termite.BProjectController.add()', 'POST', 1, 'admin', '研发部门', '/system/project', '127.0.0.1', '内网IP', '{\"address\":\"1\",\"createTime\":\"2024-03-05 18:48:16\",\"delFlag\":\"0\",\"deptId\":102,\"dykeGrade\":1,\"dykeLength\":1,\"dykePatrolLength\":1,\"dykePatrolPile\":\"1\",\"dykePile\":\"1\",\"id\":5,\"params\":{},\"person\":\"1\",\"personPhone\":\"1\",\"projectName\":\"堤坝\",\"projectType\":1,\"registrationNumber\":\"1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-05 18:48:16', 38);
INSERT INTO `sys_oper_log` VALUES (116, '项目信息', 3, 'com.ruoyi.web.controller.termite.BProjectController.remove()', 'DELETE', 1, 'admin', '研发部门', '/system/project/4', '127.0.0.1', '内网IP', '{}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-05 18:50:48', 38);
INSERT INTO `sys_oper_log` VALUES (117, '项目信息', 3, 'com.ruoyi.web.controller.termite.BProjectController.remove()', 'DELETE', 1, 'admin', '研发部门', '/system/project/5', '127.0.0.1', '内网IP', '{}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-05 18:50:50', 35);
INSERT INTO `sys_oper_log` VALUES (118, '项目信息', 1, 'com.ruoyi.web.controller.termite.BProjectController.add()', 'POST', 1, 'admin', '研发部门', '/system/project', '127.0.0.1', '内网IP', '{\"address\":\"2\",\"createTime\":\"2024-03-05 18:51:05\",\"delFlag\":\"0\",\"deptId\":101,\"dykeGrade\":2,\"dykeLength\":2,\"dykePatrolLength\":2,\"dykePatrolPile\":\"2\",\"dykePile\":\"2\",\"id\":6,\"params\":{},\"person\":\"2\",\"personPhone\":\"2\",\"projectName\":\"堤坝\",\"projectType\":2,\"registrationNumber\":\"2\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-05 18:51:05', 94);
INSERT INTO `sys_oper_log` VALUES (119, '项目信息', 1, 'com.ruoyi.web.controller.termite.BProjectController.add()', 'POST', 1, 'admin', '研发部门', '/system/project', '127.0.0.1', '内网IP', '{\"address\":\"1\",\"createTime\":\"2024-03-05 18:51:23\",\"delFlag\":\"0\",\"deptId\":101,\"id\":7,\"params\":{},\"person\":\"1\",\"personPhone\":\"1\",\"projectName\":\"水库3\",\"projectType\":1,\"registrationNumber\":\"1\",\"reservoirGrade\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-05 18:51:23', 37);
INSERT INTO `sys_oper_log` VALUES (120, '项目信息', 1, 'com.ruoyi.web.controller.termite.BProjectController.add()', 'POST', 1, 'admin', '研发部门', '/system/project', '127.0.0.1', '内网IP', '{\"address\":\"1\",\"createTime\":\"2024-03-05 18:51:36\",\"delFlag\":\"0\",\"deptId\":100,\"dykeGrade\":1,\"dykeLength\":1,\"dykePatrolLength\":1,\"dykePatrolPile\":\"1\",\"dykePile\":\"1\",\"id\":8,\"params\":{},\"person\":\"1\",\"personPhone\":\"1\",\"projectName\":\"堤坝2\",\"projectType\":2,\"registrationNumber\":\"1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-05 18:51:37', 41);
INSERT INTO `sys_oper_log` VALUES (121, '项目信息', 5, 'com.ruoyi.web.controller.termite.BProjectController.export()', 'POST', 1, 'admin', '研发部门', '/system/project/export', '127.0.0.1', '内网IP', '{\"projectType\":\"1\",\"pageSize\":\"10\",\"pageNum\":\"1\"}', NULL, 0, NULL, '2024-03-06 08:53:27', 828);
INSERT INTO `sys_oper_log` VALUES (122, '项目信息', 1, 'com.ruoyi.web.controller.termite.BProjectController.add()', 'POST', 1, 'admin', '研发部门', '/system/project', '127.0.0.1', '内网IP', '{\"address\":\"4\",\"createTime\":\"2024-03-06 08:55:43\",\"delFlag\":\"0\",\"deptId\":100,\"id\":9,\"params\":{},\"person\":\"4\",\"personPhone\":\"4\",\"projectName\":\"水库4\",\"projectType\":1,\"registrationNumber\":\"4\",\"reservoirGrade\":4}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 08:55:43', 32);
INSERT INTO `sys_oper_log` VALUES (123, '项目信息', 2, 'com.ruoyi.web.controller.termite.BProjectController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/project', '127.0.0.1', '内网IP', '{\"address\":\"1\",\"createTime\":\"2024-03-05 18:51:23\",\"delFlag\":\"0\",\"deptId\":101,\"id\":7,\"params\":{},\"person\":\"1\",\"personPhone\":\"122\",\"projectName\":\"水库3\",\"projectType\":1,\"registrationNumber\":\"1\",\"reservoirGrade\":1,\"updateTime\":\"2024-03-06 09:21:24\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 09:21:24', 94);
INSERT INTO `sys_oper_log` VALUES (124, '项目信息', 2, 'com.ruoyi.web.controller.termite.BProjectController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/project', '127.0.0.1', '内网IP', '{\"address\":\"1\",\"createTime\":\"2024-03-05 18:40:36\",\"delFlag\":\"0\",\"deptId\":100,\"id\":1,\"params\":{},\"person\":\"1\",\"personPhone\":\"1\",\"projectName\":\"水库\",\"projectType\":1,\"registrationNumber\":\"1\",\"reservoirGrade\":1,\"updateTime\":\"2024-03-06 09:21:53\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 09:21:53', 30);
INSERT INTO `sys_oper_log` VALUES (125, '项目信息', 2, 'com.ruoyi.web.controller.termite.BProjectController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/project', '127.0.0.1', '内网IP', '{\"address\":\"1\",\"createTime\":\"2024-03-05 18:40:36\",\"delFlag\":\"0\",\"deptId\":100,\"id\":1,\"params\":{},\"person\":\"1\",\"personPhone\":\"18732445930\",\"projectName\":\"水库\",\"projectType\":1,\"registrationNumber\":\"1\",\"reservoirGrade\":1,\"updateTime\":\"2024-03-06 09:22:09\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 09:22:09', 69);
INSERT INTO `sys_oper_log` VALUES (126, '字典类型', 1, 'com.ruoyi.web.controller.system.SysDictTypeController.add()', 'POST', 1, 'admin', '研发部门', '/system/dict/type', '192.168.1.129', '内网IP', '{\"createBy\":\"admin\",\"dictName\":\"致险类型\",\"dictType\":\"danger_type\",\"params\":{},\"status\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 10:01:58', 42);
INSERT INTO `sys_oper_log` VALUES (127, '字典数据', 1, 'com.ruoyi.web.controller.system.SysDictDataController.add()', 'POST', 1, 'admin', '研发部门', '/system/dict/data', '192.168.1.129', '内网IP', '{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"渗漏处数\",\"dictSort\":0,\"dictType\":\"danger_type\",\"dictValue\":\"1\",\"listClass\":\"default\",\"params\":{},\"status\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 10:02:15', 57);
INSERT INTO `sys_oper_log` VALUES (128, '字典数据', 1, 'com.ruoyi.web.controller.system.SysDictDataController.add()', 'POST', 1, 'admin', '研发部门', '/system/dict/data', '192.168.1.129', '内网IP', '{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"穿坝处数\",\"dictSort\":0,\"dictType\":\"danger_type\",\"dictValue\":\"2\",\"listClass\":\"default\",\"params\":{},\"status\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 10:02:24', 48);
INSERT INTO `sys_oper_log` VALUES (129, '字典数据', 2, 'com.ruoyi.web.controller.system.SysDictDataController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/dict/data', '192.168.1.129', '内网IP', '{\"createBy\":\"admin\",\"createTime\":\"2024-03-06 10:02:24\",\"default\":false,\"dictCode\":101,\"dictLabel\":\"穿坝处数\",\"dictSort\":1,\"dictType\":\"danger_type\",\"dictValue\":\"2\",\"isDefault\":\"N\",\"listClass\":\"default\",\"params\":{},\"status\":\"0\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 10:02:30', 40);
INSERT INTO `sys_oper_log` VALUES (130, '字典数据', 1, 'com.ruoyi.web.controller.system.SysDictDataController.add()', 'POST', 1, 'admin', '研发部门', '/system/dict/data', '192.168.1.129', '内网IP', '{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"跌窝处数\",\"dictSort\":2,\"dictType\":\"danger_type\",\"dictValue\":\"3\",\"listClass\":\"default\",\"params\":{},\"status\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 10:02:41', 37);
INSERT INTO `sys_oper_log` VALUES (131, '字典类型', 1, 'com.ruoyi.web.controller.system.SysDictTypeController.add()', 'POST', 1, 'admin', '研发部门', '/system/dict/type', '192.168.1.129', '内网IP', '{\"createBy\":\"admin\",\"dictName\":\"治理情况\",\"dictType\":\"govern_type\",\"params\":{},\"status\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 10:03:12', 39);
INSERT INTO `sys_oper_log` VALUES (132, '字典数据', 1, 'com.ruoyi.web.controller.system.SysDictDataController.add()', 'POST', 1, 'admin', '研发部门', '/system/dict/data', '192.168.1.129', '内网IP', '{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"挖巢数量 （个）\",\"dictSort\":0,\"dictType\":\"govern_type\",\"dictValue\":\"1\",\"listClass\":\"default\",\"params\":{},\"status\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 10:03:37', 70);
INSERT INTO `sys_oper_log` VALUES (133, '字典数据', 1, 'com.ruoyi.web.controller.system.SysDictDataController.add()', 'POST', 1, 'admin', '研发部门', '/system/dict/data', '192.168.1.129', '内网IP', '{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"施药面积 （m2）\",\"dictSort\":1,\"dictType\":\"govern_type\",\"dictValue\":\"2\",\"listClass\":\"default\",\"params\":{},\"status\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 10:03:55', 76);
INSERT INTO `sys_oper_log` VALUES (134, '字典数据', 1, 'com.ruoyi.web.controller.system.SysDictDataController.add()', 'POST', 1, 'admin', '研发部门', '/system/dict/data', '192.168.1.129', '内网IP', '{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"灌浆量 （延米）\",\"dictSort\":2,\"dictType\":\"govern_type\",\"dictValue\":\"3\",\"listClass\":\"default\",\"params\":{},\"status\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 10:04:10', 57);
INSERT INTO `sys_oper_log` VALUES (135, '字典数据', 1, 'com.ruoyi.web.controller.system.SysDictDataController.add()', 'POST', 1, 'admin', '研发部门', '/system/dict/data', '192.168.1.129', '内网IP', '{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"投入资金 （万元）\",\"dictSort\":3,\"dictType\":\"govern_type\",\"dictValue\":\"4\",\"listClass\":\"default\",\"params\":{},\"status\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 10:04:37', 36);
INSERT INTO `sys_oper_log` VALUES (136, '字典数据', 1, 'com.ruoyi.web.controller.system.SysDictDataController.add()', 'POST', 1, 'admin', '研发部门', '/system/dict/data', '192.168.1.129', '内网IP', '{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"治理 数量\",\"dictSort\":4,\"dictType\":\"govern_type\",\"dictValue\":\"5\",\"listClass\":\"default\",\"params\":{},\"status\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 10:04:52', 37);
INSERT INTO `sys_oper_log` VALUES (137, '字典数据', 2, 'com.ruoyi.web.controller.system.SysDictDataController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/dict/data', '192.168.1.129', '内网IP', '{\"createBy\":\"admin\",\"createTime\":\"2024-03-06 10:04:52\",\"default\":false,\"dictCode\":107,\"dictLabel\":\"治理数量\",\"dictSort\":4,\"dictType\":\"govern_type\",\"dictValue\":\"5\",\"isDefault\":\"N\",\"listClass\":\"default\",\"params\":{},\"status\":\"0\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 10:04:58', 42);
INSERT INTO `sys_oper_log` VALUES (138, '菜单管理', 3, 'com.ruoyi.web.controller.system.SysMenuController.remove()', 'DELETE', 1, 'admin', '研发部门', '/system/menu/4', '127.0.0.1', '内网IP', '{}', '{\"msg\":\"菜单已分配,不允许删除\",\"code\":601}', 0, NULL, '2024-03-06 11:07:39', 5);
INSERT INTO `sys_oper_log` VALUES (139, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '研发部门', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"createTime\":\"2024-03-05 09:07:03\",\"icon\":\"guide\",\"isCache\":\"0\",\"isFrame\":\"0\",\"menuId\":4,\"menuName\":\"若依官网\",\"menuType\":\"M\",\"orderNum\":4,\"params\":{},\"parentId\":0,\"path\":\"http://ruoyi.vip\",\"perms\":\"\",\"query\":\"\",\"status\":\"1\",\"updateBy\":\"admin\",\"visible\":\"1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 11:07:48', 41);
INSERT INTO `sys_oper_log` VALUES (140, '代码生成', 8, 'com.ruoyi.generator.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '若依科技', '/tool/gen/batchGenCode', '127.0.0.1', '内网IP', '{\"tables\":\"b_patrol_unit\"}', NULL, 0, NULL, '2024-03-06 14:41:00', 179);
INSERT INTO `sys_oper_log` VALUES (141, '菜单管理', 1, 'com.ruoyi.web.controller.system.SysMenuController.add()', 'POST', 1, 'admin', '若依科技', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"views/system/unit\",\"createBy\":\"admin\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"普查单元\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":0,\"path\":\"unit\",\"status\":\"0\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 14:42:58', 63);
INSERT INTO `sys_oper_log` VALUES (142, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/unit\",\"createTime\":\"2024-03-06 14:42:58\",\"icon\":\"#\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2001,\"menuName\":\"普查单元\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":0,\"path\":\"unit\",\"perms\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 14:43:06', 43);
INSERT INTO `sys_oper_log` VALUES (143, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/unit/index\",\"createTime\":\"2024-03-06 14:42:58\",\"icon\":\"#\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2001,\"menuName\":\"普查单元\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":0,\"path\":\"unit\",\"perms\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 14:43:17', 43);
INSERT INTO `sys_oper_log` VALUES (144, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/unit/index\",\"createTime\":\"2024-03-06 14:42:58\",\"icon\":\"#\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2001,\"menuName\":\"普查单元\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":1,\"path\":\"unit\",\"perms\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 14:54:01', 41);
INSERT INTO `sys_oper_log` VALUES (145, '巡查单元', 1, 'com.ruoyi.web.controller.termite.BPatrolUnitController.add()', 'POST', 1, 'admin', '若依科技', '/bPatrol/unit', '127.0.0.1', '内网IP', '{\"createTime\":\"2024-03-06 15:01:13\",\"delFlag\":\"false\",\"params\":{},\"projectId\":1,\"unitName\":\"111\"}', NULL, 1, '\r\n### Error updating database.  Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'del_flag\' at row 1\r\n### The error may exist in file [D:\\tj\\bypc\\ruoyi-system\\target\\classes\\mapper\\system\\BPatrolUnitMapper.xml]\r\n### The error may involve com.ruoyi.system.mapper.BPatrolUnitMapper.insertBPatrolUnit-Inline\r\n### The error occurred while setting parameters\r\n### SQL: insert into b_patrol_unit          ( project_id,             unit_name,             del_flag,                          create_time )           values ( ?,             ?,             ?,                          ? )\r\n### Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'del_flag\' at row 1\n; Data truncation: Data too long for column \'del_flag\' at row 1; nested exception is com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column \'del_flag\' at row 1', '2024-03-06 15:01:13', 106);
INSERT INTO `sys_oper_log` VALUES (146, '巡查单元', 1, 'com.ruoyi.web.controller.termite.BPatrolUnitController.add()', 'POST', 1, 'admin', '若依科技', '/bPatrol/unit', '127.0.0.1', '内网IP', '{\"createTime\":\"2024-03-06 15:16:42\",\"delFlag\":\"0\",\"id\":1,\"params\":{},\"projectId\":1,\"unitName\":\"1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 15:16:42', 35);
INSERT INTO `sys_oper_log` VALUES (147, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/unit/index\",\"createTime\":\"2024-03-06 14:42:58\",\"icon\":\"#\",\"isCache\":\"1\",\"isFrame\":\"1\",\"menuId\":2001,\"menuName\":\"普查单元\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":1,\"path\":\"unit\",\"perms\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 15:38:14', 47);
INSERT INTO `sys_oper_log` VALUES (148, '项目信息', 1, 'com.ruoyi.web.controller.termite.BProjectController.add()', 'POST', 1, 'admin', '若依科技', '/system/project', '127.0.0.1', '内网IP', '{\"address\":\"3\",\"createTime\":\"2024-03-06 16:11:01\",\"delFlag\":\"0\",\"deptId\":101,\"dykeGrade\":3,\"dykeLength\":3,\"dykePatrolLength\":3,\"dykePatrolPile\":\"k2+256\",\"dykePatrolPileEnd\":\"k10+200\",\"dykePile\":\"3\",\"dykePileEnd\":\"3\",\"id\":10,\"params\":{},\"person\":\"3\",\"personPhone\":\"13333333333\",\"projectName\":\"堤防3\",\"projectType\":2,\"registrationNumber\":\"3\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 16:11:01', 543);
INSERT INTO `sys_oper_log` VALUES (149, '菜单管理', 1, 'com.ruoyi.web.controller.system.SysMenuController.add()', 'POST', 1, 'admin', '若依科技', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/users/index\",\"createBy\":\"admin\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"用户管理\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":0,\"path\":\"users\",\"status\":\"0\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-06 16:30:17', 36);
INSERT INTO `sys_oper_log` VALUES (150, '用户管理', 1, 'com.ruoyi.web.controller.system.SysUserController.add()', 'POST', 1, 'admin', '若依科技', '/system/user', '192.168.1.117', '内网IP', '{\"admin\":false,\"createBy\":\"admin\",\"deptId\":101,\"nickName\":\"ceshi\",\"params\":{},\"postIds\":[],\"roleIds\":[2],\"status\":\"0\",\"userId\":100,\"userName\":\"ceshi\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-07 10:30:02', 146);
INSERT INTO `sys_oper_log` VALUES (151, '角色管理', 2, 'com.ruoyi.web.controller.system.SysRoleController.dataScope()', 'PUT', 1, 'admin', '若依科技', '/system/role/dataScope', '192.168.1.117', '内网IP', '{\"admin\":false,\"createTime\":\"2024-03-05 09:07:03\",\"dataScope\":\"4\",\"delFlag\":\"0\",\"deptCheckStrictly\":true,\"deptIds\":[],\"flag\":false,\"menuCheckStrictly\":true,\"params\":{},\"remark\":\"普通角色\",\"roleId\":2,\"roleKey\":\"common\",\"roleName\":\"普通角色\",\"roleSort\":2,\"status\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-07 10:34:39', 151);
INSERT INTO `sys_oper_log` VALUES (152, '代码生成', 8, 'com.ruoyi.generator.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '若依科技', '/tool/gen/batchGenCode', '127.0.0.1', '内网IP', '{\"tables\":\"b_inspector\"}', NULL, 0, NULL, '2024-03-07 14:46:58', 240);
INSERT INTO `sys_oper_log` VALUES (153, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/inspector/index\",\"createTime\":\"2024-03-06 16:30:17\",\"icon\":\"#\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2002,\"menuName\":\"用户管理\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":0,\"path\":\"inspector\",\"perms\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-07 14:48:27', 47);
INSERT INTO `sys_oper_log` VALUES (154, '代码生成', 6, 'com.ruoyi.generator.controller.GenController.importTableSave()', 'POST', 1, 'admin', '若依科技', '/tool/gen/importTable', '192.168.1.117', '内网IP', '{\"tables\":\"b_fill\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-07 15:43:53', 172);
INSERT INTO `sys_oper_log` VALUES (155, '代码生成', 8, 'com.ruoyi.generator.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '若依科技', '/tool/gen/batchGenCode', '192.168.1.117', '内网IP', '{\"tables\":\"b_fill\"}', NULL, 0, NULL, '2024-03-07 15:44:00', 213);
INSERT INTO `sys_oper_log` VALUES (156, '巡查员用户', 1, 'com.ruoyi.web.controller.termite.BInspectorController.add()', 'POST', 1, 'admin', '若依科技', '/system/inspector', '127.0.0.1', '内网IP', '{\"delFlag\":\"0\",\"deptId\":100,\"id\":2,\"inspectorName\":\"b\",\"params\":{},\"phone\":\"187255444\",\"userId\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-07 15:51:16', 118);
INSERT INTO `sys_oper_log` VALUES (157, '巡查员用户', 2, 'com.ruoyi.web.controller.termite.BInspectorController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/inspector', '127.0.0.1', '内网IP', '{\"delFlag\":\"0\",\"deptId\":100,\"id\":2,\"inspectorName\":\"b\",\"params\":{},\"phone\":\"18725544400\",\"userId\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-07 16:04:09', 39);
INSERT INTO `sys_oper_log` VALUES (158, '巡查员用户', 2, 'com.ruoyi.web.controller.termite.BInspectorController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/inspector', '127.0.0.1', '内网IP', '{\"delFlag\":\"0\",\"deptId\":100,\"id\":2,\"inspectorName\":\"b\",\"params\":{},\"phone\":\"18725544400\",\"userId\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-07 16:04:21', 38);
INSERT INTO `sys_oper_log` VALUES (159, '巡查员用户', 2, 'com.ruoyi.web.controller.termite.BInspectorController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/inspector', '127.0.0.1', '内网IP', '{\"delFlag\":\"0\",\"deptId\":100,\"id\":2,\"inspectorName\":\"b\",\"params\":{},\"phone\":\"18725544400\",\"userId\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-07 16:19:40', 40);
INSERT INTO `sys_oper_log` VALUES (160, '巡查员用户', 2, 'com.ruoyi.web.controller.termite.BInspectorController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/inspector', '127.0.0.1', '内网IP', '{\"delFlag\":\"0\",\"deptId\":100,\"id\":1,\"inspectorName\":\"a\",\"params\":{},\"phone\":\"13521962466\",\"userId\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-07 16:19:46', 42);
INSERT INTO `sys_oper_log` VALUES (161, '巡查员用户', 2, 'com.ruoyi.web.controller.termite.BInspectorController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/inspector', '127.0.0.1', '内网IP', '{\"delFlag\":\"0\",\"deptId\":100,\"id\":2,\"inspectorName\":\"b\",\"params\":{},\"phone\":\"18725544400\",\"userId\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-07 16:20:39', 28);
INSERT INTO `sys_oper_log` VALUES (162, '巡查员用户', 2, 'com.ruoyi.web.controller.termite.BInspectorController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/inspector', '127.0.0.1', '内网IP', '{\"delFlag\":\"0\",\"deptId\":100,\"id\":1,\"inspectorName\":\"a\",\"params\":{},\"phone\":\"13521962466\",\"userId\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-07 16:24:32', 62);
INSERT INTO `sys_oper_log` VALUES (163, '巡查员用户', 2, 'com.ruoyi.web.controller.termite.BInspectorController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/inspector', '127.0.0.1', '内网IP', '{\"delFlag\":\"0\",\"deptId\":100,\"id\":1,\"inspectorName\":\"a\",\"params\":{},\"phone\":\"13521962466\",\"userId\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-07 16:26:56', 54);
INSERT INTO `sys_oper_log` VALUES (164, '巡查员用户', 2, 'com.ruoyi.web.controller.termite.BInspectorController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/inspector', '127.0.0.1', '内网IP', '{\"delFlag\":\"0\",\"deptId\":100,\"id\":1,\"inspectorName\":\"a\",\"params\":{},\"phone\":\"13521962466\",\"userId\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-07 16:27:12', 105);
INSERT INTO `sys_oper_log` VALUES (165, '巡查员用户', 2, 'com.ruoyi.web.controller.termite.BInspectorController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/inspector', '127.0.0.1', '内网IP', '{\"delFlag\":\"0\",\"deptId\":100,\"id\":1,\"inspectorName\":\"a\",\"params\":{},\"phone\":\"13521962466\",\"userId\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-07 16:29:06', 114);
INSERT INTO `sys_oper_log` VALUES (166, '代码生成', 8, 'com.ruoyi.generator.controller.GenController.batchGenCode()', 'GET', 1, 'admin', '若依科技', '/tool/gen/batchGenCode', '127.0.0.1', '内网IP', '{\"tables\":\"b_audit\"}', NULL, 0, NULL, '2024-03-07 16:30:43', 74);
INSERT INTO `sys_oper_log` VALUES (167, '菜单管理', 1, 'com.ruoyi.web.controller.system.SysMenuController.add()', 'POST', 1, 'admin', '若依科技', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"createBy\":\"admin\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"数据审批\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":0,\"path\":\"audit\",\"status\":\"0\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-07 16:33:04', 78);
INSERT INTO `sys_oper_log` VALUES (168, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/audit/index\",\"createTime\":\"2024-03-07 16:33:04\",\"icon\":\"#\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2003,\"menuName\":\"数据审批\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":0,\"path\":\"audit\",\"perms\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-07 16:33:13', 76);
INSERT INTO `sys_oper_log` VALUES (169, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/audit/index\",\"createTime\":\"2024-03-07 16:33:04\",\"icon\":\"#\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2003,\"menuName\":\"巡查审批\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":0,\"path\":\"audit\",\"perms\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 09:38:30', 163);
INSERT INTO `sys_oper_log` VALUES (170, '菜单管理', 1, 'com.ruoyi.web.controller.system.SysMenuController.add()', 'POST', 1, 'admin', '若依科技', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"createBy\":\"admin\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"巡查记录\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":0,\"path\":\"record\",\"status\":\"0\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 09:42:42', 85);
INSERT INTO `sys_oper_log` VALUES (171, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/record/index\",\"createTime\":\"2024-03-08 09:42:42\",\"icon\":\"#\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2004,\"menuName\":\"巡查记录\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":0,\"path\":\"record\",\"perms\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 09:42:54', 107);
INSERT INTO `sys_oper_log` VALUES (172, '菜单管理', 3, 'com.ruoyi.web.controller.system.SysMenuController.remove()', 'DELETE', 1, 'admin', '若依科技', '/system/menu/2004', '127.0.0.1', '内网IP', '{}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 09:48:51', 179);
INSERT INTO `sys_oper_log` VALUES (173, '菜单管理', 1, 'com.ruoyi.web.controller.system.SysMenuController.add()', 'POST', 1, 'admin', '若依科技', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"patrol\",\"createBy\":\"admin\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"巡查记录\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":1,\"path\":\"patrol\",\"status\":\"0\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 10:08:32', 72);
INSERT INTO `sys_oper_log` VALUES (174, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/patrol/index\",\"createTime\":\"2024-03-08 10:08:32\",\"icon\":\"#\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2005,\"menuName\":\"巡查记录\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":1,\"path\":\"patrol\",\"perms\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 10:08:42', 108);
INSERT INTO `sys_oper_log` VALUES (175, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/patrol/index\",\"createTime\":\"2024-03-08 10:08:32\",\"icon\":\"#\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2005,\"menuName\":\"巡查记录\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":1,\"path\":\"patrol\",\"perms\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 10:08:53', 137);
INSERT INTO `sys_oper_log` VALUES (176, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/patrol/index\",\"createTime\":\"2024-03-08 10:08:32\",\"icon\":\"#\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2005,\"menuName\":\"巡查记录\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":1,\"path\":\"patrol\",\"perms\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 10:11:16', 144);
INSERT INTO `sys_oper_log` VALUES (177, '菜单管理', 1, 'com.ruoyi.web.controller.system.SysMenuController.add()', 'POST', 1, 'admin', '若依科技', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"fill\",\"createBy\":\"admin\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"填报记录\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":100,\"path\":\"fill\",\"status\":\"0\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 10:24:21', 101);
INSERT INTO `sys_oper_log` VALUES (178, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/fill/index\",\"createTime\":\"2024-03-08 10:24:21\",\"icon\":\"#\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2006,\"menuName\":\"填报记录\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":1,\"path\":\"fill\",\"perms\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 10:25:09', 227);
INSERT INTO `sys_oper_log` VALUES (179, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/fill/index\",\"createTime\":\"2024-03-08 10:24:21\",\"icon\":\"#\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2006,\"menuName\":\"填报记录\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":1,\"path\":\"fill\",\"perms\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 10:25:20', 63);
INSERT INTO `sys_oper_log` VALUES (180, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/patrol/index\",\"createTime\":\"2024-03-08 10:08:32\",\"icon\":\"#\",\"isCache\":\"1\",\"isFrame\":\"1\",\"menuId\":2005,\"menuName\":\"巡查记录\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":1,\"path\":\"patrol\",\"perms\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 10:25:37', 130);
INSERT INTO `sys_oper_log` VALUES (181, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/fill/index\",\"createTime\":\"2024-03-08 10:24:21\",\"icon\":\"#\",\"isCache\":\"1\",\"isFrame\":\"1\",\"menuId\":2006,\"menuName\":\"填报记录\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":1,\"path\":\"fill\",\"perms\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 10:25:41', 115);
INSERT INTO `sys_oper_log` VALUES (182, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/menu', '114.241.111.94', 'XX XX', '{\"children\":[],\"createTime\":\"2024-03-05 09:07:03\",\"icon\":\"system\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":1,\"menuName\":\"系统管理\",\"menuType\":\"M\",\"orderNum\":2,\"params\":{},\"parentId\":0,\"path\":\"system\",\"perms\":\"\",\"query\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 15:06:19', 32);
INSERT INTO `sys_oper_log` VALUES (183, '部门管理', 2, 'com.ruoyi.web.controller.system.SysDeptController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/dept', '120.244.206.205', 'XX XX', '{\"ancestors\":\"0\",\"children\":[],\"deptId\":100,\"deptName\":\"水利水电科学研究院\",\"email\":\"ry@qq.com\",\"leader\":\"研究院\",\"orderNum\":0,\"params\":{},\"parentId\":0,\"phone\":\"15888888888\",\"status\":\"0\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 21:43:52', 15);
INSERT INTO `sys_oper_log` VALUES (184, '部门管理', 2, 'com.ruoyi.web.controller.system.SysDeptController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/dept', '120.244.206.205', 'XX XX', '{\"ancestors\":\"0,100\",\"children\":[],\"deptId\":101,\"deptName\":\"广西省水利厅\",\"email\":\"ry@qq.com\",\"leader\":\"若依\",\"orderNum\":1,\"params\":{},\"parentId\":100,\"parentName\":\"水利水电科学研究院\",\"phone\":\"15888888888\",\"status\":\"0\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 21:44:09', 22);
INSERT INTO `sys_oper_log` VALUES (185, '部门管理', 2, 'com.ruoyi.web.controller.system.SysDeptController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/dept', '120.244.206.205', 'XX XX', '{\"ancestors\":\"0,100,101\",\"children\":[],\"deptId\":103,\"deptName\":\"南宁市水利局\",\"email\":\"ry@qq.com\",\"leader\":\"南宁\",\"orderNum\":1,\"params\":{},\"parentId\":101,\"parentName\":\"广西省水利厅\",\"phone\":\"15888888888\",\"status\":\"0\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 21:44:47', 17);
INSERT INTO `sys_oper_log` VALUES (186, '部门管理', 2, 'com.ruoyi.web.controller.system.SysDeptController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/dept', '120.244.206.205', 'XX XX', '{\"ancestors\":\"0,100\",\"children\":[],\"deptId\":101,\"deptName\":\"广西省水利厅\",\"email\":\"ry@qq.com\",\"leader\":\"广西省水利厅\",\"orderNum\":1,\"params\":{},\"parentId\":100,\"parentName\":\"水利水电科学研究院\",\"phone\":\"15888888888\",\"status\":\"0\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 21:45:01', 22);
INSERT INTO `sys_oper_log` VALUES (187, '部门管理', 2, 'com.ruoyi.web.controller.system.SysDeptController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/dept', '120.244.206.205', 'XX XX', '{\"ancestors\":\"0,100,101\",\"children\":[],\"deptId\":104,\"deptName\":\"桂林市水利局\",\"email\":\"ry@qq.com\",\"leader\":\"桂林市水利局\",\"orderNum\":2,\"params\":{},\"parentId\":101,\"parentName\":\"广西省水利厅\",\"phone\":\"15888888888\",\"status\":\"0\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 21:45:35', 18);
INSERT INTO `sys_oper_log` VALUES (188, '部门管理', 2, 'com.ruoyi.web.controller.system.SysDeptController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/dept', '120.244.206.205', 'XX XX', '{\"ancestors\":\"0,100,101\",\"children\":[],\"deptId\":105,\"deptName\":\"北海市水利厅\",\"email\":\"ry@qq.com\",\"leader\":\"\",\"orderNum\":3,\"params\":{},\"parentId\":101,\"parentName\":\"广西省水利厅\",\"phone\":\"15888888888\",\"status\":\"0\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 21:45:59', 19);
INSERT INTO `sys_oper_log` VALUES (189, '部门管理', 2, 'com.ruoyi.web.controller.system.SysDeptController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/dept', '120.244.206.205', 'XX XX', '{\"ancestors\":\"0,100,101\",\"children\":[],\"deptId\":106,\"deptName\":\"柳州市水利厅\",\"email\":\"ry@qq.com\",\"leader\":\"\",\"orderNum\":4,\"params\":{},\"parentId\":101,\"parentName\":\"广西省水利厅\",\"phone\":\"15888888888\",\"status\":\"0\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 21:46:13', 18);
INSERT INTO `sys_oper_log` VALUES (190, '部门管理', 2, 'com.ruoyi.web.controller.system.SysDeptController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/dept', '120.244.206.205', 'XX XX', '{\"ancestors\":\"0,100,101\",\"children\":[],\"deptId\":107,\"deptName\":\"玉林市水利局\",\"email\":\"ry@qq.com\",\"leader\":\"\",\"orderNum\":5,\"params\":{},\"parentId\":101,\"parentName\":\"广西省水利厅\",\"phone\":\"15888888888\",\"status\":\"0\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 21:46:39', 18);
INSERT INTO `sys_oper_log` VALUES (191, '部门管理', 2, 'com.ruoyi.web.controller.system.SysDeptController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/dept', '120.244.206.205', 'XX XX', '{\"ancestors\":\"0,100\",\"children\":[],\"deptId\":102,\"deptName\":\"北京市水利局\",\"email\":\"ry@qq.com\",\"leader\":\"\",\"orderNum\":2,\"params\":{},\"parentId\":100,\"parentName\":\"水利水电科学研究院\",\"phone\":\"15888888888\",\"status\":\"0\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 21:46:54', 20);
INSERT INTO `sys_oper_log` VALUES (192, '部门管理', 2, 'com.ruoyi.web.controller.system.SysDeptController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/dept', '120.244.206.205', 'XX XX', '{\"ancestors\":\"0,100,102\",\"children\":[],\"deptId\":108,\"deptName\":\"密云水利\",\"email\":\"ry@qq.com\",\"leader\":\"\",\"orderNum\":1,\"params\":{},\"parentId\":102,\"parentName\":\"北京市水利局\",\"phone\":\"15888888888\",\"status\":\"0\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 21:47:07', 20);
INSERT INTO `sys_oper_log` VALUES (193, '部门管理', 2, 'com.ruoyi.web.controller.system.SysDeptController.edit()', 'PUT', 1, 'admin', '若依科技', '/system/dept', '120.244.206.205', 'XX XX', '{\"ancestors\":\"0,100,102\",\"children\":[],\"deptId\":109,\"deptName\":\"通州水利\",\"email\":\"ry@qq.com\",\"leader\":\"\",\"orderNum\":2,\"params\":{},\"parentId\":102,\"parentName\":\"北京市水利局\",\"phone\":\"15888888888\",\"status\":\"0\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-08 21:47:17', 16);
INSERT INTO `sys_oper_log` VALUES (194, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '123.127.143.209', 'XX XX', '{\"auditOpinion\":\"通过\",\"auditStatus\":1,\"currentDept\":100,\"dataState\":1,\"id\":16,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":28,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"11号单元\",\"uptime\":\"2024-03-12 08:56:08\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-15 09:59:23', 19);
INSERT INTO `sys_oper_log` VALUES (195, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '123.127.143.209', 'XX XX', '{\"auditOpinion\":\"通过\",\"auditStatus\":1,\"currentDept\":100,\"dataState\":1,\"id\":16,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":28,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"11号单元\",\"uptime\":\"2024-03-12 08:56:08\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-15 09:59:34', 7);
INSERT INTO `sys_oper_log` VALUES (196, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/menu', '114.241.108.229', 'XX XX', '{\"children\":[],\"component\":\"system/dept/index\",\"createTime\":\"2024-03-05 09:07:04\",\"icon\":\"tree\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":103,\"menuName\":\"组织管理\",\"menuType\":\"C\",\"orderNum\":4,\"params\":{},\"parentId\":1,\"path\":\"dept\",\"perms\":\"system:dept:list\",\"query\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-16 15:20:47', 20);
INSERT INTO `sys_oper_log` VALUES (197, '用户管理', 2, 'com.ruoyi.web.controller.system.SysUserController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/user', '114.241.108.229', 'XX XX', '{\"admin\":false,\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":\"2024-03-05 09:07:02\",\"delFlag\":\"0\",\"dept\":{\"ancestors\":\"0,100,101\",\"children\":[],\"deptId\":105,\"deptName\":\"北海市水利厅\",\"leader\":\"\",\"orderNum\":3,\"params\":{},\"parentId\":101,\"status\":\"0\"},\"deptId\":105,\"email\":\"ry@qq.com\",\"loginDate\":\"2024-03-05 09:07:02\",\"loginIp\":\"127.0.0.1\",\"nickName\":\"北海\",\"params\":{},\"phonenumber\":\"15666666666\",\"postIds\":[2],\"remark\":\"测试员\",\"roleIds\":[2],\"roles\":[{\"admin\":false,\"dataScope\":\"4\",\"deptCheckStrictly\":false,\"flag\":false,\"menuCheckStrictly\":false,\"params\":{},\"roleId\":2,\"roleKey\":\"common\",\"roleName\":\"普通角色\",\"roleSort\":2,\"status\":\"0\"}],\"sex\":\"1\",\"status\":\"0\",\"updateBy\":\"admin\",\"userId\":2,\"userName\":\"ry\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-16 15:22:57', 53);
INSERT INTO `sys_oper_log` VALUES (198, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/menu', '114.241.108.229', 'XX XX', '{\"children\":[],\"component\":\"system/inspector/index\",\"createTime\":\"2024-03-06 16:30:17\",\"icon\":\"#\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2002,\"menuName\":\"巡查员管理\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":0,\"path\":\"inspector\",\"perms\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-16 15:45:28', 10);
INSERT INTO `sys_oper_log` VALUES (199, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '127.0.0.1', '内网IP', '{\"auditStatus\":1,\"currentDept\":100,\"dataState\":1,\"id\":22,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":33,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"12号单元\",\"uptime\":\"2024-03-15 11:31:20\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-18 10:32:20', 16);
INSERT INTO `sys_oper_log` VALUES (200, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '127.0.0.1', '内网IP', '{\"auditStatus\":1,\"currentDept\":100,\"dataState\":1,\"id\":22,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":33,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"12号单元\",\"uptime\":\"2024-03-15 11:31:20\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-18 10:32:58', 7);
INSERT INTO `sys_oper_log` VALUES (201, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '127.0.0.1', '内网IP', '{\"auditStatus\":1,\"currentDept\":100,\"dataState\":1,\"id\":22,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":33,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"12号单元\",\"uptime\":\"2024-03-15 11:31:20\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-18 10:33:53', 6);
INSERT INTO `sys_oper_log` VALUES (202, '用户管理', 1, 'com.ruoyi.web.controller.system.SysUserController.add()', 'POST', 1, 'admin', '水利水电科学研究院', '/system/user', '123.127.143.209', 'XX XX', '{\"admin\":false,\"createBy\":\"admin\",\"deptId\":108,\"email\":\"13691145454@qq.com\",\"nickName\":\"李刚\",\"params\":{},\"phonenumber\":\"13691145454\",\"postIds\":[1],\"roleIds\":[2],\"sex\":\"0\",\"status\":\"0\",\"userId\":101,\"userName\":\"miyun001 \"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-19 11:42:14', 134);
INSERT INTO `sys_oper_log` VALUES (203, '项目信息', 1, 'com.ruoyi.web.controller.termite.BProjectController.add()', 'POST', 1, 'admin', '水利水电科学研究院', '/system/project', '123.127.143.209', 'XX XX', '{\"address\":\"1212\",\"createTime\":\"2024-03-19 11:47:39\",\"delFlag\":\"0\",\"deptId\":108,\"id\":11,\"params\":{},\"person\":\"李亮\",\"personPhone\":\"13691145478\",\"projectName\":\"水库项目测试-\",\"projectType\":1,\"registrationNumber\":\"121212\",\"reservoirGrade\":12}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-19 11:47:39', 16);
INSERT INTO `sys_oper_log` VALUES (204, '巡查单元', 1, 'com.ruoyi.web.controller.termite.BPatrolUnitController.add()', 'POST', 1, 'admin', '水利水电科学研究院', '/bPatrol/unit', '123.127.143.209', 'XX XX', '{\"createTime\":\"2024-03-19 11:47:52\",\"delFlag\":\"0\",\"id\":15,\"params\":{},\"projectId\":11,\"unitName\":\"1-11\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-19 11:47:52', 15);
INSERT INTO `sys_oper_log` VALUES (205, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '123.127.143.209', 'XX XX', '{\"auditOpinion\":\"22\",\"auditStatus\":1,\"currentDept\":100,\"dataState\":1,\"id\":1,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":18,\"patrol_type\":\"2\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"12号单元\",\"uptime\":\"2024-03-08 09:20:03\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-19 14:20:19', 17);
INSERT INTO `sys_oper_log` VALUES (206, '巡查员用户', 1, 'com.ruoyi.web.controller.termite.BInspectorController.add()', 'POST', 1, 'admin', '水利水电科学研究院', '/system/inspector', '114.241.108.229', 'XX XX', '{\"delFlag\":\"0\",\"deptId\":100,\"id\":4,\"inspectorName\":\"孙黎明\",\"params\":{},\"phone\":\"13001011630\",\"userId\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-19 14:37:16', 16);
INSERT INTO `sys_oper_log` VALUES (207, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '123.127.143.209', 'XX XX', '{\"auditOpinion\":\"啊撒撒撒\",\"auditStatus\":1,\"currentDept\":100,\"dataState\":1,\"id\":18,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":30,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"11号单元\",\"uptime\":\"2024-03-15 10:48:58\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-19 14:57:49', 9);
INSERT INTO `sys_oper_log` VALUES (208, '岗位管理', 1, 'com.ruoyi.web.controller.system.SysPostController.add()', 'POST', 1, 'admin', '水利水电科学研究院', '/system/post', '120.244.226.157', 'XX XX', '{\"createBy\":\"admin\",\"flag\":false,\"params\":{},\"postCode\":\"manager\",\"postId\":5,\"postName\":\"工程师\",\"postSort\":5,\"status\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-23 17:16:42', 14);
INSERT INTO `sys_oper_log` VALUES (209, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '114.241.108.229', 'XX XX', '{\"auditOpinion\":\"vdxf\",\"auditStatus\":2,\"currentDept\":100,\"dataState\":2,\"id\":48,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":59,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"12号单元\",\"uptime\":\"2024-03-25 13:39:38\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-25 13:42:16', 14);
INSERT INTO `sys_oper_log` VALUES (210, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '114.241.108.229', 'XX XX', '{\"auditOpinion\":\"iuyiyu\",\"auditStatus\":2,\"currentDept\":100,\"dataState\":2,\"id\":61,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":71,\"patrol_type\":\"2\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"12号单元\",\"uptime\":\"2024-03-25 15:03:06\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-25 15:03:42', 14);
INSERT INTO `sys_oper_log` VALUES (211, '巡查员用户', 1, 'com.ruoyi.web.controller.termite.BInspectorController.add()', 'POST', 1, 'admin', '水利水电科学研究院', '/system/inspector', '123.127.143.209', 'XX XX', '{\"delFlag\":\"0\",\"deptId\":100,\"id\":5,\"inspectorName\":\"宁智华\",\"params\":{},\"phone\":\"13691386792\",\"userId\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 15:00:02', 23);
INSERT INTO `sys_oper_log` VALUES (212, '项目信息', 1, 'com.ruoyi.web.controller.termite.BProjectController.add()', 'POST', 1, 'admin', '水利水电科学研究院', '/system/project', '123.127.143.209', 'XX XX', '{\"address\":\"北京\",\"createTime\":\"2024-03-26 15:04:32\",\"delFlag\":\"0\",\"deptId\":102,\"id\":12,\"params\":{},\"person\":\"李亮\",\"personPhone\":\"13691386792\",\"projectName\":\"斋堂水库\",\"projectType\":1,\"registrationNumber\":\"111111\",\"reservoirGrade\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 15:04:32', 18);
INSERT INTO `sys_oper_log` VALUES (213, '巡查单元', 1, 'com.ruoyi.web.controller.termite.BPatrolUnitController.add()', 'POST', 1, 'admin', '水利水电科学研究院', '/bPatrol/unit', '123.127.143.209', 'XX XX', '{\"createTime\":\"2024-03-26 15:04:55\",\"delFlag\":\"0\",\"id\":16,\"params\":{},\"projectId\":12,\"unitName\":\"206\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 15:04:55', 9);
INSERT INTO `sys_oper_log` VALUES (214, '巡查员用户', 3, 'com.ruoyi.web.controller.termite.BInspectorController.remove()', 'DELETE', 1, 'admin', '水利水电科学研究院', '/system/inspector/5', '123.127.143.209', 'XX XX', '{}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 15:09:51', 8);
INSERT INTO `sys_oper_log` VALUES (215, '巡查员用户', 1, 'com.ruoyi.web.controller.termite.BInspectorController.add()', 'POST', 1, 'admin', '水利水电科学研究院', '/system/inspector', '123.127.143.209', 'XX XX', '{\"delFlag\":\"0\",\"deptId\":100,\"id\":6,\"inspectorName\":\"李亮\",\"params\":{},\"phone\":\"13691386792\",\"userId\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 15:10:24', 7);
INSERT INTO `sys_oper_log` VALUES (216, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/menu', '114.241.108.229', 'XX XX', '{\"children\":[],\"component\":\"system/project/index\",\"createTime\":\"2024-03-05 18:00:17\",\"icon\":\"#\",\"isCache\":\"1\",\"isFrame\":\"1\",\"menuId\":2000,\"menuName\":\"项目管理\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":0,\"path\":\"project\",\"perms\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 15:21:58', 19);
INSERT INTO `sys_oper_log` VALUES (217, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/menu', '114.241.108.229', 'XX XX', '{\"children\":[],\"component\":\"system/inspector/index\",\"createTime\":\"2024-03-06 16:30:17\",\"icon\":\"#\",\"isCache\":\"1\",\"isFrame\":\"1\",\"menuId\":2002,\"menuName\":\"巡查员管理\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":0,\"path\":\"inspector\",\"perms\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 15:22:01', 11);
INSERT INTO `sys_oper_log` VALUES (218, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/menu', '114.241.108.229', 'XX XX', '{\"children\":[],\"component\":\"system/audit/index\",\"createTime\":\"2024-03-07 16:33:04\",\"icon\":\"#\",\"isCache\":\"1\",\"isFrame\":\"1\",\"menuId\":2003,\"menuName\":\"巡查审批\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":0,\"path\":\"audit\",\"perms\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 15:22:04', 11);
INSERT INTO `sys_oper_log` VALUES (219, '巡查员用户', 2, 'com.ruoyi.web.controller.termite.BInspectorController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/inspector', '123.127.143.209', 'XX XX', '{\"delFlag\":\"0\",\"deptId\":100,\"id\":6,\"inspectorName\":\"李亮\",\"params\":{},\"phone\":\"13691386792\",\"userId\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 15:23:45', 4);
INSERT INTO `sys_oper_log` VALUES (220, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/menu', '114.241.108.229', 'XX XX', '{\"children\":[],\"component\":\"system/inspector/index\",\"createTime\":\"2024-03-06 16:30:17\",\"icon\":\"peoples\",\"isCache\":\"1\",\"isFrame\":\"1\",\"menuId\":2002,\"menuName\":\"巡查员管理\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":0,\"path\":\"inspector\",\"perms\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 15:24:07', 10);
INSERT INTO `sys_oper_log` VALUES (221, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/menu', '114.241.108.229', 'XX XX', '{\"children\":[],\"component\":\"system/audit/index\",\"createTime\":\"2024-03-07 16:33:04\",\"icon\":\"peoples\",\"isCache\":\"1\",\"isFrame\":\"1\",\"menuId\":2003,\"menuName\":\"巡查审批\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":0,\"path\":\"audit\",\"perms\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 15:24:31', 11);
INSERT INTO `sys_oper_log` VALUES (222, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/menu', '114.241.108.229', 'XX XX', '{\"children\":[],\"component\":\"system/project/index\",\"createTime\":\"2024-03-05 18:00:17\",\"icon\":\"peoples\",\"isCache\":\"1\",\"isFrame\":\"1\",\"menuId\":2000,\"menuName\":\"项目管理\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":0,\"path\":\"project\",\"perms\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 15:24:38', 11);
INSERT INTO `sys_oper_log` VALUES (223, '菜单管理', 3, 'com.ruoyi.web.controller.system.SysMenuController.remove()', 'DELETE', 1, 'admin', '水利水电科学研究院', '/system/menu/4', '114.241.108.229', 'XX XX', '{}', '{\"msg\":\"菜单已分配,不允许删除\",\"code\":601}', 0, NULL, '2024-03-26 15:24:53', 8);
INSERT INTO `sys_oper_log` VALUES (224, '用户管理', 1, 'com.ruoyi.web.controller.system.SysUserController.add()', 'POST', 1, 'admin', '水利水电科学研究院', '/system/user', '123.127.143.209', 'XX XX', '{\"admin\":false,\"createBy\":\"admin\",\"deptId\":102,\"nickName\":\"test\",\"params\":{},\"postIds\":[],\"roleIds\":[],\"status\":\"0\",\"userId\":102,\"userName\":\"13001011630\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 15:36:47', 106);
INSERT INTO `sys_oper_log` VALUES (225, '用户管理', 2, 'com.ruoyi.web.controller.system.SysUserController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/user', '114.241.108.229', 'XX XX', '{\"admin\":false,\"avatar\":\"\",\"createBy\":\"admin\",\"createTime\":\"2024-03-26 15:36:47\",\"delFlag\":\"0\",\"dept\":{\"ancestors\":\"0,100\",\"children\":[],\"deptId\":102,\"deptName\":\"北京市水利局\",\"leader\":\"\",\"orderNum\":2,\"params\":{},\"parentId\":100,\"status\":\"0\"},\"deptId\":102,\"email\":\"\",\"loginDate\":\"2024-03-26 15:37:01\",\"loginIp\":\"123.127.143.209\",\"nickName\":\"test\",\"params\":{},\"phonenumber\":\"\",\"postIds\":[],\"roleIds\":[2],\"roles\":[],\"sex\":\"0\",\"status\":\"0\",\"updateBy\":\"admin\",\"userId\":102,\"userName\":\"13001011630\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 15:38:07', 19);
INSERT INTO `sys_oper_log` VALUES (226, '角色管理', 2, 'com.ruoyi.web.controller.system.SysRoleController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/role', '114.241.108.229', 'XX XX', '{\"admin\":false,\"createTime\":\"2024-03-05 09:07:03\",\"dataScope\":\"4\",\"delFlag\":\"0\",\"deptCheckStrictly\":true,\"flag\":false,\"menuCheckStrictly\":true,\"menuIds\":[1,2000,2002,2003,100,1000,1001,1002,1003,1004,1005,1006,101,1007,1008,1009,1010,1011,102,1012,1013,1014,1015,103,1016,1017,1018,1019,104,1020,1021,1022,1023,1024,105,1025,1026,1027,1028,1029,106,1030,1031,1032,1033,1034,107,1035,1036,1037,1038,108,500,1039,1040,1041,501,1042,1043,1044,1045,2,109,1046,1047,1048,110,1049,1050,1051,1052,1053,1054,111,112,113,114,3,115,116,1055,1056,1057,1058,1059,1060,117,4],\"params\":{},\"remark\":\"普通角色\",\"roleId\":2,\"roleKey\":\"common\",\"roleName\":\"普通角色\",\"roleSort\":2,\"status\":\"0\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 15:38:50', 29);
INSERT INTO `sys_oper_log` VALUES (227, '角色管理', 2, 'com.ruoyi.web.controller.system.SysRoleController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/role', '114.241.108.229', 'XX XX', '{\"admin\":false,\"createTime\":\"2024-03-05 09:07:03\",\"dataScope\":\"4\",\"delFlag\":\"0\",\"deptCheckStrictly\":true,\"flag\":false,\"menuCheckStrictly\":true,\"menuIds\":[2000,2002,2003,1,100,1000,1001,1002,1003,1004,1005,1006,101,1007,1008,1009,1010,1011,2006,2005,2001,102,1012,1013,1014,1015,103,1016,1017,1018,1019,104,1020,1021,1022,1023,1024,105,1025,1026,1027,1028,1029,106,1030,1031,1032,1033,1034,107,1035,1036,1037,1038,108,500,1039,1040,1041,501,1042,1043,1044,1045,2,109,1046,1047,1048,110,1049,1050,1051,1052,1053,1054,111,112,113,114,3,115,116,1055,1056,1057,1058,1059,1060,117,4],\"params\":{},\"remark\":\"普通角色\",\"roleId\":2,\"roleKey\":\"common\",\"roleName\":\"普通角色\",\"roleSort\":2,\"status\":\"0\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 15:40:10', 26);
INSERT INTO `sys_oper_log` VALUES (228, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, '13001011630', '北京市水利局', '/system/audit', '123.127.143.209', 'XX XX', '{\"auditOpinion\":\"1\",\"auditStatus\":1,\"currentDept\":102,\"dataState\":3,\"id\":73,\"inspector_name\":\"李亮\",\"params\":{},\"patrolId\":82,\"patrol_type\":\"1\",\"phone\":\"13691386792\",\"project_name\":\"斋堂水库\",\"subordinateDept\":100,\"unit_name\":\"206\",\"uptime\":\"2024-03-26 15:28:09\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 15:40:11', 18);
INSERT INTO `sys_oper_log` VALUES (229, '菜单管理', 2, 'com.ruoyi.web.controller.system.SysMenuController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"component\":\"system/project/index\",\"createTime\":\"2024-03-05 18:00:17\",\"icon\":\"peoples\",\"isCache\":\"1\",\"isFrame\":\"1\",\"menuId\":2000,\"menuName\":\"项目管理\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":0,\"path\":\"project\",\"perms\":\"system:project:list\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 15:43:51', 9);
INSERT INTO `sys_oper_log` VALUES (230, '菜单管理', 1, 'com.ruoyi.web.controller.system.SysMenuController.add()', 'POST', 1, 'admin', '水利水电科学研究院', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"createBy\":\"admin\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"system:unit:list\",\"menuType\":\"F\",\"orderNum\":1,\"params\":{},\"parentId\":2002,\"perms\":\"system:unit:list\",\"status\":\"0\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 15:47:03', 13);
INSERT INTO `sys_oper_log` VALUES (231, '菜单管理', 1, 'com.ruoyi.web.controller.system.SysMenuController.add()', 'POST', 1, 'admin', '水利水电科学研究院', '/system/menu', '127.0.0.1', '内网IP', '{\"children\":[],\"createBy\":\"admin\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"system:inspector:list\",\"menuType\":\"F\",\"orderNum\":2,\"params\":{},\"parentId\":2002,\"perms\":\"system:inspector:list\",\"status\":\"0\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 15:47:16', 9);
INSERT INTO `sys_oper_log` VALUES (232, '角色管理', 2, 'com.ruoyi.web.controller.system.SysRoleController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/role', '127.0.0.1', '内网IP', '{\"admin\":false,\"createTime\":\"2024-03-05 09:07:03\",\"dataScope\":\"4\",\"delFlag\":\"0\",\"deptCheckStrictly\":true,\"flag\":false,\"menuCheckStrictly\":true,\"menuIds\":[2000,2002,2007,2008,2003,1,100,1000,1001,1002,1003,1004,1005,1006,101,1007,1008,1009,1010,1011,2001,2006,2005,102,1012,1013,1014,1015,103,1016,1017,1018,1019,104,1020,1021,1022,1023,1024,105,1025,1026,1027,1028,1029,106,1030,1031,1032,1033,1034,107,1035,1036,1037,1038,108,500,1039,1040,1041,501,1042,1043,1044,1045,2,109,1046,1047,1048,110,1049,1050,1051,1052,1053,1054,111,112,113,114,3,115,116,1055,1056,1057,1058,1059,1060,117,4],\"params\":{},\"remark\":\"普通角色\",\"roleId\":2,\"roleKey\":\"common\",\"roleName\":\"普通角色\",\"roleSort\":2,\"status\":\"0\",\"updateBy\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 15:47:25', 24);
INSERT INTO `sys_oper_log` VALUES (233, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '127.0.0.1', '内网IP', '{\"auditStatus\":1,\"currentDept\":100,\"dataState\":1,\"id\":7,\"params\":{},\"subordinateDept\":0}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 15:50:46', 10);
INSERT INTO `sys_oper_log` VALUES (234, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, '13001011630', '北京市水利局', '/system/audit', '123.127.143.209', 'XX XX', '{\"auditOpinion\":\"2\",\"auditStatus\":1,\"currentDept\":102,\"dataState\":3,\"id\":75,\"inspector_name\":\"李亮\",\"params\":{},\"patrolId\":84,\"patrol_type\":\"1\",\"phone\":\"13691386792\",\"project_name\":\"斋堂水库\",\"subordinateDept\":100,\"unit_name\":\"206\",\"uptime\":\"2024-03-26 15:55:18\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 16:23:07', 14);
INSERT INTO `sys_oper_log` VALUES (235, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, '13001011630', '北京市水利局', '/system/audit', '123.127.143.209', 'XX XX', '{\"auditOpinion\":\"3\",\"auditStatus\":2,\"currentDept\":102,\"dataState\":2,\"id\":74,\"inspector_name\":\"李亮\",\"params\":{},\"patrolId\":83,\"patrol_type\":\"2\",\"phone\":\"13691386792\",\"project_name\":\"斋堂水库\",\"subordinateDept\":100,\"unit_name\":\"206\",\"uptime\":\"2024-03-26 15:32:04\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 16:23:27', 11);
INSERT INTO `sys_oper_log` VALUES (236, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '114.241.108.229', 'XX XX', '{\"auditStatus\":1,\"currentDept\":100,\"dataState\":1,\"id\":72,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":81,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"12号单元\",\"uptime\":\"2024-03-26 08:47:17\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 17:47:03', 8);
INSERT INTO `sys_oper_log` VALUES (237, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '114.241.108.229', 'XX XX', '{\"auditStatus\":2,\"currentDept\":100,\"dataState\":2,\"id\":72,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":81,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"12号单元\",\"uptime\":\"2024-03-26 08:47:17\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 17:48:07', 14);
INSERT INTO `sys_oper_log` VALUES (238, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '114.241.108.229', 'XX XX', '{\"auditStatus\":1,\"currentDept\":100,\"dataState\":1,\"id\":71,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":80,\"patrol_type\":\"2\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"12号单元\",\"uptime\":\"2024-03-25 17:53:23\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 17:48:49', 8);
INSERT INTO `sys_oper_log` VALUES (239, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '114.241.108.229', 'XX XX', '{\"auditOpinion\":\"啊撒撒撒\",\"auditStatus\":1,\"currentDept\":100,\"dataState\":1,\"id\":18,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":30,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"11号单元\",\"uptime\":\"2024-03-15 10:48:58\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-26 17:53:54', 7);
INSERT INTO `sys_oper_log` VALUES (240, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '114.241.108.229', 'XX XX', '{\"auditOpinion\":\"uuuuuu\",\"auditStatus\":1,\"currentDept\":100,\"dataState\":1,\"id\":52,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":62,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"12号单元\",\"uptime\":\"2024-03-25 14:00:34\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 08:42:41', 9);
INSERT INTO `sys_oper_log` VALUES (241, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '114.241.108.229', 'XX XX', '{\"auditOpinion\":\"v\",\"auditStatus\":2,\"currentDept\":100,\"dataState\":2,\"id\":51,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":61,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"11号单元\",\"uptime\":\"2024-03-25 13:58:39\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 08:44:23', 12);
INSERT INTO `sys_oper_log` VALUES (242, '用户管理', 2, 'com.ruoyi.web.controller.system.SysUserController.resetPwd()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/user/resetPwd', '114.241.108.229', 'XX XX', '{\"admin\":false,\"params\":{},\"updateBy\":\"admin\",\"userId\":100}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 08:45:41', 91);
INSERT INTO `sys_oper_log` VALUES (243, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'ceshi', '广西省水利厅', '/system/audit', '114.241.108.229', 'XX XX', '{\"auditStatus\":1,\"currentDept\":101,\"dataState\":1,\"id\":4,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":21,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"11号单元\",\"uptime\":\"2024-03-08 09:20:03\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 08:46:54', 9);
INSERT INTO `sys_oper_log` VALUES (244, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'ceshi', '广西省水利厅', '/system/audit', '114.241.108.229', 'XX XX', '{\"auditStatus\":1,\"currentDept\":101,\"dataState\":1,\"id\":4,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":21,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"11号单元\",\"uptime\":\"2024-03-08 09:20:03\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 08:48:43', 6);
INSERT INTO `sys_oper_log` VALUES (245, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '192.168.1.118', '内网IP', '{\"auditStatus\":2,\"currentDept\":100,\"dataState\":2,\"id\":71,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":80,\"patrol_type\":\"2\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"12号单元\",\"uptime\":\"2024-03-25 17:53:23\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 10:22:05', 12);
INSERT INTO `sys_oper_log` VALUES (246, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '192.168.1.118', '内网IP', '{\"auditStatus\":2,\"currentDept\":100,\"dataState\":2,\"id\":78,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":80,\"patrol_type\":\"2\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"12号单元\",\"uptime\":\"2024-03-27 10:22:05\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 10:45:23', 12);
INSERT INTO `sys_oper_log` VALUES (247, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '192.168.1.118', '内网IP', '{\"auditStatus\":2,\"currentDept\":100,\"dataState\":2,\"id\":79,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":80,\"patrol_type\":\"2\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"12号单元\",\"uptime\":\"2024-03-27 10:45:23\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 10:53:54', 13);
INSERT INTO `sys_oper_log` VALUES (248, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '192.168.1.118', '内网IP', '{\"auditStatus\":2,\"currentDept\":100,\"dataState\":2,\"id\":70,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":79,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"12号单元\",\"uptime\":\"2024-03-25 17:52:27\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 11:08:22', 13);
INSERT INTO `sys_oper_log` VALUES (249, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '192.168.1.118', '内网IP', '{\"auditStatus\":2,\"currentDept\":100,\"dataState\":2,\"id\":81,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":79,\"patrol_type\":\"2\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"12号单元\",\"uptime\":\"2024-03-27 11:08:22\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 11:12:01', 12);
INSERT INTO `sys_oper_log` VALUES (250, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '192.168.1.118', '内网IP', '{\"auditStatus\":2,\"currentDept\":100,\"dataState\":2,\"id\":82,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":79,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"12号单元\",\"uptime\":\"2024-03-27 11:12:01\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 11:18:34', 12);
INSERT INTO `sys_oper_log` VALUES (251, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '192.168.1.118', '内网IP', '{\"auditStatus\":2,\"currentDept\":100,\"dataState\":2,\"id\":83,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":79,\"patrol_type\":\"2\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"12号单元\",\"uptime\":\"2024-03-27 11:18:34\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 11:20:00', 13);
INSERT INTO `sys_oper_log` VALUES (252, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '192.168.1.118', '内网IP', '{\"auditStatus\":2,\"currentDept\":100,\"dataState\":2,\"id\":85,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":86,\"patrol_type\":\"2\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"12号单元\",\"uptime\":\"2024-03-27 11:32:02\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 11:33:58', 14);
INSERT INTO `sys_oper_log` VALUES (253, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '192.168.1.118', '内网IP', '{\"auditStatus\":2,\"currentDept\":100,\"dataState\":2,\"id\":86,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":87,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"12号单元\",\"uptime\":\"2024-03-27 11:33:30\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 11:56:48', 14);
INSERT INTO `sys_oper_log` VALUES (254, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '192.168.1.118', '内网IP', '{\"auditStatus\":2,\"currentDept\":100,\"dataState\":2,\"id\":89,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":88,\"patrol_type\":\"2\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"12号单元\",\"uptime\":\"2024-03-27 11:58:10\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 11:58:53', 11);
INSERT INTO `sys_oper_log` VALUES (255, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'admin', '水利水电科学研究院', '/system/audit', '192.168.1.118', '内网IP', '{\"auditOpinion\":\"11111\",\"auditStatus\":1,\"currentDept\":100,\"dataState\":1,\"id\":90,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":88,\"patrol_type\":\"2\",\"phone\":\"13521962466\",\"project_name\":\"水库\",\"subordinateDept\":0,\"unit_name\":\"12号单元\",\"uptime\":\"2024-03-27 11:58:53\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 12:00:05', 8);
INSERT INTO `sys_oper_log` VALUES (256, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'ceshi', '广西省水利厅', '/system/audit', '192.168.1.118', '内网IP', '{\"auditStatus\":2,\"currentDept\":101,\"dataState\":2,\"id\":43,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":54,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"堤坝\",\"subordinateDept\":100,\"unit_name\":\"3号单元\",\"uptime\":\"2024-03-25 10:41:55\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 13:37:48', 12);
INSERT INTO `sys_oper_log` VALUES (257, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'ceshi', '广西省水利厅', '/system/audit', '192.168.1.118', '内网IP', '{\"auditOpinion\":\"55555\",\"auditStatus\":1,\"currentDept\":101,\"dataState\":3,\"id\":37,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":48,\"patrol_type\":\"2\",\"phone\":\"13521962466\",\"project_name\":\"堤坝\",\"subordinateDept\":100,\"unit_name\":\"3号单元\",\"uptime\":\"2024-03-21 16:45:50\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 13:38:23', 12);
INSERT INTO `sys_oper_log` VALUES (258, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'ceshi', '广西省水利厅', '/system/audit', '192.168.1.118', '内网IP', '{\"auditOpinion\":\"11111\",\"auditStatus\":2,\"currentDept\":101,\"dataState\":2,\"id\":1,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":1,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"堤坝\",\"subordinateDept\":100,\"unit_name\":\"3号单元\",\"uptime\":\"2024-03-27 14:06:39\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 14:11:34', 137);
INSERT INTO `sys_oper_log` VALUES (259, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'ceshi', '广西省水利厅', '/system/audit', '192.168.1.118', '内网IP', '{\"auditOpinion\":\"111\",\"auditStatus\":2,\"currentDept\":101,\"dataState\":2,\"id\":5,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":1,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"堤坝\",\"subordinateDept\":100,\"unit_name\":\"3号单元\",\"uptime\":\"2024-03-27 14:11:33\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 14:12:54', 194);
INSERT INTO `sys_oper_log` VALUES (260, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'ceshi', '广西省水利厅', '/system/audit', '192.168.1.118', '内网IP', '{\"auditOpinion\":\"444\",\"auditStatus\":2,\"currentDept\":101,\"dataState\":2,\"id\":4,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":4,\"patrol_type\":\"2\",\"phone\":\"13521962466\",\"project_name\":\"堤坝\",\"subordinateDept\":100,\"unit_name\":\"3号单元\",\"uptime\":\"2024-03-27 14:07:45\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 14:13:25', 234);
INSERT INTO `sys_oper_log` VALUES (261, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'ceshi', '广西省水利厅', '/system/audit', '192.168.1.118', '内网IP', '{\"auditOpinion\":\"22222\",\"auditStatus\":1,\"currentDept\":101,\"dataState\":3,\"id\":2,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":2,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"堤坝\",\"subordinateDept\":100,\"unit_name\":\"3号单元\",\"uptime\":\"2024-03-27 14:07:01\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 14:13:56', 131);
INSERT INTO `sys_oper_log` VALUES (262, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'ceshi', '广西省水利厅', '/system/audit', '192.168.1.118', '内网IP', '{\"auditOpinion\":\"11111\",\"auditStatus\":1,\"currentDept\":101,\"dataState\":3,\"id\":6,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":1,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"堤坝\",\"subordinateDept\":100,\"unit_name\":\"3号单元\",\"uptime\":\"2024-03-27 14:12:53\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 14:15:59', 180);
INSERT INTO `sys_oper_log` VALUES (263, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'ceshi', '广西省水利厅', '/system/audit', '192.168.1.118', '内网IP', '{\"auditOpinion\":\"33333\",\"auditStatus\":2,\"currentDept\":101,\"dataState\":2,\"id\":3,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":3,\"patrol_type\":\"2\",\"phone\":\"13521962466\",\"project_name\":\"堤坝\",\"subordinateDept\":100,\"unit_name\":\"3号单元\",\"uptime\":\"2024-03-27 14:07:28\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 14:16:46', 124);
INSERT INTO `sys_oper_log` VALUES (264, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'ceshi', '广西省水利厅', '/system/audit', '192.168.1.118', '内网IP', '{\"auditOpinion\":\"5555\",\"auditStatus\":2,\"currentDept\":101,\"dataState\":2,\"id\":8,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":5,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"堤坝\",\"subordinateDept\":100,\"unit_name\":\"3号单元\",\"uptime\":\"2024-03-27 14:18:36\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 14:19:32', 117);
INSERT INTO `sys_oper_log` VALUES (265, '项目信息', 1, 'com.ruoyi.web.controller.termite.BProjectController.add()', 'POST', 1, 'admin', '水利水电科学研究院', '/system/project', '192.168.1.118', '内网IP', '{\"address\":\"广西\",\"createTime\":\"2024-03-27 14:30:54\",\"delFlag\":\"0\",\"deptId\":101,\"id\":13,\"params\":{},\"person\":\"a\",\"personPhone\":\"13521962466\",\"projectName\":\"T-1\",\"projectType\":1,\"registrationNumber\":\"123655\",\"reservoirGrade\":2}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 14:30:55', 103);
INSERT INTO `sys_oper_log` VALUES (266, '巡查单元', 1, 'com.ruoyi.web.controller.termite.BPatrolUnitController.add()', 'POST', 1, 'admin', '水利水电科学研究院', '/bPatrol/unit', '192.168.1.118', '内网IP', '{\"createTime\":\"2024-03-27 14:32:21\",\"delFlag\":\"0\",\"id\":17,\"params\":{},\"projectId\":13,\"unitName\":\"单元1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 14:32:22', 140);
INSERT INTO `sys_oper_log` VALUES (267, '巡查单元', 1, 'com.ruoyi.web.controller.termite.BPatrolUnitController.add()', 'POST', 1, 'admin', '水利水电科学研究院', '/bPatrol/unit', '192.168.1.118', '内网IP', '{\"createTime\":\"2024-03-27 14:32:29\",\"id\":18,\"params\":{},\"projectId\":13,\"unitName\":\"单元2\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 14:32:30', 59);
INSERT INTO `sys_oper_log` VALUES (268, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'ceshi', '广西省水利厅', '/system/audit', '192.168.1.118', '内网IP', '{\"auditStatus\":2,\"currentDept\":101,\"dataState\":2,\"id\":10,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":6,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"T-1\",\"subordinateDept\":100,\"unit_name\":\"单元2\",\"uptime\":\"2024-03-27 14:33:48\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 14:34:29', 122);
INSERT INTO `sys_oper_log` VALUES (269, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'ceshi', '广西省水利厅', '/system/audit', '192.168.1.118', '内网IP', '{\"auditStatus\":1,\"currentDept\":101,\"dataState\":3,\"id\":11,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":6,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"T-1\",\"subordinateDept\":100,\"unit_name\":\"单元2\",\"uptime\":\"2024-03-27 14:34:28\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 14:35:02', 155);
INSERT INTO `sys_oper_log` VALUES (270, '审核记录', 2, 'com.ruoyi.web.controller.termite.BAuditController.edit()', 'PUT', 1, 'ceshi', '广西省水利厅', '/system/audit', '192.168.1.118', '内网IP', '{\"auditStatus\":2,\"currentDept\":101,\"dataState\":2,\"id\":12,\"inspector_name\":\"a\",\"params\":{},\"patrolId\":7,\"patrol_type\":\"1\",\"phone\":\"13521962466\",\"project_name\":\"T-1\",\"subordinateDept\":100,\"unit_name\":\"单元2\",\"uptime\":\"2024-03-27 14:35:22\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2024-03-27 14:35:47', 190);

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '岗位名称',
  `post_sort` int(11) NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '岗位信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'ceo', '董事长', 1, '0', 'admin', '2024-03-05 09:07:02', '', NULL, '');
INSERT INTO `sys_post` VALUES (2, 'se', '项目经理', 2, '0', 'admin', '2024-03-05 09:07:02', '', NULL, '');
INSERT INTO `sys_post` VALUES (3, 'hr', '人力资源', 3, '0', 'admin', '2024-03-05 09:07:02', '', NULL, '');
INSERT INTO `sys_post` VALUES (4, 'user', '普通员工', 4, '0', 'admin', '2024-03-05 09:07:02', '', NULL, '');
INSERT INTO `sys_post` VALUES (5, 'manager', '工程师', 5, '0', 'admin', '2024-03-23 17:16:42', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(11) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2024-03-05 09:07:03', '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '4', 1, 1, '0', '0', 'admin', '2024-03-05 09:07:03', 'admin', '2024-03-26 15:47:25', '普通角色');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '角色和部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (2, 3);
INSERT INTO `sys_role_menu` VALUES (2, 4);
INSERT INTO `sys_role_menu` VALUES (2, 100);
INSERT INTO `sys_role_menu` VALUES (2, 101);
INSERT INTO `sys_role_menu` VALUES (2, 102);
INSERT INTO `sys_role_menu` VALUES (2, 103);
INSERT INTO `sys_role_menu` VALUES (2, 104);
INSERT INTO `sys_role_menu` VALUES (2, 105);
INSERT INTO `sys_role_menu` VALUES (2, 106);
INSERT INTO `sys_role_menu` VALUES (2, 107);
INSERT INTO `sys_role_menu` VALUES (2, 108);
INSERT INTO `sys_role_menu` VALUES (2, 109);
INSERT INTO `sys_role_menu` VALUES (2, 110);
INSERT INTO `sys_role_menu` VALUES (2, 111);
INSERT INTO `sys_role_menu` VALUES (2, 112);
INSERT INTO `sys_role_menu` VALUES (2, 113);
INSERT INTO `sys_role_menu` VALUES (2, 114);
INSERT INTO `sys_role_menu` VALUES (2, 115);
INSERT INTO `sys_role_menu` VALUES (2, 116);
INSERT INTO `sys_role_menu` VALUES (2, 117);
INSERT INTO `sys_role_menu` VALUES (2, 500);
INSERT INTO `sys_role_menu` VALUES (2, 501);
INSERT INTO `sys_role_menu` VALUES (2, 1000);
INSERT INTO `sys_role_menu` VALUES (2, 1001);
INSERT INTO `sys_role_menu` VALUES (2, 1002);
INSERT INTO `sys_role_menu` VALUES (2, 1003);
INSERT INTO `sys_role_menu` VALUES (2, 1004);
INSERT INTO `sys_role_menu` VALUES (2, 1005);
INSERT INTO `sys_role_menu` VALUES (2, 1006);
INSERT INTO `sys_role_menu` VALUES (2, 1007);
INSERT INTO `sys_role_menu` VALUES (2, 1008);
INSERT INTO `sys_role_menu` VALUES (2, 1009);
INSERT INTO `sys_role_menu` VALUES (2, 1010);
INSERT INTO `sys_role_menu` VALUES (2, 1011);
INSERT INTO `sys_role_menu` VALUES (2, 1012);
INSERT INTO `sys_role_menu` VALUES (2, 1013);
INSERT INTO `sys_role_menu` VALUES (2, 1014);
INSERT INTO `sys_role_menu` VALUES (2, 1015);
INSERT INTO `sys_role_menu` VALUES (2, 1016);
INSERT INTO `sys_role_menu` VALUES (2, 1017);
INSERT INTO `sys_role_menu` VALUES (2, 1018);
INSERT INTO `sys_role_menu` VALUES (2, 1019);
INSERT INTO `sys_role_menu` VALUES (2, 1020);
INSERT INTO `sys_role_menu` VALUES (2, 1021);
INSERT INTO `sys_role_menu` VALUES (2, 1022);
INSERT INTO `sys_role_menu` VALUES (2, 1023);
INSERT INTO `sys_role_menu` VALUES (2, 1024);
INSERT INTO `sys_role_menu` VALUES (2, 1025);
INSERT INTO `sys_role_menu` VALUES (2, 1026);
INSERT INTO `sys_role_menu` VALUES (2, 1027);
INSERT INTO `sys_role_menu` VALUES (2, 1028);
INSERT INTO `sys_role_menu` VALUES (2, 1029);
INSERT INTO `sys_role_menu` VALUES (2, 1030);
INSERT INTO `sys_role_menu` VALUES (2, 1031);
INSERT INTO `sys_role_menu` VALUES (2, 1032);
INSERT INTO `sys_role_menu` VALUES (2, 1033);
INSERT INTO `sys_role_menu` VALUES (2, 1034);
INSERT INTO `sys_role_menu` VALUES (2, 1035);
INSERT INTO `sys_role_menu` VALUES (2, 1036);
INSERT INTO `sys_role_menu` VALUES (2, 1037);
INSERT INTO `sys_role_menu` VALUES (2, 1038);
INSERT INTO `sys_role_menu` VALUES (2, 1039);
INSERT INTO `sys_role_menu` VALUES (2, 1040);
INSERT INTO `sys_role_menu` VALUES (2, 1041);
INSERT INTO `sys_role_menu` VALUES (2, 1042);
INSERT INTO `sys_role_menu` VALUES (2, 1043);
INSERT INTO `sys_role_menu` VALUES (2, 1044);
INSERT INTO `sys_role_menu` VALUES (2, 1045);
INSERT INTO `sys_role_menu` VALUES (2, 1046);
INSERT INTO `sys_role_menu` VALUES (2, 1047);
INSERT INTO `sys_role_menu` VALUES (2, 1048);
INSERT INTO `sys_role_menu` VALUES (2, 1049);
INSERT INTO `sys_role_menu` VALUES (2, 1050);
INSERT INTO `sys_role_menu` VALUES (2, 1051);
INSERT INTO `sys_role_menu` VALUES (2, 1052);
INSERT INTO `sys_role_menu` VALUES (2, 1053);
INSERT INTO `sys_role_menu` VALUES (2, 1054);
INSERT INTO `sys_role_menu` VALUES (2, 1055);
INSERT INTO `sys_role_menu` VALUES (2, 1056);
INSERT INTO `sys_role_menu` VALUES (2, 1057);
INSERT INTO `sys_role_menu` VALUES (2, 1058);
INSERT INTO `sys_role_menu` VALUES (2, 1059);
INSERT INTO `sys_role_menu` VALUES (2, 1060);
INSERT INTO `sys_role_menu` VALUES (2, 2000);
INSERT INTO `sys_role_menu` VALUES (2, 2001);
INSERT INTO `sys_role_menu` VALUES (2, 2002);
INSERT INTO `sys_role_menu` VALUES (2, 2003);
INSERT INTO `sys_role_menu` VALUES (2, 2005);
INSERT INTO `sys_role_menu` VALUES (2, 2006);
INSERT INTO `sys_role_menu` VALUES (2, 2007);
INSERT INTO `sys_role_menu` VALUES (2, 2008);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 100, 'admin', '若依', '00', 'ry@163.com', '15888888888', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '192.168.1.118', '2024-03-27 14:23:51', 'admin', '2024-03-05 09:07:02', '', '2024-03-27 14:23:52', '管理员');
INSERT INTO `sys_user` VALUES (2, 105, 'ry', '北海', '00', 'ry@qq.com', '15666666666', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2024-03-05 09:07:02', 'admin', '2024-03-05 09:07:02', 'admin', '2024-03-16 15:22:57', '测试员');
INSERT INTO `sys_user` VALUES (100, 101, 'ceshi', 'ceshi', '00', '', '', '0', '', '$2a$10$gGs2BDfl3pvmdvRefvtML.s2HJU8GMhCJVqt8UjdHNNIQw2IebOve', '0', '0', '192.168.1.118', '2024-03-27 14:34:15', 'admin', '2024-03-07 10:30:02', 'admin', '2024-03-27 14:34:16', NULL);
INSERT INTO `sys_user` VALUES (101, 108, 'miyun001 ', '李刚', '00', '13691145454@qq.com', '13691145454', '0', '', '$2a$10$r.Rct73mc9hUsEQlK8oI/erNf16OutowUYSxgruKsCc2aRLSAY40e', '0', '0', '123.127.143.209', '2024-03-19 15:02:58', 'admin', '2024-03-19 11:42:14', '', '2024-03-19 15:02:58', NULL);
INSERT INTO `sys_user` VALUES (102, 102, '13001011630', 'test', '00', '', '', '0', '', '$2a$10$MHvtcJIXfre854ljxPRSV.zlJLvzerKetkD0ZW2vJGQTIhZkj3IGe', '0', '0', '123.127.143.209', '2024-03-26 16:22:51', 'admin', '2024-03-26 15:36:47', 'admin', '2024-03-26 16:22:50', NULL);

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '用户与岗位关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, 1);
INSERT INTO `sys_user_post` VALUES (2, 2);
INSERT INTO `sys_user_post` VALUES (101, 1);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '用户和角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2);
INSERT INTO `sys_user_role` VALUES (100, 2);
INSERT INTO `sys_user_role` VALUES (101, 2);
INSERT INTO `sys_user_role` VALUES (102, 2);

SET FOREIGN_KEY_CHECKS = 1;
