create database if not exists koubei;
-- 1.创建用户浏览表
create table if not exists koubei.user_view (
 user_id string,
 shop_id string,
 time_stamp string
)ROW FORMAT DELIMITED
 FIELDS TERMINATED BY ',';

-- 2.加载数据
load data inpath '/data/user_view.txt' overwrite into table koubei.user_view;

-- 3.创建临时表
create table if not exists koubei.user_view_temp (
 user_id string,
 shop_id string,
 partition_date string
)ROW FORMAT DELIMITED
 FIELDS TERMINATED BY ',';

-- 4.加载数据 将用户浏览表的time_stamp 时间字段 转换为为日期
insert into user_view_temp select user_id, shop_id, to_date(time_stamp) from user_view;

-- 5.创建用户浏览分区表
create table if not exists koubei.user_view_p (
 user_id string,
 shop_id string
)
 PARTITIONED BY (
 partition_date STRING
 )
 ROW FORMAT DELIMITED
 FIELDS TERMINATED BY ','
 STORED AS ORC;

-- 6.加载数据到 用户浏览分区表
SET hive.exec.mode.local.auto=true;
set hive.exec.dynamic.partition.mode=nonstrict;
set hive.exec.dynamic.partition=true;
set hive.exec.max.dynamic.partitions.pernode=100000;
set hive.exec.max.dynamic.partitions=100000;
set hive.exec.max.created.files=100000;
insert into table koubei.user_view_p partition(partition_date) select * from koubei.user_view_temp;