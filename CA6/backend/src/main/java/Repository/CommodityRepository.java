package Repository;

import Domain.Commodity;
import Domain.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommodityRepository extends JpaRepository<Commodity, Integer> {
    List<Commodity> findByCategories(String category);

    List<Commodity> findByNameContainingIgnoreCase(String name);

    List<Commodity> findByProviderId(Integer id);

    List<Commodity> findCommoditiesByProvider(Provider provider);

    List<Commodity> findByInStockGreaterThan(int i);

    List<Commodity> findAllByOrderByName();

    List<Commodity> findAllByOrderByPrice();
}

