package org.egov.product.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* ProductSearch
*/
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2022-12-02T16:45:24.641+05:30")

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSearch   {
        @JsonProperty("id")
    


    private String id = null;

        @JsonProperty("tenantId")
    


    private String tenantId = null;

        @JsonProperty("type")
    


    private String type = null;

        @JsonProperty("name")
    


    private String name = null;

        @JsonProperty("manufacturer")
    


    private String manufacturer = null;


}

