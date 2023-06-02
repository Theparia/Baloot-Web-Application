package Repository;

import Domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {
    @Query("SELECT r.score FROM Rating r WHERE r.commodityId = :commodityId")
    List<Float> findScoresByCommodityId(Integer commodityId);
}
