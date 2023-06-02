package Repository;

import Domain.Commodity;
import Domain.Id.ItemId;
import Domain.PurchasedListItem;
import Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PurchasedListItemRepository extends JpaRepository<PurchasedListItem, ItemId> {
    Optional<PurchasedListItem> findByUserAndCommodity(User user, Commodity commodity);

    List<PurchasedListItem> findByUser(User user);
}
