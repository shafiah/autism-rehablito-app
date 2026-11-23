
package com.autism_rehablito_app.dto.admin;

public class AdminParentOptionResponse {

    private Long parentId;
    private String name;
    private String email;
    private String mobileNumber;
    private Boolean active;
    private Long branchId;
    private String branchName;
    private Boolean childLinked;

    public AdminParentOptionResponse(
            Long parentId,
            String name,
            String email,
            String mobileNumber,
            Boolean active,
            Long branchId,
            String branchName,
            Boolean childLinked
    ) {
        this.parentId = parentId;
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.active = active;
        this.branchId = branchId;
        this.branchName = branchName;
        this.childLinked = childLinked;
    }

    public Long getParentId() {
        return parentId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public Boolean getActive() {
        return active;
    }

    public Long getBranchId() {
        return branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public Boolean getChildLinked() {
        return childLinked;
    }
}
