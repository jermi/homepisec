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


package org.homepisec.android.homepisecapp.control.rest.client.model;

import java.util.Objects;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import org.homepisec.android.homepisecapp.control.rest.client.model.Device;
import org.joda.time.DateTime;
import android.os.Parcelable;
import android.os.Parcel;

/**
 * DeviceEvent
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-10-15T21:11:19.348+02:00")
public class DeviceEvent implements Parcelable {
  @SerializedName("device")
  private Device device = null;

  @SerializedName("payload")
  private String payload = null;

  @SerializedName("time")
  private DateTime time = null;

  /**
   * Gets or Sets type
   */
  @JsonAdapter(TypeEnum.Adapter.class)
  public enum TypeEnum {
    DEVICE_READ("DEVICE_READ"),
    
    ALARM_DISARM("ALARM_DISARM"),
    
    ALARM_ARM("ALARM_ARM"),
    
    ALARM_COUNTDOWN("ALARM_COUNTDOWN"),
    
    ALARM_TRIGGER("ALARM_TRIGGER");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static TypeEnum fromValue(String text) {
      for (TypeEnum b : TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public static class Adapter extends TypeAdapter<TypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final TypeEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public TypeEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return TypeEnum.fromValue(String.valueOf(value));
      }
    }
  }

  @SerializedName("type")
  private TypeEnum type = null;

  public DeviceEvent device(Device device) {
    this.device = device;
    return this;
  }

   /**
   * Get device
   * @return device
  **/
  @ApiModelProperty(value = "")
  public Device getDevice() {
    return device;
  }

  public void setDevice(Device device) {
    this.device = device;
  }

  public DeviceEvent payload(String payload) {
    this.payload = payload;
    return this;
  }

   /**
   * Get payload
   * @return payload
  **/
  @ApiModelProperty(value = "")
  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }

  public DeviceEvent time(DateTime time) {
    this.time = time;
    return this;
  }

   /**
   * Get time
   * @return time
  **/
  @ApiModelProperty(value = "")
  public DateTime getTime() {
    return time;
  }

  public void setTime(DateTime time) {
    this.time = time;
  }

  public DeviceEvent type(TypeEnum type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @ApiModelProperty(value = "")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceEvent deviceEvent = (DeviceEvent) o;
    return Objects.equals(this.device, deviceEvent.device) &&
        Objects.equals(this.payload, deviceEvent.payload) &&
        Objects.equals(this.time, deviceEvent.time) &&
        Objects.equals(this.type, deviceEvent.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(device, payload, time, type);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeviceEvent {\n");
    
    sb.append("    device: ").append(toIndentedString(device)).append("\n");
    sb.append("    payload: ").append(toIndentedString(payload)).append("\n");
    sb.append("    time: ").append(toIndentedString(time)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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
  
  public void writeToParcel(Parcel out, int flags) {
     
    out.writeValue(device);

    out.writeValue(payload);

    out.writeValue(time);

    out.writeValue(type);
  }

  public DeviceEvent() {
    super();
  }

  DeviceEvent(Parcel in) {
    
    device = (Device)in.readValue(Device.class.getClassLoader());
    payload = (String)in.readValue(null);
    time = (DateTime)in.readValue(DateTime.class.getClassLoader());
    type = (TypeEnum)in.readValue(null);
  }
  
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<DeviceEvent> CREATOR = new Parcelable.Creator<DeviceEvent>() {
    public DeviceEvent createFromParcel(Parcel in) {
      return new DeviceEvent(in);
    }
    public DeviceEvent[] newArray(int size) {
      return new DeviceEvent[size];
    }
  };
}

