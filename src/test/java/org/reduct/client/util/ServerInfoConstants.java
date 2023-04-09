package org.reduct.client.util;

public class ServerInfoConstants {

   public static final String SUCCESSFUL_SERVER_INFO_RESPONSE =
           """
                   {
                      "version": "1.3.2",
                      "bucket_count": "1",
                      "usage": "0",
                      "uptime": "4021",
                      "oldest_record": "18446744073709551615",
                      "latest_record": "0",
                      "defaults": {
                         "bucket": {
                            "max_block_size": "64000000",
                            "quota_type": "NONE",
                            "quota_size": "0",
                            "max_block_records": "1024"
                         }
                      }
                   }
                     """;
}
