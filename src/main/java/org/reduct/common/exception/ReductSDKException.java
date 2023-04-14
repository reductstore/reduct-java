package org.reduct.common.exception;

public class ReductSDKException extends RuntimeException {

   public ReductSDKException(String message) {
      super(message);
   }

   public ReductSDKException(String message, Throwable cause) {
      super(message, cause);
   }
}
