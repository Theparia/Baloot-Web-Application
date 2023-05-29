package Controller;


import Domain.Comment;
import Service.Baloot;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping
public class CommentsController {

    private Baloot baloot;

    public CommentsController(Baloot baloot){
        this.baloot = baloot;
    }

    @RequestMapping(value = "/comments/{commodityId}/", method = RequestMethod.GET)
    public ResponseEntity<List<Comment>> getCommentsCommodity(@PathVariable String commodityId) {
        return ResponseEntity.ok(baloot.getCommodityComments(Integer.valueOf(commodityId)));
    }

    @RequestMapping(value = "/comments/{commentId}/like/", method = RequestMethod.POST)
    public ResponseEntity<String> likeComment(@PathVariable String commentId, @RequestBody Map<String, String> info){
        try {
            baloot.voteComment(UUID.fromString(commentId), info.get("username"), 1);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/comments/{commentId}/dislike/", method = RequestMethod.POST)
    public ResponseEntity<String> dislikeComment(@PathVariable String commentId, @RequestBody Map<String, String> info){
        try {
            baloot.voteComment(UUID.fromString(commentId), info.get("username"), -1);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/comments/add/", method = RequestMethod.POST)
    public ResponseEntity<String> dislikeComment(@RequestBody Map<String, String> info)  {
        try {
            baloot.addComment(baloot.getEmailByUsername(info.get("username")), Integer.valueOf(info.get("commodityId")), info.get("text"));
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
