package com.newland.paas.common.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;

import com.newland.paas.common.exception.ErrorCode;
import com.newland.paas.common.exception.NLCheckedException;
import com.newland.paas.common.util.IOUtils;

/**
 * http调用客户端
 * 
 * @author Administrator
 *
 */
public class HttpClient {

	/**
	 * 发送HTTP POST报文数据
	 * @param url
	 * @param msg
	 * @return
	 * @throws NLCheckedException
	 */
	public static String sendHttpPostMsg(String url, String msg) throws NLCheckedException {
		return sendHttpPostMsg(url, msg, null);
	}

	/**
	 * http发送multipart请求到服务
	 * @param serverUrl
	 * @param messageEntity
	 * @throws Exception
	 */
	public static String httpRequestExecute(String serverUrl, HttpEntity messageEntity) throws NLCheckedException {
		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String MULTIPART_FROM_DATA = "multipart/form-data";

		HttpURLConnection connection = null;
		InputStream inputStream = null;
		String response = null;
		try {
			URL url = URI.create(serverUrl).toURL();
			connection = (HttpURLConnection) url.openConnection();
			connection.setReadTimeout(50 * 1000);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("connection", "keep-alive");
			connection.setRequestProperty("Charsert", "UTF-8");
			connection.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
			connection.setRequestProperty("Content-Type", messageEntity.getContentType().getValue());

			try (OutputStream out = connection.getOutputStream()) {
				messageEntity.writeTo(out);
			}

			inputStream = connection.getInputStream();
//			response = StreamUtil.asString(inputStream);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		IOUtils.closeQuietly(inputStream);
		connection.disconnect();
		return response;
	}

	/**
	 * 发送HTTP POST报文数据
	 * @param url
	 * @param msg
	 * @param httpHeadParams HTTP头部参数,默认设置以下值，如有参数传入，则覆盖
	 * 		Content-Type = application/json
	 * 		accept = application/json
	 * 		Charset = UTF-8
	 * @return
	 * @throws NLCheckedException
	 */
	public static String sendHttpPostMsg(String url, String msg, Map<String, String> httpHeadParams) throws NLCheckedException {
		HttpURLConnection conn = null;
		try {
			URL url1 = new URL(url);
			conn = (HttpURLConnection) url1.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("accept", "application/json");
			// conn.setRequestProperty("Authorization", "Bearer
			// 11d7139d-16fa-3270-9f80-64188610e1e2");
			conn.setRequestProperty("Charset", "UTF-8");

			if (httpHeadParams != null) {
				Iterator<String> it = httpHeadParams.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					String v = httpHeadParams.get(key);
					conn.setRequestProperty(key, v);
				}
			}

			conn.connect();
			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(msg);
			writer.flush();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String bufferString = null;
			StringBuffer buffer = new StringBuffer();
			while ((bufferString = reader.readLine()) != null) {
				buffer.append(bufferString);
			}
			String reStr = buffer.toString();
			return reStr;
		} catch (Exception e) {
			throw new NLCheckedException(ErrorCode.SYSERROR_NETWORK_HTTP, e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}
	
	public static String sendHttpGet(String url, Map<String, String> httpHeadParams) throws NLCheckedException {
		HttpURLConnection conn = null;
		try {
			URL url1 = new URL(url);
			conn = (HttpURLConnection) url1.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("accept", "application/json");
			// conn.setRequestProperty("Authorization", "Bearer
			// 11d7139d-16fa-3270-9f80-64188610e1e2");
			conn.setRequestProperty("Charset", "UTF-8");

			if (httpHeadParams != null) {
				Iterator<String> it = httpHeadParams.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					String v = httpHeadParams.get(key);
					conn.setRequestProperty(key, v);
				}
			}

			conn.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String bufferString = null;
			StringBuffer buffer = new StringBuffer();
			while ((bufferString = reader.readLine()) != null) {
				buffer.append(bufferString);
			}
			String reStr = buffer.toString();
			return reStr;
		} catch (Exception e) {
			throw new NLCheckedException(ErrorCode.SYSERROR_NETWORK_HTTP, e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}
	
	

	public static void main(String[] args) {
		/*try {
			String result = HttpClient.sendHttpPostMsg("http://10.1.8.13:8081/paas/v1/epe/scheduler/operation/execute",
					"{\"content\":{\"operationId\":6},\"header\":{\"requestSeq\":\"1501741544442\"}}");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}*/

		try {
			String result = HttpClient.sendHttpPostMsg("http://node1:8090/add",
					"{\"name\":\"abc\"}");
			System.out.println(result);
			result = HttpClient.sendHttpGet("http://www.baidu.com", null);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
