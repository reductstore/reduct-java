package org.reduct.model.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class TokenPermissions {

   @JsonProperty("full_access")
   private Boolean fullAccess;

   @JsonProperty("read")
   private List<String> read;

   @JsonProperty("write")
   private List<String> write;
}
