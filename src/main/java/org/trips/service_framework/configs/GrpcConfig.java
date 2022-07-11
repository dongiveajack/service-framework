package org.trips.service_framework.configs;

import org.trips.service_framework.utils.Context;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author Abhinav Tripathi 08/04/20
 */
@Configuration
public class GrpcConfig {
    @Bean
    @GRpcGlobalInterceptor
    public ServerInterceptor globalInterceptor() {
        return new ServerInterceptor() {
            @Override
            public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
                Metadata.Key<String> xRequestedByHeaderKey = Metadata.Key.of("X-Requested-By", Metadata.ASCII_STRING_MARSHALLER);
                String requestId = "System";
                if (headers.containsKey(xRequestedByHeaderKey) && !StringUtils.isEmpty(headers.get(xRequestedByHeaderKey))) {
                    requestId = headers.get(xRequestedByHeaderKey);
                }
                Context.setUserId(requestId);
                return next.startCall(call, headers);
            }
        };
    }
}
