package Repository;

import Domain.Commodity;
//import Domain.Id.CommentId;
import Domain.Id.CommentId;
import Domain.Id.VoteId;
import Domain.User;
import Domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, VoteId> {
    List<Vote> findByCommodityAndCommentWriter(Commodity commodity, User user);
}
