package com.csuft.studyplatform.model;

import com.csuft.studyplatform.model.domain.Categories;
import com.csuft.studyplatform.model.domain.SearchResult;
import com.csuft.studyplatform.model.domain.SelectedContent;
import com.csuft.studyplatform.model.domain.HomePagerContent;
import com.csuft.studyplatform.model.domain.OnSellContent;
import com.csuft.studyplatform.model.domain.SearchRecommend;
import com.csuft.studyplatform.model.domain.SelectedPageCategory;
import com.csuft.studyplatform.model.domain.TicketParams;
import com.csuft.studyplatform.model.domain.TicketResult;
import com.csuft.studyplatform.model.domain.VerifiCode;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Api {

    @GET("discovery/categories")
    Call<Categories> getCategories();

    @GET
    Call<HomePagerContent> getHomePageContent(@Url String url);

    @POST("tpwd")
    Call<TicketResult> getTicket(@Body TicketParams ticketParams);

    @GET("recommend/categories")
    Call<SelectedPageCategory> getSelectedPageCategories();

    @GET
    Call<SelectedContent> getSelectedPageContent(@Url String url);

    @GET
    Call<OnSellContent> getOnSellPageContent(@Url String url);

    @GET("search/recommend")
    Call<SearchRecommend> getRecommendWords();

    @GET("search")
    Call<SearchResult> doSearch(@Query("page") int page, @Query("keyword") String keyword);


    @GET("uc/ex/reset/mail-code")
    Call<VerifiCode> sendMailCode(@Query("mail") String mail);

    @GET("uc/ex/reset/mail-code")
    Call<VerifiCode> sendResetMailCode(@Query("mail") String mail);


    @POST("uc/user/register")
    Call<ResponseWithoutData> registerAccount(@Query("mailCode") String mailCode, @Body RegisterVo registerVo);

    @POST("uc/user/login")
    Call<UserVoResponse> doLogin(@Body LoginVo loginVo);

    @GET("uc/user/token")
    Call<UserVoResponse> checkToken();

    @GET("uc/user/logout")
    Call<ResponseWithoutData> doLogout();

    @PUT("uc/user/reset")
    Call<ResponseWithoutData> resetPassword(@Query("mailCode") String mailCode, @Body RegisterVo registerVo);
}
