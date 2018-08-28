-- 1.创建表保存 商家 日均交易额

create table if not exists koubei.shop_avg_trans (
 shop_id string,
 avg_trans double
)ROW FORMAT DELIMITED
 FIELDS TERMINATED BY ',';

--  2.查询分析商家日均交易额保存到结果表
insert into koubei.shop_avg_trans SELECT s.shop_id, (s.per_pay * b.total_count) /(b.total_day * 1.0) as total_trans
FROM(
SELECT a.shop_id, COUNT(a.partition_date) as total_day, SUM(a.tran_count) as total_count FROM
(select v.shop_id, v.partition_date, COUNT(*) as tran_count FROM user_pay_p v GROUP BY v.shop_id, v.partition_date) a
GROUP BY a.shop_id
) b
join shop_info s ON b.shop_id = s.shop_id
ORDER BY total_trans desc;


-- 3.北京、上海和广州三个城市最受欢迎的 10 家火锅商店编号

SELECT shop_id, city_name, 0.7 *(score/5) + 0.3*(per_pay/((SELECT MAX(CAST(per_pay AS SIGNED)) FROM shop_info )* 1.0)) as hot
FROM shop_info WHERE city_name in ('北京', '上海', '广州') AND cate_2_name LIKE '%火锅%' ORDER BY hot desc LIMIT 10;


