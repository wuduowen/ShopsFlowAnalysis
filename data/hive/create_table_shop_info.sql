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
 cate_3_name varchar(255)
 )ROW FORMAT DELIMITED
  FIELDS TERMINATED BY ',';


  load data inpath '/koubei_data/b94c739e-36c3-4ab5-aa25-a579c4ea6af5.txt' overwrite into table koubei.shop_info;