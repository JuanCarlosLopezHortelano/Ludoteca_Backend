package com.ccsw.tutorial.client.model;

public class ClientDto {
    private Long id;
    private String name;

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name new value of {@link #getName}
     *
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
