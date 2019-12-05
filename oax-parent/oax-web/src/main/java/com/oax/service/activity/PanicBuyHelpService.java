package com.oax.service.activity;

import com.oax.exception.VoException;
import com.oax.form.PanicBuyHelpForm;
import com.oax.vo.PanicBuyShareVo;

import javax.servlet.http.HttpServletRequest;

public interface PanicBuyHelpService {

    PanicBuyShareVo getIndexData(String shareCode);

    PanicBuyShareVo saveHelp(HttpServletRequest request,PanicBuyHelpForm form) throws VoException;
}
