package onerfid.com.oneptt.api;

import java.util.ArrayList;

import onerfid.com.oneptt.bean.UserRequest;
import onerfid.com.oneptt.bean.UserResponse;
import onerfid.com.oneptt.bean.UserResult;
import onerfid.com.oneptt.bean.onehandlebean.HandleCommitReq;
import onerfid.com.oneptt.bean.onehandlebean.HandleCommitResponse;
import onerfid.com.oneptt.bean.onehandlebean.SimHandleReq;
import onerfid.com.oneptt.bean.onehandlebean.UseOrgReq;
import onerfid.com.oneptt.bean.onehandlebean.UseOrgbean;
import onerfid.com.oneptt.bean.onehandlechange.ChangeResultReq;
import onerfid.com.oneptt.bean.onehandlechange.ChangeResultResp;
import onerfid.com.oneptt.bean.onehandlechange.HandleChangeBean;
import onerfid.com.oneptt.bean.onehandlechange.HandleChangeReq;
import onerfid.com.oneptt.bean.onekeybean.ElevatorReq;
import onerfid.com.oneptt.bean.onekeybean.ElevatorResp;
import onerfid.com.oneptt.bean.onekeybean.OneHandleReq;
import onerfid.com.oneptt.bean.onekeybean.OneHandles;
import onerfid.com.oneptt.bean.onekeybean.OneKeyReq;
import onerfid.com.oneptt.bean.onekeybean.OneKeyResp;
import onerfid.com.oneptt.bean.onekeyset.MtpersonReq;
import onerfid.com.oneptt.bean.onekeyset.OKeyBasicReq;
import onerfid.com.oneptt.bean.onekeyset.OneKeyBasicInfo;
import onerfid.com.oneptt.bean.onekeyset.OneKeySetReq;
import onerfid.com.oneptt.bean.onekeyset.OneKeySetResp;
import onerfid.com.oneptt.bean.onekeyset.OneMTPhoneBean;
import onerfid.com.oneptt.bean.onekeyset.SubmitOKeySetReq;
import onerfid.com.oneptt.bean.updatebean.UpdateApkBean;
import onerfid.com.oneptt.bean.updatebean.UpdateReq;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by jiayong on 2018/4/13.
 */
public interface LoginApi {
    @POST("/OneSYSWebAPI/api/user/login")//用户登录
    Observable<UserResponse<UserResult>> login(@Body UserRequest request);

    @POST("/OneSYSWebAPI/api/Org/GetUseOrg")//获取小区
    Observable<UserResponse<UseOrgbean>> useorglist(@Body UseOrgReq request);

    @POST("/OneSYSWebAPI/api/onekey/Activate")//发送短信
    Observable<UserResponse> handleSim(@Body SimHandleReq req);

    @POST("/OneSYSWebAPI/api/OneKey/OneHandleCreate")//总机提交
    Observable<UserResponse<HandleCommitResponse>> handleSubmit(@Body HandleCommitReq req);

    @POST("/OneSYSWebAPI/api/OneKey/GetOneHandle")//总机查询
    Observable<UserResponse<OneHandles>> getOneHandle(@Body OneHandleReq req);

    @POST("/OneSYSWebAPI/api/Elevator/QueryEleInfo")//查询电梯
    Observable<UserResponse<ElevatorResp>> getElevators(@Body ElevatorReq req);

    @POST("/OneSYSWebAPI/api/OneKey/OneKeyCreate")//一键通绑定
    Observable<UserResponse<OneKeyResp>> bindOnkey(@Body OneKeyReq req);

    @POST("/OneSYSWebAPI/api/OneKey/GetOneKey")//一键通查询
    Observable<UserResponse<OneKeySetResp>> getOneKeyList(@Body OneKeySetReq req);

    @POST("OneSYSWebAPI/api/OneKeyInfo/FindIdByPhone")//一键通号码查询
    Observable<UserResponse<OneKeyBasicInfo>> getOKeyBasic(@Body OKeyBasicReq req);

    @POST("/OneSYSWebAPI/api/Org/GetMtPersonInfo") //一键通维保人员电话
    Observable<UserResponse<ArrayList<OneMTPhoneBean>>> getMtPerson(@Body MtpersonReq req);

    @POST("/OneSYSWebAPI/api/OneKeyInfo/UpdateOneKeyForApp") //一键通参数设置
    Observable<UserResponse> updateOKeySet(@Body SubmitOKeySetReq req);

    @POST("/OneSYSWebAPI/api/OneKey/ChangeHandle")//总机ID更换
    Observable<UserResponse<HandleChangeBean>> handleChange(@Body HandleChangeReq req);

    @POST("/OneSYSWebAPI/api/OneKey/GetOneHandleChangeSum")//获取更换结果汇总
    Observable<UserResponse<ChangeResultResp>> getChangeData(@Body ChangeResultReq req);

    @POST("/OneSYSWebAPI/api/OnlineUpdate/GetProgramNewVersionDetailByProgramId")
    Observable<UserResponse<UpdateApkBean>> updateVersion(@Body UpdateReq req);
}
