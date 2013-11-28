package com.michaelfitzmaurice;

/**
 * Simple bean to represent a person.
 * 
 * @author Michael Fitzmaurice, November 2013
 */
public class Person {
    
    private String fullName;
    private String title;
    private String location;
    private String floor;
    private String desk;
    private String telephoneNumber;
    private String emailAddress;
    private String department;
    private String manager;
    private String managerLevel;
    private String username;
    
    public Person() {
        super();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getDesk() {
        return desk;
    }

    public void setDesk(String desk) {
        this.desk = desk;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
    
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getManagerLevel() {
        return managerLevel;
    }

    public void setManagerLevel(String managerLevel) {
        this.managerLevel = managerLevel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
        + ((department == null) ? 0 : department.hashCode());
        result = prime * result + ((desk == null) ? 0 : desk.hashCode());
        result = prime * result
        + ((emailAddress == null) ? 0 : emailAddress.hashCode());
        result = prime * result + ((floor == null) ? 0 : floor.hashCode());
        result = prime * result
        + ((fullName == null) ? 0 : fullName.hashCode());
        result = prime * result
        + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((manager == null) ? 0 : manager.hashCode());
        result = prime * result
        + ((managerLevel == null) ? 0 : managerLevel.hashCode());
        result = prime * result
        + ((telephoneNumber == null) ? 0 : telephoneNumber.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result
        + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Person other = (Person) obj;
        if (department == null) {
            if (other.department != null)
                return false;
        } else if (!department.equals(other.department))
            return false;
        if (desk == null) {
            if (other.desk != null)
                return false;
        } else if (!desk.equals(other.desk))
            return false;
        if (emailAddress == null) {
            if (other.emailAddress != null)
                return false;
        } else if (!emailAddress.equals(other.emailAddress))
            return false;
        if (floor == null) {
            if (other.floor != null)
                return false;
        } else if (!floor.equals(other.floor))
            return false;
        if (fullName == null) {
            if (other.fullName != null)
                return false;
        } else if (!fullName.equals(other.fullName))
            return false;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;
        if (manager == null) {
            if (other.manager != null)
                return false;
        } else if (!manager.equals(other.manager))
            return false;
        if (managerLevel == null) {
            if (other.managerLevel != null)
                return false;
        } else if (!managerLevel.equals(other.managerLevel))
            return false;
        if (telephoneNumber == null) {
            if (other.telephoneNumber != null)
                return false;
        } else if (!telephoneNumber.equals(other.telephoneNumber))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Person [fullName=" + fullName + ", title=" + title
        + ", location=" + location + ", floor=" + floor + ", desk=" + desk
        + ", telephoneNumber=" + telephoneNumber + ", emailAddress="
        + emailAddress + ", department=" + department + ", manager=" + manager
        + ", managerLevel=" + managerLevel + ", username=" + username + "]";
    }
    
}
