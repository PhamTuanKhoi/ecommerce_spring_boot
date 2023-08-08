package com.v1.ecommerce.repository;

import com.v1.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM #{#entityName} c WHERE c.user.id =:userId")
    public Cart findCartByUserId(@Param("userId") Long userId);
}
