package Repository;

import Domain.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, String> {
    Optional<Discount> findByDiscountCode(String discountCode);
}
