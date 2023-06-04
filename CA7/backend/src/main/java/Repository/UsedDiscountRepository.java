package Repository;

import Model.Discount;
import Model.Id.UsedDiscountId;
import Model.UsedDiscount;
import Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsedDiscountRepository extends JpaRepository<UsedDiscount, UsedDiscountId> {
    Optional<UsedDiscount> findByDiscountAndUser(Discount discount, User user);
}
