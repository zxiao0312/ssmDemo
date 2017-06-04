package com.zxiao.cm.exception;


/**
 * 
 * @author lsf
 *
 */
public class CmdExecException extends BaseRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CmdExecException(String code, String msg, Throwable cause) {
		super(code, msg, cause);
	}

}
