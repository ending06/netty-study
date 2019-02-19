package com.netty.authority.nio.netty.multiprotocol.http;

import java.util.List;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/18<p>
// -------------------------------------------------------

public class Customer {

    private long customerNumber;

    private String firstName;

    private String lastName;

    private List<String> middleNames;

    public long getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(long customerNumber) {
        this.customerNumber = customerNumber;
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

    public List<String> getMiddleNames() {
        return middleNames;
    }

    public void setMiddleNames(List<String> middleNames) {
        this.middleNames = middleNames;
    }
}