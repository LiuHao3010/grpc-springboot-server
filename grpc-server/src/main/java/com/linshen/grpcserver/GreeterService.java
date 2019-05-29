package com.linshen.grpcserver;


import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.springboot.autoconfigure.grpc.server.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;

@Slf4j
@GrpcService(GreeterOuterClass.class)
public class GreeterService extends GreeterGrpc.GreeterImplBase {
    @Autowired
    Core core;
    public void sayHello(GreeterOuterClass.HelloRequest request, StreamObserver<GreeterOuterClass.HelloReply> responseObserver) {
        String message = "Hello " + request.getImag();
       // GreeterOuterClass.HelloReply helloReply= GreeterOuterClass.HelloReply.newBuilder().setMessage("12").build();
        byte[] bytes=request.getImag().toByteArray();
        String path=request.getURL();
        try{
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));
            imageOutput.write(bytes, 0, bytes.length);
            imageOutput.close();
            System.out.println("Make Picture success,Please find image in " + path);
            int[] result=core.doservice(path);
            String rs="";
            for (int i:result
            ) {
                rs=rs+"  "+i;
            }
            message=rs;
        } catch(Exception ex) {
            System.out.println("Exception: " + ex);
            message="发生错误";
            ex.printStackTrace();
        }
        final GreeterOuterClass.HelloReply.Builder replyBuilder = GreeterOuterClass.HelloReply.newBuilder().setMessage(message);
        responseObserver.onNext(replyBuilder.build());
        responseObserver.onCompleted();
}
}

