package Repository;

import Domain.BuyListItem;
import Domain.Commodity;
import Domain.Id.BuyListId;
import Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BuyListRepository extends JpaRepository<BuyListItem, BuyListId> {
    public Optional<BuyListItem> findByUserAndCommodity(User user, Commodity commodity);

    List<BuyListItem> findByUser(User user);
}
