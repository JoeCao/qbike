package club.newtech.qbike.order.domain.exception;

import club.newtech.qbike.order.util.SpringContextHolder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

/**
 * 运行时异常
 * Created by jinwei on 12/1/2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderRuntimeException extends RuntimeException {

    private String errorCode = "";

    private String errorMessage = "";

    private Object[] params;

    private MessageSource messageSource = SpringContextHolder.getBean(MessageSource.class);

    public OrderRuntimeException() {
        super();
        this.errorCode = "000000";
    }

    public OrderRuntimeException(String errorCode) {
        super();
        this.errorCode = errorCode;
        this.params = null;
    }

    public OrderRuntimeException(String errorCode, Object[] params) {
        super();
        this.errorCode = errorCode;
        this.params = params;
    }

    public OrderRuntimeException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.params = null;
    }

    public OrderRuntimeException(String errorCode, Object[] params, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.params = params;
    }

    public OrderRuntimeException(Throwable cause) {
        super(cause);

    }


    /**
     * 生成异常信息
     *
     * @return
     */
    public String getErrorMessage() {
        String msg = "";
        Throwable cause = this.getCause();

        String errorCode = this.getErrorCode();

        if (StringUtils.isNotEmpty(errorCode) && StringUtils.isEmpty(msg)) {
            //2、如果有异常码，以异常码对应的提示信息为准
            msg = this.getMessage(errorCode, this.getParams());
        }
        if (StringUtils.isEmpty(msg) && cause != null) {
            msg = cause.getMessage();
        }
        if (StringUtils.isEmpty(msg)) {
            //3、异常码为空 & msg为空，提示系统异常
            msg = this.getMessage("000000", this.getParams());
        }
        return msg;
    }

    /**
     * 获取错误码描述
     *
     * @param code
     * @return
     */
    private String getMessage(String code, Object[] params) {
        try {
            return messageSource.getMessage(code, params, Locale.CHINA);
        } catch (NoSuchMessageException e) {
            return code;
        }
    }
}
