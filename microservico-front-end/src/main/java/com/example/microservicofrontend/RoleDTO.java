package com.example.microservicofrontend;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class RoleDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonCreator
    public RoleDTO(@JsonProperty("id") Integer id,@JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }
}
