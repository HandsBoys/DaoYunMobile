//package com.fzu.daoyunmobile.Utils;
//
//import com.baidubce.http.ApiExplorerClient;
//import com.baidubce.http.AppSigner;
//import com.baidubce.http.HttpMethodName;
//import com.baidubce.model.ApiExplorerRequest;
//import com.baidubce.model.ApiExplorerResponse;
//
//public class CXMessageUtil {
//    public static void SendMessage() {
//        String path = "http://gwgp-mxgiayx955h.n.bdcloudapi.com/msg";
//        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.POST, path);
//        request.setCredentials("5cec2865321043cc8c8e2edb22f78590", "7d86f91cb5524250b22241bd04230cbd");
//
//        request.addHeaderParameter("Content-Type", "application/x-www-form-urlencoded");
//
//
//        ApiExplorerClient client = new ApiExplorerClient(new AppSigner());
//
//        try {
//            ApiExplorerResponse response = client.sendRequest(request);
//            // 返回结果格式为Json字符串
//            System.out.println(response.getResult());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
