package org.reduct.common;

public enum BucketURL {
    CREATE_BUCKET("api/v1/b/%s"),
    GET_BUCKET("api/v1/b/%s");
    private final String url;
    BucketURL(String url) {
         this.url = url;
      }
      public String getUrl() {
         return url;
      }
}
