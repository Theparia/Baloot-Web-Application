package Repository;

import Domain.BuyListItem;
import Domain.Commodity;
import Domain.Id.ItemId;
import Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BuyListItemRepository extends JpaRepository<BuyListItem, ItemId> {
    public Optional<BuyListItem> findByUserAndCommodity(User user, Commodity commodity);

    List<BuyListItem> findByUser(User user);
}
