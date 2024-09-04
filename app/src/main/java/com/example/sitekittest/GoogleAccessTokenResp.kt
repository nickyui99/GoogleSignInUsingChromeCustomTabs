package com.example.sitekittest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GoogleAccessTokenResp (
    @SerializedName("access_token") val accessToken: String?,
    @SerializedName("token_type") val tokenType: String?,
    @SerializedName("expires_in") val expiresIn: Long?,
    @SerializedName("refresh_token") val refreshToken: String?,
    @SerializedName("scope") val scope: String?
): Parcelable