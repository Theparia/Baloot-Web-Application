package Repository;

import Model.Commodity;
import Model.Id.CommodityUserId;
import Model.PurchasedListItem;
import Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PurchasedListItemRepository extends JpaRepository<PurchasedListItem, CommodityUserId> {
    Optional<PurchasedListItem> findByUserAndCommodity(User user, Commodity commodity);
    List<PurchasedListItem> findByUser(User user);
}
