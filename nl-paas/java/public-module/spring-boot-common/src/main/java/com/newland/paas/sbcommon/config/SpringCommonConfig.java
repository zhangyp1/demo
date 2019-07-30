package com.newland.paas.sbcommon.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory.Feature;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RoundRobinRule;
import com.newland.paas.sbcommon.utils.RestTemplateUtils;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

/**
 * @author wrp
 * @Date 2017/10/30
 */
@Configuration
public class SpringCommonConfig {

	/**
	 * 配置a:以json请求后台，多字段不会报错 b:后端空对象以 {} 返回或不返回
	 * 使用方式：在各自微服务使用@Import(com.newland.paas.sbcommon.config.SpringConfig.class)导入配置
	 *
	 * @return
	 */
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		objectMapper.setDateFormat(sdf);

		// 属性为null时不转为json
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		// 属性为空对象以 {} 返回
		// objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// 以json请求后台，多字段不会报错
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper;
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	// Loadbalance的策略配置
	@Bean
	@Scope("prototype")
	public IRule ribbonRule() {
		// 轮询策略
		return new RoundRobinRule();
		//        return new BestAvailableRule(); //选择一个最小的并发请求的server
		//        return new WeightedResponseTimeRule(); //根据相应时间分配一个weight，相应时间越长，weight越小，被选中的可能性越低。
		//        return new RetryRule(); //对选定的负载均衡策略机上重试机制。
		//        return new RoundRobinRule(); //roundRobin方式轮询选择server
		//        return new RandomRule(); //随机选择一个server
		//        return new ZoneAvoidanceRule(); //复合判断server所在区域的性能和server的可用性选择server
		//        return new AvailabilityFilteringRule();
	}

	@Bean
	public RestTemplateUtils restTemplateUtils() {
		return new RestTemplateUtils();
	}

}
