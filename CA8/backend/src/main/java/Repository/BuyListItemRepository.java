package Repository;

import Model.BuyListItem;
import Model.Commodity;
import Model.Id.CommodityUserId;
import Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BuyListItemRepository extends JpaRepository<BuyListItem, CommodityUserId> {
    Optional<BuyListItem> findByUserAndCommodity(User user, Commodity commodity);

    List<BuyListItem> findByUser(User user);
}
