package Controller;

import Domain.Commodity;
import Domain.Provider;
import Domain.User;
import Service.Baloot;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping
public class ProviderController {
    @RequestMapping(value = "/providers/{id}", method = RequestMethod.GET)
    protected ResponseEntity<Provider> getProvider(@PathVariable String id) {
        try {
            Provider provider = Baloot.getInstance().findProviderById(Integer.valueOf(id));
            return ResponseEntity.ok(provider);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @RequestMapping(value = "/providers/{providerId}/commodities/", method = RequestMethod.GET)
    protected ResponseEntity<List<Commodity>> getProviderCommodities(@PathVariable String providerId) {
        try {
            return ResponseEntity.ok(Baloot.getInstance().findCommoditiesByProvider(Integer.valueOf(providerId)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
