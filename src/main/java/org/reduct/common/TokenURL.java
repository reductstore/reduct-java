package org.reduct.common;

public enum TokenURL {

   CREATE_TOKEN("api/v1/tokens/%s");

   private final String url;

   TokenURL(String url) {
      this.url = url;
   }

   public String getUrl() {
      return url;
   }
}
