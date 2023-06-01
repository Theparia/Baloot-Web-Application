package Service;

import Database.Database;
import Domain.*;
import Domain.Id.CommentId;
import Exceptions.CommentNotFound;
import Exceptions.InvalidCommentVote;
import Exceptions.UserNotFound;
import HTTPRequestHandler.HTTPRequestHandler;
import Repository.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.type.TypeFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommodityRepository commodityRepository;

    private final VoteRepository voteRepository;

    private final UserRepository userRepository;

    private CommentService(CommentRepository commentRepository, CommodityRepository commodityRepository, VoteRepository voteRepository, UserRepository userRepository) throws Exception {
        this.commentRepository = commentRepository;
        this.commodityRepository = commodityRepository;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        fetchData();
    }

    private void fetchData() throws Exception {
        final String COMMENTS_URI = "http://5.253.25.110:5000/api/comments";
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        List<Comment> comments = objectMapper.readValue(HTTPRequestHandler.getRequest(COMMENTS_URI), typeFactory.constructCollectionType(List.class, Comment.class));
        commentRepository.saveAll(comments);
        setCommentsUsername();
    }

    public void addComment(String userEmail, Integer commodityId, String text){
        Comment comment = new Comment(commodityId, userEmail, text, LocalDate.now().toString());
        commentRepository.save(comment);
//        Database.getInstance().addComment(comment);
        setCommentsUsername();
    }

    public void setCommentsUsername() {
        List<Comment> comments = commentRepository.findAll();
        for (Comment comment : comments) {
            User user = userRepository.findByEmail(comment.getUserEmail());
            if (user != null) {
                comment.setUsername(user.getUsername());
                commentRepository.save(comment);
            }
        }
    }

    public String getEmailByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getEmail();
        }
        return null; // or throw an exception if desired
    }

    public List<Comment> getCommodityComments(Integer commodityId) {
        Commodity commodity = commodityRepository.findById(commodityId)
                .orElseThrow(() -> new EntityNotFoundException("Commodity not found"));

        return commentRepository.findByCommodityId(commodity.getId());
    }

    private boolean isVoteValid(int vote){
        return vote == 1 || vote == 0 || vote == -1;
    }


    public void voteComment(Integer commentId, String username, int value) throws InvalidCommentVote, CommentNotFound, UserNotFound{
        if(!isVoteValid(value))
            throw new InvalidCommentVote();
        System.out.println("COMMENT ID IN VOTE COMMENT: " + commentId);
        Vote vote = new Vote(username, commentRepository.findById(commentId).get().getId(), value);
        voteRepository.save(vote);
        updateCommentVotes(commentId);
//        findCommentById(commentId).addVote(findUserByUsername(username).getUsername(), vote);
    }

    public void updateCommentVotes(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        List<Vote> votes = voteRepository.findByCommentId(commentId);

        int likeCount = 0;
        int dislikeCount = 0;

        for (Vote vote : votes) {
            if (vote.getCommentId() == commentId){
                if (vote.getValue() == 1) {
                    likeCount++;
                } else if (vote.getValue() == -1) {
                    dislikeCount++;
                }
            }
        }

        comment.setLikeCount(likeCount);
        comment.setDislikeCount(dislikeCount);

        commentRepository.save(comment);
    }
}
