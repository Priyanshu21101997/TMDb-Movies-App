//package com.example.domainapp.network
//
//import retrofit2.Retrofit
//import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
//import retrofit2.converter.gson.GsonConverterFactory
//
//class RetroInstance {
//
//    companion object{
//        private const val BASE_URL = "https://api.themoviedb.org/3/"
//        var retrofit: Retrofit? = null
//
//        @JvmStatic
//        fun getRetroClient():Retrofit? {
//
//            if(retrofit == null) {
//
//                retrofit = Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
//                    .build()
//            }
//
//            return retrofit
//        }
//    }
//}