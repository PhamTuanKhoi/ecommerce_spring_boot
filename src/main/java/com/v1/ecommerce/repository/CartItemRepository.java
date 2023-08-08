package com.v1.ecommerce.repository;

import com.v1.ecommerce.model.Cart;
import com.v1.ecommerce.model.CartItem;
import com.v1.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT ci FROM #{#entityName} ci WHERE ci.cart =:cart AND ci.product =:product " +
            "AND ci.size =:size AND ci.userId =:userId")
    CartItem isCartItemExist(
            @Param("cart") Cart cart, @Param("product") Product product,
            @Param("size") String size, @Param("userId") Long userId
    );
}
