package com.example.isaiahrawlinson.whatsplaying;

import java.io.Serializable;

/**
 * Created by isaiahrawlinson on 5/5/17.
 */

class Theatre implements Serializable {
    private String id, name;

    Theatre() {
        this.id = "";
        this.name = "";
    }

    Theatre(String id, String name){
        this.id = id;
        this.name = name;
    }

    String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Theatre)) return false;

        Theatre theatre = (Theatre) o;

        if (getId() != null ? !getId().equals(theatre.getId()) : theatre.getId() != null)
            return false;
        return getName() != null ? getName().equals(theatre.getName()) : theatre.getName() == null;

    }

    @Override
    public String toString() {
        return name;
    }
}
