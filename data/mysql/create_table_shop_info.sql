create database if not exists koubei;

create table if not exists koubei.shop_info(
shop_id varchar(50),
city_name varchar(50),
location_id varchar(50),
per_pay varchar(25),
score varchar(25),
comment_cnt varchar(25),
shop_level varchar(25),
cate_1_name varchar(255),
cate_2_name varchar(255),
cate_3_name varchar(255),
primary key (shop_id)
) default charset=utf8;

LOAD DATA LOCAL INFILE '/home/zkpk/Documents/dataset/shop_info.txt'
INTO TABLE koubei.shop_info
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'