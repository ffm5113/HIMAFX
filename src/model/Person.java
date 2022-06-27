
package model;

import java.io.Serializable;

abstract class Person implements Serializable{ // Parent to Account
    private String firstName;
    private String lastName;
    private String userEmail;
    
    public Person() {}
    
    public Person(String firstName, String lastName, String userEmail) {
        this. firstName = firstName;
        this.lastName = lastName;
        this.userEmail = userEmail;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
