package com.oax.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.oax.common.ResultResponse;
import com.oax.service.I18nMessageService;

@RestControllerAdvice
@Slf4j
public class MyExceptionHandler {
	 @Autowired
	private I18nMessageService I18nMessageService;

	private final int SHOW_MSG_SIZE = 600;

	@ExceptionHandler(VoException.class)
    public ResultResponse handlerMyException(VoException ex) {
		String msg = ex.getMessage();
		log.error(msg + "\n" + StringUtils.substring(ExceptionUtils.getStackTrace(ex), 0, SHOW_MSG_SIZE));
        return new ResultResponse(ex.getCode(),false, msg);
    }

	@ExceptionHandler(RuntimeException.class)
	public ResultResponse handlerRuntimeException(RuntimeException ex) {
		String msg = ex.getMessage();
		log.error(msg + "\n" + StringUtils.substring(ExceptionUtils.getStackTrace(ex), 0, SHOW_MSG_SIZE));
		if (StringUtils.isNotBlank(msg)) {
			msg = msg.replaceAll(".*Exception:", "");
		}
		return new ResultResponse("10101", false, msg);
	}

	@ExceptionHandler(Exception.class)
	public ResultResponse exception(Exception ex) {
		String msg = ex.getMessage();
		log.error(msg + "\n" + StringUtils.substring(ExceptionUtils.getStackTrace(ex), 0, SHOW_MSG_SIZE));
		if (StringUtils.isNotBlank(msg)) {
			msg = msg.replaceAll(".*Exception:", "");
		}
		return new ResultResponse("10101", false, msg);
	}

	@ExceptionHandler(NullPointerException.class)
	public ResultResponse handleNullPointerException(Exception ex) {
		String msg = ex.getMessage();
		log.error(msg + "\n" + StringUtils.substring(ExceptionUtils.getStackTrace(ex), 0, SHOW_MSG_SIZE));
		return new ResultResponse("10101", false, "系统异常");
	}

	@ExceptionHandler(NoAuthenticationException.class)
	public ResultResponse handlerNoAuthenticationException(RuntimeException ex) {
		String msg = ex.getMessage();
		log.error(msg + "\n" + StringUtils.substring(ExceptionUtils.getStackTrace(ex), 0, SHOW_MSG_SIZE));
		if (StringUtils.isNotBlank(msg)) {
			msg = msg.replaceAll(".*Exception:", "");
		}
		return new ResultResponse(ResultResponse.CODE_NOAUTH, false, msg);
	}

	@ExceptionHandler(NoBankCardException.class)
	public ResultResponse handlerNoBankCardException(RuntimeException ex) {
		String msg = ex.getMessage();
		log.error(msg + "\n" + StringUtils.substring(ExceptionUtils.getStackTrace(ex), 0, SHOW_MSG_SIZE));
		if (StringUtils.isNotBlank(msg)) {
			msg = msg.replaceAll(".*Exception:", "");
		}
		return new ResultResponse(ResultResponse.CODE_NOBANKCARD, false, msg);
	}

	@ExceptionHandler(NoPhoneException.class)
	public ResultResponse handlerNoPhoneException(RuntimeException ex) {
		String msg = ex.getMessage();
		log.error(msg + "\n" + StringUtils.substring(ExceptionUtils.getStackTrace(ex), 0, SHOW_MSG_SIZE));
		if (StringUtils.isNotBlank(msg)) {
			msg = msg.replaceAll(".*Exception:", "");
		}
		return new ResultResponse(ResultResponse.CODE_NOPHONE, false, msg);
	}

	@ExceptionHandler(NoEmailException.class)
	public ResultResponse handlerNoEmailException(RuntimeException ex) {
		String msg = ex.getMessage();
		log.error(msg + "\n" + StringUtils.substring(ExceptionUtils.getStackTrace(ex), 0, SHOW_MSG_SIZE));
		if (StringUtils.isNotBlank(msg)) {
			msg = msg.replaceAll(".*Exception:", "");
		}
		return new ResultResponse(ResultResponse.CODE_NOEMAIL, false, msg);
	}

	@ExceptionHandler(NoTranpwException.class)
	public ResultResponse handlerNoTranpwException(RuntimeException ex) {
		String msg = ex.getMessage();
		log.error(msg + "\n" + StringUtils.substring(ExceptionUtils.getStackTrace(ex), 0, SHOW_MSG_SIZE));
		if (StringUtils.isNotBlank(msg)) {
			msg = msg.replaceAll(".*Exception:", "");
		}
		return new ResultResponse(ResultResponse.CODE_NOTRANPW, false, msg);
	}

	@ExceptionHandler(NoLoginException.class)
	public ResultResponse handlerNoLoginException(RuntimeException ex) {
		String msg = ex.getMessage();
		log.error(msg + "\n" + StringUtils.substring(ExceptionUtils.getStackTrace(ex), 0, SHOW_MSG_SIZE));
		if (StringUtils.isNotBlank(msg)) {
			msg = msg.replaceAll(".*Exception:", "");
		}
		return new ResultResponse(ResultResponse.CODE_NOLOGIN, false, "The login timeout.");
	}

}
