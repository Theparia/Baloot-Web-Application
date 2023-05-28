package Controller;

import Domain.Commodity;
import Domain.Provider;
import Domain.User;
import Service.Baloot;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@NoArgsConstructor

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping
public class ProviderController {
    private Baloot baloot;

    public ProviderController(Baloot baloot){
        this.baloot = baloot;
    }
    @RequestMapping(value = "/providers/{id}", method = RequestMethod.GET)
    protected ResponseEntity<Provider> getProvider(@PathVariable String id) {
        try {
            Provider provider = baloot.findProviderById(Integer.valueOf(id));
            return ResponseEntity.ok(provider);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @RequestMapping(value = "/providers/{providerId}/commodities/", method = RequestMethod.GET)
    protected ResponseEntity<List<Commodity>> getProviderCommodities(@PathVariable String providerId) {
        try {
            return ResponseEntity.ok(baloot.findCommoditiesByProvider(Integer.valueOf(providerId)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
