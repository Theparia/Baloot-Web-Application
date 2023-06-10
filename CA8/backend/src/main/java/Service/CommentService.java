package Service;

//import Domain.Id.CommodityUserId;
import Model.Comment;
import Model.Commodity;
import Model.Id.CommodityUserId;
import Exceptions.CommodityNotFound;
import Exceptions.InvalidCommentVote;
import Exceptions.UserNotFound;
import HTTPRequestHandler.HTTPRequestHandler;
import Model.User;
import Model.Vote;
import Repository.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

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
            User user = userRepository.findByEmail(((String) rawDataList.get(i).get("userEmail")));
            Commodity commodity = findCommodityById((Integer) rawDataList.get(i).get("commodityId"));
            comments.get(i).setUser(user);
            comments.get(i).setCommodity(commodity);
            Optional<Comment> existingComment = commentRepository.findById(new CommodityUserId(commodity, user));
            if (existingComment.isPresent())
                continue;
            commentRepository.save(comments.get(i));
        }

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
    public String getEmailByUsername(String username) throws UserNotFound {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFound::new);
        return user.getEmail();
    }

    public List<Comment> getCommodityComments(Integer commodityId) {
        Commodity commodity = commodityRepository.findById(commodityId)
                .orElseThrow(() -> new EntityNotFoundException("Commodity not found"));

        return commentRepository.findByCommodityId(commodity.getId());
    }

    private boolean isVoteValid(int vote){
        return vote == 1 || vote == 0 || vote == -1;
    }

    public Comment findCommentById(CommodityUserId id) throws CommodityNotFound {
        return commentRepository.findById(id)
                .orElseThrow(CommodityNotFound::new);
    }
    public void voteComment(String username, Integer commodityId, String usernameComment, int value) throws InvalidCommentVote, CommodityNotFound, UserNotFound {
        if(!isVoteValid(value))
            throw new InvalidCommentVote();
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFound::new);
        User userComment = userRepository.findByUsername(usernameComment).orElseThrow(UserNotFound::new);
        Commodity commodity = commodityRepository.findById(commodityId)
                .orElseThrow(CommodityNotFound::new);
        CommodityUserId commodityUserId = new CommodityUserId(commodity, userComment);
        Vote vote = new Vote(user, commodity, userComment, value);
        voteRepository.save(vote);
        updateCommentVotes(commodityUserId);
    }

    public void updateCommentVotes(CommodityUserId commodityUserId) throws CommodityNotFound {
        Comment comment = findCommentById(commodityUserId);
        List<Vote> votes = voteRepository.findByCommodityAndCommentWriter(commodityUserId.getCommodity(), commodityUserId.getUser());

        int likeCount = 0;
        int dislikeCount = 0;

        for (Vote vote : votes) {
            if (Objects.equals(vote.getCommodity().getId(), commodityUserId.getCommodity().getId()) && Objects.equals(vote.getCommentWriter().getUsername(), commodityUserId.getUser().getUsername())){
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
