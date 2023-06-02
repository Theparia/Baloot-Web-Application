package Repository;

import Model.Comment;
//import Domain.Id.CommentId;
import Model.Id.CommentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, CommentId> {
    List<Comment> findByCommodityId(Integer commodityId);
}
