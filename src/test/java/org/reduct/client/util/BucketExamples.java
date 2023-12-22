package org.reduct.client.util;

public class BucketExamples {

   public static final String EMPTY_BUCKET_SETTINGS_BODY = "{}";

   public static final String SAMPLE_BUCKET_SETTINGS_BODY = """
           {
              "max_block_size": "64000000",
              "quota_type": "NONE",
              "quota_size": "0",
              "max_block_records": "1024"
           }
           """;
}
