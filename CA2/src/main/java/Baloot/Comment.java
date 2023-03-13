package Baloot;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Comment {

    static private long commentId = 0;
    private Integer commodityId;
    private String userEmail;
    private String text;
    private String date;

    public Comment(Integer commodityId, String userEmail, String text, String date){
        commentId += 1;
        this.date = date;
        this.commodityId = commodityId;
        this.text = text;
        this.userEmail = userEmail;
    }

    public Comment(){
        commentId += 1;
    }



}
