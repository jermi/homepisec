/*
 * Api Documentation
 * Api Documentation
 *
 * OpenAPI spec version: 1.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package org.homepisec.sensor.rest.client.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.homepisec.sensor.rest.client.model.Device;

/**
 * SensorAppEndpoint
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-03-04T09:14:48.656+01:00")
public class SensorAppEndpoint {
  @JsonProperty("alarmRelays")
  private List<Device> alarmRelays = null;

  @JsonProperty("relays")
  private List<Device> relays = null;

  @JsonProperty("url")
  private String url = null;

  public SensorAppEndpoint alarmRelays(List<Device> alarmRelays) {
    this.alarmRelays = alarmRelays;
    return this;
  }

  public SensorAppEndpoint addAlarmRelaysItem(Device alarmRelaysItem) {
    if (this.alarmRelays == null) {
      this.alarmRelays = new ArrayList<Device>();
    }
    this.alarmRelays.add(alarmRelaysItem);
    return this;
  }

   /**
   * Get alarmRelays
   * @return alarmRelays
  **/
  @ApiModelProperty(value = "")
  public List<Device> getAlarmRelays() {
    return alarmRelays;
  }

  public void setAlarmRelays(List<Device> alarmRelays) {
    this.alarmRelays = alarmRelays;
  }

  public SensorAppEndpoint relays(List<Device> relays) {
    this.relays = relays;
    return this;
  }

  public SensorAppEndpoint addRelaysItem(Device relaysItem) {
    if (this.relays == null) {
      this.relays = new ArrayList<Device>();
    }
    this.relays.add(relaysItem);
    return this;
  }

   /**
   * Get relays
   * @return relays
  **/
  @ApiModelProperty(value = "")
  public List<Device> getRelays() {
    return relays;
  }

  public void setRelays(List<Device> relays) {
    this.relays = relays;
  }

  public SensorAppEndpoint url(String url) {
    this.url = url;
    return this;
  }

   /**
   * Get url
   * @return url
  **/
  @ApiModelProperty(value = "")
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SensorAppEndpoint sensorAppEndpoint = (SensorAppEndpoint) o;
    return Objects.equals(this.alarmRelays, sensorAppEndpoint.alarmRelays) &&
        Objects.equals(this.relays, sensorAppEndpoint.relays) &&
        Objects.equals(this.url, sensorAppEndpoint.url);
  }

  @Override
  public int hashCode() {
    return Objects.hash(alarmRelays, relays, url);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SensorAppEndpoint {\n");
    
    sb.append("    alarmRelays: ").append(toIndentedString(alarmRelays)).append("\n");
    sb.append("    relays: ").append(toIndentedString(relays)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
  
}

