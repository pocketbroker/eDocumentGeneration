package com.pocketbroker.edocument.model;

/**
 * Represents a signer for an electronic document.
 */
public class Signer {
    private String email;
    private String name;
    private String clientUserId;
    private int routingOrder;

    public Signer() {
        this.routingOrder = 1;
    }

    public Signer(String email, String name) {
        this.email = email;
        this.name = name;
        this.routingOrder = 1;
    }

    public Signer(String email, String name, String clientUserId) {
        this.email = email;
        this.name = name;
        this.clientUserId = clientUserId;
        this.routingOrder = 1;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClientUserId() {
        return clientUserId;
    }

    public void setClientUserId(String clientUserId) {
        this.clientUserId = clientUserId;
    }

    public int getRoutingOrder() {
        return routingOrder;
    }

    public void setRoutingOrder(int routingOrder) {
        this.routingOrder = routingOrder;
    }

    @Override
    public String toString() {
        return "Signer{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", clientUserId='" + clientUserId + '\'' +
                ", routingOrder=" + routingOrder +
                '}';
    }
}
