package store.reduct.client;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@Disabled
@ExtendWith(MockitoExtension.class)
class ReductServerClientTest {
//
//   private static final String REDUCT_ERROR_HEADER = "x-reduct-error";
//
//   @Spy
//   private ServerProperties serverProperties = new ServerProperties(false, "localhost", 8383);
//   @Mock
//   private HttpClient httpClient;
//   @InjectMocks
//   @Spy
//   private ReductServerClient reductServerClient;
//   private String accessToken = "testToken";
//
//   @BeforeEach
//   public void setup() {
//      doReturn(accessToken).when(reductServerClient).getToken();
//   }
//
//   @Test
//   void getServerInfo_validDetails_returnServerInfo() throws IOException, InterruptedException {
//      //Init
//      HttpRequest httpRequest = createHttpGetRequest(ServerURL.SERVER_INFO);
//      HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
//      when(httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenReturn(mockHttpResponse);
//      when(mockHttpResponse.statusCode()).thenReturn(200);
//      when(mockHttpResponse.body()).thenReturn(ResponseExamples.SUCCESSFUL_SERVER_INFO_RESPONSE);
//      //Act
//      ServerInfo result = reductServerClient.getServerInfo();
//      //Assert
//      assertNotNull(result);
//      assertEquals(sampleServerInfo(), result);
//   }
//   @Test
//   void getServerInfo_serverReturnsMalformedJson_throwException() throws IOException, InterruptedException {
//      //Init
//      HttpRequest httpRequest = createHttpGetRequest(ServerURL.SERVER_INFO);
//      HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
//      when(mockHttpResponse.statusCode()).thenReturn(200);
//      when(mockHttpResponse.body()).thenReturn("{{}");
//      when(httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenReturn(mockHttpResponse);
//      //Act
//      ReductSDKException result = assertThrows(ReductSDKException.class, () -> reductServerClient.getServerInfo());
//      //Assert
//      assertEquals("The server returned a malformed response.", result.getMessage());
//   }
//   @Test
//   void getServerInfo_ioExceptionOccurs_throwException() throws IOException, InterruptedException {
//      //Init
//      HttpRequest httpRequest = createHttpGetRequest(ServerURL.SERVER_INFO);
//      when(httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenThrow(IOException.class);
//      //Act
//      ReductSDKException result = assertThrows(ReductSDKException.class, () -> reductServerClient.getServerInfo());
//      //Assert
//      assertEquals("An error occurred while processing the request", result.getMessage());
//   }
//   @Test
//   void getServerInfo_threadInterrupted_throwException() throws IOException, InterruptedException {
//      //Init
//      HttpRequest httpRequest = createHttpGetRequest(ServerURL.SERVER_INFO);
//      when(httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenThrow(InterruptedException.class);
//      //Act
//      ReductSDKException result = assertThrows(ReductSDKException.class, () -> reductServerClient.getServerInfo());
//      //Assert
//      assertEquals("Thread has been interrupted while processing the request", result.getMessage());
//   }
//   @Test
//   void getServerInfo_invalidToken_throwException() throws IOException, InterruptedException {
//      //Init
//      HttpRequest httpRequest = createHttpGetRequest(ServerURL.SERVER_INFO);
//      HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
//      Optional<String> errorHeader = Optional.of("Invalid token");
//      HttpHeaders mockHttpHeaders = mock(HttpHeaders.class);
//
//      when(mockHttpResponse.headers()).thenReturn(mockHttpHeaders);
//      when(mockHttpHeaders.firstValue(REDUCT_ERROR_HEADER)).thenReturn(errorHeader);
//      when(mockHttpResponse.statusCode()).thenReturn(401);
//      when(httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenReturn(mockHttpResponse);
//      //Act
//      ReductException result = assertThrows(ReductException.class, () -> reductServerClient.getServerInfo());
//      //Assert
//      assertEquals("Invalid token", result.getMessage());
//      assertEquals(401, result.getStatusCode());
//   }
//   @Test
//   void getList_validDetails_returnServerInfo() throws IOException, InterruptedException {
//      //Init
//      HttpRequest httpRequest = createHttpGetRequest(ServerURL.LIST);
//      HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
//      when(httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenReturn(mockHttpResponse);
//      when(mockHttpResponse.statusCode()).thenReturn(200);
//      when(mockHttpResponse.body()).thenReturn(ResponseExamples.SUCCESSFUL_LIST_RESPONSE);
//      //Act
//      Buckets result = reductServerClient.getList();
//      //Assert
//      assertNotNull(result);
//      assertEquals(sampleList(), result);
//   }
//   @Test
//   void getList_serverReturnsMalformedJson_throwException() throws IOException, InterruptedException {
//      //Init
//      HttpRequest httpRequest = createHttpGetRequest(ServerURL.LIST);
//      HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
//      when(mockHttpResponse.statusCode()).thenReturn(200);
//      when(mockHttpResponse.body()).thenReturn("{{}");
//      when(httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenReturn(mockHttpResponse);
//      //Act
//      ReductSDKException result = assertThrows(ReductSDKException.class, () -> reductServerClient.getList());
//      //Assert
//      assertEquals("The server returned a malformed response.", result.getMessage());
//   }
//   @Test
//   void getList_ioExceptionOccurs_throwException() throws IOException, InterruptedException {
//      //Init
//      HttpRequest httpRequest = createHttpGetRequest(ServerURL.LIST);
//      when(httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenThrow(IOException.class);
//      //Act
//      ReductSDKException result = assertThrows(ReductSDKException.class, () -> reductServerClient.getList());
//      //Assert
//      assertEquals("An error occurred while processing the request", result.getMessage());
//   }
//   @Test
//   void getList_threadInterrupted_throwException() throws IOException, InterruptedException {
//      //Init
//      HttpRequest httpRequest = createHttpGetRequest(ServerURL.LIST);
//      when(httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenThrow(InterruptedException.class);
//      //Act
//      ReductSDKException result = assertThrows(ReductSDKException.class, () -> reductServerClient.getList());
//      //Assert
//      assertEquals("Thread has been interrupted while processing the request", result.getMessage());
//   }
//   @Test
//   void getList_invalidToken_throwException() throws IOException, InterruptedException {
//      //Init
//      HttpRequest httpRequest = createHttpGetRequest(ServerURL.LIST);
//      HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
//      Optional<String> errorHeader = Optional.of("Invalid token");
//      HttpHeaders mockHttpHeaders = mock(HttpHeaders.class);
//
//      when(mockHttpResponse.headers()).thenReturn(mockHttpHeaders);
//      when(mockHttpHeaders.firstValue(REDUCT_ERROR_HEADER)).thenReturn(errorHeader);
//      when(mockHttpResponse.statusCode()).thenReturn(401);
//      when(httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenReturn(mockHttpResponse);
//      //Act
//      ReductException result = assertThrows(ReductException.class, () -> reductServerClient.getList());
//      //Assert
//      assertEquals("Invalid token", result.getMessage());
//      assertEquals(401, result.getStatusCode());
//   }
//   @Test
//   void isAlive_validDetails() throws IOException, InterruptedException {
//      //Init
//      HttpRequest httpRequest = createHttpHeadRequest(ServerURL.ALIVE);
//      HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
//      when(mockHttpResponse.statusCode()).thenReturn(HttpStatus.OK.getCode());
//      when(httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenReturn(mockHttpResponse);
//      when(mockHttpResponse.statusCode()).thenReturn(200);
//      //Act
//      Boolean result = reductServerClient.isAlive();
//      //Assert
//      assertNotNull(result);
//      assertEquals(true, result);
//   }
//   @Test
//   void isAlive_ioExceptionOccurs_throwException() throws IOException, InterruptedException {
//      //Init
//      HttpRequest httpRequest = createHttpHeadRequest(ServerURL.ALIVE);
//      when(httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenThrow(IOException.class);
//      //Act
//      ReductSDKException result = assertThrows(ReductSDKException.class, () -> reductServerClient.isAlive());
//      //Assert
//      assertEquals("An error occurred while processing the request", result.getMessage());
//   }
//   @Test
//   void isAlive_threadInterrupted_throwException() throws IOException, InterruptedException {
//      //Init
//      HttpRequest httpRequest = createHttpHeadRequest(ServerURL.ALIVE);
//      when(httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenThrow(InterruptedException.class);
//      //Act
//      ReductSDKException result = assertThrows(ReductSDKException.class, () -> reductServerClient.isAlive());
//      //Assert
//      assertEquals("Thread has been interrupted while processing the request", result.getMessage());
//   }
//   @Test
//   void isAlive_invalidToken_throwException() throws IOException, InterruptedException {
//      //Init
//      HttpRequest httpRequest = createHttpHeadRequest(ServerURL.ALIVE);
//      HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
//      Optional<String> errorHeader = Optional.of("Invalid token");
//      HttpHeaders mockHttpHeaders = mock(HttpHeaders.class);
//
//      when(mockHttpResponse.headers()).thenReturn(mockHttpHeaders);
//      when(mockHttpHeaders.firstValue(REDUCT_ERROR_HEADER)).thenReturn(errorHeader);
//      when(mockHttpResponse.statusCode()).thenReturn(401);
//      when(httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenReturn(mockHttpResponse);
//      //Act
//      ReductException result = assertThrows(ReductException.class, () -> reductServerClient.isAlive());
//      //Assert
//      assertEquals("Invalid token", result.getMessage());
//      assertEquals(401, result.getStatusCode());
//   }
//   private HttpRequest createHttpGetRequest(ServerURL url) {
//      return createHttp(url).GET().build();
//   }
//   private HttpRequest createHttpHeadRequest(ServerURL url) {
//      return createHttp(url).method("HEAD", HttpRequest.BodyPublishers.noBody()).build();
//   }
//
//   private HttpRequest.Builder createHttp(ServerURL url) {
//      return HttpRequest.newBuilder()
//              .uri(URI.create("%s/%s".formatted(serverProperties.getBaseUrl(), url.getUrl())))
//              .header("Authorization", "Bearer %s".formatted(accessToken));
//   }
//   private ServerInfo sampleServerInfo() throws JsonProcessingException {
//      return new ObjectMapper().readValue(ResponseExamples.SUCCESSFUL_SERVER_INFO_RESPONSE, ServerInfo.class);
//   }
//
//   private Buckets sampleList() throws JsonProcessingException {
//      return new ObjectMapper().readValue(ResponseExamples.SUCCESSFUL_LIST_RESPONSE, Buckets.class);
//   }
}