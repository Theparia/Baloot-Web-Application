package DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {
    private Integer id;
    private String username;
    private Integer commodityId;
    private String userEmail;
    private String text;
    private String date;
    private Integer likeCount;
    private Integer dislikeCount;
}
