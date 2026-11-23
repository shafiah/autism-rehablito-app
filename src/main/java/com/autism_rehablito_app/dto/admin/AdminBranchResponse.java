package com.autism_rehablito_app.dto.admin;

public class AdminBranchResponse {

    private Long id;
    private String branchName;
    private String address;
    private Boolean active;

    public AdminBranchResponse() {
    }

    public AdminBranchResponse(
            Long id,
            String branchName,
            String address,
            Boolean active) {

        this.id = id;
        this.branchName = branchName;
        this.address = address;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getAddress() {
        return address;
    }

    public Boolean getActive() {
        return active;
    }
}