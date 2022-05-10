SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb
-- ----------------------------
DROP TABLE IF EXISTS `tb`;
CREATE TABLE `tb` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '注释1',
  `field1` varchar(255) DEFAULT NULL COMMENT '注释2',
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Records of tb
-- ----------------------------
INSERT INTO `tb` VALUES ('1', 'f1');
INSERT INTO `tb` VALUES ('2', 'f2');
INSERT INTO `tb` VALUES ('3', 'f3');
INSERT INTO `tb` VALUES ('4', 'f4');
