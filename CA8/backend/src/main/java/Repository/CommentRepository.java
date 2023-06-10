package Repository;

import Model.Comment;
//import Domain.Id.CommodityUserId;
import Model.Id.CommodityUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, CommodityUserId> {
    List<Comment> findByCommodityId(Integer commodityId);
}
