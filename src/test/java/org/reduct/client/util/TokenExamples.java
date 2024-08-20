package store.reduct.client.util;

public class TokenExamples {

   public static final String CREATE_TOKEN_REQUEST_BODY =
           """
                   {
                     "full_access" : true,
                     "read" : ["test-bucket"],
                     "write" : ["test-bucket"]
                   }
                   """;

   public static final String CREATE_TOKEN_SUCCESSFUL_RESPONSE =
           """
                   {
                       "name" : "test-042e0a25ad9d0c6be3d9cb53e824b9de371bc697a6eb1ac99609aaae1d04c414",
                       "created_at" : "2023-04-09T22:29:17Z"
                   }
                   """;

   public static final String EXPECTED_TOKEN_VALUE =
           "test-042e0a25ad9d0c6be3d9cb53e824b9de371bc697a6eb1ac99609aaae1d04c414";

   public static final String EXPECTED_CREATION_DATE = "2023-04-09T22:29:17Z";
}
