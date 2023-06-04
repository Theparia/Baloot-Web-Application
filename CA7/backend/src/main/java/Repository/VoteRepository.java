package Repository;

import Model.Commodity;
//import Domain.Id.CommentId;
import Model.Id.VoteId;
import Model.User;
import Model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, VoteId> {
    List<Vote> findByCommodityAndCommentWriter(Commodity commodity, User user);
}
