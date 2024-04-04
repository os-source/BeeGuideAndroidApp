package com.example.beeguide.data

import android.util.Log
import com.example.beeguide.model.BioRequest
import com.example.beeguide.model.NameRequest
import com.example.beeguide.model.User
import com.example.beeguide.network.BeeGuideApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException

interface BeeGuideRepository {
    suspend fun getUser(): User
    suspend fun saveUserName(name: String)
    suspend fun saveUserBio(bio: String)
    suspend fun saveUserProfilePicture(image: File)
}

class NetworkBeeGuideRepository(
    private val beeGuideApiService: BeeGuideApiService,
) : BeeGuideRepository {
    override suspend fun getUser(): User = beeGuideApiService.getUser()
    override suspend fun saveUserName(name: String) = beeGuideApiService.saveUserName(
        NameRequest(name)
    )

    override suspend fun saveUserBio(bio: String) = beeGuideApiService.saveUserBio(
        BioRequest(bio)
    )

    override suspend fun saveUserProfilePicture(image: File) {
        try {
            Log.d("NetworkBeeGuideRepository", "uploading")
            beeGuideApiService.saveUserProfilePicture(
                file = MultipartBody.Part.createFormData(
                    "file",
                    image.name,
                    image.asRequestBody()
                )
            )
        } catch (e: IOException) {
            Log.d("NetworkBeeGuideRepository", e.toString())
            e.printStackTrace()
        } catch (e: HttpException) {
            Log.d("NetworkBeeGuideRepository", e.toString())
            e.printStackTrace()
        }
    }
}