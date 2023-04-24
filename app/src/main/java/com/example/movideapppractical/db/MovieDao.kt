package com.example.movideapppractical.db

import androidx.room.*


@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCartDetails(cartDetail: CartDetails)

    @Query("SELECT * FROM movie_cart") // return all the cart detail
    fun getCartDetails(): MutableList<CartDetails>

    @Query("DELETE FROM movie_cart")// return all the cart detail
    fun deleteCartDetails()

}
