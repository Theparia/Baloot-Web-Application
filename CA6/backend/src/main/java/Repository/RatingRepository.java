package Repository;

import Model.Rating;
import Model.Id.RatingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingId> {
    @Query("SELECT r.score FROM Rating r WHERE r.commodity.id = :commodityId")
    List<Float> findScoresByCommodityId(Integer commodityId);
}
