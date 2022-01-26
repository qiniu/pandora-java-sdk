package io.qiniu.exception;

import com.qiniu.pandora.common.QiniuException;
import io.qiniu.utils.ExceptionUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResponseExceptionHandler {

  private static final Logger logger = LogManager.getLogger(ResponseExceptionHandler.class);

  /**
   * Handle {@link QiniuException}.
   *
   * @param ex NacosException
   * @return ResponseEntity
   */
  @ExceptionHandler(QiniuException.class)
  public ResponseEntity<String> handlePandoraException(QiniuException ex) {
    logger.error(ex.getMessage());
    return ResponseEntity.status(ex.getErrCode()).body(ex.getErrMsg());
  }

  /**
   * Handle {@link IllegalArgumentException}.
   *
   * @param ex IllegalArgumentException
   * @return ResponseEntity
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleParameterError(IllegalArgumentException ex) {
    logger.error(ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  /**
   * Handle missing request parameter exception.
   *
   * @param ex {@link MissingServletRequestParameterException}
   * @return ResponseEntity
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
    String name = ex.getParameterName();

    logger.error("Parameter '" + name + "' is missing");

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("Parameter '" + name + "' is missing");
  }

  /**
   * Handle other exception.
   *
   * @param ex other exception
   * @return ResponseEntity
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception ex) {
    logger.error(ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ExceptionUtils.getAllExceptionMsg(ex));
  }
}
