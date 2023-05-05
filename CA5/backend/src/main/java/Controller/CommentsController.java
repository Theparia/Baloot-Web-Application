package Controller;


import Database.Database;
import Domain.Comment;
import Service.Baloot;
import Service.Exceptions.CommentNotFound;
import Service.Exceptions.InvalidCommentVote;
import Service.Exceptions.UserNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping
public class CommentsController {
    @RequestMapping(value = "/comments/{commodityId}/", method = RequestMethod.GET)
    public ResponseEntity<List<Comment>> getCommentsCommodity(@PathVariable String commodityId) {
        return ResponseEntity.ok(Baloot.getInstance().getCommodityComments(Integer.valueOf(commodityId)));
    }

    @RequestMapping(value = "/comments/{commentId}/like/", method = RequestMethod.POST)
    public ResponseEntity<String> likeComment(@PathVariable String commentId, @RequestBody Map<String, String> info) throws UserNotFound, InvalidCommentVote, CommentNotFound {
        try {
            Baloot.getInstance().voteComment(UUID.fromString(commentId), info.get("username"), 1);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/comments/{commentId}/dislike/", method = RequestMethod.POST)
    public ResponseEntity<String> dislikeComment(@PathVariable String commentId, @RequestBody Map<String, String> info) throws UserNotFound, InvalidCommentVote, CommentNotFound {
        try {
            Baloot.getInstance().voteComment(UUID.fromString(commentId), info.get("username"), -1);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
