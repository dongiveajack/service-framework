package org.trips.service_framework.exceptions.interceptor;

import org.trips.service_framework.exceptions.handlers.GrpcServiceExceptionHandler;
import io.grpc.*;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;

/**
 * @author Abhinav Tripathi 19/05/20
 */
@GRpcGlobalInterceptor
public class GrpcExceptionInterceptor implements ServerInterceptor {
    private final GrpcServiceExceptionHandler grpcServiceExceptionHandler;

    public GrpcExceptionInterceptor(GrpcServiceExceptionHandler grpcServiceExceptionHandler) {
        this.grpcServiceExceptionHandler = grpcServiceExceptionHandler;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        ServerCall.Listener<ReqT> listener = serverCallHandler.startCall(serverCall, metadata);
        return new ExceptionHandlingServerCallListener<>(listener, serverCall, metadata);
    }

    private class ExceptionHandlingServerCallListener<ReqT, RespT>
            extends ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT> {
        private ServerCall<ReqT, RespT> serverCall;
        private Metadata metadata;

        ExceptionHandlingServerCallListener(ServerCall.Listener<ReqT> listener, ServerCall<ReqT, RespT> serverCall,
                                            Metadata metadata) {
            super(listener);
            this.serverCall = serverCall;
            this.metadata = metadata;
        }

        @Override
        public void onHalfClose() {
            try {
                super.onHalfClose();
            } catch (RuntimeException ex) {
                handleException(ex, serverCall, metadata);
                throw ex;
            }
        }

        @Override
        public void onReady() {
            try {
                super.onReady();
            } catch (RuntimeException ex) {
                handleException(ex, serverCall, metadata);
                throw ex;
            }
        }

        private void handleException(RuntimeException exception, ServerCall<ReqT, RespT> serverCall, Metadata metadata) {
            Status status = grpcServiceExceptionHandler.handleException(exception);
            serverCall.close(status, metadata);
        }
    }
}
