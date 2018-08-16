1.创建user_pay 用户支付表
create table if not exists koubei.user_pay (
 user_id string,
 shop_id string,
 time_stamp string
)ROW FORMAT DELIMITED
 FIELDS TERMINATED BY ',';

2.加载数据
load data inpath '/data/user_pay.txt' overwrite into table koubei.user_pay;

3.创建临时表
create table if not exists koubei.user_pay_temp (
 user_id string,
 shop_id string,
 partition_date string
)ROW FORMAT DELIMITED
 FIELDS TERMINATED BY ',';

4.加载数据 将用户支付表的time_stamp 时间字段 转换为为日期
insert into user_pay_temp select user_id, shop_id, to_date(time_stamp) from user_pay;

5.创建用户支付分区表
create table if not exists koubei.user_pay_p (
 user_id string,
 shop_id string
)
 PARTITIONED BY (
 partition_date STRING
 )
 ROW FORMAT DELIMITED
 FIELDS TERMINATED BY ','
 STORED AS ORC;

6.加载数据到 用户支付分区表
SET hive.exec.mode.local.auto=true;
set hive.exec.dynamic.partition.mode=nonstrict;
set hive.exec.dynamic.partition=true;
set hive.exec.max.dynamic.partitions.pernode=100000;
set hive.exec.max.dynamic.partitions=100000;
set hive.exec.max.created.files=100000;
insert into table koubei.user_pay_p partition(partition_date) select * from koubei.user_pay_temp;

