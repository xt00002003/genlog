/**
　随机字符串函数
 */
delimiter //
CREATE FUNCTION rand_string(n INT) RETURNS varchar(255) CHARSET latin1
  BEGIN
    DECLARE chars_str varchar(100) DEFAULT 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    DECLARE return_str varchar(255) DEFAULT '';
    DECLARE i INT DEFAULT 0;
    WHILE i < n DO
      SET return_str = concat(return_str,substring(chars_str , FLOOR(1 + RAND()*62 ),1));
      SET i = i +1;
    END WHILE;
    RETURN return_str;
  END;
//
/**
 创建测试用户表
 */
CREATE TABLE test_user (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键自动增长',
  uuid varchar(40) DEFAULT NULL COMMENT '用来标识同一记录不同站点的版本',
  im varchar(30) DEFAULT NULL COMMENT 'IMEI',
  mac varchar(30) DEFAULT NULL COMMENT 'mac',
  ai varchar(30) DEFAULT NULL COMMENT 'ANDROID_ID',
  t varchar(16) DEFAULT NULL COMMENT 'model',
  c varchar(16) NOT NULL COMMENT 'country',
  la varchar(16) NOT NULL COMMENT 'language',
  create_time varchar(30) DEFAULT NULL COMMENT '创建时间yyyy-MM-dd',
  PRIMARY KEY (id),
  KEY idx_uuid (uuid,create_time),
  KEY idx_mac (mac,create_time),
  KEY idx_ai (ai,create_time),
  KEY idx_im (im,create_time),
  KEY idx_create_time (create_time)
) ENGINE=InnoDB AUTO_INCREMENT=1510 DEFAULT CHARSET=utf8 COMMENT='测试用户表';
/**
　创建国家表
 */
CREATE TABLE test_country (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键自动增长',
  uuid varchar(40) NOT NULL COMMENT '用来标识同一记录不同站点的版本',
  country_code varchar(64) NOT NULL COMMENT '国家代码',
  country_name varchar(64) NOT NULL COMMENT '国家名称',
  region_code varchar(64) NOT NULL COMMENT '大洲',
  language_code varchar(64) NOT NULL COMMENT '语言',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1124 DEFAULT CHARSET=utf8 COMMENT='国家列表';

insert into test_country (uuid,country_code,country_name,region_code,language_code) VALUES
  (uuid(),'cn','China','Asia Pacific','cn'),
  (uuid(),'ru','Russia','Europe','ru'),
  (uuid(),'usa','United States','Americas','en'),
  (uuid(),'mx','Mexico','Americas','es-rLA'),
  (uuid(),'fr','France','Europe','fr');

CREATE TABLE test_user_history (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键自动增长',
  user_id bigint(20) NOT NULL COMMENT '用来标识同一记录不同站点的版本',
  app_id bigint(20) NOT NULL COMMENT '国家代码',
  pkg_name varchar(64) NOT NULL COMMENT '国家代码',
  channel_name varchar(64) NOT NULL COMMENT '国家名称',
  create_time varchar(30) DEFAULT NULL COMMENT '创建时间yyyy-MM-dd',
  PRIMARY KEY (id),
  KEY idx_user_id (user_id,create_time)
) ENGINE=InnoDB AUTO_INCREMENT=1124 DEFAULT CHARSET=utf8 COMMENT='用户历史记录表,在留存率的计算中需要用到这个表的记录';

/**
　生成测试用户的存储过程.每天５条记录.
 */
delimiter //
CREATE PROCEDURE create_new_test_user()
  BEGIN
    DECLARE v_country_code VARCHAR(64);
    DECLARE v_language_code VARCHAR(64);
    DECLARE i INT DEFAULT 1;
    WHILE i < 6  DO
      select country_code,language_code into v_country_code,v_language_code from test_country order by rand() LIMIT 1;
      insert into test_user (uuid,im,mac,ai,t,c,la,create_time) VALUES
        (uuid(),rand_string(15),rand_string(12),rand_string(16),rand_string(7),v_country_code,v_language_code,date_format(now(),'%Y-%m-%d'));
      set i=i+1;
    END WHILE;
  END;
//

/**
　要查看当前是否已开启事件调度器
 */
SHOW VARIABLES LIKE 'event_scheduler';
/**
  开启触发器功能
 */
SET GLOBAL event_scheduler = 1;
/**
　创建事件
 */
create event e_create_user
  on schedule
  every 1 DAY starts '2016-09-07 05:00:00'
  do call create_new_test_user();
/**
  立刻启动事件
 */
alter event e_create_user on
completion preserve enable;


