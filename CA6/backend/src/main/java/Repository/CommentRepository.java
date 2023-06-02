package Repository;

import Domain.Comment;
import Domain.Commodity;
//import Domain.Id.CommentId;
import Domain.Id.CommentId;
import Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, CommentId> {

    List<Comment> findByCommodityId(Integer commodityId);
}
