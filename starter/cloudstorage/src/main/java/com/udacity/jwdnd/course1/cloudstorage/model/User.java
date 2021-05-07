package com.udacity.jwdnd.course1.cloudstorage.model;

public class User {
    private String username, password, salt, firstName, lastName;
    private Integer userId;

    public User(Integer userId, String username, String salt, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    @Override
    public String toString(){
        return "Username: "+ username+" Password: "+ password+ " FirstName: "+ firstName+" LastName: "+lastName+" Salt: "+ salt;
    }
}
