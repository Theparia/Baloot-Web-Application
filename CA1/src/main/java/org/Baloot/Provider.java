package org.Baloot;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor

public class Provider {
    //@Id
    private String id; //TODO: unique validation
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-DD")
    private Date registryDate;
    public Provider(String id, String name, Date registryDate) {
        this.id = id;
        this.name = name;
        this.registryDate = registryDate;
    }

    public boolean isEqual(String providerId) {
        return this.id.equals(providerId);
    }

}
