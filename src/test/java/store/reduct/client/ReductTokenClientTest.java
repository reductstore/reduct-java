package store.reduct.client;

import org.junit.jupiter.api.Disabled;

@Disabled
class ReductTokenClientTest {
//
//   private static final String REDUCT_ERROR_HEADER = "x-reduct-error";
//   private static final String TOKEN_NAME = "test";
//
//   private TokenClient reductTokenClient;
//   private ServerProperties serverProperties;
//   private HttpClient httpClient;
//   private String accessToken;
//
//   @BeforeEach
//   public void setup() {
//      accessToken = "testToken";
//      httpClient = mock(HttpClient.class);
//      serverProperties = new ServerProperties(false, "localhost", 8383);
//      reductTokenClient = new ReductTokenClient(serverProperties, httpClient, accessToken);
//   }
//
//   @Test
//   void createToken_validDetails_returnToken() throws IOException, InterruptedException {
//      HttpRequest httpRequest = buildCreateTokenRequest();
//      HttpResponse<String> httpResponse = mock(HttpResponse.class);
//      doReturn(200).when(httpResponse).statusCode();
//      doReturn(TokenExamples.CREATE_TOKEN_SUCCESSFUL_RESPONSE).when(httpResponse).body();
//      doReturn(httpResponse).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.ofString());
//
//      AccessToken token = reductTokenClient.createToken(TOKEN_NAME,
//              TokenPermissions.of(true, List.of("test-bucket"), List.of("test-bucket")));
//
//      assertEquals(TokenExamples.EXPECTED_TOKEN_VALUE, token.name());
//      assertEquals(TokenExamples.EXPECTED_CREATION_DATE, token.createdAt());
//   }
//
//   @Test
//   void createToken_tokenAlreadyExists_throwException() throws IOException, InterruptedException {
//      HttpRequest httpRequest = buildCreateTokenRequest();
//      HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
//      Optional<String> errorHeader = Optional.of("Token 'test' already exists");
//      HttpHeaders mockHttpHeaders = mock(HttpHeaders.class);
//
//      doReturn(mockHttpHeaders).when(mockHttpResponse).headers();
//      doReturn(errorHeader).when(mockHttpHeaders).firstValue(REDUCT_ERROR_HEADER);
//      doReturn(409).when(mockHttpResponse).statusCode();
//      doReturn(mockHttpResponse).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.ofString());
//
//      ReductException reductException = assertThrows(ReductException.class,
//              () -> reductTokenClient.createToken(TOKEN_NAME,
//                      TokenPermissions.of(true, List.of("test-bucket"), List.of("test-bucket"))));
//
//      assertEquals("Token 'test' already exists", reductException.getMessage());
//      assertEquals(409, reductException.getStatusCode());
//   }
//
//   @Test
//   void createToken_serverReturnsInvalidJson_throwException() throws IOException, InterruptedException {
//      HttpRequest httpRequest = buildCreateTokenRequest();
//      HttpResponse<String> httpResponse = mock(HttpResponse.class);
//      doReturn(200).when(httpResponse).statusCode();
//      doReturn("{{}").when(httpResponse).body();
//      doReturn(httpResponse).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.ofString());
//
//      ReductSDKException reductException = assertThrows(ReductSDKException.class,
//              () -> reductTokenClient.createToken(TOKEN_NAME,
//                      TokenPermissions.of(true, List.of("test-bucket"), List.of("test-bucket"))));
//
//      assertEquals("The server returned a malformed response.",
//              reductException.getMessage());
//   }
//
//   @Test
//   void createToken_ioExceptionOccurs_throwException() throws IOException, InterruptedException {
//      HttpRequest httpRequest = buildCreateTokenRequest();
//      doThrow(IOException.class).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.ofString());
//      ReductSDKException result = assertThrows(ReductSDKException.class, () -> reductTokenClient.createToken(TOKEN_NAME,
//              TokenPermissions.of(true, List.of("test-bucket"), List.of("test-bucket"))));
//
//      assertEquals("An error occurred while processing the request", result.getMessage());
//   }
//
//   @Test
//   void createToken_threadInterrupted_throwException() throws IOException, InterruptedException {
//      HttpRequest httpRequest = buildCreateTokenRequest();
//      doThrow(InterruptedException.class).when(httpClient).send(httpRequest, HttpResponse.BodyHandlers.ofString());
//      ReductSDKException result = assertThrows(ReductSDKException.class,
//              () -> reductTokenClient.createToken(TOKEN_NAME,
//                      TokenPermissions.of(true, List.of("test-bucket"), List.of("test-bucket"))));
//
//      assertEquals("Thread has been interrupted while processing the request", result.getMessage());
//   }
//
//   @Test
//   void createToken_tokenNameIsNull_throwException() {
//      assertThrows(IllegalArgumentException.class, () -> reductTokenClient.createToken(null,
//              TokenPermissions.of(true, List.of("test-bucket"), List.of("test-bucket"))));
//   }
//
//   @Test
//   void createToken_tokenNameIsEmpty_throwException() {
//      assertThrows(IllegalArgumentException.class, () -> reductTokenClient.createToken("",
//              TokenPermissions.of(true, List.of("test-bucket"), List.of("test-bucket"))));
//   }
//
//   @Test
//   void createToken_permissionsObjectIsNull_throwException() {
//      assertThrows(IllegalArgumentException.class, () -> reductTokenClient.createToken(TOKEN_NAME, null));
//   }
//
//   @Test
//   void getTokensValidDetails() throws IOException, InterruptedException {
//      //Init
//      String body = "{\"tokens\": []}";
//      HttpRequest httpRequest = createHttpGetRequest(TokenURL.GET_TOKENS);
//      HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
//      when(mockHttpResponse.statusCode()).thenReturn(HttpStatus.OK.getCode());
//      when(httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenReturn(mockHttpResponse);
//      when(mockHttpResponse.statusCode()).thenReturn(200);
//      when(mockHttpResponse.body()).thenReturn(body);
//      //Act
//      AccessTokens tokens = reductTokenClient.getTokens();
//      //Assert
//      assertNotNull(tokens);
//      assertTrue(tokens.tokens().isEmpty());
//   }
//   @Test
//   void getTokensIoExceptionOccursThrowException() throws IOException, InterruptedException {
//      //Init
//      HttpRequest httpRequest = createHttpGetRequest(TokenURL.GET_TOKENS);
//      when(httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenThrow(IOException.class);
//      //Act
//      ReductSDKException result = assertThrows(ReductSDKException.class, () -> reductTokenClient.getTokens());
//      //Assert
//      assertEquals("An error occurred while processing the request", result.getMessage());
//   }
//   @Test
//   void getTokensThreadInterruptedThrowException() throws IOException, InterruptedException {
//      //Init
//      HttpRequest httpRequest = createHttpGetRequest(TokenURL.GET_TOKENS);
//      when(httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenThrow(InterruptedException.class);
//      //Act
//      ReductSDKException result = assertThrows(ReductSDKException.class, () -> reductTokenClient.getTokens());
//      //Assert
//      assertEquals("Thread has been interrupted while processing the request", result.getMessage());
//   }
//   @Test
//   void getTokensInvalidTokenThrowException() throws IOException, InterruptedException {
//      //Init
//      HttpRequest httpRequest = createHttpGetRequest(TokenURL.GET_TOKENS);
//      HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
//      Optional<String> errorHeader = Optional.of("Invalid token");
//      HttpHeaders mockHttpHeaders = mock(HttpHeaders.class);
//
//      when(mockHttpResponse.headers()).thenReturn(mockHttpHeaders);
//      when(mockHttpHeaders.firstValue(REDUCT_ERROR_HEADER)).thenReturn(errorHeader);
//      when(mockHttpResponse.statusCode()).thenReturn(401);
//      when(httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenReturn(mockHttpResponse);
//      //Act
//      ReductException result = assertThrows(ReductException.class, () -> reductTokenClient.getTokens());
//      //Assert
//      assertEquals("Invalid token", result.getMessage());
//      assertEquals(401, result.getStatusCode());
//   }
//
//   private HttpRequest buildCreateTokenRequest() {
//      String createTokenPath = TokenURL.CREATE_TOKEN.getUrl().formatted(TOKEN_NAME);
//      URI createTokenUri = URI.create("%s/%s".formatted(serverProperties.getBaseUrl(), createTokenPath));
//      return HttpRequest.newBuilder()
//              .uri(createTokenUri)
//              .POST(HttpRequest.BodyPublishers.ofString(TokenExamples.CREATE_TOKEN_REQUEST_BODY))
//              .header("Authorization", "Bearer %s".formatted(accessToken))
//              .build();
//   }
//   private HttpRequest createHttpGetRequest(TokenURL url) {
//      return createHttp(url).GET().build();
//   }
//   private HttpRequest.Builder createHttp(TokenURL url) {
//      return HttpRequest.newBuilder()
//              .uri(URI.create("%s/%s".formatted(serverProperties.getBaseUrl(), url.getUrl())))
//              .header("Authorization", "Bearer %s".formatted(accessToken));
//   }
}