package woowacourse.shoppingcart.dto;

import woowacourse.shoppingcart.domain.CartItem;

public class CartItemResponse {

    private Long id;
    private Long productId;
    private String name;
    private int price;
    private int quantity;
    private String imageURL;

    private CartItemResponse() {
    }

    public CartItemResponse(final Long id, final Long productId, final String name, final int price, final int quantity,
                            final String imageURL) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageURL = imageURL;
    }

    public static CartItemResponse from(final CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getProductId(),
                cartItem.getName(),
                cartItem.getPrice(),
                cartItem.getQuantity(),
                cartItem.getImageUrl()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImageURL() {
        return imageURL;
    }
}
