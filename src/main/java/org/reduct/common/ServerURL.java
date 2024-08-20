package store.reduct.common;

public enum ServerURL {

   SERVER_INFO("api/v1/info"),
   LIST("api/v1/list"),
   ALIVE("api/v1/alive");

   private final String url;

   ServerURL(String url) {
      this.url = url;
   }

   public String getUrl() {
      return url;
   }
}
