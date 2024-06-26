package com.example.beeguide.network

import com.example.beeguide.model.BioRequest
import com.example.beeguide.model.Map
import com.example.beeguide.model.NameRequest
import com.example.beeguide.model.SignInRequest
import com.example.beeguide.model.SignUpRequest
import com.example.beeguide.model.TokenResponse
import com.example.beeguide.model.User
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface BeeGuideApiService {
    @GET("regions/{uuid}/maps/{major}")
    suspend fun getMap(@Path("uuid") uuId: String, @Path("major") major: Int): Map

    @GET("user")
    suspend fun getUser(): User

    @POST("register")
    suspend fun signUp(
        @Body body: SignUpRequest,
    )

    @POST("login")
    suspend fun signIn(
        @Body body: SignInRequest,
        @Query("rememberme") remember: Boolean
    ): TokenResponse

    @GET("maps/{id}/file")
    suspend fun getMapFile(@Path("id") mapId: Int): String

    @PATCH("user/name")
    suspend fun saveUserName(@Body name: NameRequest)

    @PATCH("user/bio")
    suspend fun saveUserBio(@Body bio: BioRequest)

    @Multipart
    @POST("user/profile")
    suspend fun saveUserProfilePicture(@Part file: MultipartBody.Part)
}