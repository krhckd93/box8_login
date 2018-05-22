package com.box8.login.models;

public class User {

    private String email;
    private String phone;
    private String password;
    private String first_name;
    private String last_name;

    // Getters
    public String getEmail() { return email; }

    public String getPhone() { return phone; }

    public String getPassword() { return password; }

    public String getFirst_name() { return first_name; }

    public String getLast_name() { return last_name; }

    // Setters
    public void setEmail(String email) { this.email = email; }

    public void setPhone(String phone) { this.phone = phone; }

    public void setPassword(String password) { this.password = password; }

    public void setFirstName(String first_name) { this.first_name = first_name; }

    public void setLastName(String last_name) { this.last_name = last_name; }

}
