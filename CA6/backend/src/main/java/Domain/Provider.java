package Domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "provider")
public class Provider {
    @Id
    private Integer id;

    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date registryDate;

    private String image;

//    public Provider(Integer id, String name, Date registryDate, String image) {
//        this.id = id;
//        this.name = name;
//        this.registryDate = registryDate;
//        this.image = image;
//    }

    public boolean isEqual(Integer providerId) {
        return this.id.equals(providerId);
    }
}
