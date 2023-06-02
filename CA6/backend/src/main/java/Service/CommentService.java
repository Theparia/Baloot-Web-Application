package Service;

import Database.Database;
import Domain.*;
//import Domain.Id.CommentId;
import Domain.Id.CommentId;
import Exceptions.CommentNotFound;
import Exceptions.CommodityNotFound;
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
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@Service
@DependsOn({"commodityService", "userService"})
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
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        List<Comment> comments = objectMapper.readValue(HTTPRequestHandler.getRequest(COMMENTS_URI), typeFactory.constructCollectionType(List.class, Comment.class));
        String rawJsonData = HTTPRequestHandler.getRequest(COMMENTS_URI);

        List<Map<String, Object>> rawDataList = objectMapper.readValue(rawJsonData, new TypeReference<List<Map<String, Object>>>() {});
        for (int i = 0 ; i < comments.size(); i++) {
            comments.get(i).setUser(userRepository.findByEmail(((String) rawDataList.get(i).get("userEmail"))));
            comments.get(i).setCommodity(findCommodityById((Integer) rawDataList.get(i).get("commodityId")));
        }
        commentRepository.saveAll(comments);

    }

    public Commodity findCommodityById(Integer id) throws CommodityNotFound {
        return commodityRepository.findById(id)
                .orElseThrow(CommodityNotFound::new);
    }

    public void addComment(String userEmail, Integer commodityId, String text){
        User user = userRepository.findByEmail(userEmail);
        Commodity commodity = commodityRepository.findById(commodityId).orElse(null);
        Comment comment = new Comment(user, commodity, userEmail, text, LocalDate.now().toString());
        commentRepository.save(comment);
    }
    public String getEmailByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getEmail();
        }
        return null;
    }

    public List<Comment> getCommodityComments(Integer commodityId) {
        Commodity commodity = commodityRepository.findById(commodityId)
                .orElseThrow(() -> new EntityNotFoundException("Commodity not found"));

        return commentRepository.findByCommodityId(commodity.getId());
    }

    private boolean isVoteValid(int vote){
        return vote == 1 || vote == 0 || vote == -1;
    }

    public Comment findCommentById(CommentId id) throws CommodityNotFound {
        return commentRepository.findById(id)
                .orElseThrow(CommodityNotFound::new);
    }
    public void voteComment(String username, Integer commodityId, String usernameComment, int value) throws InvalidCommentVote, CommodityNotFound {
        if(!isVoteValid(value))
            throw new InvalidCommentVote();
        User user = userRepository.findByUsername(username);
        User userComment = userRepository.findByUsername(usernameComment);
        Commodity commodity = commodityRepository.findById(commodityId)
                .orElseThrow(() -> new IllegalArgumentException("Commodity not found"));
        CommentId commentId = new CommentId(commodity, userComment);
        Vote vote = new Vote(user, commodity, userComment, value);
        voteRepository.save(vote);
        updateCommentVotes(commentId);
    }

    public void updateCommentVotes(CommentId commentId) throws CommodityNotFound {
        Comment comment = findCommentById(commentId);
        List<Vote> votes = voteRepository.findByCommodityAndCommentWriter(commentId.getCommodity(), commentId.getUser());

        int likeCount = 0;
        int dislikeCount = 0;

        for (Vote vote : votes) {
            if (Objects.equals(vote.getCommodity().getId(), commentId.getCommodity().getId()) && Objects.equals(vote.getCommentWriter().getUsername(), commentId.getUser().getUsername())){
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
