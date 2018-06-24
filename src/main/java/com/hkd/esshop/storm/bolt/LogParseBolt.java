package com.hkd.esshop.storm.bolt;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import com.alibaba.fastjson.JSONObject;

/**
 * 日志解析的bolt
 * @author Administrator
 *
 */
public class LogParseBolt extends BaseRichBolt {

	private static final long serialVersionUID = -8017609899644290359L;

	private OutputCollector collector;
	
	@SuppressWarnings("rawtypes")
	public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}
	
	public void execute(Tuple tuple) {
		String message = tuple.getStringByField("message");
		System.out.println("[bolt收到一条数据]"+message);
		JSONObject messageJSON = JSONObject.parseObject(message);
		JSONObject uriArgsJSON = messageJSON.getJSONObject("uri_args"); 
		Long productId = uriArgsJSON.getLong("productId"); 
		
		if(productId != null) {
			collector.emit(new Values(productId));  
		}
	}
	
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		System.out.println("[bolt发送一条数据]productId");
		declarer.declare(new Fields("productId"));   
	}

}
