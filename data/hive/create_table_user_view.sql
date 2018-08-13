create database koubei;

create table if not exists koubei.user_view (
 user_id string,
 shop_id string,
 time_stamp string
)ROW FORMAT DELIMITED
 FIELDS TERMINATED BY ',';

load data inpath '/data/user_view.txt' overwrite into table koubei.user_view;