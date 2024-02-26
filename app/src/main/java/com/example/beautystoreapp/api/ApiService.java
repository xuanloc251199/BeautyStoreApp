package com.example.beautystoreapp.api;

import com.example.beautystoreapp.api.request.AddToCartRequest;
import com.example.beautystoreapp.api.request.RemoveFromCartRequest;
import com.example.beautystoreapp.api.response.AddToCartResponse;
import com.example.beautystoreapp.api.response.AuthResponse;
import com.example.beautystoreapp.api.response.CartItemCountResponse;
import com.example.beautystoreapp.api.response.CartResponse;
import com.example.beautystoreapp.api.response.RemoveFromCartResponse;
import com.example.beautystoreapp.api.response.UserResponse;
import com.example.beautystoreapp.api.response.VerificationResponse;
import com.example.beautystoreapp.model.Brand;
import com.example.beautystoreapp.model.CartItem;
import com.example.beautystoreapp.model.Category;
import com.example.beautystoreapp.model.Comment;
import com.example.beautystoreapp.model.Product;
import com.example.beautystoreapp.model.UserDetails;
import com.example.beautystoreapp.model.Users;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("auth/login")
    Call<AuthResponse> loginUser(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("auth/register")
    Call<AuthResponse> registerUser(@Field("name") String name, @Field("email") String email, @Field("password") String password, @Field("password_confirmation") String password_confirmation);

    @POST("auth/register")
    Call<UserResponse> registerUsers(@Body Users user);

    @GET("auth/check-email-verification")
    Call<VerificationResponse> checkEmailVerification(@Query("email") String email);

    @GET("auth/user/show")
    Call<Users> getUserInfo(@Header("Authorization") String authToken);

    @PUT("auth/user/details")
    Call<ResponseBody> updateUserDetails(@Header("Authorization") String token, @Body UserDetails userDetails);

    @GET("products")
    Call<List<Product>> getProducts();

    @GET("products/{id}")
    Call<Product> getProductsById(@Path("id") int id);

    @GET("categories/{categoryId}/products")
    Call<List<Product>> getProductsByCategory(@Path("categoryId") int categoryId);

    @GET("products/{productId}/comments")
    Call<List<Comment>> getCommentsByProductId(@Path("productId") int productId);

    @GET("categories")
    Call<List<Category>> getCategories();

    @GET("brand")
    Call<List<Brand>> getBrands();

//    @POST("cart/add")
//    Call<ResponseBody> addToCart(@Header("Authorization") String token, @Body AddToCartRequest addToCartRequest);x

    @POST("cart/add")
    Call<AddToCartResponse> addToCart(@Header("Authorization") String token, @Body AddToCartRequest addToCartRequest);

    @POST("cart/removeFromCart")
    Call<RemoveFromCartResponse> removeFromCart(@Header("Authorization") String token, @Body RemoveFromCartRequest removeFromCartRequest);

    @GET("cart/itemcount")
    Call<CartItemCountResponse> getCartItemCount(@Header("Authorization") String token);

    @GET("cart/showCartByUser")
    Call<CartResponse> showCartByUser(@Header("Authorization") String token);
}
