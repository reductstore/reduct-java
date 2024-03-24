package org.reduct.common;

public enum EntryURL {
      WRITE_ENTRY("/api/v1/b/%s/%s");

      private final String url;

      EntryURL(String url) {
         this.url = url;
      }

      public String getUrl() {
         return url;
      }
}
