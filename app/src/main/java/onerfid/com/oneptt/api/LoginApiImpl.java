package onerfid.com.oneptt.api;

import java.util.ArrayList;

import onerfid.com.oneptt.base.ApiService;
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
import onerfid.com.oneptt.bean.onekeybean.ElevatorResponse;
import onerfid.com.oneptt.bean.onekeybean.OneHandleReq;
import onerfid.com.oneptt.bean.onekeybean.OneHandles;
import onerfid.com.oneptt.bean.onekeybean.OneKeyReq;
import onerfid.com.oneptt.bean.onekeybean.OneKeyResp;
import onerfid.com.oneptt.bean.onekeyset.MtpersonReq;
import onerfid.com.oneptt.bean.onekeyset.OKeyBasicReq;
import onerfid.com.oneptt.bean.onekeyset.OneKeyBasicInfo;
import onerfid.com.oneptt.bean.onekeyset.OneKeySetBean;
import onerfid.com.oneptt.bean.onekeyset.OneKeySetReq;
import onerfid.com.oneptt.bean.onekeyset.OneKeySetResp;
import onerfid.com.oneptt.bean.onekeyset.OneMTPhoneBean;
import onerfid.com.oneptt.bean.onekeyset.SubmitOKeySetReq;
import onerfid.com.oneptt.bean.updatebean.UpdateApkBean;
import onerfid.com.oneptt.bean.updatebean.UpdateReq;
import rx.Observable;

/**
 * Created by jiayong on 2018/4/13.
 */
public class LoginApiImpl extends ApiService {
    protected static final LoginApi apiManager = retrofit.create(LoginApi.class);
//    protected static final LoginApi apiManagerA = retrofitA.create(LoginApi.class);
    /**
     * @return
     */
    public static Observable<UserResponse<UserResult> > login(UserRequest request) {
        Observable<UserResponse<UserResult> > result = apiManager.login(request);
        return result;
    }

    public static Observable<UserResponse<UseOrgbean>> useOrgList(UseOrgReq request) {
        Observable<UserResponse<UseOrgbean>> result = apiManager.useorglist(request);
        return result;
    }

    public static Observable<UserResponse> handleSim(SimHandleReq request) {
        Observable<UserResponse> result = apiManager.handleSim(request);
        return result;
    }

    public static Observable<UserResponse<HandleCommitResponse>> handleSubmit(HandleCommitReq request) {
        Observable<UserResponse<HandleCommitResponse>> result = apiManager.handleSubmit(request);
        return result;
    }

    public static Observable<UserResponse<OneHandles>> getOneHandler(OneHandleReq request) {
        Observable<UserResponse<OneHandles>> result = apiManager.getOneHandle(request);
        return result;
    }

    public static Observable<UserResponse<ElevatorResp>> getElevators(ElevatorReq request) {
        Observable<UserResponse<ElevatorResp>> result = apiManager.getElevators(request);
        return result;
    }

    public static Observable<UserResponse<OneKeyResp>> bindOneKey(OneKeyReq request) {
        Observable<UserResponse<OneKeyResp>> result = apiManager.bindOnkey(request);
        return result;
    }

    public static Observable<UserResponse<OneKeySetResp>> getOneKeySetList(OneKeySetReq request) {
        Observable<UserResponse<OneKeySetResp>> result = apiManager.getOneKeyList(request);
        return result;
    }

    public static Observable<UserResponse<OneKeyBasicInfo>> getOKeyBasic(OKeyBasicReq request) {
        Observable<UserResponse<OneKeyBasicInfo>> result = apiManager.getOKeyBasic(request);
        return result;
    }

    public static Observable<UserResponse<ArrayList<OneMTPhoneBean>>> getMtPerson(MtpersonReq request) {
        Observable<UserResponse<ArrayList<OneMTPhoneBean>>> result = apiManager.getMtPerson(request);
        return result;
    }

    public static Observable<UserResponse> SubmitSetMsg(SubmitOKeySetReq request) {
        Observable<UserResponse> result = apiManager.updateOKeySet(request);
        return result;
    }

    public static Observable<UserResponse<HandleChangeBean>> handleChange(HandleChangeReq request) {
        Observable<UserResponse<HandleChangeBean>> result = apiManager.handleChange(request);
        return result;
    }

    public static Observable<UserResponse<ChangeResultResp>> getChangeData(ChangeResultReq request) {
        Observable<UserResponse<ChangeResultResp>> result = apiManager.getChangeData(request);
        return result;
    }

    public static Observable<UserResponse<UpdateApkBean>> updateVersion(UpdateReq request) {
        Observable<UserResponse<UpdateApkBean>> result = apiManager.updateVersion(request);
        return result;
    }


}
