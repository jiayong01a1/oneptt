package onerfid.com.oneptt.bean.updatebean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Apk 更新
 */

public class UpdateApkBean {

    /**
     * id : 3442fa26-4df3-e611-a4a1-3497f685fa15
     * programCode : OneDtAppforAndroid
     * programName : 梯信通安卓App
     * programType : 15
     * programMajor : 2
     * programMinor : 0
     * programBuild : 0
     * programRevision : 0
     * programVersion : 2.0.0.0
     * description : null
     * createTime : 2017-02-15T15:05:38
     * updateInfo : {"id":10082,"updateName":"OneDtMt","isUpdated":true,"isPulish":true,"pushStatus":1,"description":null,"onLieMajor":2,"onLieMinor":0,"onLieBuild":0,"onLieRevision":0,"onLieVersion":"2.0.0.0","isActive":true,"appId":"8eeec3e2-ba95-43ef-9217-d53a5b5e6919"}
     * files : [{"id":82,"fileName":"OneDtMt.apk","fileType":1,"fileSize":42851,"fileURL":"http://www.onedt.com.cn/FileUpLoad/OnlineFile/223c4634-f161-4916-957b-323c109517b4_0.apk","programMajor":2,"programMinor":0,"programBuild":0,"programRevision":0,"programVersion":"2.0.0.0","isActive":false,"fileGuid":"00000000-0000-0000-0000-000000000000"}]
     */

    public String id;
    public String programCode;
    public String programName;
    public int programType;
    public int programMajor;
    public int programMinor;
    public int programBuild;
    public int programRevision;
    public String programVersion;
    public String description;
    public String createTime;
    public UpdateInfoBean updateInfo;
    public List<FilesBean> files;

    public static class UpdateInfoBean {

        /**
         * id : 10082
         * updateName : OneDtMt
         * isUpdated : true
         * isPulish : true
         * pushStatus : 1
         * description : null
         * onLieMajor : 2
         * onLieMinor : 0
         * onLieBuild : 0
         * onLieRevision : 0
         * onLieVersion : 2.0.0.0
         * isActive : true
         * appId : 8eeec3e2-ba95-43ef-9217-d53a5b5e6919
         */

        public int id;
        public String updateName;
        public boolean isUpdated;
        public boolean isPulish;
        public int pushStatus;
        public String description;
        public int onLieMajor;
        public int onLieMinor;
        public int onLieBuild;
        public int onLieRevision;
        public String onLieVersion;
        public boolean isActive;
        public String appId;

        @Override
        public String toString() {
            return "UpdateInfoBean{" +
                    "id=" + id +
                    ", updateName='" + updateName + '\'' +
                    ", isUpdated=" + isUpdated +
                    ", isPulish=" + isPulish +
                    ", pushStatus=" + pushStatus +
                    ", description='" + description + '\'' +
                    ", onLieMajor=" + onLieMajor +
                    ", onLieMinor=" + onLieMinor +
                    ", onLieBuild=" + onLieBuild +
                    ", onLieRevision=" + onLieRevision +
                    ", onLieVersion='" + onLieVersion + '\'' +
                    ", isActive=" + isActive +
                    ", appId='" + appId + '\'' +
                    '}';
        }
    }

    public static class FilesBean {

        /**
         * id : 82
         * fileName : OneDtMt.apk
         * fileType : 1
         * fileSize : 42851
         * fileURL : http://www.onedt.com.cn/FileUpLoad/OnlineFile/223c4634-f161-4916-957b-323c109517b4_0.apk
         * programMajor : 2
         * programMinor : 0
         * programBuild : 0
         * programRevision : 0
         * programVersion : 2.0.0.0
         * isActive : false
         * fileGuid : 00000000-0000-0000-0000-000000000000
         */
        public int id;
        public String fileName;
        public int fileType;
        public int fileSize;
        public String fileURL;
        public int programMajor;
        public int programMinor;
        public int programBuild;
        public int programRevision;
        public String programVersion;
        public boolean isActive;
        public String fileGuid;

        @Override
        public String toString() {
            return "FilesBean{" +
                    "id=" + id +
                    ", fileName='" + fileName + '\'' +
                    ", fileType=" + fileType +
                    ", fileSize=" + fileSize +
                    ", fileURL='" + fileURL + '\'' +
                    ", programMajor=" + programMajor +
                    ", programMinor=" + programMinor +
                    ", programBuild=" + programBuild +
                    ", programRevision=" + programRevision +
                    ", programVersion='" + programVersion + '\'' +
                    ", isActive=" + isActive +
                    ", fileGuid='" + fileGuid + '\'' +
                    '}';
        }
    }
}
