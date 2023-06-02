package Controller;

import Model.Commodity;
import Model.Provider;
import Service.ProviderService;
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
    @Autowired
    private ProviderService providerService;

    @RequestMapping(value = "/providers/{id}", method = RequestMethod.GET)
    protected ResponseEntity<Provider> getProvider(@PathVariable String id) {
        try {
            Provider provider = providerService.findProviderById(Integer.valueOf(id));
            return ResponseEntity.ok(provider);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @RequestMapping(value = "/providers/{providerId}/commodities/", method = RequestMethod.GET)
    protected ResponseEntity<List<Commodity>> getProviderCommodities(@PathVariable String providerId) {
        try {
            return ResponseEntity.ok(providerService.findCommoditiesByProvider(Integer.valueOf(providerId)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
