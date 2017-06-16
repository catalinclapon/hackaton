package com.db.hackaton.service.dto;

/**
 * Created by Ungureanu Adrian on 16/06/2017.
 */
public class PatientDTO {
    private Integer flag;

    private String cnp;

    public int getFlag() {
        return flag;
    }

    public String getCnp() {
        return cnp;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }
}
