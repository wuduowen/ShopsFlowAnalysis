#!/usr/bin/env python
# -*- coding: utf-8 -*-

from pyhive import presto

PRESTO_SERVER = {'host': 'bigdata', 'port': 8080, 'catalog': 'hive', 'schema': 'koubei'}

CITY_PRICE_TOTAL_QUERY="select s.city_name, sum(s.per_pay*p.totalcount)as total from (select shop_id,count(shop_id) as totalcount from user_pay_p group by shop_id) as p join (select city_name,cast(per_pay as integer) as per_pay,shop_id from shop_info) as s on p.shop_id=s.shop_id group by s.city_name order by total desc"

TRANSACTION_COUNT_QUERY="SELECT p.partition_date, count(*) as paycount FROM user_pay_p p GROUP BY p.partition_date order by p.partition_date"

VIEW_COUNT_QUERY="SELECT v.partition_date, count(*) as viewcount FROM user_view_p v GROUP BY v.partition_date order by v.partition_date"

class Presto_Query:

    def query_city_price_total(self):
        conn = presto.connect(**PRESTO_SERVER)
        cursor = conn.cursor()
        cursor.execute(CITY_PRICE_TOTAL_QUERY)
        tuples=cursor.fetchall()
        return tuples

    def getData(self, tuples):
        arr={}
        for tuple in tuples:
            city=tuple[0]
            price=tuple[1]
            arr[city]=price
        return arr

    def query_transaction_count(self):
        conn = presto.connect(**PRESTO_SERVER)
        cursor = conn.cursor()
        cursor.execute(TRANSACTION_COUNT_QUERY)
        tuples=cursor.fetchall()
        return tuples

    def getPayXData(self, tuples):
        payxlist=[]
        for tuple in tuples:
            payxlist.append(tuple[0])
        return payxlist

    def getPayYData(self, tuples):
        payylist=[]
        for tuple in tuples:
            payylist.append(tuple[1])
        return payylist

    def query_view_count(self):
        conn = presto.connect(**PRESTO_SERVER)
        cursor = conn.cursor()
        cursor.execute(VIEW_COUNT_QUERY)
        tuples=cursor.fetchall()
        return tuples