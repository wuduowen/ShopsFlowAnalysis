# -*- coding:utf-8 -*-

from flask import Flask, render_template
import json
from models import Chart
from query_presto import Presto_Query

app = Flask(__name__)

@app.route("/")
def index():
    presto=Presto_Query()

    city_price_total_tuples=presto.query_city_price_total()
    city_data=presto.getData(city_price_total_tuples)
    chart1=Chart().pie("饼图", data=city_data)

    transaction_count_tuples=presto.query_transaction_count()
    xAxisData=presto.getPayXData(transaction_count_tuples)
    yAxisData=presto.getPayYData(transaction_count_tuples)
    chart2= Chart().line("折线图",data=yAxisData, date = xAxisData)

    view_count_tuples=presto.query_view_count()
    xAxisData=presto.getPayXData(view_count_tuples)
    yAxisData=presto.getPayYData(view_count_tuples)
    chart3= Chart().line("折线图",data = yAxisData, date = xAxisData)

    top_10_shop_tuples=presto.query_top_10_shop()
    keys=presto.getKeys(top_10_shop_tuples)
    values=presto.getValues(top_10_shop_tuples)
    chart4=Chart() \
        .x_axis(data=keys,name="商家编号") \
        .y_axis(formatter="{value}",name="日均交易额") \
        .bar(u"shop Average daily turnover", values, show_item_label=True)

    render = {
        "title": u"阿里巴巴口碑商家流量分析",
        "templates": [
            {"type": "chart", "title":u"每个城市总体消费金额", "option": json.dumps(chart1, indent=2)},
            {"type": "chart", "title":u"所有商家交易发生次数", "option": json.dumps(chart2, indent=2)},
            {"type": "chart", "title":u"所有商家被用户浏览次数", "option": json.dumps(chart3, indent=2)},
            {"type": "chart", "title":u"平均日交易额最大的前 10 个商家", "option": json.dumps(chart4, indent=2)}
        ]
    }
    return render_template("main.html", **render)

if __name__ == "__main__":
    app.run(debug=True)