package com.sunrise.interview.interviewspot.enity;

/**
 * Created by donnv on 7/3/2015.
 */
public class JobsNearbyEnity {

    public JobsNearbyEnity() {

    }

    public JobsNearbyEnity(String comapny, String km, String address, String role, String salary, String logo) {
        this.comapny = comapny;
        this.km = km;
        this.address = address;
        this.role = role;
        this.salary = salary;
        this.logo = logo;
    }

    private String comapny;
    private String km;
    private String role;
    private String address;
    private String salary;
    private String logo;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getComapny() {
        return comapny;
    }

    public void setComapny(String comapny) {
        this.comapny = comapny;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
