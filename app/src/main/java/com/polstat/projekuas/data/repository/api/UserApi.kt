package com.polstat.projekuas.data.repository.api

import com.polstat.projekuas.data.network.BlokIIIResponse
import com.polstat.projekuas.data.network.BlokIIResponse
import com.polstat.projekuas.data.network.BlokIResponse
import com.polstat.projekuas.data.network.BlokIVResponse
import com.polstat.projekuas.data.network.CheckedKuesionerResponse
import com.polstat.projekuas.data.network.CreateKuesionerResponse
import com.polstat.projekuas.data.network.EditPasswordResponse
import com.polstat.projekuas.data.network.GetBlokIVResponse
import com.polstat.projekuas.data.network.KuesionerByPencacahResponse
import com.polstat.projekuas.data.network.PencacahByPengawasResponse
import com.polstat.projekuas.data.network.ProfileRequest
import com.polstat.projekuas.data.network.ProfileResponse
import com.polstat.projekuas.data.network.RegisterRequest
import com.polstat.projekuas.data.network.RegisterResponse
import com.polstat.projekuas.data.network.UserRequest
import com.polstat.projekuas.data.network.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {
    @POST("login")
    fun login(
        @Body userRequest: UserRequest
    ): Call<UserResponse>

    @POST("register")
    fun register(
        @Body registerRequest: RegisterRequest
    ): Call<RegisterResponse>

    @GET("/profile")
    fun getUserProfile(): Call<ProfileResponse>

    @FormUrlEncoded
    @POST("/kuesioner")
    fun postKuesioner(
        @Field("pencacahId") pencacahId: Long,
    ): Call<CreateKuesionerResponse>

    @FormUrlEncoded
    @POST("/bloki")
    fun postBlokI(
        @Field("kode_provinsi") kodeProvinsi: String,
        @Field("kode_kabkota") kodeKabupatenKota: String,
        @Field("kode_kec") kodeKecamatan: String,
        @Field("kode_desa") kodeDesaKelurahan: String,
        @Field("alamat") alamat: String,
        @Field("jml_anggotakel") jmlAnggotaKeluarga: String,
        @Field("nama_sls") namaSLS: String,
        @Field("no_bangunan") noBangunan: String,
        @Field("nama_kk") namaKK: String,
        @Field("nohp_kk") noHpKK: String,
        @Field("no_urut_keluarga") noUrutKeluarga: String,
        @Field("kuesioner_id") kuesionerId: Long
    ): Call<BlokIResponse>

    @FormUrlEncoded
    @PUT("/bloki/{id}")
    fun putBlokI(
        @Path("id") id: Long,
        @Field("kode_provinsi") kodeProvinsi: String,
        @Field("kode_kabkota") kodeKabupatenKota: String,
        @Field("kode_kec") kodeKecamatan: String,
        @Field("kode_desa") kodeDesaKelurahan: String,
        @Field("alamat") alamat: String,
        @Field("jml_anggotakel") jmlAnggotaKeluarga: String,
        @Field("nama_sls") namaSLS: String,
        @Field("no_bangunan") noBangunan: String,
        @Field("nama_kk") namaKK: String,
        @Field("nohp_kk") noHpKK: String,
        @Field("no_urut_keluarga") noUrutKeluarga: String,
        @Field("kuesioner_id") kuesionerId: Long
    ): Call<BlokIResponse>

    @FormUrlEncoded
    @POST("/blokii")
    fun postBlokII(
        @Field("nama_pencacah") namaPencacah: String,
        @Field("nama_pengawas") namaPengawas: String,
        @Field("id_kues") kuesionerId: Long
    ): Call<BlokIIResponse>

    @FormUrlEncoded
    @POST("/blokiii")
    fun postBlokIII(
        @Field("kepemilikan_rumah") kepemilikanRumah: String,
        @Field("penggunaan_listrik") penggunaanListrik: String,
        @Field("air_minum") airMinumUtama: String,
        @Field("fasilitas_bab") fasilitasBAB: String,
        @Field("lantai_terluas") lantaiTerluas: String,
        @Field("id_kues") kuesionerId: Long
    ): Call<BlokIIIResponse>

    @FormUrlEncoded
    @PUT("/blokiii/{id}")
    fun putBlokIII(
        @Path("id") id: Long,
        @Field("kepemilikan_rumah") kepemilikanRumah: String,
        @Field("penggunaan_listrik") penggunaanListrik: String,
        @Field("air_minum") airMinumUtama: String,
        @Field("fasilitas_bab") fasilitasBAB: String,
        @Field("lantai_terluas") lantaiTerluas: String,
    ): Call<BlokIIIResponse>

    @GET("bloki/{kuesionerId}")
    suspend fun getBlokI(@Path("kuesionerId") kuesionerId: Long): BlokIResponse

    @GET("bloki/{kuesionerId}")
    fun getBlokII(@Path("kuesionerId") kuesionerId: Long): BlokIIResponse

    @GET("blokiii/{kuesionerId}")
    suspend fun getBlokIII(@Path("kuesionerId") kuesionerId: Long): BlokIIIResponse

    @GET("blokiv/{kuesionerId}")
    suspend fun getBlokIV(@Path("kuesionerId") kuesionerId: Long): GetBlokIVResponse

    @GET("blokiv-id/{id}")
    suspend fun getBlokIVById(@Path("id") id: Long): BlokIVResponse

    @FormUrlEncoded
    @POST("/blokiv")
    fun postBlokIV(
        @Field("nama_lengkap") namaLengkap: String,
        @Field("jenis_kelamin") jenisKelamin: String,
        @Field("nik") NIK: String,
        @Field("alamat_sesuai_KK") alamatSesuaiKK: String,
        @Field("lahir_provinsi") tempatLahirProvinsi: String,
        @Field("lahir_kab_kota") tempatLahirkabKota: String,
        @Field("tgl_lahir_sesuai_kk") tglLahirSesuaiKK: String,
        @Field("tgl_lahir") tglLahir: String,
        @Field("punya_akta_lahir") punyaAktaLahir: String,
        @Field("kewarganegaraan") kewarganegaraan: String,
        @Field("agama") agama: String,
        @Field("status_kawin") statusPerkawinan: String,
        @Field("hubungan_kk") hubunganKK: String,
        @Field("lama_tinggal_tahun") lamaTinggalTahun: Int,
        @Field("lama_tinggal_bulan") lamaTinggalBulan: Int,
        @Field("ijazah_tertinggi") ijazahTertinggi: String,
        @Field("id_kues") kuesionerId: Long
    ): Call<BlokIVResponse>

    @FormUrlEncoded
    @PUT("/blokiv/{id}")
    fun putBlokIV(
        @Path("id") id: Long,
        @Field("nama_lengkap") namaLengkap: String,
        @Field("jenis_kelamin") jenisKelamin: String,
        @Field("nik") NIK: String,
        @Field("alamat_sesuai_KK") alamatSesuaiKK: String,
        @Field("lahir_provinsi") tempatLahirProvinsi: String,
        @Field("lahir_kab_kota") tempatLahirkabKota: String,
        @Field("tgl_lahir_sesuai_kk") tglLahirSesuaiKK: String,
        @Field("tgl_lahir") tglLahir: String,
        @Field("punya_akta_lahir") punyaAktaLahir: String,
        @Field("kewarganegaraan") kewarganegaraan: String,
        @Field("agama") agama: String,
        @Field("status_kawin") statusPerkawinan: String,
        @Field("hubungan_kk") hubunganKK: String,
        @Field("lama_tinggal_tahun") lamaTinggalTahun: Int,
        @Field("lama_tinggal_bulan") lamaTinggalBulan: Int,
        @Field("ijazah_tertinggi") ijazahTertinggi: String,
    ): Call<BlokIVResponse>

    @GET("/kuesioner/pencacah")
    fun getKuesionerByPencacah(): Call<KuesionerByPencacahResponse>

    @GET("/kuesioner/pengawas")
    fun getKuesionerByPengawas(): Call<KuesionerByPencacahResponse>

    @GET("/kuesioner/pengawas/{id_pencacah}")
    fun getKuesionerPencacahByPengawas(@Path("id_pencacah") pencacahId: Long): Call<KuesionerByPencacahResponse>

    @GET("/pencacah")
    fun getPencacahByPengawas(): Call<PencacahByPengawasResponse>

    @FormUrlEncoded
    @PUT("/kuesioner/checked/{id}")
    fun checkedKuesioner(@Path("id") id: Long, @Field("status") Status: String): Call<CheckedKuesionerResponse>

    @PUT("/profile")
    fun editProfile(
        @Body profileRequest: ProfileRequest
    ): Call<ProfileResponse>

    @FormUrlEncoded
    @PUT("/profile/changePassword")
    fun changePassword(
        @Field("username") username: String,
        @Field("pass_lama") passLama: String,
        @Field("pass_baru") passBaru: String,
    ): Call<EditPasswordResponse>

}