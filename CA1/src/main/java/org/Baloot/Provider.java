package org.Baloot;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Provider {
    //@Id
    private int id; //unique validation
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-DD")
    private Date registryDate;
    public Provider(int id, String name, Date registryDate) {
        this.id = id;
        this.name = name;
        this.registryDate = registryDate;
    }

}
