create table if not exists koubei.user_pay (
 user_id string,
 shop_id string,
 time_stamp string
)ROW FORMAT DELIMITED
 FIELDS TERMINATED BY ',';

load data inpath '/data/user_pay.txt' overwrite into table koubei.user_pay;