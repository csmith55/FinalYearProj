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
 * on 2015-04-07 at 12:08:53 UTC 
 * Modify at your own risk.
 */

package com.example.csmith.myapplication.backend.myApi.model;

/**
 * Model definition for MyRTreeBean.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the myApi. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class MyRTreeBean extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<LocationDetails> data;

  static {
    // hack to force ProGuard to consider LocationDetails used, since otherwise it would be stripped out
    // see http://code.google.com/p/google-api-java-client/issues/detail?id=528
    com.google.api.client.util.Data.nullOf(LocationDetails.class);
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<LocationDetails> getData() {
    return data;
  }

  /**
   * @param data data or {@code null} for none
   */
  public MyRTreeBean setData(java.util.List<LocationDetails> data) {
    this.data = data;
    return this;
  }

  @Override
  public MyRTreeBean set(String fieldName, Object value) {
    return (MyRTreeBean) super.set(fieldName, value);
  }

  @Override
  public MyRTreeBean clone() {
    return (MyRTreeBean) super.clone();
  }

}
