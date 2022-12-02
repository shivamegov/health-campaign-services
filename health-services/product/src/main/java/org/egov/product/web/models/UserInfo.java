package org.egov.product.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.request.Role;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * This is acting ID token of the authenticated user on the server. Any value provided by the clients will be ignored and actual user based on authtoken will be used on the server.
 */
@ApiModel(description = "This is acting ID token of the authenticated user on the server. Any value provided by the clients will be ignored and actual user based on authtoken will be used on the server.")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2022-12-02T16:45:24.641+05:30")

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {
    @JsonProperty("tenantId")
    @NotNull


    private String tenantId = null;

    @JsonProperty("uuid")


    private String uuid = null;

    @JsonProperty("userName")
    @NotNull


    private String userName = null;

    @JsonProperty("password")


    private String password = null;

    @JsonProperty("idToken")


    private String idToken = null;

    @JsonProperty("mobile")


    private String mobile = null;

    @JsonProperty("email")


    private String email = null;

    @JsonProperty("primaryrole")
    @NotNull

    @Valid


    private List<Role> primaryrole = new ArrayList<>();

    @JsonProperty("additionalroles")

    @Valid


    private List<TenantRole> additionalroles = null;


    public UserInfo addPrimaryroleItem(Role primaryroleItem) {
        this.primaryrole.add(primaryroleItem);
        return this;
    }

    public UserInfo addAdditionalrolesItem(TenantRole additionalrolesItem) {
        if (this.additionalroles == null) {
            this.additionalroles = new ArrayList<>();
        }
        this.additionalroles.add(additionalrolesItem);
        return this;
    }

}

