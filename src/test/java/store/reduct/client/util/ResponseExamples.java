package store.reduct.client.util;

public class ResponseExamples {

   public static final String SUCCESSFUL_SERVER_INFO_RESPONSE =
           """
                   {
                      "version": "1.3.2",
                      "bucket_count": 1,
                      "usage": 0,
                      "uptime": 4021,
                      "oldest_record": 18446744073709551615,
                      "latest_record": 0,
                      "defaults": {
                         "bucket": {
                            "max_block_size": 64000000,
                            "quota_type": "NONE",
                            "quota_size": 0,
                            "max_block_records": 1024
                         }
                      }
                   }
                     """;
   public static final String SUCCESSFUL_LIST_RESPONSE =
           """
                   {
                     "buckets": [
                       {
                         "name": "test_bucket_1",
                         "entry_count": 0,
                         "size": 0,
                         "oldest_record": 18446744073709552000,
                         "latest_record": 0,
                         "is_provisioned": false
                       }
                     ]
                   }
                     """;
}
