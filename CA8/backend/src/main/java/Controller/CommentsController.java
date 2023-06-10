package Controller;


import DTO.CommentDTO;
import Model.Comment;
import Service.CommentService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@RestController
@RequestMapping
public class CommentsController {
    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/comments/{commodityId}/", method = RequestMethod.GET)
    public ResponseEntity<List<CommentDTO>> getCommentsCommodity(@PathVariable String commodityId) {
        List<Comment> comments = commentService.getCommodityComments(Integer.valueOf(commodityId));
        List<CommentDTO> commentDTOs = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setUsername(comment.getUser().getUsername());
            commentDTO.setCommodityId(comment.getCommodity().getId());
            commentDTO.setUserEmail(comment.getUserEmail());
            commentDTO.setText(comment.getText());
            commentDTO.setDate(comment.getDate());
            commentDTO.setLikeCount(comment.getLikeCount());
            commentDTO.setDislikeCount(comment.getDislikeCount());
            commentDTOs.add(commentDTO);
        }
        return ResponseEntity.ok(commentDTOs);
    }

    @RequestMapping(value = "/comments/like/", method = RequestMethod.POST)
    public ResponseEntity<String> likeComment(@RequestBody Map<String, String> info){
        try {
            commentService.voteComment(info.get("username"), Integer.valueOf(info.get("commodityId")), info.get("userComment"), 1);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/comments/dislike/", method = RequestMethod.POST)
    public ResponseEntity<String> dislikeComment(@RequestBody Map<String, String> info){
        try {
            commentService.voteComment(info.get("username"), Integer.valueOf(info.get("commodityId")), info.get("userComment"), -1);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/comments/add/", method = RequestMethod.POST)
    public ResponseEntity<String> addComment(@RequestBody Map<String, String> info)  {
        try {
            commentService.addComment(commentService.getEmailByUsername(info.get("username")), Integer.valueOf(info.get("commodityId")), info.get("text"));
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
