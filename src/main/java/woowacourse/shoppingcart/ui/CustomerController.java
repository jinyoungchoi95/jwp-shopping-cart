package woowacourse.shoppingcart.ui;

import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import woowacourse.auth.support.AuthenticationPrincipal;
import woowacourse.shoppingcart.application.CustomerService;
import woowacourse.shoppingcart.dto.customer.CustomerResponse;
import woowacourse.shoppingcart.dto.customer.CustomerSignUpRequest;
import woowacourse.shoppingcart.dto.customer.CustomerUpdatePasswordRequest;
import woowacourse.shoppingcart.dto.customer.CustomerUpdateRequest;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(final CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody CustomerSignUpRequest request) {
        customerService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<CustomerResponse> myInfo(@AuthenticationPrincipal String username) {
        return ResponseEntity.ok(customerService.findByUsername(username));
    }

    @PutMapping
    public ResponseEntity<Void> update(@Valid @RequestBody CustomerUpdateRequest request,
                                       @AuthenticationPrincipal String username) {
        customerService.update(request, username);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody CustomerUpdatePasswordRequest request,
                                               @AuthenticationPrincipal String username) {
        customerService.updatePassword(username, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@AuthenticationPrincipal String username) {
        customerService.deleteByUsername(username);
        return ResponseEntity.noContent().build();
    }
}
