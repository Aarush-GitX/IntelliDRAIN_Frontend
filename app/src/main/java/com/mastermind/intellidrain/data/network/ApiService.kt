package com.mastermind.intellidrain.data.network

import com.mastermind.intellidrain.data.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("flood-risk/")
    suspend fun getFloodRisk(@Body request: FloodRiskRequest): FloodRiskResponse

    @POST("detect/")
    suspend fun detectBlockage(@Body request: DetectRequest): DetectResponse

    @GET("drains/")
    suspend fun getAllDrains(): List<Drain>

    @GET("drains/{id}/")
    suspend fun getDrainDetail(@Path("id") id: Int): DrainDetail

    @GET("drains/{id}/history/")
    suspend fun getDrainHistory(@Path("id") id: Int): List<InspectionRecord>
}