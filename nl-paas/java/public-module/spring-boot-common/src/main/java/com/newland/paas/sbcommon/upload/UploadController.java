package com.newland.paas.sbcommon.upload;

import com.alibaba.fastjson.JSON;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.newland.paas.common.util.DateUtils;
import com.newland.paas.common.util.SFTPUtil;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.upload.vo.FileVo;
import com.newland.paas.sbcommon.upload.vo.TimeoutInterfaceVo;
import com.newland.paas.sbcommon.utils.RestTemplateUtils;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import com.newland.paas.sbcommon.vo.PaasError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传 微服务通用接口，负责上传文件。返回文件信息
 *
 * @author WRP
 * @since 2018/10/11
 */
@RestController
@RequestMapping("/v1/commonUpload")
public class UploadController {
	/**
	 * 获取上传文件ftp配置
	 */
	@Value("${sftp.cacheser.account}")
	private String account;
	@Value("${sftp.cacheser.password}")
	private String password;
	@Value("${sftp.cacheser.ipaddress}")
	private String ipaddress;
	@Value("${sftp.cacheser.port}")
	private int port;
	@Value("${sftp.cacheser.uploadaddress}")
	private String uploadaddress;

	private static final Log LOG = LogFactory.getLogger(UploadController.class);
	public static final String BACK_SLANT = "/";
	public static final String UPLOAD_PATH = "tmp";

	@Autowired
	private RestTemplateUtils restTemplateUtils;


	/**    
	 * @描述: sftp上传文件 
	 *        response示例:{"content":[{"fileName":"redis.latest.tar","fileAddr":"/tmp/20181011/","fileSize":84010496}]}
	 * @param fileList
	 * @return List<FileVo> 文件详情
	 * @throws ApplicationException         
	 * @创建人：zyp
	 * @创建时间：2019/02/26 
	 */
	@CrossOrigin
	@PostMapping(value = "upload", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public List<FileVo> upload(@RequestParam("fileList") MultipartFile[] fileList) throws ApplicationException {
		LOG.info(LogProperty.LOGTYPE_CALL, "====upload:" + JSON.toJSONString(fileList.length));
		if (fileList == null || fileList.length == 0) {
			throw new ApplicationException(new PaasError("21500008", "上传文件不能为空"));
		}
		String dateDir = DateUtils.formatDate(new Date(), DateUtils.DATE_SIMP_FORMAT);
		SFTPUtil sftp = new SFTPUtil(account, password, ipaddress, port);
		List<FileVo> fileVos = new ArrayList<>(fileList.length);
		String url = uploadaddress + BACK_SLANT + dateDir;
		InputStream inputStream = null;
		try {
			sftp.login();
			sftp.createRootDir(url);
			if (null != fileList && fileList.length > 0) {
				for (MultipartFile file : fileList) {
					FileVo fileVo = new FileVo();
					fileVo.setFileSize(file.getSize());
					String filename = file.getOriginalFilename();
					String suffix = filename.substring(filename.lastIndexOf('.') + 1);
					inputStream = file.getInputStream();
					String nameUUID = UUID.randomUUID().toString().replaceAll("-", "");
					String filedendname = nameUUID + "." + suffix;
					sftp.upload(url, filedendname, inputStream);
					fileVo.setFileName(filename);
					fileVo.setFileAddr(url + BACK_SLANT + filedendname);
					LOG.info(LogProperty.LOGTYPE_DETAIL, "文件名称>>>>>" + filename + "文件路径>>>>>" + filedendname);
					fileVos.add(fileVo);
				}
			}
			sftp.logout();
			inputStream.close();
		} catch (JSchException e) {
			LOG.error(LogProperty.LOGTYPE_DETAIL, "JSchException", e);
			throw new ApplicationException(new PaasError("21500023", "上传文件失败，请检查文件"));
		} catch (SftpException e) {
			LOG.error(LogProperty.LOGTYPE_DETAIL, "SftpException", e);
			throw new ApplicationException(new PaasError("21500023", "上传文件失败，请检查文件"));
		} catch (IOException e) {
			LOG.error(LogProperty.LOGTYPE_DETAIL, "IOException", e);
			throw new ApplicationException(new PaasError("21500023", "上传文件失败，请检查文件"));
		}
		LOG.info(LogProperty.LOGTYPE_DETAIL, "upload>>>>>上传文件结束");
		return fileVos;
	}

	/**
	 * 超时转发接口
	 *
	 * @param params
	 *            请求参数
	 * @param request
	 *            request
	 * @return 接口返回信息
	 */
	@CrossOrigin
	@PostMapping(value = "timeout", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public BasicResponseContentVo<Map<String, Object>> timeout(
			@RequestBody BasicRequestContentVo<TimeoutInterfaceVo> params, HttpServletRequest request) {
		LOG.info(LogProperty.LOGTYPE_CALL, "====timeout:" + JSON.toJSONString(params));
		TimeoutInterfaceVo timeoutInterfaceVo = params.getParams();
		Map<String, Object> param = JSON.parseObject(timeoutInterfaceVo.getParam());
		String tokens = request.getHeader("Authorization");
		LOG.info(LogProperty.LOGTYPE_DETAIL, "Authorization:" + tokens);
		if (!StringUtils.isEmpty(tokens)) {
			tokens = tokens.replaceAll("Bearer ", "");
		}
		BasicRequestContentVo<Map<String, Object>> reqVo = new BasicRequestContentVo<>();
		reqVo.setParams(param);
		return restTemplateUtils.postForTokenEntity(timeoutInterfaceVo.getMicroservices(),
				timeoutInterfaceVo.getInterfaceUrl(), tokens, reqVo,
				new ParameterizedTypeReference<BasicResponseContentVo<Map<String, Object>>>() {
				});
	}

}
