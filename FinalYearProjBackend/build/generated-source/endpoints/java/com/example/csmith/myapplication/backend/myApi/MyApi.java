/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2015-03-26 20:30:19 UTC)
 * on 2015-04-25 at 16:48:24 UTC 
 * Modify at your own risk.
 */

package com.example.csmith.myapplication.backend.myApi;

/**
 * Service definition for MyApi (v1).
 *
 * <p>
 * This is an API
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link MyApiRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class MyApi extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.20.0 of the myApi library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
  }

  /**
   * The default encoded root URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_ROOT_URL = "https://myApplicationId.appspot.com/_ah/api/";

  /**
   * The default encoded service path of the service. This is determined when the library is
   * generated and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_SERVICE_PATH = "myApi/v1/";

  /**
   * The default encoded base URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   */
  public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

  /**
   * Constructor.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport HTTP transport, which should normally be:
   *        <ul>
   *        <li>Google App Engine:
   *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
   *        <li>Android: {@code newCompatibleTransport} from
   *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
   *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
   *        </li>
   *        </ul>
   * @param jsonFactory JSON factory, which may be:
   *        <ul>
   *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
   *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
   *        <li>Android Honeycomb or higher:
   *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
   *        </ul>
   * @param httpRequestInitializer HTTP request initializer or {@code null} for none
   * @since 1.7
   */
  public MyApi(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  MyApi(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * Create a request for the method "getAllLocations".
   *
   * This request holds the parameters needed by the myApi server.  After setting any optional
   * parameters, call the {@link GetAllLocations#execute()} method to invoke the remote operation.
   *
   * @return the request
   */
  public GetAllLocations getAllLocations() throws java.io.IOException {
    GetAllLocations result = new GetAllLocations();
    initialize(result);
    return result;
  }

  public class GetAllLocations extends MyApiRequest<com.example.csmith.myapplication.backend.myApi.model.MyRTreeBean> {

    private static final String REST_PATH = "myrtreebean";

    /**
     * Create a request for the method "getAllLocations".
     *
     * This request holds the parameters needed by the the myApi server.  After setting any optional
     * parameters, call the {@link GetAllLocations#execute()} method to invoke the remote operation.
     * <p> {@link GetAllLocations#initialize(com.google.api.client.googleapis.services.AbstractGoogleC
     * lientRequest)} must be called to initialize this instance immediately after invoking the
     * constructor. </p>
     *
     * @since 1.13
     */
    protected GetAllLocations() {
      super(MyApi.this, "GET", REST_PATH, null, com.example.csmith.myapplication.backend.myApi.model.MyRTreeBean.class);
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public GetAllLocations setAlt(java.lang.String alt) {
      return (GetAllLocations) super.setAlt(alt);
    }

    @Override
    public GetAllLocations setFields(java.lang.String fields) {
      return (GetAllLocations) super.setFields(fields);
    }

    @Override
    public GetAllLocations setKey(java.lang.String key) {
      return (GetAllLocations) super.setKey(key);
    }

    @Override
    public GetAllLocations setOauthToken(java.lang.String oauthToken) {
      return (GetAllLocations) super.setOauthToken(oauthToken);
    }

    @Override
    public GetAllLocations setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetAllLocations) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetAllLocations setQuotaUser(java.lang.String quotaUser) {
      return (GetAllLocations) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetAllLocations setUserIp(java.lang.String userIp) {
      return (GetAllLocations) super.setUserIp(userIp);
    }

    @Override
    public GetAllLocations set(String parameterName, Object value) {
      return (GetAllLocations) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "getLocation".
   *
   * This request holds the parameters needed by the myApi server.  After setting any optional
   * parameters, call the {@link GetLocation#execute()} method to invoke the remote operation.
   *
   * @param facebookId
   * @return the request
   */
  public GetLocation getLocation(java.lang.String facebookId) throws java.io.IOException {
    GetLocation result = new GetLocation(facebookId);
    initialize(result);
    return result;
  }

  public class GetLocation extends MyApiRequest<com.example.csmith.myapplication.backend.myApi.model.MyRTreeBean> {

    private static final String REST_PATH = "myrtreebean/{facebookId}";

    /**
     * Create a request for the method "getLocation".
     *
     * This request holds the parameters needed by the the myApi server.  After setting any optional
     * parameters, call the {@link GetLocation#execute()} method to invoke the remote operation. <p>
     * {@link
     * GetLocation#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param facebookId
     * @since 1.13
     */
    protected GetLocation(java.lang.String facebookId) {
      super(MyApi.this, "GET", REST_PATH, null, com.example.csmith.myapplication.backend.myApi.model.MyRTreeBean.class);
      this.facebookId = com.google.api.client.util.Preconditions.checkNotNull(facebookId, "Required parameter facebookId must be specified.");
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public GetLocation setAlt(java.lang.String alt) {
      return (GetLocation) super.setAlt(alt);
    }

    @Override
    public GetLocation setFields(java.lang.String fields) {
      return (GetLocation) super.setFields(fields);
    }

    @Override
    public GetLocation setKey(java.lang.String key) {
      return (GetLocation) super.setKey(key);
    }

    @Override
    public GetLocation setOauthToken(java.lang.String oauthToken) {
      return (GetLocation) super.setOauthToken(oauthToken);
    }

    @Override
    public GetLocation setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetLocation) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetLocation setQuotaUser(java.lang.String quotaUser) {
      return (GetLocation) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetLocation setUserIp(java.lang.String userIp) {
      return (GetLocation) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String facebookId;

    /**

     */
    public java.lang.String getFacebookId() {
      return facebookId;
    }

    public GetLocation setFacebookId(java.lang.String facebookId) {
      this.facebookId = facebookId;
      return this;
    }

    @Override
    public GetLocation set(String parameterName, Object value) {
      return (GetLocation) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "updateLocation".
   *
   * This request holds the parameters needed by the myApi server.  After setting any optional
   * parameters, call the {@link UpdateLocation#execute()} method to invoke the remote operation.
   *
   * @param fbId
   * @param lng
   * @param lat
   * @param date
   * @return the request
   */
  public UpdateLocation updateLocation(java.lang.String fbId, java.lang.Double lng, java.lang.Double lat, com.google.api.client.util.DateTime date) throws java.io.IOException {
    UpdateLocation result = new UpdateLocation(fbId, lng, lat, date);
    initialize(result);
    return result;
  }

  public class UpdateLocation extends MyApiRequest<Void> {

    private static final String REST_PATH = "void/{fbId}/{Lng}/{Lat}/{Date}";

    /**
     * Create a request for the method "updateLocation".
     *
     * This request holds the parameters needed by the the myApi server.  After setting any optional
     * parameters, call the {@link UpdateLocation#execute()} method to invoke the remote operation.
     * <p> {@link UpdateLocation#initialize(com.google.api.client.googleapis.services.AbstractGoogleCl
     * ientRequest)} must be called to initialize this instance immediately after invoking the
     * constructor. </p>
     *
     * @param fbId
     * @param lng
     * @param lat
     * @param date
     * @since 1.13
     */
    protected UpdateLocation(java.lang.String fbId, java.lang.Double lng, java.lang.Double lat, com.google.api.client.util.DateTime date) {
      super(MyApi.this, "PUT", REST_PATH, null, Void.class);
      this.fbId = com.google.api.client.util.Preconditions.checkNotNull(fbId, "Required parameter fbId must be specified.");
      this.lng = com.google.api.client.util.Preconditions.checkNotNull(lng, "Required parameter lng must be specified.");
      this.lat = com.google.api.client.util.Preconditions.checkNotNull(lat, "Required parameter lat must be specified.");
      this.date = com.google.api.client.util.Preconditions.checkNotNull(date, "Required parameter date must be specified.");
    }

    @Override
    public UpdateLocation setAlt(java.lang.String alt) {
      return (UpdateLocation) super.setAlt(alt);
    }

    @Override
    public UpdateLocation setFields(java.lang.String fields) {
      return (UpdateLocation) super.setFields(fields);
    }

    @Override
    public UpdateLocation setKey(java.lang.String key) {
      return (UpdateLocation) super.setKey(key);
    }

    @Override
    public UpdateLocation setOauthToken(java.lang.String oauthToken) {
      return (UpdateLocation) super.setOauthToken(oauthToken);
    }

    @Override
    public UpdateLocation setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (UpdateLocation) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public UpdateLocation setQuotaUser(java.lang.String quotaUser) {
      return (UpdateLocation) super.setQuotaUser(quotaUser);
    }

    @Override
    public UpdateLocation setUserIp(java.lang.String userIp) {
      return (UpdateLocation) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String fbId;

    /**

     */
    public java.lang.String getFbId() {
      return fbId;
    }

    public UpdateLocation setFbId(java.lang.String fbId) {
      this.fbId = fbId;
      return this;
    }

    @com.google.api.client.util.Key("Lng")
    private java.lang.Double lng;

    /**

     */
    public java.lang.Double getLng() {
      return lng;
    }

    public UpdateLocation setLng(java.lang.Double lng) {
      this.lng = lng;
      return this;
    }

    @com.google.api.client.util.Key("Lat")
    private java.lang.Double lat;

    /**

     */
    public java.lang.Double getLat() {
      return lat;
    }

    public UpdateLocation setLat(java.lang.Double lat) {
      this.lat = lat;
      return this;
    }

    @com.google.api.client.util.Key("Date")
    private com.google.api.client.util.DateTime date;

    /**

     */
    public com.google.api.client.util.DateTime getDate() {
      return date;
    }

    public UpdateLocation setDate(com.google.api.client.util.DateTime date) {
      this.date = date;
      return this;
    }

    @Override
    public UpdateLocation set(String parameterName, Object value) {
      return (UpdateLocation) super.set(parameterName, value);
    }
  }

  /**
   * Builder for {@link MyApi}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   *
   * @since 1.3.0
   */
  public static final class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {

    /**
     * Returns an instance of a new builder.
     *
     * @param transport HTTP transport, which should normally be:
     *        <ul>
     *        <li>Google App Engine:
     *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
     *        <li>Android: {@code newCompatibleTransport} from
     *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
     *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
     *        </li>
     *        </ul>
     * @param jsonFactory JSON factory, which may be:
     *        <ul>
     *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
     *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
     *        <li>Android Honeycomb or higher:
     *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
     *        </ul>
     * @param httpRequestInitializer HTTP request initializer or {@code null} for none
     * @since 1.7
     */
    public Builder(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
        com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      super(
          transport,
          jsonFactory,
          DEFAULT_ROOT_URL,
          DEFAULT_SERVICE_PATH,
          httpRequestInitializer,
          false);
    }

    /** Builds a new instance of {@link MyApi}. */
    @Override
    public MyApi build() {
      return new MyApi(this);
    }

    @Override
    public Builder setRootUrl(String rootUrl) {
      return (Builder) super.setRootUrl(rootUrl);
    }

    @Override
    public Builder setServicePath(String servicePath) {
      return (Builder) super.setServicePath(servicePath);
    }

    @Override
    public Builder setHttpRequestInitializer(com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      return (Builder) super.setApplicationName(applicationName);
    }

    @Override
    public Builder setSuppressPatternChecks(boolean suppressPatternChecks) {
      return (Builder) super.setSuppressPatternChecks(suppressPatternChecks);
    }

    @Override
    public Builder setSuppressRequiredParameterChecks(boolean suppressRequiredParameterChecks) {
      return (Builder) super.setSuppressRequiredParameterChecks(suppressRequiredParameterChecks);
    }

    @Override
    public Builder setSuppressAllChecks(boolean suppressAllChecks) {
      return (Builder) super.setSuppressAllChecks(suppressAllChecks);
    }

    /**
     * Set the {@link MyApiRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setMyApiRequestInitializer(
        MyApiRequestInitializer myapiRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(myapiRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}
