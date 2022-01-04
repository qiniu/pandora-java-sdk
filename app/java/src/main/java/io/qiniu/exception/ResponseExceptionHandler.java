package io.qiniu.exception;

import com.qiniu.pandora.common.QiniuException;
import io.qiniu.utils.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResponseExceptionHandler {
  /**
   * Handle {@link QiniuException}.
   *
   * @param e NacosException
   * @return ResponseEntity
   */
  @ExceptionHandler(QiniuException.class)
  public ResponseEntity<String> handlePandoraException(QiniuException e) {
    return ResponseEntity.status(e.getErrCode()).body(e.getErrMsg());
  }

  /**
   * Handle {@link IllegalArgumentException}.
   *
   * @param ex IllegalArgumentException
   * @return ResponseEntity
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleParameterError(IllegalArgumentException ex) {
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
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("Parameter '" + name + "' is missing");
  }

  /**
   * Handle other exception.
   *
   * @param e other exception
   * @return ResponseEntity
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ExceptionUtils.getAllExceptionMsg(e));
  }
}
